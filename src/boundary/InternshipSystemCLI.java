package boundary;
import control.UserDataLoader;
import entity.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class InternshipSystemCLI {
    //private attributes
    private static final Pattern STUDENT_ID_PATTERN = Pattern.compile("^U\\d{7}[A-Za-z]$");// U, 7 digits, 1 letter
    private static final String COMPANY_REP_CSV = "data/sample_company_representative_list.csv";
    private static final String COMPANY_REP_HEADER = "CompanyRepID,Name,CompanyName,Department,Position,Email,Approved";
    private ArrayList<User> users = new ArrayList<>();
    private User currentUser;
    private final Scanner scanner = new Scanner(System.in);
    private final UserDataLoader loader = new UserDataLoader();

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
        System.out.println("What do you want to do?");
        System.out.println("(1) Display details");
        System.out.println("(2) View internship opportunities");
        System.out.println("(3) View internship application status");
        System.out.println("(4) Request application withdrawal");
        System.out.println("(5) Exit");
        System.out.println("Enter choice: ");
        String choice = scanner.nextLine();
        switch (choice){
            case "1" -> student.displayDetails();
        }
     }
    public void displayRepMenu() { }
    public void displayStaffMenu() { }

    private void handleStudentLogin() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine().trim();
        if (!STUDENT_ID_PATTERN.matcher(id).matches()) {
            System.out.println("Invalid student ID.");
            return;
        }
        System.out.println("Password: ");
        User user = findUserById(id, Student.class);
        String password = scanner.nextLine().trim();
        if (!user.verifyPassword(password)){
            System.out.println("Incorrect Password");
            return;
        }
        
        if (user == null) {
            System.out.println("Student not found.");
            return;
        }
        
        currentUser = user;
        System.out.println("Welcome, " + user.getName());
        displayStudentMenu((Student)user);
        currentUser = null;
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
    

    private void handleCompanyRepLogin() {
        System.out.print("Enter Company Rep email: ");
        String email = scanner.nextLine().trim();
        System.out.println(("Enter Password: "));
        User user = findUserById(email, CompanyRep.class);
        String password = scanner.nextLine().trim();
        if (!user.verifyPassword(password)){
            System.out.println("Incorrect Password");
            return;
        }
        // verify password is correct
        if (!email.endsWith(".com")) {
            System.out.println("Invalid company email format.");
            return;
        }
        String[] record = getCompanyRepRecord(email);
        if (!isApprovedStatus(record[6])) {
            System.out.println("Your account is pending approval. Please wait for Career Center Staff to approve your registration.");
            return;
        }

        if (user == null) {
            System.out.println("Company Rep not found.");
            return;
        }

        displayStaffMenu();
        System.out.println("Welcome, " + user.getName());


    }

    private void handleCareerStaffLogin() {
        System.out.print("Enter Career Staff ID: ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        User user = findUserById(email, CareerCenterStaff.class);
        String password  = scanner.nextLine().trim();
        if (!user.verifyPassword(password)){
            System.out.println("Incorrect Password");
            return;
        }
        if (!email.endsWith("@ntu.edu.sg")) {
            System.out.println("Invalid email format for career staff.");
            return;
        }
        if (user == null) {
            System.out.println("Career staff not found.");
            return;
        }
        currentUser = user;
        System.out.println("Welcome, " + user.getName());
        displayStaffMenu();
        currentUser = null;
    }

    private User findUserById(String id, Class<? extends User> type) { // checks if user is of correct type
        for (User user : users) {
            if (type.isInstance(user) && user.getId().equalsIgnoreCase(id)) {
                return user;
            }
        }
        return null;
    }

    private String[] getCompanyRepRecord(String email) { // read from CSV
        Path path = Paths.get(COMPANY_REP_CSV);
        if (!Files.exists(path)) {
            return null;
        }
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line = reader.readLine(); // skip header
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

    // private CompanyRep ensureCompanyRepUser(String[] record) { // load or create CompanyRep user
    //     String email = record[5];
    //     User existing = findUserById(email, CompanyRep.class);
    //     if (existing != null) {
    //         return (CompanyRep) existing;
    //     }
    //     CompanyRep rep = new CompanyRep(email, record[1], "");
    //     rep.setAuthorised(isApprovedStatus(record[6]));
    //     users.add(rep);
    //     return rep;
    // }

    private boolean isApprovedStatus(String status) {
        return "true".equalsIgnoreCase(status) || "approved".equalsIgnoreCase(status);
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
                case "1" -> cli.handleStudentLogin();
                case "2" -> cli.handleCompanyRepLogin();
                case "3" -> cli.handleCareerStaffLogin();
                case "4" -> cli.register();
                case "0" -> running = false;
                default -> System.out.println("Invalid choice. Try again.");
            }

        // if successfully log in then can access manager
        }
        System.out.println("Goodbye.");
    }
}
