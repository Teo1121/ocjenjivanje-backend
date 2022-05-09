package net.unipu.Backend.controllers;

import net.unipu.Backend.payload.response.ProfessorResponse;
import net.unipu.Backend.payload.response.ReviewResponse;
import net.unipu.Backend.repository.ProfessorRepository;
import net.unipu.Backend.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/review")
public class ReviewController {
  @Autowired
  ProfessorRepository professorRepository;

  @Autowired
  ReviewRepository reviewRepository;

  @GetMapping("/list/professors")
  public ResponseEntity<?> listProfessors() {
    return ResponseEntity.ok(professorRepository.findAll().stream().map(professor -> new ProfessorResponse(professor.getName(),professor.getDetails())).toList());
  }

  @GetMapping("/list/reviews")
  public ResponseEntity<?> listReviews() {
    return  ResponseEntity.ok(reviewRepository.findAll().stream().map(review -> new ReviewResponse(review.getScore(), review.getComment(),review.getProfessor().getName(),review.getAnonymous() ? "Anon" : review.getStudent().getUsername())).toList());
  }

}
