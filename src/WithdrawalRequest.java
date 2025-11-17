public class WithdrawalRequest {
    private final Application application;
    private final Student student;
    private final String reason;
    private String status = "Pending";

    public WithdrawalRequest(Application application, Student student, String reason) {
        this.application = application;
        this.student = student;
        this.reason = reason;
    }

    public Application getApplication() {
        return application;
    }

    public Student getStudent() {
        return student;
    }

    public String getReason() {
        return reason;
    }

    public String getStatus() {
        return status;
    }

    public void approve() {
        this.status = "Approved";
        application.setStatus("Withdrawn");
    }

    public void reject() {
        this.status = "Rejected";
    }
}
