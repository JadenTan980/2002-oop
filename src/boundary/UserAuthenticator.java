package boundary;

import entity.CareerCenterStaff;
import entity.CompanyRep;
import entity.Student;
import entity.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.regex.Pattern;

public class UserAuthenticator {
    private static final Pattern STUDENT_ID_PATTERN = Pattern.compile("^U\\d{7}[A-Za-z]$");
    private static final String COMPANY_REP_CSV = "data/sample_company_representative_list.csv";
    private final Scanner scanner;
    private final InternshipSystemCLI cli;

    public UserAuthenticator(Scanner scanner, InternshipSystemCLI cli) {
        this.scanner = scanner;
        this.cli = cli;
    }

    public void handleStudentLogin() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine().trim();
        if (!STUDENT_ID_PATTERN.matcher(id).matches()) {
            System.out.println("Invalid student ID.");
            return;
        }
        System.out.println("Password: ");
        User user = findUserById(id, Student.class);
        if (user == null) {
            System.out.println("Student not found.");
            return;
        }
        String password = scanner.nextLine().trim();
        if (!user.verifyPassword(password)){
            System.out.println("Incorrect Password");
            return;
        }
        cli.setCurrentUser(user);
        System.out.println("Welcome, " + user.getName());
        cli.displayStudentMenu((Student)user);
        cli.setCurrentUser(null);
    }

    public void handleCompanyRepLogin() {
        System.out.print("Enter Company Rep email: ");
        String email = scanner.nextLine().trim();
        System.out.println(("Enter Password: "));
        User user = findUserById(email, CompanyRep.class);
        if (user == null) {
            System.out.println("Company Rep not found.");
            return;
        }
        String password = scanner.nextLine().trim();
        if (!user.verifyPassword(password)){
            System.out.println("Incorrect Password");
            return;
        }
        if (!email.endsWith(".com")) {
            System.out.println("Invalid company email format.");
            return;
        }
        String[] record = getCompanyRepRecord(email);
        if (!isApprovedStatus(record[6])) {
            System.out.println("Your account is pending approval. Please wait for Career Center Staff to approve your registration.");
            return;
        }
        System.out.println("Welcome, " + user.getName());
        cli.displayRepMenu((CompanyRep)user);
    }

    public void handleCareerStaffLogin() {
        System.out.print("Enter Career Staff ID: ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password  = scanner.nextLine().trim();
        User user = findUserById(email, CareerCenterStaff.class);
        if (user == null) {
            System.out.println("Staff not found.");
            return;
        }
        if (!user.verifyPassword(password)){
            System.out.println("Incorrect Password");
            return;
        }
        if (!email.endsWith("@ntu.edu.sg")) {
            System.out.println("Invalid email format for career staff.");
            return;
        }
        cli.setCurrentUser(user);
        cli.displayStaffMenu((CareerCenterStaff)user);
        System.out.println("Welcome, " + user.getName());
        cli.setCurrentUser(null);
    }

    private User findUserById(String id, Class<? extends User> type) {
        for (User user : cli.getUsers()) {
            if (type.isInstance(user) && user.getId().equalsIgnoreCase(id)) {
                return user;
            }
        }
        return null;
    }

    private String[] getCompanyRepRecord(String email) {
        Path path = Paths.get(COMPANY_REP_CSV);
        if (!Files.exists(path)) {
            return null;
        }
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] columns = line.split(",", -1);
                if (columns.length < 7) continue;
                if (columns[5].equalsIgnoreCase(email)) {
                    return columns;
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to read company representative list: " + e.getMessage());
        }
        return null;
    }

    private boolean isApprovedStatus(String status) {
        return "true".equalsIgnoreCase(status) || "approved".equalsIgnoreCase(status);
    }
}
