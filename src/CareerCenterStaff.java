public class CareerCenterStaff extends User {
    private String staffDepartment;

    public CareerCenterStaff(String userID, String name, String password, String staffDepartment) {
        super(userID, name, password);
        this.staffDepartment = staffDepartment;
    }

    public String getStaffDepartment() {
        return staffDepartment;
    }

    public void setStaffDepartment(String staffDepartment) {
        this.staffDepartment = staffDepartment;
    }

    public void approveInternship(Internship internship) {
        if (internship != null) {
            internship.setStatus(InternshipStatus.APPROVED);
        }
    }

    public void rejectInternship(Internship internship) {
        if (internship != null) {
            internship.setStatus(InternshipStatus.REJECTED);
        }
    }

    public void approveRepAccount(AccountRequest request) {
        if (request == null) {
            return;
        }
        request.setStatus(AccountRequest.STATUS_APPROVED);
        request.setApprover(this);
        if (request.getRep() != null) {
            request.getRep().setApproved(true);
        }
    }

    public void rejectRepAccount(AccountRequest request, String notes) {
        if (request == null) {
            return;
        }
        request.setStatus(AccountRequest.STATUS_REJECTED);
        request.setApprover(this);
        request.setDecisionNotes(notes);
    }

    public void processWithdrawal(WithdrawalRequest request, boolean approve) {
        if (request == null) {
            return;
        }
        request.setProcessedBy(this);
        if (approve) {
            request.approve();
        } else {
            request.reject();
        }
    }
}
