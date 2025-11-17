package control;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import entities.InternshipOpportunity;
import repository.InternshipRepository;

/**
 * Handles lifecycle of internship opportunities.
 */
public class InternshipController {

    private final InternshipRepository internshipRepository;

    public InternshipController(InternshipRepository internshipRepository) {
        this.internshipRepository = internshipRepository;
    }

    public void createOpportunity(InternshipOpportunity opportunity) {
        internshipRepository.save(opportunity);
    }

    public void updateOpportunity(InternshipOpportunity opportunity) {
        internshipRepository.save(opportunity);
    }

    public void toggleVisibility(String opportunityId, boolean visible) {
        InternshipOpportunity opportunity = findById(opportunityId);
        if (opportunity != null) {
            opportunity.setVisible(visible);
            internshipRepository.save(opportunity);
        }
    }

    public List<InternshipOpportunity> findEligible(String major,
                                                     InternshipOpportunity.Level level,
                                                     Date asOf) {
        return internshipRepository.findAll().stream()
                .filter(InternshipOpportunity::isVisible)
                .filter(op -> op.openForMajor(major))
                .filter(op -> op.getLevel().ordinal() <= level.ordinal())
                .filter(op -> op.withinApplicationWindow(asOf))
                .collect(Collectors.toList());
    }

    public InternshipOpportunity findById(String opportunityId) {
        return internshipRepository.findById(opportunityId);
    }
}
