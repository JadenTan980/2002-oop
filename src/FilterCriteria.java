import java.util.Date;

public class FilterCriteria {
    private InternshipStatus status;
    private String preferredMajor;
    private InternshipLevel level;
    private Date closingDate;

    public FilterCriteria() {}

    public FilterCriteria(InternshipStatus status, String preferredMajor,
                          InternshipLevel level, Date closingDate) {
        this.status = status;
        this.preferredMajor = preferredMajor;
        this.level = level;
        this.closingDate = closingDate;
    }

    public InternshipStatus getStatus() {
        return status;
    }

    public void setStatus(InternshipStatus status) {
        this.status = status;
    }

    public String getPreferredMajor() {
        return preferredMajor;
    }

    public void setPreferredMajor(String preferredMajor) {
        this.preferredMajor = preferredMajor;
    }

    public InternshipLevel getLevel() {
        return level;
    }

    public void setLevel(InternshipLevel level) {
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
        if (status != null && internship.getStatus() != status) {
            return false;
        }
        if (preferredMajor != null && !preferredMajor.equalsIgnoreCase(internship.getPreferredMajor())) {
            return false;
        }
        if (level != null && internship.getLevel() != level) {
            return false;
        }
        if (closingDate != null && internship.getCloseDate() != null
                && internship.getCloseDate().after(closingDate)) {
            return false;
        }
        return true;
    }
}
