package net.unipu.Backend.repository;

import net.unipu.Backend.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import net.unipu.Backend.models.ProfessorScore;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProfessorName(String name);
    List<Review> findByStudentUsername(String username);
    @Transactional
    void deleteByProfessorName(String name);

    @Transactional
    void deleteByStudentUsername(String name);

    @Query("SELECT new net.unipu.Backend.models.ProfessorScore(COUNT(r.id), AVG(r.score), p.name) FROM Review AS r JOIN Professor AS p ON r.professor = p GROUP BY p.id")
    List<ProfessorScore> scoreByProfessor();
}
