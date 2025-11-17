package control;

import boundary.UserRepositoryInterface;
import entities.CompanyRepresentative;
import entities.InternshipApplication;
import entities.InternshipOpportunity;

/**
 * Implements approval logic for staff.
 */
public class CareerCenterController {

    private final UserRepositoryInterface userRepository;
    private final InternshipController internshipController;
    private final ApplicationController applicationController;

    public CareerCenterController(UserRepositoryInterface userRepository,
                                  InternshipController internshipController,
                                  ApplicationController applicationController) {
        this.userRepository = userRepository;
        this.internshipController = internshipController;
        this.applicationController = applicationController;
    }

    public boolean approveRepresentative(String repId) {
        CompanyRepresentative representative = findRepresentative(repId);
        if (representative == null) {
            return false;
        }
        representative.setApproved(true);
        userRepository.save(representative);
        return true;
    }

    public boolean rejectRepresentative(String repId) {
        CompanyRepresentative representative = findRepresentative(repId);
        if (representative == null) {
            return false;
        }
        representative.setApproved(false);
        userRepository.save(representative);
        return true;
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

    private CompanyRepresentative findRepresentative(String id) {
        if (id == null) {
            return null;
        }
        return (CompanyRepresentative) userRepository.findById(id);
    }
}
