package repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import entities.InternshipApplication;

/**
 * Repository for internship applications.
 */
public class ApplicationRepository {

    private final Map<String, InternshipApplication> applications = new HashMap<>();

    public InternshipApplication findById(String id) {
        return applications.get(id);
    }

    public Collection<InternshipApplication> findAll() {
        return applications.values();
    }

    public void save(InternshipApplication application) {
        applications.put(application.getId(), application);
    }

    public List<InternshipApplication> findByOpportunity(String opportunityId) {
        List<InternshipApplication> results = new ArrayList<>();
        for (InternshipApplication application : applications.values()) {
            if (application.getOpportunity().getId().equals(opportunityId)) {
                results.add(application);
            }
        }
        return results;
    }
}
