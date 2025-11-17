package entities;

/**
 * Company representative entity representing one contact per company.
 */
public class CompanyRepresentative extends User {

    private String companyName;
    private String department;
    private String position;
    private boolean approved;

    public CompanyRepresentative(String id, String name, String password,
                                 String companyName, String department,
                                 String position) {
        super(id, name, password);
        this.companyName = companyName;
        this.department = department;
        this.position = position;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
