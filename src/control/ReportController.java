package control;

import java.util.List;
import java.util.stream.Collectors;
import entities.InternshipApplication;
import entities.InternshipOpportunity;
import entities.ReportCriteria;
import repository.ApplicationRepository;
import repository.InternshipRepository;

/**
 * Builds reports for the career center staff.
 */
public class ReportController {

    private final InternshipRepository internshipRepository;
    private final ApplicationRepository applicationRepository;

    public ReportController(InternshipRepository internshipRepository,
                            ApplicationRepository applicationRepository) {
        this.internshipRepository = internshipRepository;
        this.applicationRepository = applicationRepository;
    }

    public List<InternshipOpportunity> generateReport(ReportCriteria criteria) {
        return internshipRepository.findAll().stream()
                .filter(criteria::matches)
                .collect(Collectors.toList());
    }

    public List<InternshipOpportunity> filterByStatus(InternshipOpportunity.Status status) {
        return internshipRepository.findByStatus(status);
    }

    public List<InternshipOpportunity> filterByMajor(String major) {
        return internshipRepository.findAll().stream()
                .filter(op -> op.openForMajor(major))
                .collect(Collectors.toList());
    }

    public List<InternshipOpportunity> filterByLevel(InternshipOpportunity.Level level) {
        return internshipRepository.findAll().stream()
                .filter(op -> op.getLevel() == level)
                .collect(Collectors.toList());
    }

    public List<InternshipApplication> applicationsForOpportunity(String opportunityId) {
        return applicationRepository.findByOpportunity(opportunityId);
    }
}
