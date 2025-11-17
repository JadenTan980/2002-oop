package repository;

import java.util.HashMap;
import java.util.Map;
import entities.FilterPreference;

/**
 * Persists filter preferences in memory.
 */
public class FilterPreferenceStore {

    private final Map<String, FilterPreference> store = new HashMap<>();

    public FilterPreference findByUser(String userId) {
        return store.get(userId);
    }

    public void save(String userId, FilterPreference preference) {
        store.put(userId, preference);
    }
}
