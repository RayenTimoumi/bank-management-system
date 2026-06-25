package interfaces;
import java.util.List;
public interface Notifiable {
    void envoyerNotification(String message);
    void afficherNotification();
    List<String> obtenirNotifications();
}
