package entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Criteria object to build reports with varying filters.
 */
public class ReportCriteria {

    private final List<InternshipOpportunity.Status> statuses = new ArrayList<>();
    private final List<String> majors = new ArrayList<>();
    private final List<InternshipOpportunity.Level> levels = new ArrayList<>();
    private Date closingDate;

    public void setStatusFilter(List<InternshipOpportunity.Status> statuses) {
        this.statuses.clear();
        if (statuses != null) {
            this.statuses.addAll(statuses);
        }
    }

    public void setMajorFilter(List<String> majors) {
        this.majors.clear();
        if (majors != null) {
            this.majors.addAll(majors);
        }
    }

    public void setLevelFilter(List<InternshipOpportunity.Level> levels) {
        this.levels.clear();
        if (levels != null) {
            this.levels.addAll(levels);
        }
    }

    public void setClosingDate(Date closingDate) {
        this.closingDate = closingDate;
    }

    public boolean matches(InternshipOpportunity opportunity) {
        boolean matchesStatus = statuses.isEmpty() || statuses.contains(opportunity.getStatus());
        boolean matchesMajor = majors.isEmpty() || majors.contains(opportunity.getPreferredMajor());
        boolean matchesLevel = levels.isEmpty() || levels.contains(opportunity.getLevel());
        boolean matchesClosing = closingDate == null
                || (opportunity.getCloseDate() != null
                && !opportunity.getCloseDate().after(closingDate));
        return matchesStatus && matchesMajor && matchesLevel && matchesClosing;
    }
}
