package net.unipu.Backend.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import net.unipu.Backend.exception.NotInDatabaseException;
import net.unipu.Backend.exception.TokenRefreshException;
import net.unipu.Backend.models.RefreshToken;
import net.unipu.Backend.payload.request.LogOutRequest;
import net.unipu.Backend.payload.response.*;
import net.unipu.Backend.security.services.RSAKeyGenerator;
import net.unipu.Backend.security.services.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import net.unipu.Backend.models.ERole;
import net.unipu.Backend.models.Role;
import net.unipu.Backend.models.User;
import net.unipu.Backend.payload.request.LoginRequest;
import net.unipu.Backend.payload.request.SignupRequest;
import net.unipu.Backend.repository.RoleRepository;
import net.unipu.Backend.repository.UserRepository;
import net.unipu.Backend.security.jwt.JwtUtils;
import net.unipu.Backend.security.services.UserDetailsImpl;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  RSAKeyGenerator rsaKeyGenerator;
  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @Autowired
  RefreshTokenService refreshTokenService;

  @GetMapping("/key")
  public ResponseEntity<?> publicKey() {
    return ResponseEntity.ok(new MessageResponse(rsaKeyGenerator.getFormattedPublicKey()));
  }
  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    loginRequest.setPassword(rsaKeyGenerator.decode(loginRequest.getPassword()));

    Authentication authentication = authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    String jwt = jwtUtils.generateJwtToken(userDetails);

    List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

    RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set(HttpHeaders.SET_COOKIE,
            "token="+refreshToken.getToken()+"; Expires="+refreshToken.getExpiryDate()+"; SameSite=None; Secure; HttpOnly");

    return ResponseEntity.ok().headers(responseHeaders).body(new LoginResponse(jwt, userDetails.getId(),
            userDetails.getUsername(), userDetails.getEmail(), roles));
  }

  @PostMapping("/signup")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

    signUpRequest.setPassword(rsaKeyGenerator.decode(signUpRequest.getPassword()));

    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
            encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    strRoles.forEach(role -> {
      switch (role) {
        case "admin" -> {
          Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                  .orElseThrow(() -> new NotInDatabaseException(ERole.ROLE_ADMIN.name(),"roleRepository"));
          roles.add(adminRole);
        }
        case "mod" -> {
          Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                  .orElseThrow(() -> new NotInDatabaseException(ERole.ROLE_MODERATOR.name(),"roleRepository"));
          roles.add(modRole);
        }
      }
    });

    Role userRole = roleRepository.findByName(ERole.ROLE_USER)
            .orElseThrow(() -> new NotInDatabaseException(ERole.ROLE_USER.name(),"roleRepository"));
    roles.add(userRole);

    user.setRoles(roles);
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }

  @GetMapping("/refreshToken")
  public ResponseEntity<?> refreshToken(@CookieValue(name = "token") String refreshToken) {
    return refreshTokenService.findByToken(refreshToken)
            .map(refreshTokenService::verifyExpiration)
            .map(RefreshToken::getUser)
            .map(user -> {
              String token = jwtUtils.generateTokenFromUsername(user.getUsername());
              return ResponseEntity.ok(new LoginResponse(token, user.getId(),
                      user.getUsername(), user.getEmail(), user.getRoles().stream().map(role -> role.getName().toString()).collect(Collectors.toList())));
            })
            .orElseThrow(() -> new TokenRefreshException(refreshToken,
                    "Refresh token is not in database!"));
  }

  @GetMapping("/users")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> listUsers() {
    return ResponseEntity.ok(userRepository.findAll().stream().map(User::getUsername).toList());
  }

  @GetMapping("/users/{username}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> listUsers(@PathVariable String username) {
    User user = userRepository.findByUsername(username).orElseThrow(() -> new NotInDatabaseException(username,"userRepository"));
    UserResponse userResponse = new UserResponse(user.getId(),user.getUsername(),user.getEmail(),
            user.getRoles().stream().map(role -> role.getName().toString()).toList());
    return ResponseEntity.ok(userResponse);
  }

  @PostMapping("/logout")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> logoutUser(@Valid @RequestBody LogOutRequest logOutRequest) {
    refreshTokenService.deleteByUserId(logOutRequest.getUserId());
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set(HttpHeaders.SET_COOKIE,
            "token="+null+"; MaxAge=0; SameSite=None; Path=/api/auth; Secure; HttpOnly");
    return ResponseEntity.ok().headers(responseHeaders).body(new MessageResponse("Log out successful!"));
  }
}