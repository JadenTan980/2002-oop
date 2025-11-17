import java.util.List;
import java.util.Scanner;

public class App {
    private final UserManager userManager = new UserManager();
    private final InternshipManager internshipManager = new InternshipManager();
    private final ApplicationManager applicationManager = new ApplicationManager();
    private final WithdrawalManager withdrawalManager = new WithdrawalManager();
    private final ReportGenerator reportGenerator = new ReportGenerator();
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        new App().start();
    }

    public void start() {
        boolean running = true;
        while (running) {
            User user = showLoginMenu();
            if (user instanceof Student student) {
                showStudentMenu(student);
            } else if (user instanceof CompanyRep rep) {
                showRepMenu(rep);
            } else if (user instanceof CareerCenterStaff staff) {
                showStaffMenu(staff);
            } else {
                running = false;
            }
        }
    }

    public User showLoginMenu() {
        System.out.print("User ID: ");
        String id = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        User user = userManager.login(id, password);
        if (user == null) {
            System.out.println("Invalid credentials");
        }
        return user;
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
        reportGenerator.generateByStatus(internshipManager.getInternships(), "Approved");
    }
}
