package net.unipu.Backend.payload.response;

import net.unipu.Backend.models.Review;

public class ReviewResponse {

  private Integer score;

  private String comment;

  private String professorsName;

  private String studentsName;

  public ReviewResponse(Integer score, String comment, String professorsName, String studentsName) {
    this.score = score;
    this.comment = comment;
    this.professorsName = professorsName;
    this.studentsName = studentsName;
  }

  public ReviewResponse(Review review) {
    this.score = review.getScore();
    this.comment = review.getComment();
    this.professorsName = review.getProfessor().getName();
    this.studentsName = review.getAnonymous() ? "Anon" : review.getStudent().getUsername();
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

  public String getProfessorsName() { return professorsName; }

  public void setProfessorsName(String professorsName) { this.professorsName = professorsName; }

  public String getStudentsName() { return studentsName; }

  public void setStudentsName(String studentsName) { this.studentsName = studentsName; }
}
