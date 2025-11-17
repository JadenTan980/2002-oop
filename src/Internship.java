import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Internship {
    private final String title;
    private final String description;
    private InternshipLevel level;
    private String preferredMajor;
    private Date openDate;
    private Date closeDate;
    private boolean visibility = true;
    private InternshipStatus status = InternshipStatus.PENDING;
    private final String companyName;
    private final CompanyRep repInCharge;
    private final List<InternshipSlot> slots = new ArrayList<>();
    private final List<Application> applications = new ArrayList<>();

    public Internship(String title, String description, String companyName, CompanyRep repInCharge) {
        this.title = title;
        this.description = description;
        this.companyName = companyName;
        this.repInCharge = repInCharge;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public InternshipLevel getLevel() {
        return level;
    }

    public void setLevel(InternshipLevel level) {
        this.level = level;
    }

    public String getPreferredMajor() {
        return preferredMajor;
    }

    public void setPreferredMajor(String preferredMajor) {
        this.preferredMajor = preferredMajor;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public InternshipStatus getStatus() {
        return status;
    }

    public void setStatus(InternshipStatus status) {
        this.status = status;
    }

    public String getCompanyName() {
        return companyName;
    }

    public CompanyRep getRepInCharge() {
        return repInCharge;
    }

    public void addSlot(InternshipSlot slot) {
        if (slot != null) {
            slots.add(slot);
        }
    }

    public List<InternshipSlot> getSlots() {
        return slots;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void addApplication(Application application) {
        if (application != null) {
            applications.add(application);
        }
    }

    public void toggleVisibility(boolean on) {
        this.visibility = on;
    }
}
