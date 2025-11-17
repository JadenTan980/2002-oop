import java.time.LocalDate;

public class ApplicationManager {
    public Application submitApplication(Student student, Internship internship) {
        if (!enforceRules(student, internship)) {
            throw new IllegalStateException("Application rules violated");
        }
        Application application = new Application(student, internship);
        internship.addApplication(application);
        student.getApplications().add(application);
        return application;
    }

    public void updateStatus(Application application, ApplicationStatus status) {
        if (application != null && status != null) {
            application.setStatus(status);
        }
    }

    public boolean enforceRules(Student student, Internship internship) {
        if (student == null || internship == null) {
            return false;
        }

        // 1. Internship must already be approved by staff.
        if (internship.getStatus() != InternshipStatus.APPROVED) {
            return false;
        }

        // 2. Opportunity must still be visible to students.
        if (!internship.isVisible()) {
            return false;
        }

        // 3. Student major must match the preferred major (when specified).
        String preferredMajor = internship.getPreferredMajor();
        String studentMajor = student.getMajor();
        if (preferredMajor != null && studentMajor != null
                && !preferredMajor.equalsIgnoreCase(studentMajor)) {
            return false;
        }

        // 4. Lower-year students (year 1-2) may only apply for BASIC opportunities.
        InternshipLevel level = internship.getLevel();
        if (student.getYearOfStudy() <= 2 && level != null && level != InternshipLevel.BASIC) {
            return false;
        }

        // 5. Application must be within the open and close dates.
        LocalDate today = LocalDate.now();
        LocalDate openDate = internship.getOpenDate();
        if (openDate != null && today.isBefore(openDate)) {
            return false;
        }
        LocalDate closeDate = internship.getCloseDate();
        if (closeDate != null && today.isAfter(closeDate)) {
            return false;
        }

        // 6. Students can hold at most 3 active applications.
        if (student.getApplications().size() >= 3) {
            return false;
        }

        // 7. Prevent duplicate submissions to the same internship.
        for (Application application : student.getApplications()) {
            if (application.getInternship() == internship) {
                return false;
            }
        }

        // 8. Students who already accepted a placement cannot apply again.
        if (student.hasAcceptedPlacement()) {
            return false;
        }

        // 9. Internship must still have open slots.
        if (internship.isFull()) {
            return false;
        }

        return true;
    }
}
