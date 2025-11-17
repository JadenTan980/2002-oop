package repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import entities.InternshipOpportunity;

/**
 * Repository for internship opportunities.
 */
public class InternshipRepository {

    private final Map<String, InternshipOpportunity> opportunities = new HashMap<>();

    public InternshipOpportunity findById(String id) {
        return opportunities.get(id);
    }

    public Collection<InternshipOpportunity> findAll() {
        return opportunities.values();
    }

    public void save(InternshipOpportunity opportunity) {
        opportunities.put(opportunity.getId(), opportunity);
    }

    public List<InternshipOpportunity> findByStatus(InternshipOpportunity.Status status) {
        List<InternshipOpportunity> results = new ArrayList<>();
        for (InternshipOpportunity opportunity : opportunities.values()) {
            if (opportunity.getStatus() == status) {
                results.add(opportunity);
            }
        }
        return results;
    }
}
