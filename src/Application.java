import java.util.Date;

public class Application {
    private final Student student;
    private final Internship internship;
    private String status = "Pending";
    private final Date timestamp = new Date();
    private boolean withdrawalRequested;

    public Application(Student student, Internship internship) {
        this.student = student;
        this.internship = internship;
    }

    public Student getStudent() {
        return student;
    }

    public Internship getInternship() {
        return internship;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public boolean isWithdrawalRequested() {
        return withdrawalRequested;
    }

    public void markSuccessful() {
        this.status = "Successful";
    }

    public void markUnsuccessful() {
        this.status = "Unsuccessful";
    }

    public WithdrawalRequest requestWithdrawal(String reason) {
        this.withdrawalRequested = true;
        return new WithdrawalRequest(this, student, reason);
    }
}
