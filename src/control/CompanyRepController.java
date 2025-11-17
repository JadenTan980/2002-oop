package control;

import entities.CompanyRepresentative;
import entities.InternshipApplication;
import entities.InternshipOpportunity;
import repository.UserRepository;

/**
 * Handles company representative workflows.
 */
public class CompanyRepController {

    private final UserRepository userRepository;
    private final InternshipController internshipController;
    private final ApplicationController applicationController;

    public CompanyRepController(UserRepository userRepository,
                                InternshipController internshipController,
                                ApplicationController applicationController) {
        this.userRepository = userRepository;
        this.internshipController = internshipController;
        this.applicationController = applicationController;
    }

    public boolean register(CompanyRepresentative representative) {
        if (representative == null || representative.getId() == null) {
            return false;
        }
        if (representative.getPassword() == null || representative.getPassword().isEmpty()) {
            representative.setPassword("password");
        }
        if (userRepository.findById(representative.getId()) != null) {
            return false;
        }
        representative.setApproved(false);
        userRepository.save(representative);
        return true;
    }

    public void submitOpportunity(CompanyRepresentative representative,
                                  InternshipOpportunity opportunity) {
        if (representative == null || opportunity == null) {
            return;
        }
        opportunity.setOwner(representative);
        internshipController.createOpportunity(opportunity);
    }

    public void updateStatus(CompanyRepresentative representative,
                             InternshipOpportunity opportunity) {
        if (representative == null || opportunity == null) {
            return;
        }
        internshipController.updateOpportunity(opportunity);
    }

    public void reviewApplication(InternshipApplication application,
                                  InternshipApplication.ApplicationStatus status) {
        if (application == null || status == null) {
            return;
        }
        applicationController.updateStatus(application.getId(), status);
    }
}
