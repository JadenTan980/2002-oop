import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class UserManager {
    private final List<User> users = new ArrayList<>();
    private final List<AccountRequest> pendingAccounts = new ArrayList<>();
    private String lastLoginMessage = "";

    public void loadAllUsers(File studentFile, File staffFile, File companyFile) {
        loadStudents(studentFile);
        loadStaff(staffFile);
        loadCompanyRepresentatives(companyFile);
    }

    public User login(String id, String pass) {
        lastLoginMessage = "Invalid credentials. Please try again.";
        if (id == null || pass == null) {
            return null;
        }
        String trimmedId = id.trim();
        for (User user : users) {
            if (user.getUserID().equalsIgnoreCase(trimmedId)) {
                if (!user.login(pass)) {
                    return null;
                }
                if (user instanceof CompanyRep rep && !rep.isApproved()) {
                    lastLoginMessage = "Company representative account pending approval.";
                    return null;
                }
                lastLoginMessage = "Login successful.";
                return user;
            }
        }
        return null;
    }

    public String getLastLoginMessage() {
        return lastLoginMessage;
    }

    public boolean registerStudent(String id, String name, String password, int year, String major) {
        if (userExists(id)) {
            return false;
        }
        Student student = new Student(id, name, password, year, major);
        return addUser(student);
    }

    public boolean registerCompanyRep(String id, String name, String password,
                                      String companyName, String department, String position) {
        if (userExists(id)) {
            return false;
        }
        CompanyRep representative = new CompanyRep(id, name, password, companyName, department, position, false);
        if (!addUser(representative)) {
            return false;
        }
        pendingAccounts.add(new AccountRequest(representative));
        return true;
    }

    public boolean registerCareerCenterStaff(String id, String name, String password, String department) {
        if (userExists(id)) {
            return false;
        }
        CareerCenterStaff staff = new CareerCenterStaff(id, name, password, department);
        return addUser(staff);
    }

    public boolean approveRepresentative(String repId, CareerCenterStaff approver) {
        if (repId == null || approver == null) {
            return false;
        }
        Iterator<AccountRequest> iterator = pendingAccounts.iterator();
        while (iterator.hasNext()) {
            AccountRequest request = iterator.next();
            if (request.getRep().getUserID().equalsIgnoreCase(repId)) {
                request.setApprover(approver);
                request.setStatus("Approved");
                request.getRep().setApproved(true);
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    public List<AccountRequest> getPendingAccounts() {
        return Collections.unmodifiableList(pendingAccounts);
    }

    private void loadStudents(File file) {
        if (!isReadable(file)) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length < 4) {
                    continue;
                }
                String id = tokens[0].trim();
                String name = tokens[1].trim();
                String major = tokens[2].trim();
                int year = parseInt(tokens[3].trim(), 1);
                registerStudent(id, name, User.DEFAULT_PASSWORD, year, major);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load students", e);
        }
    }

    private void loadStaff(File file) {
        if (!isReadable(file)) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length < 4) {
                    continue;
                }
                String id = tokens[0].trim();
                String name = tokens[1].trim();
                String department = tokens[3].trim();
                registerCareerCenterStaff(id, name, User.DEFAULT_PASSWORD, department);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load staff", e);
        }
    }

    private void loadCompanyRepresentatives(File file) {
        if (!isReadable(file)) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] tokens = line.split(",");
                if (tokens.length < 6) {
                    continue;
                }
                String id = tokens[0].trim();
                String name = tokens[1].trim();
                String company = tokens[2].trim();
                String department = tokens[3].trim();
                String position = tokens[4].trim();
                boolean approved = tokens.length > 6 && Boolean.parseBoolean(tokens[6].trim());
                CompanyRep rep = new CompanyRep(id, name, User.DEFAULT_PASSWORD, company, department, position, approved);
                addUser(rep);
                if (!approved) {
                    pendingAccounts.add(new AccountRequest(rep));
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load company representatives", e);
        }
    }

    private boolean addUser(User user) {
        if (user == null || userExists(user.getUserID())) {
            return false;
        }
        users.add(user);
        return true;
    }

    private boolean userExists(String id) {
        if (id == null) {
            return false;
        }
        for (User user : users) {
            if (user.getUserID().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    private boolean isReadable(File file) {
        return file != null && file.exists() && file.isFile();
    }

    private int parseInt(String value, int fallback) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

}
