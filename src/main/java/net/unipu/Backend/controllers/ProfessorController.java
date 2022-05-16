package net.unipu.Backend.controllers;

import net.unipu.Backend.models.Professor;
import net.unipu.Backend.payload.request.ProfessorRequest;
import net.unipu.Backend.payload.response.MessageResponse;
import net.unipu.Backend.payload.response.ProfessorResponse;
import net.unipu.Backend.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/professor")
public class ProfessorController {
  @Autowired
  ProfessorRepository professorRepository;

  @GetMapping("/list")
  public ResponseEntity<?> listProfessors() {
    return ResponseEntity.ok(professorRepository.findAll().stream().map(professor -> new ProfessorResponse(professor.getName(),professor.getDetails())).toList());
  }
  @GetMapping("/{name}")
  public ResponseEntity<?> getProfessor(@PathVariable String name) {
    Professor professor = professorRepository.findByName(name).orElseThrow(() -> new RuntimeException("professor not found"));
    return ResponseEntity.ok(new ProfessorResponse(professor.getName(),professor.getDetails()));
  }

  @PostMapping("/post")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> postProfessor(@Valid @RequestBody ProfessorRequest professorRequest) {
    Professor professor = new Professor();
    professor.setDetails(professorRequest.getDetails());
    professor.setName(professorRequest.getName());
    professorRepository.save(professor);
    return  ResponseEntity.ok(new MessageResponse("Professor saved!"));
  }

}
