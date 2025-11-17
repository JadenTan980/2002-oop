package entities;

import java.util.UUID;

/**
 * Represents one student's application to an internship.
 */
public class InternshipApplication {

    public enum ApplicationStatus {
        PENDING,
        SUCCESSFUL,
        UNSUCCESSFUL,
        WITHDRAWN
    }

    private final String id = UUID.randomUUID().toString();
    private final Student student;
    private final InternshipOpportunity opportunity;
    private ApplicationStatus status = ApplicationStatus.PENDING;
    private boolean withdrawalRequested;

    public InternshipApplication(Student student, InternshipOpportunity opportunity) {
        this.student = student;
        this.opportunity = opportunity;
    }

    public String getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public InternshipOpportunity getOpportunity() {
        return opportunity;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public void requestWithdrawal() {
        withdrawalRequested = true;
        status = ApplicationStatus.WITHDRAWN;
    }

    public boolean isWithdrawalRequested() {
        return withdrawalRequested;
    }

    public boolean isPending() {
        return status == ApplicationStatus.PENDING;
    }
}
