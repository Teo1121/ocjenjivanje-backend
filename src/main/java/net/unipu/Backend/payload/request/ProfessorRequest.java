package net.unipu.Backend.payload.request;

import javax.validation.constraints.NotBlank;

public class ProfessorRequest {

  @NotBlank
  private String details;

  @NotBlank
  private String name;

  public String getDetails() {
    return details;
  }

  public void setDetails(String details) {
    this.details = details;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
