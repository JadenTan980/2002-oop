package control;

import java.util.List;
import boundary.ApplicationRepositoryInterface;
import entities.InternshipApplication;
import entities.InternshipOpportunity;
import entities.Student;

/**
 * Coordinates application creation, updates, and queries.
 */
public class ApplicationController {

    private final ApplicationRepositoryInterface applicationRepository;

    public ApplicationController(ApplicationRepositoryInterface applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public InternshipApplication createApplication(Student student,
                                                    InternshipOpportunity opportunity) {
        InternshipApplication application = new InternshipApplication(student, opportunity);
        student.addApplication(application);
        opportunity.addApplication(application);
        applicationRepository.save(application);
        return application;
    }

    public void updateStatus(String applicationId,
                             InternshipApplication.ApplicationStatus status) {
        InternshipApplication application = findById(applicationId);
        if (application != null) {
            application.setStatus(status);
            applicationRepository.save(application);
        }
    }

    public void recordWithdrawal(String applicationId) {
        InternshipApplication application = findById(applicationId);
        if (application != null) {
            application.requestWithdrawal();
            applicationRepository.save(application);
        }
    }

    public List<InternshipApplication> listByOpportunity(String opportunityId) {
        return applicationRepository.findByOpportunity(opportunityId);
    }

    public InternshipApplication findById(String applicationId) {
        return applicationRepository.findById(applicationId);
    }
}
