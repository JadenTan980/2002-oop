public class WithdrawalManager {
    public WithdrawalRequest submitRequest(Application app, String reason) {
        if (app == null) {
            throw new IllegalArgumentException("Application required");
        }
        return app.requestWithdrawal(reason);
    }

    public void processRequest(WithdrawalRequest request, CareerCenterStaff staff) {
        if (request == null || staff == null) {
            return;
        }
        staff.processWithdrawal(request, true);
    }
}
