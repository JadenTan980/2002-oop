package boundary;

import control.AuthController;
import entities.CareerCenterStaff;
import entities.Student;
import entities.User;

/**
 * Entry point boundary for all users to authenticate and reach their portals.
 */
public class AuthUI {

    private final AuthController authController;

    public AuthUI(AuthController authController) {
        this.authController = authController;
    }

    public void displayLogin() {
        // Placeholder for login prompt rendering.
    }

    public void displayRegistrationForm() {
        // Collect registration data (e.g., for company representatives).
    }

    public void promptPasswordChange() {
        // Placeholder for password change user interaction.
    }

    public boolean registerStudent(String id, String name, int yearOfStudy, String major) {
        Student student = new Student(id, name, "password", yearOfStudy, major);
        return authController.registerUser(student);
    }

    public boolean registerCareerStaff(String id, String name, String department) {
        CareerCenterStaff staff = new CareerCenterStaff(id, name, "password", department);
        return authController.registerUser(staff);
    }

    public void routeUser(User user) {
        // Placeholder to redirect authenticated user to the correct portal.
    }
}
