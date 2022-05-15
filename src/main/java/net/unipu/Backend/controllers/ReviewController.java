package net.unipu.Backend.controllers;

import net.unipu.Backend.models.Professor;
import net.unipu.Backend.models.Review;
import net.unipu.Backend.payload.request.ReviewRequest;
import net.unipu.Backend.payload.response.MessageResponse;
import net.unipu.Backend.payload.response.ProfessorResponse;
import net.unipu.Backend.payload.response.ReviewResponse;
import net.unipu.Backend.repository.ProfessorRepository;
import net.unipu.Backend.repository.ReviewRepository;
import net.unipu.Backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/review")
public class ReviewController {
  @Autowired
  ProfessorRepository professorRepository;

  @Autowired
  ReviewRepository reviewRepository;

  @Autowired
  UserRepository userRepository;

  @GetMapping("/list/professors")
  public ResponseEntity<?> listProfessors() {
    return ResponseEntity.ok(professorRepository.findAll().stream().map(professor -> new ProfessorResponse(professor.getName(),professor.getDetails())).toList());
  }
  @GetMapping("/list/professors/{name}")
  public ResponseEntity<?> getProfessor(@PathVariable String name) {
    Professor professor = professorRepository.findByName(name).orElseThrow(() -> new RuntimeException("professor not found"));
    return ResponseEntity.ok(new ProfessorResponse(professor.getName(),professor.getDetails()));
  }
  @GetMapping("/list/reviews")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> listReviews() {
    return  ResponseEntity.ok(reviewRepository.findAll().stream().map(ReviewResponse::new).toList());
  }

  @GetMapping("/list/reviews/{name}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> getReview(@PathVariable String name) {
    return  ResponseEntity.ok(reviewRepository.findByProfessorName(professorRepository.findByName(name)
            .orElseThrow(() -> new RuntimeException("professor not found")).getName())
            .stream()
            .map(ReviewResponse::new)
            .toList());

  }

  @PostMapping("/post")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> postReview(@Valid @RequestBody ReviewRequest reviewRequest) {
    Review review = new Review();
    review.setAnonymous(reviewRequest.getAnonymous());
    review.setComment(reviewRequest.getComment());
    review.setScore(reviewRequest.getScore());
    review.setProfessor(professorRepository.findByName(reviewRequest.getProfessorsName())
            .orElseThrow(() -> new RuntimeException("Professor not found")));
    review.setStudent(userRepository.findByUsername(reviewRequest.getStudentsName())
            .orElseThrow(() -> new RuntimeException("Student not found")));
    reviewRepository.save(review);
    return  ResponseEntity.ok(new MessageResponse("Review saved!"));
  }

}
