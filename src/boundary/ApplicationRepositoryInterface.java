package boundary;

import java.util.Collection;
import java.util.List;
import entities.InternshipApplication;

/**
 * Contract for persisting applications.
 */
public interface ApplicationRepositoryInterface {

    InternshipApplication findById(String id);

    Collection<InternshipApplication> findAll();

    void save(InternshipApplication application);

    List<InternshipApplication> findByOpportunity(String opportunityId);
}
