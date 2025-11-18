import java.io.Console;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;

public class App {
    private static final Pattern STUDENT_ID_PATTERN = Pattern.compile("^U\\d{7}[A-Z]$");
    private static final Pattern STAFF_ID_PATTERN = Pattern.compile("^[A-Za-z]{3}\\d{3}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private static final String STUDENT_HEADER = "StudentID,Name,Major,Year,Email";
    private static final String STAFF_HEADER = "StaffID,Name,Role,Department,Email";
    private static final String COMPANY_HEADER = "CompanyRepID,Name,CompanyName,Department,Position,Email,Approved";

    private final UserManager userManager = new UserManager();
    private final InternshipManager internshipManager = new InternshipManager();
    private final ApplicationManager applicationManager = new ApplicationManager();
    private final WithdrawalManager withdrawalManager = new WithdrawalManager();
    private final ReportGenerator reportGenerator = new ReportGenerator();
    private final Scanner scanner = new Scanner(System.in);
    private final String studentDataPath;
    private final String staffDataPath;
    private final String companyDataPath;

    public App() {
        this(System.getProperty("students.file", "data/sample_student_list.csv"),
                System.getProperty("staff.file", "data/sample_staff_list.csv"),
                System.getProperty("reps.file", "data/sample_company_representative_list.csv"));
    }

    public App(String studentDataPath, String staffDataPath, String companyDataPath) {
        this.studentDataPath = studentDataPath;
        this.staffDataPath = staffDataPath;
        this.companyDataPath = companyDataPath;
        loadInitialUsers();
    }

    public static void main(String[] args) {
        new App().start();
    }

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("=== Internship Hub ===");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Quit");
            System.out.print("Select an option: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> {
                    User user = promptLogin();
                    if (user != null) {
                        routeUser(user);
                    }
                }
                case "2" -> handleRegistration();
                case "3" -> running = false;
                default -> System.out.println("Invalid choice. Please try again.");
            }
            System.out.println();
        }
        System.out.println("Goodbye.");
    }

    private void loadInitialUsers() {
        File studentCsv = new File(studentDataPath);
        File staffCsv = new File(staffDataPath);
        File companyCsv = new File(companyDataPath);
        try {
            userManager.loadAllUsers(studentCsv, staffCsv, companyCsv);
            System.out.println("Users loaded from configured CSV files.");
        } catch (IllegalStateException e) {
            System.err.println("Failed to load user data: " + e.getMessage());
        }
    }


    private User promptLogin() {
        int attempts = 0;
        while (true) {
            System.out.print("User ID (or 'cancel' to return): ");
            String id = scanner.nextLine().trim();
            if (id.equalsIgnoreCase("cancel")) {
                System.out.println("Login cancelled.");
                return null;
            }
            String password = readPassword("Password (type 'reset' to reset, 'cancel' to exit)");
            if ("cancel".equalsIgnoreCase(password)) {
                System.out.println("Login cancelled.");
                return null;
            }
            if ("reset".equalsIgnoreCase(password)) {
                handlePasswordReset(id);
                continue;
            }
            User user = userManager.login(id, password);
            if (user != null) {
                System.out.println("Welcome back, " + user.getName() + ".");
                return user;
            }
            System.out.println(userManager.getLastLoginMessage());
            attempts++;
            if (!promptYesNo("Try again? (y/n): ", true)) {
                return null;
            }
            if (attempts >= 3) {
                System.out.println("Tip: consider using the reset option if you continue to face issues.");
            }
        }
    }

    private void handlePasswordReset(String id) {
        if (id == null || id.isBlank()) {
            System.out.println("Provide your user ID before requesting a reset.");
            return;
        }
        System.out.print("Enter new temporary password (min 8 chars) or 'cancel': ");
        String newPass = scanner.nextLine();
        if ("cancel".equalsIgnoreCase(newPass)) {
            System.out.println("Reset cancelled.");
            return;
        }
        if (!userManager.resetPassword(id, newPass)) {
            System.out.println("Unable to reset password. Ensure the account exists and password meets requirements.");
        } else {
            System.out.println("Password updated. Use it to log in.");
        }
    }

    private String readPassword(String prompt) {
        Console console = System.console();
        if (console != null) {
            char[] chars = console.readPassword(prompt + ": ");
            return chars == null ? "" : new String(chars);
        }
        System.out.print(prompt + " (input visible): ");
        return scanner.nextLine();
    }

    private void routeUser(User user) {
        if (user instanceof Student student) {
            showStudentMenu(student);
        } else if (user instanceof CompanyRep rep) {
            showRepMenu(rep);
        } else if (user instanceof CareerCenterStaff staff) {
            showStaffMenu(staff);
        }
    }

    private void handleRegistration() {
        System.out.println("Select user type to register:");
        System.out.println("1. Student");
        System.out.println("2. Company Representative");
        System.out.println("3. Career Center Staff");
        System.out.println("4. Cancel");
        System.out.print("Choice: ");
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1" -> registerStudent();
            case "2" -> registerCompanyRep();
            case "3" -> registerCareerCenterStaff();
            case "4" -> System.out.println("Registration cancelled.");
            default -> System.out.println("Unknown user type.");
        }
    }

    private void registerStudent() {
        System.out.print("Student ID (e.g., U1234567A): ");
        String id = scanner.nextLine().trim().toUpperCase(Locale.ROOT);
        if (!STUDENT_ID_PATTERN.matcher(id).matches()) {
            System.out.println("Invalid ID format.");
            return;
        }
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }
        String password = promptPasswordInput("Password (min 8 chars): ");
        if (password == null) {
            return;
        }
        Integer year = readInt("Year of Study (1-6, or type 'cancel'): ", 1, 6, null, true);
        if (year == null) {
            System.out.println("Registration cancelled.");
            return;
        }
        System.out.print("Major: ");
        String major = scanner.nextLine().trim();
        if (!promptYesNo("Confirm registration? (y/n): ", true)) {
            System.out.println("Registration cancelled.");
            return;
        }
        boolean registered = userManager.registerStudent(id, name, password, year, major);
        if (registered) {
            persistStudentRecord(id, name, major, year);
            System.out.println("Student registered successfully.");
        } else {
            System.out.println("Registration failed. Ensure ID is unique and password meets requirements.");
        }
    }

    private void registerCompanyRep() {
        System.out.print("Company Rep ID (email): ");
        String id = scanner.nextLine().trim();
        if (!EMAIL_PATTERN.matcher(id).matches()) {
            System.out.println("Invalid email format.");
            return;
        }
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }
        String password = promptPasswordInput("Password (min 8 chars): ");
        if (password == null) {
            return;
        }
        System.out.print("Company Name: ");
        String companyName = scanner.nextLine().trim();
        if (companyName.isEmpty()) {
            System.out.println("Company name is required.");
            return;
        }
        System.out.print("Department: ");
        String department = scanner.nextLine().trim();
        System.out.print("Position: ");
        String position = scanner.nextLine().trim();
        if (!promptYesNo("Submit registration for approval? (y/n): ", true)) {
            System.out.println("Registration cancelled.");
            return;
        }
        boolean registered = userManager.registerCompanyRep(id, name, password, companyName, department, position);
        if (registered) {
            persistCompanyRepRecord(id, name, companyName, department, position);
            System.out.println("Registration submitted. A Career Center Staff member must approve your account before you can log in.");
        } else {
            System.out.println("Registration failed. Ensure all fields are valid and the ID has not been used.");
        }
    }

    private void registerCareerCenterStaff() {
        System.out.print("Staff ID (e.g., abc123): ");
        String id = scanner.nextLine().trim();
        if (!STAFF_ID_PATTERN.matcher(id).matches()) {
            System.out.println("Invalid staff ID format.");
            return;
        }
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }
        String password = promptPasswordInput("Password (min 8 chars): ");
        if (password == null) {
            return;
        }
        System.out.print("Department: ");
        String department = scanner.nextLine().trim();
        if (!promptYesNo("Confirm staff registration? (y/n): ", true)) {
            System.out.println("Registration cancelled.");
            return;
        }
        boolean registered = userManager.registerCareerCenterStaff(id, name, password, department);
        if (registered) {
            persistStaffRecord(id, name, department);
            System.out.println("Career Center Staff registered successfully.");
        } else {
            System.out.println("Registration failed. Ensure the ID is unique and password meets requirements.");
        }
    }

    private String promptPasswordInput(String prompt) {
        System.out.print(prompt);
        String password = scanner.nextLine();
        if (password.equalsIgnoreCase("cancel")) {
            System.out.println("Registration cancelled.");
            return null;
        }
        if (password.length() < 8) {
            System.out.println("Password must be at least 8 characters long.");
            return null;
        }
        return password;
    }

    private boolean promptYesNo(String prompt, boolean defaultYes) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return defaultYes;
            }
            if ("y".equalsIgnoreCase(input) || "yes".equalsIgnoreCase(input)) {
                return true;
            }
            if ("n".equalsIgnoreCase(input) || "no".equalsIgnoreCase(input)) {
                return false;
            }
            System.out.println("Please enter y or n.");
        }
    }

    private Integer readInt(String prompt, int min, int max, Integer defaultValue, boolean allowCancel) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty() && defaultValue != null) {
                return defaultValue;
            }
            if (allowCancel && input.equalsIgnoreCase("cancel")) {
                return null;
            }
            try {
                int value = Integer.parseInt(input);
                if (value < min || value > max) {
                    throw new NumberFormatException();
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number between " + min + " and " + max + ".");
            }
        }
    }

    private int readInt(String prompt, int min, int max) {
        Integer value = readInt(prompt, min, max, null, false);
        return value == null ? min : value;
    }

    private void persistStudentRecord(String id, String name, String major, int year) {
        String line = String.join(",", id, name, major, String.valueOf(year), "");
        appendCsvLine(studentDataPath, STUDENT_HEADER, line);
    }

    private void persistCompanyRepRecord(String id, String name, String company, String department, String position) {
        String line = String.join(",", id, name, company, department, position, id, "false");
        appendCsvLine(companyDataPath, COMPANY_HEADER, line);
    }

    private void persistStaffRecord(String id, String name, String department) {
        String line = String.join(",", id, name, "Career Center Staff", department, "");
        appendCsvLine(staffDataPath, STAFF_HEADER, line);
    }

    private void appendCsvLine(String path, String header, String line) {
        File file = new File(path);
        boolean exists = file.exists();
        try (FileWriter writer = new FileWriter(file, true)) {
            if (!exists) {
                writer.write(header);
                writer.write(System.lineSeparator());
            }
            writer.write(line);
            writer.write(System.lineSeparator());
        } catch (IOException e) {
            System.err.println("Failed to persist record to " + path + ": " + e.getMessage());
        }
    }

    public void showStudentMenu(Student student) {
        System.out.println("Student menu for " + student.getName());
        List<Internship> internships = internshipManager.getInternships();
        if (internships.isEmpty()) {
            System.out.println("No internships available yet.");
        } else {
            for (Internship internship : internships) {
                System.out.println("- " + internship.getTitle());
            }
        }
        System.out.println("Returning to main menu.");
    }

    public void showRepMenu(CompanyRep rep) {
        System.out.println("Representative menu for " + rep.getName());
        System.out.println("Pending account status: " + (rep.isApproved() ? "Approved" : "Awaiting approval"));
        System.out.println("Returning to main menu.");
    }

    public void showStaffMenu(CareerCenterStaff staff) {
        System.out.println("Staff menu for " + staff.getName());
        reportGenerator.generateByStatus(internshipManager.getInternships(), InternshipStatus.APPROVED);
        System.out.println("Returning to main menu.");
    }
}
