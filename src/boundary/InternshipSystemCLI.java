package boundary;
import control.InternshipManager;
import control.UserDataLoader;
import entity.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
        System.out.println("(3) View internship application status");
        System.out.println("(4) Request application withdrawal");
        System.out.println("(5) Exit");
        while (running){
            System.out.println("Enter choice: ");
            String choice = scanner.nextLine();
            switch (choice){
                case "1" -> student.displayDetails();
                case "2" -> showInternshipOpportunities(student);
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
                case "2" -> rep.createInternship();
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
        System.out.println("(2) Manage internship opportunities");
        System.out.println("(3) Change student withdrawal requests");
        System.out.println("(4) View internship information");
        System.out.println("(5) Change password");
        System.out.println("(6) Exit");
        while (running){
            System.out.println("Enter choice: ");
            String choice = scanner.nextLine();
            switch (choice){
                case "1" -> staff.displayDetails();
                case "5" -> staff.changePassword();
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void register() {
        System.out.println("I am a:\n1. Student\n2. Company Representative\n3. Career Center Staff\n4. Return");
        String choice = scanner.nextLine();
        if (choice.equals("1")){ 
            System.out.println("=== Student Registration ===");
        
            System.out.print("Enter Student ID: ");
            String studentID = scanner.nextLine().trim();
        
            System.out.print("Enter Name: ");
            String name = scanner.nextLine().trim();
        
            System.out.print("Enter Major: ");
            String major = scanner.nextLine().trim();
        
            System.out.print("Enter Year of Study: ");
            String year = scanner.nextLine();
        
            System.out.print("Enter Email: ");
            String email = scanner.nextLine().trim();
        
            File file = new File("data/sample_student_list.csv");
            boolean writeHeader = !file.exists() || file.length() == 0;
        
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
        
                // Write column header ONLY if file is new or empty
                if (writeHeader) {
                    writer.write("StudentID,Name,Major,Year,Email");
                    writer.newLine();
                }
        
                // Build CSV row manually
                String record = String.join(",", studentID, name, major, year, email);
        
                writer.write(record);
                writer.newLine();
                writer.flush();
        
                System.out.println("Registration completed! Welcome, " + name);
        
            } catch (IOException e) {
                System.out.println("Error writing student record: " + e.getMessage());
            }
        }

        if (choice.equals("2")){
            System.out.println("=== Company Representative Registration ===");
        
            System.out.print("Enter Company Rep ID: ");
            String companyrepid = scanner.nextLine().trim();
        
            System.out.print("Enter Name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter Company Name: ");
            String companyname = scanner.nextLine().trim();
        
            System.out.print("Enter Department: ");
            String department = scanner.nextLine().trim();
        
            System.out.print("Enter Position: ");
            String position = scanner.nextLine();
        
            System.out.print("Enter Email: ");
            String email = scanner.nextLine().trim();

            String approved = "false";
        
            File file = new File("data/sample_company_representative_list``.csv");
            boolean writeHeader = !file.exists() || file.length() == 0;
        
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
        
                // Write column header ONLY if file is new or empty
                if (writeHeader) {
                    writer.write("CompanyRepID,Name,CompanyName,Department,Position,Email,Approved");
                    writer.newLine();
                }
        
                // Build CSV row manually
                String record = String.join(",", companyrepid,name,companyname,department,position,email,approved);
                writer.write(record);
                writer.newLine();
                writer.flush();
        
                System.out.println("Registration completed! Welcome, " + name);
        
            } catch (IOException e) {
                System.out.println("Error writing student record: " + e.getMessage());
            }
        }

        if (choice.equals("3")){
            System.out.println("=== Career Staff Registration ===");
        
            System.out.print("Enter Staff ID: ");
            String staffid = scanner.nextLine().trim();
        
            System.out.print("Enter Name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter Role: ");
            String role = scanner.nextLine().trim();
        
            System.out.print("Enter Department: ");
            String department = scanner.nextLine().trim();

            System.out.print("Enter Email: ");
            String email = scanner.nextLine().trim();
        
            File file = new File("data/sample_staff_list``.csv");
            boolean writeHeader = !file.exists() || file.length() == 0;
        
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
        
                // Write column header ONLY if file is new or empty
                if (writeHeader) {
                    writer.write("StaffID,Name,Role,Department,Email");
                    writer.newLine();
                }
        
                // Build CSV row manually
                String record = String.join(",", staffid, name, role, department, email);
        
                writer.write(record);
                writer.newLine();
                writer.flush();
        
                System.out.println("Registration completed! Welcome, " + name);
        
            } catch (IOException e) {
                System.out.println("Error writing student record: " + e.getMessage());
            }
        }

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
    private String safeValue(String value) {
        return (value == null || value.isBlank()) ? "N/A" : value;
    }

    public static void main(String[] args){
        InternshipSystemCLI cli = new InternshipSystemCLI();
        cli.loadInitialData();
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
                case "4" -> cli.register();
                case "0" -> running = false;
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
        System.out.println("Goodbye.");
    }
}
