import views.GameWindow;
import views.OrizonSplashScreen;
import javax.swing.SwingUtilities;

/**
 * Arcane Spire: Kartların Çarpışması
 * Ana giriş noktası - Oyun pencersini başlatır.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            OrizonSplashScreen splash = new OrizonSplashScreen(() -> {
                // Splash animasyonu bittiğinde ana oyunu başlat
                GameWindow.main(args);
            });
            splash.startAnimation();
        });
    }
}
