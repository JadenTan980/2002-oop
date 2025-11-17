package control;

import java.io.File;
import entities.User;
import entities.CompanyRepresentative;
import repository.UserRepository;

/**
 * Handles authentication-related use cases for all user types.
 */
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
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

    public boolean registerUser(User user) {
        if (user == null || user.getId() == null || userRepository.findById(user.getId()) != null) {
            return false;
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword("password");
        }
        userRepository.save(user);
        return true;
    }
}
