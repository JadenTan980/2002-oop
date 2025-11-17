package boundary;

import control.ApplicationController;
import control.CareerCenterController;
import control.InternshipController;
import control.ReportController;
import entities.CareerCenterStaff;
import java.util.Scanner;

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
        boolean success = careerCenterController.approveRepresentative(repId);
        if (success) {
            System.out.println("Company representative " + repId + " approved.");
        } else {
            System.out.println("Representative not found: " + repId);
        }
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

    public void displayMenu(CareerCenterStaff staff) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("=== Career Center Menu ===");
            System.out.println("1. Approve company representative");
            System.out.println("2. Review postings");
            System.out.println("3. Decide withdrawal");
            System.out.println("4. Generate reports");
            System.out.println("5. Logout");
            System.out.print("Select option: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> {
                    System.out.print("Enter representative ID to approve: ");
                    String repId = scanner.nextLine();
                    approveCompanyRep(repId);
                }
                case "2" -> {
                    // placeholder for review posting flow
                }
                case "3" -> {
                    // placeholder for withdrawal decision flow
                }
                case "4" -> generateReports();
                case "5" -> running = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }
}
