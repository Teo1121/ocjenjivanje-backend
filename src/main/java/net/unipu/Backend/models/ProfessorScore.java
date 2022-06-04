package net.unipu.Backend.models;

public class ProfessorScore {

    private long numOfReviews;
    private double averageScore;
    private String professorsName;

    public ProfessorScore(long numOfReviews, double averageScore, String professorsName) {
        this.numOfReviews = numOfReviews;
        this.averageScore = averageScore;
        this.professorsName = professorsName;
    }

    public long getNumOfReviews() {
        return numOfReviews;
    }

    public void setNumOfReviews(long numOfReviews) {
        this.numOfReviews = numOfReviews;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    public String getProfessorsName() {
        return professorsName;
    }

    public void setProfessorsName(String professorsName) {
        this.professorsName = professorsName;
    }
}
