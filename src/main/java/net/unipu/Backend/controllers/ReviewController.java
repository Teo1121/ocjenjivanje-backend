package net.unipu.Backend.controllers;

import net.unipu.Backend.exception.NotInDatabaseException;
import net.unipu.Backend.models.Review;
import net.unipu.Backend.payload.request.ReviewRequest;
import net.unipu.Backend.payload.response.MessageResponse;
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

  @GetMapping("")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> listReviews() {
    return  ResponseEntity.ok(reviewRepository.findAll().stream().map(ReviewResponse::new).toList());
  }

  @GetMapping("/{name}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> getReviews(@PathVariable String name) {
    return  ResponseEntity.ok(reviewRepository.findByProfessorName(professorRepository.findByName(name)
            .orElseThrow(() -> new NotInDatabaseException(name,"professorRepository")).getName())
            .stream()
            .map(ReviewResponse::new)
            .toList());
  }


  @GetMapping("/stats")
  @PreAuthorize("hasRole('MODERATOR')")
  public ResponseEntity<?> getReviewStats() {
    return ResponseEntity.ok(reviewRepository.scoreByProfessor());
  }

  @PostMapping("")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> postReview(@Valid @RequestBody ReviewRequest reviewRequest) {
    Review review = new Review();
    review.setAnonymous(reviewRequest.getAnonymous());
    review.setComment(reviewRequest.getComment());
    review.setScore(reviewRequest.getScore());
    review.setProfessor(professorRepository.findByName(reviewRequest.getProfessorsName())
            .orElseThrow(() -> new NotInDatabaseException(reviewRequest.getProfessorsName(),"professorRepository")));
    review.setStudent(userRepository.findByUsername(reviewRequest.getStudentsName())
            .orElseThrow(() -> new NotInDatabaseException(reviewRequest.getStudentsName(),"userRepository")));
    reviewRepository.save(review);
    return  ResponseEntity.ok(new MessageResponse("Review saved!"));
  }
}
