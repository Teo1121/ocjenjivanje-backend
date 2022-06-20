package net.unipu.Backend.controllers;

import net.unipu.Backend.exception.NotInDatabaseException;
import net.unipu.Backend.models.User;
import net.unipu.Backend.payload.request.UserRequest;
import net.unipu.Backend.payload.response.MessageResponse;
import net.unipu.Backend.payload.response.UserResponse;
import net.unipu.Backend.repository.ReviewRepository;
import net.unipu.Backend.repository.UserRepository;
import net.unipu.Backend.security.services.RSAKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = {"http://tricky-able.surge.sh/","https://tricky-able.surge.sh/","https://ocjenjivanje.surge.sh/","http://ocjenjivanje.surge.sh/"}, maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/user")
public class UserController {

  @Autowired
  RSAKeyGenerator rsaKeyGenerator;
  @Autowired
  UserRepository userRepository;

  @Autowired
  ReviewRepository reviewRepository;

  @Autowired
  PasswordEncoder encoder;

  @GetMapping("/all")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> listUsers() {
    return ResponseEntity.ok(userRepository.findAll().stream().map(User::getUsername).toList());
  }

  @GetMapping("/{username}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> listUser(@PathVariable String username) {
    User user = userRepository.findByUsername(username).orElseThrow(() -> new NotInDatabaseException(username,"userRepository"));
    UserResponse userResponse = new UserResponse(user.getId(),user.getUsername(),user.getEmail(),
            user.getRoles().stream().map(role -> role.getName().toString()).toList());
    return ResponseEntity.ok(userResponse);
  }

  @PutMapping("/{username}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> putUser(@Valid @RequestBody UserRequest userRequest, @PathVariable String username) {
    userRepository.findByUsername(username).map(user -> {
      user.setUsername(userRequest.getUsername());
      user.setEmail(userRequest.getEmail());
      if (userRequest.getPassword() != null) {
        user.setPassword(encoder.encode(rsaKeyGenerator.decode(userRequest.getPassword())));
      }
      return userRepository.save(user);
    }).orElseThrow(() -> new NotInDatabaseException(username,"userRepository"));
    return ResponseEntity.ok(new MessageResponse("User updated!"));
  }

  @DeleteMapping("/{username}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> deleteUser(@PathVariable String username) {
    reviewRepository.deleteByStudentUsername(username);
    userRepository.deleteByUsername(username).orElseThrow(() -> new NotInDatabaseException(username,"userRepository"));
    return ResponseEntity.ok(new MessageResponse("User deleted!"));
  }
}