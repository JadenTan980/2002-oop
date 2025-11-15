package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Student extends User {
    private String year;
    private String major;

    private final List<Application> applications = new ArrayList<>();

    public Student(String userId, String name, String password, String year, String major) {
        super(userId, name, password);
        this.year = year;
        this.major = major;
    }
    


    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }

    public List<Application> getApplications(){return applications;}

    // functions

    public boolean applyInternship(Internship internship) {
        if (internship == null) return false;
        if (!canApply()) return false;

        String id = "APP-" + UUID.randomUUID().toString().substring(0,8).toUpperCase();

        Application app = new Application(id, this, internship, LocalDate.now());

        addApplication(app);
        return true;

    }

    public List<Application> viewApplications() {
        return Collections.unmodifiableList(applications);
    }


    public boolean acceptOffer(Application application) {
        if (application == null) return false;
        if (!applications.contains(application)) return false;
        if (application.getStatus() != ApplicationStatus.SUCCESSFUL) return false;
        if (hasAcceptedPlacement()) return false;

        application.markAccepted();
        application.updateStatus(ApplicationStatus.ACCEPTED);

        for (Application other : applications) {
            if (other != application) {
                ApplicationStatus st = other.getStatus();
                if (st == ApplicationStatus.PENDING || st == ApplicationStatus.ACCEPTED) {
                    other.updateStatus(ApplicationStatus.WITHDRAWN);
                }
            }
        }

        return true;
    }
    public boolean reqWithdrawal(Application application) {
        if (application == null) return false;
        return applications.contains(application);
    }
    public boolean canApply() {
        long activeCount = applications.stream()
                .map(Application::getStatus)
                .filter(st -> st == ApplicationStatus.PENDING || st == ApplicationStatus.SUCCESSFUL)
                .count();
        int maxApplications = 3;
        return activeCount < maxApplications && !hasAcceptedPlacement();
    }

    public boolean hasAcceptedPlacement() {
        return applications.stream().anyMatch(Application::isAccepted);
    }

    public void addApplication(Application application) {
        if (application != null) applications.add(application);
    }

    public void displayDetails() {
        System.out.println("Student ID: " + getId());
        System.out.println("Name: " + getName());
        System.out.println("Major: " + getMajor());
        System.out.println("Year: " + getYear());
        System.out.println("Applications submitted: " + applications.size());
        System.out.println("Password: " + getPassword());
    }

}
