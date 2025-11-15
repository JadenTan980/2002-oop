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
    public void displayStudentMenu() { }
    public void displayRepMenu() { }
    public void displayStaffMenu() { }

    private void handleStudentLogin() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine().trim();
        if (!STUDENT_ID_PATTERN.matcher(id).matches()) {
            System.out.println("Invalid student ID.");
            return;
        }
        User user = findUserById(id, Student.class);
        if (user == null) {
            System.out.println("Student not found.");
            return;
        }
        
        currentUser = user;
        System.out.println("Welcome, " + user.getName());
        displayStudentMenu();
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
        
                // Add to in-memory users list
                Student student = new Student(studentID, name, major, Integer.parseInt(year), email);
                users.add(student);
        
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

            String approved = "0";
        
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
    }
    

    private void handleCompanyRepLogin() {
        System.out.print("Enter Company Rep email: ");
        String email = scanner.nextLine().trim();
        if (!email.endsWith(".com")) {
            System.out.println("Invalid company email format.");
            return;
        }
        String[] record = getCompanyRepRecord(email);
        if (record == null) {
            record = registerCompanyRep(email);
            if (record == null) {
                System.out.println("Unable to register company representative at this time.");
                return;
            }
            System.out.println("Registration submitted. Await approval from Career Center Staff.");
            return;
        }
        if (!isApprovedStatus(record[6])) {
            System.out.println("Your account is pending approval. Please wait for Career Center Staff to approve your registration.");
            return;
        }


    }

    private void handleCareerStaffLogin() {
        System.out.print("Enter Career Staff ID: ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password  = scanner.nextLine().trim();
        if (!email.endsWith("@ntu.edu.sg")) {
            System.out.println("Invalid email format for career staff.");
            return;
        }
        User user = findUserById(email, password, CareerCenterStaff.class);
        if (user == null) {
            System.out.println("Career staff not found.");
            return;
        }
        currentUser = user;
        System.out.println("Welcome, " + user.getName());
        displayStaffMenu();
        currentUser = null;
    }

    private User findUserById(String id, String password, Class<? extends User> type) { // checks if user is of correct type
        for (User user : users) {
            if (type.isInstance(user) && user.getId().equalsIgnoreCase(id) && user.getId().equals(password)) {
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

    private String[] registerCompanyRep(String email) { // first time registration
        System.out.println("No existing company representative record found.");
        System.out.println("Please provide details for approval.");
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Company Name: ");
        String company = scanner.nextLine().trim();
        System.out.print("Department: ");
        String department = scanner.nextLine().trim();
        System.out.print("Position: ");
        String position = scanner.nextLine().trim();

        if (name.isEmpty() || company.isEmpty()) {
            System.out.println("Name and company are required.");
            return null;
        }

        String[] record = new String[]{
                "REP-" + System.currentTimeMillis(),name,company,department,position,email,"false"
        };

        if (appendCompanyRepRecord(record)) {
            return record;
        }
        return null;
    }

    private boolean appendCompanyRepRecord(String[] record) { // append to CSV
        Path path = Paths.get(COMPANY_REP_CSV);
        try {
            Files.createDirectories(path.getParent());
            if (!Files.exists(path) || Files.size(path) == 0) {
                Files.writeString(path, COMPANY_REP_HEADER + System.lineSeparator());
            }
            Files.writeString(path, String.join(",", record) + System.lineSeparator(), StandardOpenOption.APPEND);
            return true;
        } catch (IOException e) {
            System.out.println("Failed to save company representative registration: " + e.getMessage());
            return false;
        }
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
                case "1":
                    cli.handleStudentLogin();
                    break;
                case "2":
                    cli.handleCompanyRepLogin();
                    break;
                case "3":
                    cli.handleCareerStaffLogin();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }

        // if successfully log in then can access manager
        }
        System.out.println("Goodbye.");
    }
}
