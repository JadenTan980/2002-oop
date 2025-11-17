import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private int yearOfStudy;
    private String major;
    private final List<Application> applications = new ArrayList<>();

    public Student(String userID, String name, int yearOfStudy, String major) {
        super(userID, name, User.DEFAULT_PASSWORD);
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
        if (internship == null) {
            return false;
        }
        Application application = new Application(this, internship);
        applications.add(application);
        internship.addApplication(application);
        return true;
    }

    public void withdraw(Application application) {
        if (application == null) {
            return;
        }
        application.setStatus("Withdrawn");
    }

    public void acceptPlacement(Application application) {
        if (application != null) {
            application.markSuccessful();
        }
    }
}
