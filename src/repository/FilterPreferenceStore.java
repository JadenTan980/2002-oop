package repository;

import boundary.FilterPreferenceStoreInterface;
import entities.FilterPreference;
import java.util.HashMap;
import java.util.Map;

/**
 * In-memory implementation storing filter preferences per user.
 */
public class FilterPreferenceStore implements FilterPreferenceStoreInterface {

    private final Map<String, FilterPreference> store = new HashMap<>();

    @Override
    public FilterPreference findByUser(String userId) {
        return store.get(userId);
    }

    @Override
    public void save(String userId, FilterPreference preference) {
        store.put(userId, preference);
    }
}
