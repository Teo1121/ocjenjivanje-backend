package net.unipu.Backend.repository;

import net.unipu.Backend.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProfessorName(String name);
    List<Review> findByStudentUsername(String username);
    @Transactional
    void deleteByProfessorName(String name);
}
