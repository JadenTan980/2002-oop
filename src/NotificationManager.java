import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationManager {
    private final Map<String, List<Notification>> inbox = new HashMap<>();

    public void notifyUser(User user, String message) {
        if (user == null || message == null || message.isBlank()) {
            return;
        }
        inbox.computeIfAbsent(user.getUserID(), key -> new ArrayList<>())
                .add(new Notification(message, LocalDateTime.now()));
    }

    public void notifyUsers(Collection<? extends User> users, String message) {
        if (users == null || users.isEmpty()) {
            return;
        }
        for (User user : users) {
            notifyUser(user, message);
        }
    }

    public List<Notification> consumeNotifications(User user) {
        if (user == null) {
            return Collections.emptyList();
        }
        List<Notification> notifications = inbox.remove(user.getUserID());
        if (notifications == null || notifications.isEmpty()) {
            return Collections.emptyList();
        }
        notifications.sort(Comparator.comparing(Notification::getTimestamp));
        return Collections.unmodifiableList(notifications);
    }

    public List<Notification> peekNotifications(User user) {
        if (user == null) {
            return Collections.emptyList();
        }
        List<Notification> notifications = inbox.get(user.getUserID());
        if (notifications == null || notifications.isEmpty()) {
            return Collections.emptyList();
        }
        notifications.sort(Comparator.comparing(Notification::getTimestamp));
        return Collections.unmodifiableList(new ArrayList<>(notifications));
    }

    public boolean hasNotifications(User user) {
        if (user == null) {
            return false;
        }
        List<Notification> notifications = inbox.get(user.getUserID());
        return notifications != null && !notifications.isEmpty();
    }
}
