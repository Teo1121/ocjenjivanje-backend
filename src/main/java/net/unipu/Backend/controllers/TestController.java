package net.unipu.Backend.controllers;

import net.unipu.Backend.payload.response.MessageResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "http://192.168.1.5:3000", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/test")
public class TestController {

  @GetMapping("/logout")
  public ResponseEntity<?> logoutUser() {
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set(HttpHeaders.SET_COOKIE,
            "token="+null+"; MaxAge=0; SameSite=None; Path=/api/auth; Secure; HttpOnly");
    return ResponseEntity.ok().headers(responseHeaders).body(new MessageResponse("Log out successful!"));
  }
  @GetMapping("/all")
  public String allAccess() {
    return "Public Content.";
  }

  @GetMapping("/user")
  @PreAuthorize("hasRole('USER')")
  public String userAccess() {
    return "User Content.";
  }

  @GetMapping("/mod")
  @PreAuthorize("hasRole('MODERATOR')")
  public String moderatorAccess() {

    return "Moderator Board.";
  }

  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public String adminAccess() {
    return "Admin Board.";
  }
}
