package entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Student entity capturing education profile and applications.
 */
public class Student extends User {

    private int yearOfStudy;
    private String major;
    private final List<InternshipApplication> applications = new ArrayList<>();

    public Student(String id, String name, String password, int yearOfStudy, String major) {
        super(id, name, password);
        this.yearOfStudy = yearOfStudy;
        this.major = major;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public boolean addApplication(InternshipApplication application) {
        if (applications.size() >= 3) {
            return false;
        }
        return applications.add(application);
    }

    public void removeApplication(InternshipApplication application) {
        applications.remove(application);
    }

    public List<InternshipApplication> getApplications() {
        return Collections.unmodifiableList(applications);
    }
}
