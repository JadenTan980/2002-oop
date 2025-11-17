package boundary;

import java.util.Collection;
import java.util.List;
import entities.InternshipOpportunity;

/**
 * Contract for managing internship postings.
 */
public interface InternshipRepositoryInterface {

    InternshipOpportunity findById(String id);

    Collection<InternshipOpportunity> findAll();

    void save(InternshipOpportunity opportunity);

    List<InternshipOpportunity> findByStatus(InternshipOpportunity.Status status);
}
