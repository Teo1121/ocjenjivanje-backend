package net.unipu.Backend.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "reviews")
public class Review {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private Integer score;

  @NotBlank
  private String comment;

  private Boolean anonymous;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="professor_id", nullable=false)
  private Professor professor;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="user_id", nullable=false)
  private User student;

  public Review() {
  }

  public Review(Integer score, String comment, Boolean anonymous) {
    this.score = score;
    this.comment = comment;
    this.anonymous = anonymous;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getScore() {
    return score;
  }

  public void setScore(Integer score) {
    this.score = score;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public Boolean getAnonymous() {
    return anonymous;
  }

  public void setAnonymous(Boolean anonymous) {
    this.anonymous = anonymous;
  }

  public Professor getProfessor() {
    return professor;
  }

  public void setProfessor(Professor professor) {
    this.professor = professor;
  }

  public User getStudent() {
    return student;
  }

  public void setStudent(User student) {
    this.student = student;
  }
}
