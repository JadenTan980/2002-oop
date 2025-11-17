import java.util.Date;

public class FilterCriteria {
    private String status;
    private String preferredMajor;
    private String level;
    private Date closingDate;

    public FilterCriteria() {}

    public FilterCriteria(String status, String preferredMajor, String level, Date closingDate) {
        this.status = status;
        this.preferredMajor = preferredMajor;
        this.level = level;
        this.closingDate = closingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPreferredMajor() {
        return preferredMajor;
    }

    public void setPreferredMajor(String preferredMajor) {
        this.preferredMajor = preferredMajor;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Date getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(Date closingDate) {
        this.closingDate = closingDate;
    }

    public boolean matches(Internship internship) {
        if (internship == null) {
            return false;
        }
        if (status != null && !status.equalsIgnoreCase(internship.getStatus())) {
            return false;
        }
        if (preferredMajor != null && !preferredMajor.equalsIgnoreCase(internship.getPreferredMajor())) {
            return false;
        }
        if (level != null && !level.equalsIgnoreCase(internship.getLevel())) {
            return false;
        }
        if (closingDate != null && internship.getCloseDate() != null
                && internship.getCloseDate().after(closingDate)) {
            return false;
        }
        return true;
    }
}
