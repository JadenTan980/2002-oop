package repository;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import entities.User;

/**
 * Primitive in-memory repository for users.
 */
public class UserRepository {

    private final Map<String, User> users = new HashMap<>();

    public User findById(String id) {
        return users.get(id);
    }

    public Collection<User> findAll() {
        return users.values();
    }

    public void save(User user) {
        users.put(user.getId(), user);
    }

    public void loadFromFile(File file) {
        // Placeholder for CSV parsing logic during bootstrap.
    }
}
