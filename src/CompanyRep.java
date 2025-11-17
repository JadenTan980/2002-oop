import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompanyRep extends User {
    private String companyName;
    private String department;
    private String position;
    private final List<Internship> internships = new ArrayList<>();
    private boolean approved;

    public CompanyRep(String userID, String name, String password,
                      String companyName, String department, String position,
                      boolean approved) {
        super(userID, name, password);
        this.companyName = companyName;
        this.department = department;
        this.position = position;
        this.approved = approved;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Internship createInternship(String title, String description) {
        Internship internship = new Internship(title, description, companyName, this);
        internships.add(internship);
        return internship;
    }

    public List<Application> viewApplications(Internship internship) {
        if (internship == null) {
            return Collections.emptyList();
        }
        return internship.getApplications();
    }

    public void toggleVisibility(Internship internship, boolean on) {
        if (internship != null) {
            internship.setVisibility(on);
        }
    }

    public List<Internship> getInternships() {
        return internships;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
