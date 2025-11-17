import java.io.File;
import java.util.List;
import java.util.Scanner;


public class App {
    private final UserManager userManager = new UserManager();
    private final InternshipManager internshipManager = new InternshipManager();
    private final ApplicationManager applicationManager = new ApplicationManager();
    private final WithdrawalManager withdrawalManager = new WithdrawalManager();
    private final ReportGenerator reportGenerator = new ReportGenerator();
    private final Scanner scanner = new Scanner(System.in);

    public App() {
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
        File studentCsv = new File("data/sample_student_list.csv");
        File staffCsv = new File("data/sample_staff_list.csv");
        File companyCsv = new File("data/sample_company_representative_list.csv");
        userManager.loadAllUsers(studentCsv, staffCsv, companyCsv);
    }

    private User promptLogin() {
        System.out.print("User ID: ");
        String id = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        User user = userManager.login(id, password);
        if (user == null) {
            System.out.println(userManager.getLastLoginMessage());
        }
        return user;
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
        System.out.print("Choice: ");
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1" -> registerStudent();
            case "2" -> registerCompanyRep();
            case "3" -> registerCareerCenterStaff();
            default -> System.out.println("Unknown user type.");
        }
    }

    private void registerStudent() {
        System.out.print("Student ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        int year = readInt("Year of Study (1-4): ", 1, 4);
        System.out.print("Major: ");
        String major = scanner.nextLine().trim();
        boolean registered = userManager.registerStudent(id, name, password, year, major);
        if (registered) {
            System.out.println("Student registered successfully.");
        } else {
            System.out.println("Registration failed. User ID may already exist.");
        }
    }

    private void registerCompanyRep() {
        System.out.print("Company Rep ID (email): ");
        String id = scanner.nextLine().trim();
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Company Name: ");
        String companyName = scanner.nextLine().trim();
        System.out.print("Department: ");
        String department = scanner.nextLine().trim();
        System.out.print("Position: ");
        String position = scanner.nextLine().trim();
        boolean registered = userManager.registerCompanyRep(id, name, password, companyName, department, position);
        if (registered) {
            System.out.println("Registration submitted. A Career Center Staff member must approve your account before you can log in.");
        } else {
            System.out.println("Registration failed. User ID may already exist.");
        }
    }

    private void registerCareerCenterStaff() {
        System.out.print("Staff ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Department: ");
        String department = scanner.nextLine().trim();
        boolean registered = userManager.registerCareerCenterStaff(id, name, password, department);
        if (registered) {
            System.out.println("Career Center Staff registered successfully.");
        } else {
            System.out.println("Registration failed. User ID may already exist.");
        }
    }

    private int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
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

    public void showStudentMenu(Student student) {
        System.out.println("Student menu for " + student.getName());
        List<Internship> internships = internshipManager.getInternships();
        for (Internship internship : internships) {
            System.out.println("- " + internship.getTitle());
        }
    }

    public void showRepMenu(CompanyRep rep) {
        System.out.println("Rep menu for " + rep.getName());
    }

    public void showStaffMenu(CareerCenterStaff staff) {
        System.out.println("Staff menu for " + staff.getName());
        reportGenerator.generateByStatus(internshipManager.getInternships(), InternshipStatus.APPROVED);
    }
}
