package control;

import entities.CompanyRepresentative;
import entities.InternshipApplication;
import entities.InternshipOpportunity;

/**
 * Handles company representative workflows.
 */
public class CompanyRepController {

    private final InternshipController internshipController;
    private final ApplicationController applicationController;

    public CompanyRepController(InternshipController internshipController,
                                ApplicationController applicationController) {
        this.internshipController = internshipController;
        this.applicationController = applicationController;
    }

    public void submitOpportunity(CompanyRepresentative representative,
                                  InternshipOpportunity opportunity) {
        if (!canManage(representative) || opportunity == null) {
            return;
        }
        opportunity.setOwner(representative);
        internshipController.createOpportunity(opportunity);
    }

    public void updateStatus(CompanyRepresentative representative,
                             InternshipOpportunity opportunity) {
        if (!canManage(representative) || opportunity == null) {
            return;
        }
        internshipController.updateOpportunity(opportunity);
    }

    public void reviewApplication(InternshipApplication application,
                                  InternshipApplication.ApplicationStatus status) {
        if (application == null || status == null) {
            return;
        }
        InternshipOpportunity opportunity = application.getOpportunity();
        if (opportunity == null || !canManage(opportunity.getOwner())) {
            return;
        }
        applicationController.updateStatus(application.getId(), status);
    }

    private boolean canManage(CompanyRepresentative representative) {
        return representative != null && representative.isApproved();
    }
}
