import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InternshipManager {
    private final List<Internship> internships = new ArrayList<>();

    public void submitInternship(Internship internship) {
        if (internship != null) {
            internships.add(internship);
        }
    }

    public void approveInternship(Internship internship) {
        if (internship != null) {
            internship.setStatus("Approved");
        }
    }

    public void rejectInternship(Internship internship) {
        if (internship != null) {
            internship.setStatus("Rejected");
        }
    }

    public List<Internship> filter(FilterCriteria criteria) {
        if (criteria == null) {
            return new ArrayList<>(internships);
        }
        return internships.stream()
                .filter(criteria::matches)
                .collect(Collectors.toList());
    }

    public List<Internship> getInternships() {
        return internships;
    }
}
