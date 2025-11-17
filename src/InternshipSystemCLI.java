import boundary.AuthUI;
import boundary.RegistrationPortal;
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
        UserRepositoryInterface userRepository = new UserRepository();
        InternshipRepositoryInterface internshipRepository = new InternshipRepository();
        ApplicationRepositoryInterface applicationRepository = new ApplicationRepository();
        FilterPreferenceStoreInterface filterPreferenceStore = new FilterPreferenceStore();

        // Load predefined users from CSVs.
        userRepository.loadFromFile(new File("data/sample_student_list.csv"));
        userRepository.loadFromFile(new File("data/sample_staff_list.csv"));
        userRepository.loadFromFile(new File("data/sample_company_representative_list.csv"));

        ApplicationController applicationController = new ApplicationController(applicationRepository);
        InternshipController internshipController = new InternshipController(internshipRepository);
        FilterController filterController = new FilterController(filterPreferenceStore);
        StudentController studentController = new StudentController(applicationController);
        CompanyRepController companyRepController = new CompanyRepController(internshipController, applicationController);
        CareerCenterController careerCenterController = new CareerCenterController(userRepository, internshipController, applicationController);
        ReportController reportController = new ReportController(internshipRepository, applicationRepository);
        RegistrationController registrationController = new RegistrationController(userRepository);
        AuthController authController = new AuthController(userRepository);

        AuthUI authUI = new AuthUI(authController);
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
                case "1":
                    handleLogin(scanner, authController, authUI);
                    break;
                case "2":
                    registrationPortal.promptRegistration();
                    break;
                case "3":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
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
