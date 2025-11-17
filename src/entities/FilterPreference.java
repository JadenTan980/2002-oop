package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Stores saved filters per user.
 */
public class FilterPreference {

    private List<String> preferredMajors = new ArrayList<>();
    private List<InternshipOpportunity.Status> statuses = new ArrayList<>();
    private List<InternshipOpportunity.Level> levels = new ArrayList<>();

    public List<String> getPreferredMajors() {
        return preferredMajors;
    }

    public void setPreferredMajors(List<String> preferredMajors) {
        this.preferredMajors = preferredMajors;
    }

    public List<InternshipOpportunity.Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<InternshipOpportunity.Status> statuses) {
        this.statuses = statuses;
    }

    public List<InternshipOpportunity.Level> getLevels() {
        return levels;
    }

    public void setLevels(List<InternshipOpportunity.Level> levels) {
        this.levels = levels;
    }

    public List<InternshipOpportunity> apply(List<InternshipOpportunity> opportunities) {
        return opportunities.stream()
                .filter(this::matchesMajor)
                .filter(this::matchesStatus)
                .filter(this::matchesLevel)
                .collect(Collectors.toList());
    }

    private boolean matchesMajor(InternshipOpportunity opportunity) {
        return preferredMajors == null || preferredMajors.isEmpty()
                || preferredMajors.contains(opportunity.getPreferredMajor());
    }

    private boolean matchesStatus(InternshipOpportunity opportunity) {
        return statuses == null || statuses.isEmpty()
                || statuses.contains(opportunity.getStatus());
    }

    private boolean matchesLevel(InternshipOpportunity opportunity) {
        return levels == null || levels.isEmpty()
                || levels.contains(opportunity.getLevel());
    }
}
