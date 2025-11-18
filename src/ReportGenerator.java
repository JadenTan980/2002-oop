import java.util.List;

public class ReportGenerator {
    public void generateByStatus(List<Internship> internships, InternshipStatus status) {
        long count = internships.stream()
                .filter(i -> i.getStatus() == status)
                .count();
        System.out.println("Internships with status " + status + ": " + count);
    }

    public void generateByMajor(List<Internship> internships, String major) {
        long count = internships.stream()
                .filter(i -> major.equalsIgnoreCase(i.getPreferredMajor()))
                .count();
        System.out.println("Internships for major " + major + ": " + count);
    }

    public void generateByLevel(List<Internship> internships, InternshipLevel level) {
        long count = internships.stream()
                .filter(i -> i.getLevel() == level)
                .count();
        System.out.println("Internships for level " + level + ": " + count);
    }

    public void generateCompanySummary(List<Internship> internships, String company) {
        long count = internships.stream()
                .filter(i -> company.equalsIgnoreCase(i.getCompanyName()))
                .count();
        System.out.println("Internships by " + company + ": " + count);
    }
}
