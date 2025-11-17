package control;

import java.io.File;
import boundary.UserRepositoryInterface;
import entities.CompanyRepresentative;
import entities.User;

/**
 * Handles authentication-related use cases for all user types.
 */
public class AuthController {

    private final UserRepositoryInterface userRepository;

    public AuthController(UserRepositoryInterface userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String id, String password) {
        User user = userRepository.findById(id);
        boolean authenticated = user != null && user.getPassword().equals(password);
        boolean approved = !(user instanceof CompanyRepresentative)
                || ((CompanyRepresentative) user).isApproved();
        return authenticated && approved ? user : null;
    }

    public void logout(User user) {
        // Placeholder for logout auditing logic.
    }

    public boolean changePassword(User user, String newPassword) {
        if (user == null || newPassword == null || newPassword.isEmpty()) {
            return false;
        }
        user.setPassword(newPassword);
        userRepository.save(user);
        return true;
    }

    public void loadUsersFromFile(File file) {
        userRepository.loadFromFile(file);
    }
}
