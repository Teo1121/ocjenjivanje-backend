package net.unipu.Backend.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://tricky-able.surge.sh/","https://tricky-able.surge.sh/","https://ocjenjivanje.surge.sh/","http://ocjenjivanje.surge.sh/"}, maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/test")
public class TestController {

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
