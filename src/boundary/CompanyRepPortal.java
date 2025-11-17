package boundary;

import control.ApplicationController;
import control.CompanyRepController;
import control.FilterController;
import control.InternshipController;
import entities.CompanyRepresentative;

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

    public void displayMenu(CompanyRepresentative representative) {
        System.out.println("Welcome company representative " + representative.getName());
        if (!representative.isApproved()) {
            System.out.println("Your account is pending approval. No actions are available until a career center staff approves your registration.");
            return;
        }
        // Placeholder for menu interaction.
    }

    public void toggleVisibility(String opportunityId) {
        // Toggle visibility flag for an opportunity.
    }

    public void reviewApplications(String opportunityId) {
        // Show application list for a particular opportunity.
    }
}
