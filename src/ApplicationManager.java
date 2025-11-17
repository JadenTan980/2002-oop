public class ApplicationManager {
    public Application submitApplication(Student student, Internship internship) {
        if (!enforceRules(student, internship)) {
            throw new IllegalStateException("Application rules violated");
        }
        Application application = new Application(student, internship);
        internship.addApplication(application);
        student.getApplications().add(application);
        return application;
    }

    public void updateStatus(Application app, String status) {
        if (app != null) {
            app.setStatus(status);
        }
    }

    public boolean enforceRules(Student student, Internship internship) {
        return student != null && internship != null;
    }
}
