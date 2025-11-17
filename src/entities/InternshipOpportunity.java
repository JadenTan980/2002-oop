package entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Internship opportunity that students can apply for.
 */
public class InternshipOpportunity {

    public enum Level {
        BASIC,
        INTERMEDIATE,
        ADVANCED
    }

    public enum Status {
        PENDING,
        APPROVED,
        REJECTED,
        FILLED
    }

    private final String id;
    private final List<InternshipApplication> applications = new ArrayList<>();

    private String title;
    private String description;
    private Level level;
    private Status status = Status.PENDING;
    private String preferredMajor;
    private Date openDate;
    private Date closeDate;
    private int slots;
    private boolean visible;
    private CompanyRepresentative owner;

    public InternshipOpportunity(String id, String title, Level level, int slots) {
        this.id = id;
        this.title = title;
        this.level = level;
        this.slots = slots;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public CompanyRepresentative getOwner() {
        return owner;
    }

    public void setOwner(CompanyRepresentative owner) {
        this.owner = owner;
    }

    public boolean hasSlotsAvailable() {
        return applications.size() < slots;
    }

    public boolean openForMajor(String major) {
        return preferredMajor == null || preferredMajor.equalsIgnoreCase(major);
    }

    public boolean withinApplicationWindow(Date date) {
        if (date == null) {
            date = new Date();
        }
        boolean afterOpen = openDate == null || !date.before(openDate);
        boolean beforeClose = closeDate == null || !date.after(closeDate);
        return afterOpen && beforeClose;
    }

    public void occupySlot() {
        if (slots > 0) {
            slots--;
            if (slots == 0) {
                status = Status.FILLED;
            }
        }
    }

    public void addApplication(InternshipApplication application) {
        applications.add(application);
    }

    public List<InternshipApplication> getApplications() {
        return Collections.unmodifiableList(applications);
    }
}
