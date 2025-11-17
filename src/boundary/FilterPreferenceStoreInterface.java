package boundary;

import entities.FilterPreference;

/**
 * Contract for persisting filter preferences.
 */
public interface FilterPreferenceStoreInterface {

    FilterPreference findByUser(String userId);

    void save(String userId, FilterPreference preference);
}
