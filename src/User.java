public abstract class User {
    public static final String DEFAULT_PASSWORD = "password";

    private final String userID;
    private String name;
    private String password;
    private FilterCriteria filterPreferences;

    protected User(String userID, String name, String password) {
        this.userID = userID;
        this.name = name;
        if (password == null || password.isBlank()) {
            this.password = DEFAULT_PASSWORD;
        } else {
            this.password = password;
        }
    }

    public String getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FilterCriteria getFilterPreferences() {
        return filterPreferences;
    }

    public void setFilterPreferences(FilterCriteria filterPreferences) {
        this.filterPreferences = filterPreferences;
    }

    public boolean login(String password) {
        return this.password != null && this.password.equals(password);
    }

    public void logout() {
        // Placeholder for logout hooks.
    }

    public void changePassword(String newPass) {
        if (newPass != null && !newPass.isBlank()) {
            this.password = newPass;
        }
    }
}
