import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private final List<User> users = new ArrayList<>();
    private final List<AccountRequest> pendingAccounts = new ArrayList<>();

    public User login(String id, String pass) {
        for (User user : users) {
            if (user.getUserID().equals(id) && user.login(pass)) {
                return user;
            }
        }
        return null;
    }

    public void addUser(User user) {
        if (user != null) {
            users.add(user);
        }
    }

    public void loadUsers(File file) {
        if (file == null || !file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 5) {
                    continue;
                }
                Student student = new Student(parts[0].trim(), parts[1].trim(),
                        Integer.parseInt(parts[3].trim()), parts[4].trim());
                addUser(student);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load users", e);
        }
    }

    public void submitAccountRequest(AccountRequest request) {
        if (request != null) {
            pendingAccounts.add(request);
        }
    }

    public void approveRepAccount(AccountRequest req) {
        if (req == null) {
            return;
        }
        req.setStatus("Approved");
        pendingAccounts.remove(req);
        addUser(req.getRep());
    }

    public List<AccountRequest> getPendingAccounts() {
        return pendingAccounts;
    }
}
