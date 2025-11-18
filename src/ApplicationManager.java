import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApplicationManager {
    private static final int MAX_ACTIVE_APPLICATIONS = 3;
    private final List<String> submissionNotifications = new ArrayList<>();
    private String lastFailureReason = "";

    public Application submitApplication(Student student, Internship internship) {
        if (student == null || internship == null) {
            throw new IllegalArgumentException("Student and internship are required.");
        }
        if (!enforceRules(student, internship)) {
            throw new IllegalStateException(lastFailureReason);
        }
        Application application = new Application(student, internship);
        internship.addApplication(application);
        student.getApplications().add(application);
        submissionNotifications.add(LocalDateTime.now() + " :: "
                + student.getName() + " applied for " + internship.getTitle());
        lastFailureReason = "";
        return application;
    }

    public void updateStatus(Application application, ApplicationStatus status) {
        updateStatus(application, status, false);
    }

    public void updateStatus(Application application, ApplicationStatus status, boolean confirmOffer) {
        if (application == null || status == null) {
            return;
        }
        application.setStatus(status);
        if (status == ApplicationStatus.SUCCESSFUL && confirmOffer) {
            assignSlot(application);
        } else if (status == ApplicationStatus.UNSUCCESSFUL) {
            releaseSlot(application);
        }
    }

    public boolean enforceRules(Student student, Internship internship) {
        lastFailureReason = "";
        String violation = evaluateRules(student, internship);
        if (violation != null) {
            lastFailureReason = violation;
            return false;
        }
        return true;
    }

    public String getLastFailureReason() {
        return lastFailureReason;
    }

    public List<String> getSubmissionNotifications() {
        return Collections.unmodifiableList(submissionNotifications);
    }

    private String evaluateRules(Student student, Internship internship) {
        if (student == null || internship == null) {
            return "Student and internship are required.";
        }
        if (internship.getStatus() != InternshipStatus.APPROVED) {
            return "Internship has not been approved yet.";
        }
        if (!internship.isVisible()) {
            return "Internship is currently hidden.";
        }
        String preferredMajor = internship.getPreferredMajor();
        String studentMajor = student.getMajor();
        if (preferredMajor != null && !preferredMajor.isBlank()
                && studentMajor != null && !preferredMajor.equalsIgnoreCase(studentMajor)) {
            return "Major does not match the preferred major for this internship.";
        }
        InternshipLevel level = internship.getLevel();
        if (student.getYearOfStudy() <= 2 && level != null && level != InternshipLevel.BASIC) {
            return "Lower-year students may only apply for BASIC level internships.";
        }
        LocalDate today = LocalDate.now();
        LocalDate openDate = internship.getOpenDate();
        if (openDate != null && today.isBefore(openDate)) {
            return "Internship is not open for applications yet.";
        }
        LocalDate closeDate = internship.getCloseDate();
        if (closeDate != null && today.isAfter(closeDate)) {
            return "Internship is already closed.";
        }
        long activeCount = student.getApplications().stream()
                .filter(app -> app.getStatus() == ApplicationStatus.PENDING
                        || app.getStatus() == ApplicationStatus.SUCCESSFUL)
                .count();
        if (activeCount >= MAX_ACTIVE_APPLICATIONS) {
            return "Maximum of " + MAX_ACTIVE_APPLICATIONS + " active applications reached.";
        }
        for (Application application : student.getApplications()) {
            if (application.getInternship() == internship) {
                return "You have already applied for this internship.";
            }
        }
        if (student.hasAcceptedPlacement()) {
            return "You have already accepted a placement.";
        }
        if (internship.isFull()) {
            return "Internship slots have been filled.";
        }
        return null;
    }

    private void assignSlot(Application application) {
        Internship internship = application.getInternship();
        for (InternshipSlot slot : internship.getSlots()) {
            if (slot.getAssignedStudent() == null) {
                slot.assignStudent(application.getStudent());
                break;
            }
        }
        if (internship.isFull()) {
            internship.setStatus(InternshipStatus.FILLED);
        }
    }

    private void releaseSlot(Application application) {
        Internship internship = application.getInternship();
        for (InternshipSlot slot : internship.getSlots()) {
            if (slot.getAssignedStudent() == application.getStudent()) {
                slot.release();
                internship.setStatus(InternshipStatus.APPROVED);
                break;
            }
        }
    }

}
