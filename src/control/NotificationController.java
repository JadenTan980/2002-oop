package control;

/**
 * Responsible for notifying users in the CLI.
 */
public class NotificationController {

    public void notifyPendingApproval(String representativeId) {
        System.out.println("Company representative " + representativeId
                + " is awaiting approval. Please contact the career center.");
    }
}
