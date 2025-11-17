package control;

import entities.CompanyRepresentative;
import entities.InternshipApplication;
import entities.InternshipOpportunity;
import repository.UserRepository;

/**
 * Implements approval logic for staff.
 */
public class CareerCenterController {

    private final UserRepository userRepository;
    private final InternshipController internshipController;
    private final ApplicationController applicationController;

    public CareerCenterController(UserRepository userRepository,
                                  InternshipController internshipController,
                                  ApplicationController applicationController) {
        this.userRepository = userRepository;
        this.internshipController = internshipController;
        this.applicationController = applicationController;
    }

    public void approveRepresentative(String repId) {
        CompanyRepresentative representative =
                (CompanyRepresentative) userRepository.findById(repId);
        if (representative != null) {
            representative.setApproved(true);
            userRepository.save(representative);
        }
    }

    public void rejectRepresentative(String repId) {
        CompanyRepresentative representative =
                (CompanyRepresentative) userRepository.findById(repId);
        if (representative != null) {
            representative.setApproved(false);
            userRepository.save(representative);
        }
    }

    public void approveOpportunity(String opportunityId) {
        InternshipOpportunity opportunity = internshipController.findById(opportunityId);
        if (opportunity != null) {
            opportunity.setStatus(InternshipOpportunity.Status.APPROVED);
            internshipController.updateOpportunity(opportunity);
        }
    }

    public void decideWithdrawal(String applicationId, boolean approve) {
        InternshipApplication application = applicationController.findById(applicationId);
        if (application == null) {
            return;
        }
        if (approve) {
            applicationController.recordWithdrawal(applicationId);
        } else {
            applicationController.updateStatus(applicationId,
                    InternshipApplication.ApplicationStatus.PENDING);
        }
    }
}
