package net.unipu.Backend.controllers;

import net.unipu.Backend.exception.NotInDatabaseException;
import net.unipu.Backend.models.Professor;
import net.unipu.Backend.payload.request.ProfessorRequest;
import net.unipu.Backend.payload.response.MessageResponse;
import net.unipu.Backend.payload.response.ProfessorResponse;
import net.unipu.Backend.repository.ProfessorRepository;
import net.unipu.Backend.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = {"http://tricky-able.surge.sh/","https://tricky-able.surge.sh/","https://ocjenjivanje.surge.sh/","http://ocjenjivanje.surge.sh/"}, maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/professor")
public class ProfessorController {
  @Autowired
  ProfessorRepository professorRepository;

  @Autowired
  ReviewRepository reviewRepository;

  @GetMapping("")
  public ResponseEntity<?> listProfessors() {
    return ResponseEntity.ok(professorRepository.findAll().stream().map(professor -> new ProfessorResponse(professor.getName(),professor.getDetails())).toList());
  }
  @GetMapping("/{name}")
  public ResponseEntity<?> getProfessor(@PathVariable String name) {
    Professor professor = professorRepository.findByName(name).orElseThrow(() -> new NotInDatabaseException(name,"professorRepository"));
    return ResponseEntity.ok(new ProfessorResponse(professor.getName(),professor.getDetails()));
  }

  @PostMapping("")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> postProfessor(@Valid @RequestBody ProfessorRequest professorRequest) {
    Professor professor = new Professor();
    professor.setDetails(professorRequest.getDetails());
    professor.setName(professorRequest.getName());
    professorRepository.save(professor);
    return  ResponseEntity.ok(new MessageResponse("Professor saved!"));
  }

  @PutMapping("/{name}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> putProfessor(@Valid @RequestBody ProfessorRequest professorRequest,@PathVariable String name) {
    professorRepository.findByName(name).map(professor -> {
      professor.setName(professorRequest.getName());
      professor.setDetails(professorRequest.getDetails());
      return professorRepository.save(professor);
    }).orElseThrow(() -> new NotInDatabaseException(name,"professorRepository"));
    return ResponseEntity.ok(new MessageResponse("Professor updated!"));
  }

  @DeleteMapping("/{name}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> deleteProfessor(@PathVariable String name) {
    reviewRepository.deleteByProfessorName(name);
    professorRepository.deleteByName(name).orElseThrow(() -> new NotInDatabaseException(name,"professorRepository"));
    return ResponseEntity.ok(new MessageResponse("Professor deleted!"));
  }
}
