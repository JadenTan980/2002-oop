public class AccountRequest {
    private final CompanyRep rep;
    private String status = "Pending";
    private CareerCenterStaff approver;

    public AccountRequest(CompanyRep rep) {
        this.rep = rep;
    }

    public CompanyRep getRep() {
        return rep;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CareerCenterStaff getApprover() {
        return approver;
    }

    public void setApprover(CareerCenterStaff approver) {
        this.approver = approver;
    }
}
