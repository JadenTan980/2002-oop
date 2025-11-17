public class WithdrawalRequest {
    private final Application application;
    private final Student student;
    private final String reason;
    private WithdrawalStatus status = WithdrawalStatus.PENDING;

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

    public WithdrawalStatus getStatus() {
        return status;
    }

    public void approve() {
        this.status = WithdrawalStatus.WITHDRAWN;
        application.setStatus(ApplicationStatus.UNSUCCESSFUL);
    }

    public void reject() {
        this.status = WithdrawalStatus.REJECTED;
    }
}
