import boundary.AuthUI;
import boundary.RegistrationPortal;
import boundary.CareerCenterPortal;
import boundary.CompanyRepPortal;
import boundary.StudentPortal;
import boundary.UserRepositoryInterface;
import boundary.InternshipRepositoryInterface;
import boundary.ApplicationRepositoryInterface;
import boundary.FilterPreferenceStoreInterface;
import control.ApplicationController;
import control.AuthController;
import control.CareerCenterController;
import control.CompanyRepController;
import control.FilterController;
import control.InternshipController;
import control.NotificationController;
import control.RegistrationController;
import control.ReportController;
import control.StudentController;
import entities.User;
import java.io.File;
import java.util.Scanner;
import repository.ApplicationRepository;
import repository.FilterPreferenceStore;
import repository.InternshipRepository;
import repository.UserRepository;

/**
 * Console entry point that wires repositories, controllers, and boundaries.
 */
public class InternshipSystemCLI {

    public static void main(String[] args) {
        File studentCsv = new File("data/sample_student_list.csv");
        File staffCsv = new File("data/sample_staff_list.csv");
        File companyCsv = new File("data/sample_company_representative_list.csv");

        UserRepositoryInterface userRepository = new UserRepository();
        InternshipRepositoryInterface internshipRepository = new InternshipRepository();
        ApplicationRepositoryInterface applicationRepository = new ApplicationRepository();
        FilterPreferenceStoreInterface filterPreferenceStore = new FilterPreferenceStore();

        // Load predefined users from CSVs.
        userRepository.loadFromFile(studentCsv);
        userRepository.loadFromFile(staffCsv);
        userRepository.loadFromFile(companyCsv);

        ApplicationController applicationController = new ApplicationController(applicationRepository);
        InternshipController internshipController = new InternshipController(internshipRepository);
        FilterController filterController = new FilterController(filterPreferenceStore);
        StudentController studentController = new StudentController(applicationController);
        CompanyRepController companyRepController = new CompanyRepController(internshipController, applicationController);
        CareerCenterController careerCenterController = new CareerCenterController(userRepository, internshipController, applicationController);
        ReportController reportController = new ReportController(internshipRepository, applicationRepository);
        RegistrationController registrationController = new RegistrationController(
                userRepository, studentCsv, companyCsv, staffCsv);
        NotificationController notificationController = new NotificationController();
        AuthController authController = new AuthController(userRepository, notificationController);

        StudentPortal studentPortal = new StudentPortal(studentController, internshipController, applicationController, filterController);
        CompanyRepPortal companyRepPortal = new CompanyRepPortal(companyRepController, internshipController, applicationController, filterController);
        CareerCenterPortal careerCenterPortal = new CareerCenterPortal(careerCenterController, internshipController, applicationController, reportController);
        AuthUI authUI = new AuthUI(authController, studentPortal, companyRepPortal, careerCenterPortal);
        RegistrationPortal registrationPortal = new RegistrationPortal(registrationController);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("=== Internship System CLI ===");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Quit");
            System.out.print("Select an option: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> handleLogin(scanner, authController, authUI);
                case "2" -> registrationPortal.promptRegistration();
                case "3" -> running = false;
                default -> System.out.println("Invalid choice. Try again.");
            }
            System.out.println();
        }
        System.out.println("Exiting Internship System. Goodbye.");
    }

    private static void handleLogin(Scanner scanner, AuthController authController, AuthUI authUI) {
        System.out.print("User ID: ");
        String userId = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        User user = authController.login(userId, password);
        if (user == null) {
            System.out.println("Login failed. Check your credentials or approval status.");
            return;
        }
        System.out.println("Welcome, " + user.getName() + "!");
        authUI.routeUser(user);
    }
}
