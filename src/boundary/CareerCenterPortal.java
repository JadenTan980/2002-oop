package boundary;

import control.ApplicationController;
import control.CareerCenterController;
import control.InternshipController;
import control.ReportController;

/**
 * Boundary for career center staff workflows.
 */
public class CareerCenterPortal {

    private final CareerCenterController careerCenterController;
    private final InternshipController internshipController;
    private final ApplicationController applicationController;
    private final ReportController reportController;

    public CareerCenterPortal(CareerCenterController careerCenterController,
                              InternshipController internshipController,
                              ApplicationController applicationController,
                              ReportController reportController) {
        this.careerCenterController = careerCenterController;
        this.internshipController = internshipController;
        this.applicationController = applicationController;
        this.reportController = reportController;
    }

    public void approveCompanyRep(String repId) {
        // Display approval interface before delegating to controller.
    }

    public void reviewPosting(String opportunityId) {
        // Show opportunity details for approval decision.
    }

    public void decideWithdrawal(String applicationId) {
        // Provide UI for withdrawal decisions.
    }

    public void generateReports() {
        // Collect criteria inputs and delegate to report controller.
    }
}
