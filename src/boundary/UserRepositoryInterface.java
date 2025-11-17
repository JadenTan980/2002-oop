package boundary;

import java.io.File;
import java.util.Collection;
import entities.User;

/**
 * Contract for user persistence operations.
 */
public interface UserRepositoryInterface {

    User findById(String id);

    Collection<User> findAll();

    void save(User user);

    void loadFromFile(File file);
}
