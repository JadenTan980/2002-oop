package boundary;

import control.AuthController;
import entities.CareerCenterStaff;
import entities.CompanyRepresentative;
import entities.Student;
import entities.User;

/**
 * Entry point boundary for all users to authenticate and reach their portals.
 */
public class AuthUI {

    private final AuthController authController;
    private final StudentPortal studentPortal;
    private final CompanyRepPortal companyRepPortal;
    private final CareerCenterPortal careerCenterPortal;

    public AuthUI(AuthController authController,
                  StudentPortal studentPortal,
                  CompanyRepPortal companyRepPortal,
                  CareerCenterPortal careerCenterPortal) {
        this.authController = authController;
        this.studentPortal = studentPortal;
        this.companyRepPortal = companyRepPortal;
        this.careerCenterPortal = careerCenterPortal;
    }

    public void displayLogin() {
        // Placeholder for login prompt rendering.
    }

    public void promptPasswordChange() {
        // Placeholder for password change user interaction.
    }

    public void routeUser(User user) {
        if (user instanceof Student) {
            studentPortal.showDashboard((Student) user);
        } else if (user instanceof CompanyRepresentative) {
            companyRepPortal.displayMenu((CompanyRepresentative) user);
        } else if (user instanceof CareerCenterStaff) {
            careerCenterPortal.displayMenu((CareerCenterStaff) user);
        } else {
            System.out.println("Unknown user type: " + user.getClass().getSimpleName());
        }
    }
}
