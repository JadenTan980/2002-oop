package repository;

import boundary.InternshipRepositoryInterface;
import entities.InternshipOpportunity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * In-memory storage for internship opportunities.
 */
public class InternshipRepository implements InternshipRepositoryInterface {

    private final Map<String, InternshipOpportunity> opportunities = new HashMap<>();

    @Override
    public InternshipOpportunity findById(String id) {
        return opportunities.get(id);
    }

    @Override
    public Collection<InternshipOpportunity> findAll() {
        return opportunities.values();
    }

    @Override
    public void save(InternshipOpportunity opportunity) {
        opportunities.put(opportunity.getId(), opportunity);
    }

    @Override
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
