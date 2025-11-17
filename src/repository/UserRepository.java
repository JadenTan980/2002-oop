package repository;

import boundary.UserRepositoryInterface;
import entities.CareerCenterStaff;
import entities.CompanyRepresentative;
import entities.Student;
import entities.User;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * In-memory implementation of the user repository.
 */
public class UserRepository implements UserRepositoryInterface {

    private static final String DEFAULT_PASSWORD = "password";

    private final Map<String, User> users = new HashMap<>();

    @Override
    public User findById(String id) {
        return users.get(id);
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public void save(User user) {
        if (user == null || user.getId() == null) {
            return;
        }
        users.put(user.getId(), user);
    }

    @Override
    public void loadFromFile(File file) {
        if (file == null || !file.exists()) {
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
            String header = reader.readLine();
            if (header == null) {
                return;
            }
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                if (header.contains("StudentID")) {
                    loadStudent(line);
                } else if (header.contains("StaffID")) {
                    loadStaff(line);
                } else if (header.contains("CompanyRepID")) {
                    loadCompanyRep(line);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load users from " + file.getName(), e);
        }
    }

    private void loadStudent(String line) {
        String[] tokens = line.split(",", -1);
        if (tokens.length < 4) {
            return;
        }
        String id = tokens[0].trim();
        String name = tokens[1].trim();
        String major = tokens[2].trim();
        int year = parseInt(tokens[3].trim(), 1);
        Student student = new Student(id, name, DEFAULT_PASSWORD, year, major);
        saveIfAbsent(student);
    }

    private void loadStaff(String line) {
        String[] tokens = line.split(",", -1);
        if (tokens.length < 4) {
            return;
        }
        String id = tokens[0].trim();
        String name = tokens[1].trim();
        String department = tokens[3].trim();
        CareerCenterStaff staff = new CareerCenterStaff(id, name, DEFAULT_PASSWORD, department);
        saveIfAbsent(staff);
    }

    private void loadCompanyRep(String line) {
        String[] tokens = line.split(",", -1);
        if (tokens.length < 5) {
            return;
        }
        String id = tokens[0].trim();
        String name = tokens[1].trim();
        String companyName = tokens[2].trim();
        String department = tokens[3].trim();
        String position = tokens[4].trim();
        boolean approved = tokens.length > 6 && parseBoolean(tokens[6]);
        CompanyRepresentative representative = new CompanyRepresentative(
                id, name, DEFAULT_PASSWORD, companyName, department, position);
        representative.setApproved(approved);
        saveIfAbsent(representative);
    }

    private void saveIfAbsent(User user) {
        if (user != null && user.getId() != null && !users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        }
    }

    private int parseInt(String value, int fallback) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    private boolean parseBoolean(String value) {
        if (value == null) {
            return false;
        }
        String normalized = value.trim().toLowerCase();
        return normalized.equals("true") || normalized.equals("yes") || normalized.equals("1");
    }
}
