package control;

import boundary.UserRepositoryInterface;
import entities.CareerCenterStaff;
import entities.CompanyRepresentative;
import entities.Student;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.regex.Pattern;

/**
 * Handles registration workflows for all user types.
 */
public class RegistrationController {

    private final UserRepositoryInterface userRepository;
    private final File studentCsv;
    private final File companyCsv;
    private final File staffCsv;

    public RegistrationController(UserRepositoryInterface userRepository,
                                  File studentCsv,
                                  File companyCsv,
                                  File staffCsv) {
        this.userRepository = userRepository;
        this.studentCsv = studentCsv;
        this.companyCsv = companyCsv;
        this.staffCsv = staffCsv;
    }

    private static final Pattern STUDENT_ID_PATTERN = Pattern.compile("^U\\d{7}[A-Za-z]$");

    public boolean registerStudent(String id, String name, String password,
                                    int yearOfStudy, String major, String email) {
        if (!isValidStudentId(id) || exists(id)) {
            return false;
        }
        String safePassword = normalizePassword(password);
        Student student = new Student(id, name, safePassword, yearOfStudy, major);
        student.setEmail(email);
        userRepository.save(student);
        appendRecord(studentCsv,
                "StudentID,Name,Major,Year,Email",
                String.join(",", id, name, major, String.valueOf(yearOfStudy), email));
        return true;
    }

    public boolean registerCompanyRepresentative(String id, String name, String password,
                                                  String companyName, String department,
                                                  String position) {
        if (exists(id)) {
            return false;
        }
        String safePassword = normalizePassword(password);
        CompanyRepresentative representative = new CompanyRepresentative(
                id, name, safePassword, companyName, department, position);
        representative.setApproved(false);
        userRepository.save(representative);
        appendRecord(companyCsv,
                "CompanyRepID,Name,CompanyName,Department,Position,Email,Approved",
                String.join(",", id, name, companyName, department, position, "", "false"));
        return true;
    }

    public boolean registerCareerStaff(String id, String name, String password,
                                       String department) {
        if (exists(id)) {
            return false;
        }
        String safePassword = normalizePassword(password);
        CareerCenterStaff staff = new CareerCenterStaff(id, name, safePassword, department);
        userRepository.save(staff);
        appendRecord(staffCsv,
                "StaffID,Name,Role,Department,Email",
                String.join(",", id, name, "Career Center Staff", department, ""));
        return true;
    }

    private boolean exists(String id) {
        return id == null || userRepository.findById(id) != null;
    }

    public boolean isValidStudentIdFormat(String id) {
        return isValidStudentId(id);
    }

    private boolean isValidStudentId(String id) {
        return id != null && STUDENT_ID_PATTERN.matcher(id).matches();
    }

    private String normalizePassword(String password) {
        return (password == null || password.isEmpty()) ? "password" : password;
    }

    private void appendRecord(File file, String header, String record) {
        try {
            if (file == null) {
                return;
            }
            File parent = file.getParentFile();
            if (parent != null) {
                Files.createDirectories(parent.toPath());
            }
            if (!file.exists() || file.length() == 0) {
                Files.writeString(file.toPath(), header + System.lineSeparator(),
                        StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            }
            Files.writeString(file.toPath(), record + System.lineSeparator(),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to append record to " + file, e);
        }
    }
}
