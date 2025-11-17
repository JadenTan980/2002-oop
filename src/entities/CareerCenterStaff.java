package entities;

/**
 * Represents staff overseeing the internship system.
 */
public class CareerCenterStaff extends User {

    private String department;

    public CareerCenterStaff(String id, String name, String password, String department) {
        super(id, name, password);
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
