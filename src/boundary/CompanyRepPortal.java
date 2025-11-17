package boundary;

import control.ApplicationController;
import control.CompanyRepController;
import control.FilterController;
import control.InternshipController;

/**
 * Boundary for company representatives to manage opportunities and applications.
 */
public class CompanyRepPortal {

    private final CompanyRepController companyRepController;
    private final InternshipController internshipController;
    private final ApplicationController applicationController;
    private final FilterController filterController;

    public CompanyRepPortal(CompanyRepController companyRepController,
                            InternshipController internshipController,
                            ApplicationController applicationController,
                            FilterController filterController) {
        this.companyRepController = companyRepController;
        this.internshipController = internshipController;
        this.applicationController = applicationController;
        this.filterController = filterController;
    }

    public void createOpportunity() {
        // Collect opportunity details and forward to controller.
    }

    public void toggleVisibility(String opportunityId) {
        // Toggle visibility flag for an opportunity.
    }

    public void reviewApplications(String opportunityId) {
        // Show application list for a particular opportunity.
    }
}
