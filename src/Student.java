import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private static final int MAX_ACTIVE_APPLICATIONS = 3;

    private final int yearOfStudy;
    private final String major;
    private final List<Application> applications = new ArrayList<>();
    private Application acceptedPlacement;

    public Student(String userID, String name, String password, int yearOfStudy, String major) {
        super(userID, name, password);
        this.yearOfStudy = yearOfStudy;
        this.major = major;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public String getMajor() {
        return major;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public boolean apply(Internship internship) {
        if (internship == null || !isEligibleForInternship(internship)) {
            return false;
        }
        Application application = new Application(this, internship);
        applications.add(application);
        internship.addApplication(application);
        return true;
    }

    public void withdraw(Application application) {
        if (application == null || !applications.contains(application)) {
            return;
        }
        if (application.getStatus() == ApplicationStatus.SUCCESSFUL) {
            throw new IllegalStateException("Cannot withdraw from an accepted placement.");
        }
        application.setStatus(ApplicationStatus.UNSUCCESSFUL);
    }

    public void acceptPlacement(Application application) {
        if (application == null || !applications.contains(application)) {
            throw new IllegalArgumentException("Application does not belong to student.");
        }
        application.markSuccessful();
        acceptedPlacement = application;
        for (Application other : applications) {
            if (other != application && other.getStatus() == ApplicationStatus.PENDING) {
                other.setStatus(ApplicationStatus.UNSUCCESSFUL);
            }
        }
    }

    public boolean hasAcceptedPlacement() {
        return acceptedPlacement != null;
    }

    public Application getAcceptedPlacement() {
        return acceptedPlacement;
    }

    private boolean isEligibleForInternship(Internship internship) {
        if (hasAcceptedPlacement()) {
            return false;
        }
        if (getActiveApplicationsCount() >= MAX_ACTIVE_APPLICATIONS) {
            return false;
        }
        for (Application application : applications) {
            if (application.getInternship() == internship) {
                return false;
            }
        }
        if (internship.getStatus() != InternshipStatus.APPROVED) {
            return false;
        }
        if (!internship.isVisible()) {
            return false;
        }
        String preferredMajor = internship.getPreferredMajor();
        if (preferredMajor != null && !preferredMajor.isBlank()
                && major != null && !preferredMajor.equalsIgnoreCase(major)) {
            return false;
        }
        InternshipLevel level = internship.getLevel();
        if (yearOfStudy <= 2 && level != null && level != InternshipLevel.BASIC) {
            return false;
        }
        LocalDate today = LocalDate.now();
        LocalDate openDate = internship.getOpenDate();
        if (openDate != null && today.isBefore(openDate)) {
            return false;
        }
        LocalDate closeDate = internship.getCloseDate();
        if (closeDate != null && today.isAfter(closeDate)) {
            return false;
        }
        return !internship.isFull();
    }

    private long getActiveApplicationsCount() {
        return applications.stream()
                .filter(app -> app.getStatus() == ApplicationStatus.PENDING)
                .count();
    }
}
