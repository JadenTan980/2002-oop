package boundary;

import control.AuthController;
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

    public void promptPasswordChange() {
        // Placeholder for password change user interaction.
    }

    public void routeUser(User user) {
        // Placeholder to redirect authenticated user to the correct portal.
    }
}
