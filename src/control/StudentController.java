package control;

import entities.InternshipApplication;
import entities.InternshipOpportunity;
import entities.Student;

/**
 * Encapsulates student rules such as eligibility and application limits.
 */
public class StudentController {

    private final ApplicationController applicationController;

    public StudentController(ApplicationController applicationController) {
        this.applicationController = applicationController;
    }

    public boolean validateEligibility(Student student, InternshipOpportunity opportunity) {
        if (student == null || opportunity == null) {
            return false;
        }
        boolean yearRequirement = student.getYearOfStudy() >= 3
                || opportunity.getLevel() == InternshipOpportunity.Level.BASIC;
        boolean majorRequirement = opportunity.openForMajor(student.getMajor());
        return yearRequirement && majorRequirement && opportunity.withinApplicationWindow(null);
    }

    public boolean apply(Student student, InternshipOpportunity opportunity) {
        if (!validateEligibility(student, opportunity)) {
            return false;
        }
        applicationController.createApplication(student, opportunity);
        return true;
    }

    public boolean withdraw(Student student, InternshipApplication application) {
        if (student == null || application == null) {
            return false;
        }
        applicationController.recordWithdrawal(application.getId());
        return true;
    }

    public boolean acceptOffer(Student student, InternshipApplication application) {
        if (student == null || application == null) {
            return false;
        }
        applicationController.updateStatus(application.getId(),
                InternshipApplication.ApplicationStatus.SUCCESSFUL);
        return true;
    }
}
