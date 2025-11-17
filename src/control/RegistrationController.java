package control;

import boundary.UserRepositoryInterface;
import entities.CareerCenterStaff;
import entities.CompanyRepresentative;
import entities.Student;

/**
 * Handles registration workflows for all user types.
 */
public class RegistrationController {

    private final UserRepositoryInterface userRepository;

    public RegistrationController(UserRepositoryInterface userRepository) {
        this.userRepository = userRepository;
    }

    public boolean registerStudent(String id, String name, String password,
                                    int yearOfStudy, String major) {
        if (exists(id)) {
            return false;
        }
        String safePassword = normalizePassword(password);
        Student student = new Student(id, name, safePassword, yearOfStudy, major);
        userRepository.save(student);
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
        return true;
    }

    private boolean exists(String id) {
        return id == null || userRepository.findById(id) != null;
    }

    private String normalizePassword(String password) {
        return (password == null || password.isEmpty()) ? "password" : password;
    }
}
