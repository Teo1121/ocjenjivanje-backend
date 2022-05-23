package net.unipu.Backend.repository;

import net.unipu.Backend.models.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
  Optional<Professor> findByName(String name);
  @Transactional
  Optional<Professor> deleteByName(String name);
}
