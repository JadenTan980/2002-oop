package control;

import java.util.List;
import boundary.FilterPreferenceStoreInterface;
import entities.FilterPreference;
import entities.InternshipOpportunity;

/**
 * Applies filters and persists per-user preferences.
 */
public class FilterController {

    private final FilterPreferenceStoreInterface store;

    public FilterController(FilterPreferenceStoreInterface store) {
        this.store = store;
    }

    public void saveFilter(String userId, FilterPreference preference) {
        store.save(userId, preference);
    }

    public FilterPreference loadFilter(String userId) {
        FilterPreference preference = store.findByUser(userId);
        return preference != null ? preference : new FilterPreference();
    }

    public List<InternshipOpportunity> applyFilter(List<InternshipOpportunity> opportunities,
                                                    FilterPreference preference) {
        return preference.apply(opportunities);
    }
}
