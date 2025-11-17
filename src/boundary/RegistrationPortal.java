package boundary;

import control.RegistrationController;
import java.util.Scanner;

/**
 * Boundary for collecting registration data for all user roles.
 */
public class RegistrationPortal {

    private final RegistrationController registrationController;

    public RegistrationPortal(RegistrationController registrationController) {
        this.registrationController = registrationController;
    }

    public boolean registerStudent(String id, String name, int yearOfStudy, String major, String email, String password) {
        return registrationController.registerStudent(id, name, password, yearOfStudy, major, email);
    }

    public boolean registerCompanyRepresentative(String email,
                                                 String name,
                                                 String companyName,
                                                 String department,
                                                 String position,
                                                 String password) {
        return registrationController.registerCompanyRepresentative(
                email, name, password, companyName, department, position);
    }

    public boolean registerCareerStaff(String id, String name, String department, String password) {
        return registrationController.registerCareerStaff(id, name, password, department);
    }

    /**
     * Prompts console users to select a role and input mandatory information.
     */
    public void promptRegistration() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select user type to register (student/company/staff): ");
        String type = scanner.nextLine().trim().toLowerCase();
        switch (type) {
            case "student" -> handleStudentRegistration(scanner);
            case "company" -> handleCompanyRegistration(scanner);
            case "staff" -> handleStaffRegistration(scanner);
            default -> System.out.println("Unknown user type: " + type);
        }
    }

    private void handleStudentRegistration(Scanner scanner) {
        String id = promptStudentId(scanner);
        String name = promptNonEmpty("Name", scanner);
        int year = promptYearOfStudy(scanner);
        String major = promptNonEmpty("Major", scanner);
        String email = promptEmail("Email", scanner);
        System.out.print("Password (leave blank for default): ");
        String password = scanner.nextLine();
        boolean success = registerStudent(id, name, year, major, email, password);
        System.out.println(success ? "Student registered." : "Registration failed.");
    }

    private void handleCompanyRegistration(Scanner scanner) {
        String id = promptEmail("Company Rep Email/ID", scanner);
        String name = promptNonEmpty("Name", scanner);
        String company = promptNonEmpty("Company Name", scanner);
        String department = promptNonEmpty("Department", scanner);
        String position = promptNonEmpty("Position", scanner);
        System.out.print("Password (leave blank for default): ");
        String password = scanner.nextLine();
        boolean success = registerCompanyRepresentative(id, name, company, department, position, password);
        System.out.println(success ? "Company representative registered (awaiting approval)." : "Registration failed.");
    }

    private void handleStaffRegistration(Scanner scanner) {
        String id = promptNonEmpty("Staff ID", scanner);
        String name = promptNonEmpty("Name", scanner);
        String department = promptNonEmpty("Department", scanner);
        System.out.print("Password (leave blank for default): ");
        String password = scanner.nextLine();
        boolean success = registerCareerStaff(id, name, department, password);
        System.out.println(success ? "Career center staff registered." : "Registration failed.");
    }

    private String promptStudentId(Scanner scanner) {
        while (true) {
            System.out.print("Student ID (Format: U1234567X): ");
            String id = scanner.nextLine().trim();
            if (registrationController.isValidStudentIdFormat(id)) {
                return id;
            }
            System.out.println("Invalid student ID format. Please try again.");
        }
    }

    private String promptNonEmpty(String label, Scanner scanner) {
        while (true) {
            System.out.print(label + ": ");
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println(label + " cannot be empty. Please try again.");
        }
    }

    private int promptYearOfStudy(Scanner scanner) {
        while (true) {
            System.out.print("Year of Study (1-4): ");
            String value = scanner.nextLine().trim();
            try {
                int year = Integer.parseInt(value);
                if (year >= 1 && year <= 4) {
                    return year;
                }
            } catch (NumberFormatException ignored) {
            }
            System.out.println("Invalid year. Enter a number from 1 to 4.");
        }
    }

    private String promptEmail(String label, Scanner scanner) {
        while (true) {
            System.out.print(label + ": ");
            String email = scanner.nextLine().trim();
            if (isValidEmail(email)) {
                return email;
            }
            System.out.println("Invalid email. Please enter a valid email address.");
        }
    }

    private boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".") && !email.startsWith("@");
    }
}
