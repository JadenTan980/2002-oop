package boundary;

import control.ApplicationController;
import control.FilterController;
import control.InternshipController;
import control.StudentController;
import entities.Student;

/**
 * Student-facing UI boundary that orchestrates actions with multiple controllers.
 */
public class StudentPortal {

    private final StudentController studentController;
    private final InternshipController internshipController;
    private final ApplicationController applicationController;
    private final FilterController filterController;

    public StudentPortal(StudentController studentController,
                         InternshipController internshipController,
                         ApplicationController applicationController,
                         FilterController filterController) {
        this.studentController = studentController;
        this.internshipController = internshipController;
        this.applicationController = applicationController;
        this.filterController = filterController;
    }

    public void showDashboard(Student student) {
        // Render personalized student dashboard using controller data.
    }

    public void listOpportunities() {
        // Display filtered internship opportunities.
    }

    public void apply(String opportunityId) {
        // Trigger an application for the selected opportunity.
    }

    public void withdraw(String applicationId) {
        // Trigger withdrawal workflow.
    }
}
