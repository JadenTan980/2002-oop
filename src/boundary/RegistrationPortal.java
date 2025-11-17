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

    public boolean registerStudent(String id, String name, int yearOfStudy, String major, String password) {
        return registrationController.registerStudent(id, name, password, yearOfStudy, major);
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
            case "student":
                handleStudentRegistration(scanner);
                break;
            case "company":
                handleCompanyRegistration(scanner);
                break;
            case "staff":
                handleStaffRegistration(scanner);
                break;
            default:
                System.out.println("Unknown user type: " + type);
        }
    }

    private void handleStudentRegistration(Scanner scanner) {
        System.out.print("Student ID: ");
        String id = scanner.nextLine();
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Year of Study (1-4): ");
        int year = parseInteger(scanner.nextLine(), 1);
        System.out.print("Major: ");
        String major = scanner.nextLine();
        System.out.print("Password (leave blank for default): ");
        String password = scanner.nextLine();
        boolean success = registerStudent(id, name, year, major, password);
        System.out.println(success ? "Student registered." : "Registration failed.");
    }

    private void handleCompanyRegistration(Scanner scanner) {
        System.out.print("Company Rep Email/ID: ");
        String id = scanner.nextLine();
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Company Name: ");
        String company = scanner.nextLine();
        System.out.print("Department: ");
        String department = scanner.nextLine();
        System.out.print("Position: ");
        String position = scanner.nextLine();
        System.out.print("Password (leave blank for default): ");
        String password = scanner.nextLine();
        boolean success = registerCompanyRepresentative(id, name, company, department, position, password);
        System.out.println(success ? "Company representative registered (awaiting approval)." : "Registration failed.");
    }

    private void handleStaffRegistration(Scanner scanner) {
        System.out.print("Staff ID: ");
        String id = scanner.nextLine();
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Department: ");
        String department = scanner.nextLine();
        System.out.print("Password (leave blank for default): ");
        String password = scanner.nextLine();
        boolean success = registerCareerStaff(id, name, department, password);
        System.out.println(success ? "Career center staff registered." : "Registration failed.");
    }

    private int parseInteger(String value, int fallback) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return fallback;
        }
    }
}
