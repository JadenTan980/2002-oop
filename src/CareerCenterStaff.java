public class CareerCenterStaff extends User {
    private String staffDepartment;

    public CareerCenterStaff(String userID, String name, String staffDepartment) {
        super(userID, name, User.DEFAULT_PASSWORD);
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
            internship.setStatus("Approved");
        }
    }

    public void rejectInternship(Internship internship) {
        if (internship != null) {
            internship.setStatus("Rejected");
        }
    }

    public void approveRepAccount(AccountRequest request) {
        if (request != null) {
            request.setStatus("Approved");
            request.setApprover(this);
        }
    }

    public void processWithdrawal(WithdrawalRequest request, boolean approve) {
        if (request == null) {
            return;
        }
        if (approve) {
            request.approve();
        } else {
            request.reject();
        }
    }
}
