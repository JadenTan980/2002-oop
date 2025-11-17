package repository;

import boundary.ApplicationRepositoryInterface;
import entities.InternshipApplication;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * In-memory storage for internship applications.
 */
public class ApplicationRepository implements ApplicationRepositoryInterface {

    private final Map<String, InternshipApplication> applications = new HashMap<>();

    @Override
    public InternshipApplication findById(String id) {
        return applications.get(id);
    }

    @Override
    public Collection<InternshipApplication> findAll() {
        return applications.values();
    }

    @Override
    public void save(InternshipApplication application) {
        applications.put(application.getId(), application);
    }

    @Override
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
