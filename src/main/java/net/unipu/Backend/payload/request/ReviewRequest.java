package net.unipu.Backend.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ReviewRequest {

  @NotNull
  private Integer score;

  @NotBlank
  private String comment;

  private Boolean anonymous;

  @NotBlank
  private String professorsName;

  @NotBlank
  private String studentsName;

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

  public String getProfessorsName() {
    return professorsName;
  }

  public void setProfessorsName(String professorsName) {
    this.professorsName = professorsName;
  }

  public String getStudentsName() {
    return studentsName;
  }

  public void setStudentsName(String studentsName) {
    this.studentsName = studentsName;
  }
}
