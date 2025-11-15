package boundary;
import control.InternshipManager;
import control.UserDataLoader;
import entity.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InternshipSystemCLI {
    //private attributes
    private ArrayList<User> users = new ArrayList<>();
    private User currentUser;
    private final Scanner scanner = new Scanner(System.in);
    private final UserDataLoader loader = new UserDataLoader();
    private final InternshipManager internshipManager;
    private final UserAuthenticator authenticator;

    public InternshipSystemCLI() {
        this(new InternshipManager());
    }

    public InternshipSystemCLI(InternshipManager internshipManager) {
        this.internshipManager = internshipManager != null ? internshipManager : new InternshipManager();
        this.authenticator = new UserAuthenticator(scanner, this);
    }

    //getters and setters
    public ArrayList<User> getUsers() { return users; }
    public void setUsers(ArrayList<User> users) { this.users = users; }
    public User getCurrentUser() { return currentUser; }
    public void setCurrentUser(User currentUser) { this.currentUser = currentUser; }

    public void loadInitialData() {
        List<Student> students = loader.loadStudents();
        List<CareerCenterStaff> staff = loader.loadStaff();
        List<CompanyRep> reps = loader.loadApprovedCompanyReps();
        users.addAll(students);
        users.addAll(staff);
        users.addAll(reps);
    }
    
    public void displayStudentMenu(Student student) {
        boolean running = true;
        System.out.println("What do you want to do?");
        System.out.println("(1) Display details");
        System.out.println("(2) View internship opportunities");
        System.out.println("(3) View your internship applications");
        System.out.println("(4) Request application withdrawal");
        System.out.println("(5) Change password");
        System.out.println("(6) Exit");
        while (running){
            System.out.println("Enter choice: ");
            String choice = scanner.nextLine();
            switch (choice){
                case "1" -> student.displayDetails();
                case "2" -> showInternshipOpportunities(student);
                case "3" -> {
                    syncStudentApplications(student);
                    student.viewApplications();
                }
                case "4" -> {
                    syncStudentApplications(student);
                    student.reqWithdrawal();
                }
                case "5" -> student.changePassword();
                case "6" -> running = false;
                default -> System.out.println("Invalid choice. Try again.");
            }  
        }
        System.out.println("Exiting...");
    }

    public void displayRepMenu(CompanyRep rep) {
        boolean running = true;
        System.out.println("What do you want to do?");
        System.out.println("(1) Display details");
        System.out.println("(2) Create internship");
        System.out.println("(3) Manage internship applications");
        System.out.println("(4) Change internship visibility");
        System.out.println("(5) Change password");
        System.out.println("(6) Exit");
        while (running){
            System.out.println("Enter choice: ");
            String choice = scanner.nextLine();
            switch (choice){
                case "1" -> rep.displayDetails();
                case "2" -> {
                    Internship created = rep.createInternship();
                    if (created != null && !internshipManager.getInternships().contains(created)) {
                        internshipManager.getInternships().add(created);
                    }
                }
                case "3" -> {
                    syncRepInternships(rep);
                    rep.manageApplications();
                }
                case "4" -> {
                    syncRepInternships(rep);
                    rep.toggleVisibility();
                }
                case "5" -> rep.changePassword();
                case "6" -> running = false;
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
        System.out.println("Exiting...");
    }

    public void displayStaffMenu(CareerCenterStaff staff) { 
        boolean running = true;
        System.out.println("What do you want to do?");
        System.out.println("(1) Display details");
        System.out.println("(2) Manage internship requests");
        System.out.println("(3) Manage student withdrawal requests");
        System.out.println("(4) Manage Company Rep account creation");
        System.out.println("(5) View internship information");
        System.out.println("(6) Generate reports");
        System.out.println("(7) Change password");
        System.out.println("(8) Exit");
        while (running){
            System.out.println("Enter choice: ");
            String choice = scanner.nextLine();
            switch (choice){
                case "1" -> staff.displayDetails();
                case "4" -> handleRepAuthorization(staff);
                case "7" -> staff.changePassword();
                case "8" -> running = false;
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
        System.out.println("Exiting...");
    }

    private void showInternshipOpportunities(Student student) {
        List<Internship> opportunities = internshipManager.getVisibleInternships(student);
        if (opportunities.isEmpty()) {
            System.out.println("No internships available for your profile at the moment.");
            return;
        }

        System.out.println("Available internships:");
        for (int i = 0; i < opportunities.size(); i++) {
            Internship internship = opportunities.get(i);
            String companyName = internship.getCompany() != null
                    ? safeValue(internship.getCompany().getCompanyName())
                    : "Unknown Company";
            System.out.printf("%d. %s at %s [%s]%n",
                    i + 1,
                    safeValue(internship.getTitle()),
                    companyName,
                    safeValue(internship.getLevel()));
            System.out.println("   Preferred major: " + safeValue(internship.getPreferredMajor()));
            if (internship.getOpenDate() != null || internship.getCloseDate() != null) {
                System.out.println("   Open: " + internship.getOpenDate() + " | Close: " + internship.getCloseDate());
            }
        }

        System.out.print("Enter internship number to apply or press Enter to cancel: ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            System.out.println("Returning to menu.");
            return;
        }
        try {
            int index = Integer.parseInt(input);
            if (index < 1 || index > opportunities.size()) {
                System.out.println("Invalid option.");
                return;
            }
            Internship selected = opportunities.get(index - 1);
            if (student.applyInternship(selected)) {
                System.out.println("Application submitted for " + safeValue(selected.getTitle()) + ".");
            } else {
                System.out.println("Unable to apply. You may have reached your application limit or already accepted an offer.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Returning to menu.");
        }
    }
    private void syncStudentApplications(Student student) { //prunes entries whose internships do not exist in manager
        if (student == null) return;
        student.getApplications().removeIf(app ->
                app == null || app.getInternship() == null ||
                        !internshipManager.getInternships().contains(app.getInternship()));
    }

    private void syncRepInternships(CompanyRep rep) { //pulls internships owned by rep
        ArrayList<Internship> owned = new ArrayList<>();
        for (Internship internship : internshipManager.getInternships()) {
            if (internship.getCompany() != null && internship.getCompany().equals(rep.getCompany())) {
                owned.add(internship);
            }
        }
        rep.setInternships(owned);
    }

    private void handleRepAuthorization(CareerCenterStaff staff) {
        ArrayList<CompanyRep> pending = new ArrayList<>();
        for (User user : users) {
            if (user instanceof CompanyRep rep && !rep.isAuthorised()) {
                pending.add(rep);
            }
        }
        if (pending.isEmpty()) {
            System.out.println("No pending company representatives.");
            return;
        }
        System.out.println("Select a representative to review:");
        for (int i = 0; i < pending.size(); i++) {
            System.out.printf("%d. %s (%s)%n", i + 1, pending.get(i).getName(), pending.get(i).getEmail());
        }
        System.out.print("Choice: ");
        int selection;
        try {
            selection = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid selection.");
            return;
        }
        if (selection < 1 || selection > pending.size()) {
            System.out.println("Invalid selection.");
            return;
        }
        System.out.print("Approve? (y/n): ");
        boolean approve = scanner.nextLine().trim().equalsIgnoreCase("y");
        staff.authoriseRep(pending.get(selection - 1), approve);
    }
    private String safeValue(String value) {
        return (value == null || value.isBlank()) ? "N/A" : value;
    }

    public static void main(String[] args){
        InternshipSystemCLI cli = new InternshipSystemCLI();
        cli.loadInitialData(); //Loads all users from csv, password set to "password by" default
        System.out.println("Welcome to the Internship Management System.");
        boolean running = true;
        while (running) {
            System.out.println("\nSelect user type:");
            System.out.println("1. Student");
            System.out.println("2. Company Representative");
            System.out.println("3. Career Center Staff");
            System.out.println("4. Register a new user");
            System.out.println("0. Exit");
            System.out.print("Choice: ");
            String choice = cli.scanner.nextLine().trim();
            switch (choice) {
                case "1" -> cli.authenticator.handleStudentLogin();
                case "2" -> cli.authenticator.handleCompanyRepLogin();
                case "3" -> cli.authenticator.handleCareerStaffLogin();
                case "4" -> cli.authenticator.register();
                case "0" -> running = false;
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
        System.out.println("Goodbye.");
    }
}
