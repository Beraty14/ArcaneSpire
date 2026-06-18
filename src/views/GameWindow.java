package views;
 
import javax.swing.*;
import java.awt.*;
import controllers.GameManager;
import controllers.SaveManager;
 
/**
 * Ana oyun penceresi. CardLayout ile ekranlar arası geçiş sağlar.
 */
public class GameWindow extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainContainer;
    private BattlePanel battlePanel;
    private String basePath;
    private SaveNotificationOverlay notificationOverlay;
    private PauseDialog activePauseDialog;
 
    public GameWindow() {
        setTitle("Arcane Spire: Kartların Çarpışması");
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Çalıştırılan dizine göre images klasörünü bul
        basePath = System.getProperty("user.dir");
        if (!basePath.endsWith("ArcaneSpire")) {
            // Eğer proje kökünden çalıştırılıyorsa
            basePath = basePath + java.io.File.separator + "ArcaneSpire";
        }
        
        Image appIcon = loadImage("app_icon.jpeg");
        if (appIcon != null) {
            setIconImage(appIcon);
        }
        
        setLocationRelativeTo(null);
 
        // Initialize global save overlay
        notificationOverlay = new SaveNotificationOverlay();
        setGlassPane(notificationOverlay);
 
        // Listen to save events
        SaveManager.setSaveListener(msg -> {
            SwingUtilities.invokeLater(() -> triggerSaveNotification(msg));
        });
 
        // Set up global hotkeys
        setupGlobalHotkeys();

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        battlePanel = new BattlePanel(this);
        CinematicPanel cinematicPanel = new CinematicPanel(this);

        mainContainer.add(new IntroPanel(this), "Intro");
        mainContainer.add(new MainMenuPanel(this), "MainMenu");
        mainContainer.add(new CharacterSelectPanel(this), "CharacterSelect");
        mainContainer.add(cinematicPanel, "Cinematic");
        mainContainer.add(battlePanel, "Battle");
        mainContainer.add(new HowToPlayPanel(this), "HowToPlay");
        mainContainer.add(new SettingsPanel(this), "Settings");
        mainContainer.add(new ShopPanel(this), "Shop");

        add(mainContainer);
        
        // Oyun açılışında menü müziğini başlat
        controllers.AudioPlayer.playMenuMusic();
    }

    public JPanel getPanel(String panelName) {
        for (Component comp : mainContainer.getComponents()) {
            if (comp.isVisible()) {
                // Not perfectly generic but good enough. Wait, actually I can just expose the specific panels.
            }
        }
        return null;
    }
    
    public CinematicPanel getCinematicPanel() {
        for (Component comp : mainContainer.getComponents()) {
            if (comp instanceof CinematicPanel) return (CinematicPanel) comp;
        }
        return null;
    }

    public HowToPlayPanel getHowToPlayPanel() {
        for (Component comp : mainContainer.getComponents()) {
            if (comp instanceof HowToPlayPanel) return (HowToPlayPanel) comp;
        }
        return null;
    }

    public ShopPanel getShopPanel() {
        for (Component comp : mainContainer.getComponents()) {
            if (comp instanceof ShopPanel) return (ShopPanel) comp;
        }
        return null;
    }

    public void showPanel(String panelName) {
        cardLayout.show(mainContainer, panelName);
        if (panelName.equals("Battle")) {
            battlePanel.requestFocusInWindow();
        } else if (panelName.equals("Cinematic")) {
            controllers.AudioPlayer.stopMusic();
            getCinematicPanel().startCinematic();
        } else if (panelName.equals("HowToPlay")) {
            controllers.AudioPlayer.playMenuMusic();
            HowToPlayPanel htp = getHowToPlayPanel();
            if (htp != null) {
                htp.resetAndFocus();
            }
        } else if (panelName.equals("MainMenu") || panelName.equals("Intro") || panelName.equals("CharacterSelect")) {
            controllers.AudioPlayer.playMenuMusic();
        } else if (panelName.equals("Shop")) {
            controllers.AudioPlayer.playMenuMusic();
            ShopPanel shop = getShopPanel();
            if (shop != null) {
                shop.initShop();
            }
        }
    }

    public BattlePanel getBattlePanel() {
        return battlePanel;
    }

    public String getBasePath() {
        return basePath;
    }

    public Image loadImage(String fileName) {
        try {
            String path = basePath + java.io.File.separator + "src" + java.io.File.separator + "images" + java.io.File.separator + fileName;
            java.io.File f = new java.io.File(path);
            if (f.exists()) {
                return new ImageIcon(path).getImage();
            }
        } catch (Exception e) {
            // Görsel bulunamazsa null
        }
        return null;
    }

    private void setupGlobalHotkeys() {
        JRootPane rp = getRootPane();
        InputMap im = rp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = rp.getActionMap();

        im.put(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "esc_pause");
        am.put("esc_pause", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                handlePauseKey();
            }
        });

        im.put(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0), "f5_quicksave");
        am.put("f5_quicksave", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                handleQuickSave();
            }
        });

        im.put(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F9, 0), "f9_quickload");
        am.put("f9_quickload", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                handleQuickLoad();
            }
        });
    }

    private void handlePauseKey() {
        if (!GameManager.getInstance().isGameActive()) return;
        if (activePauseDialog != null && activePauseDialog.isShowing()) {
            activePauseDialog.dispose();
            activePauseDialog = null;
            return;
        }
        activePauseDialog = new PauseDialog(this);
        activePauseDialog.setVisible(true);
    }

    private void handleQuickSave() {
        GameManager gm = GameManager.getInstance();
        if (!gm.isGameActive()) return;
        SaveManager.saveGame(gm, 0, "QUICK");
    }

    private void handleQuickLoad() {
        GameManager gm = GameManager.getInstance();
        if (!SaveManager.hasSaveGame(0)) {
            triggerSaveNotification(models.LanguageManager.getInstance().getText("no_quicksave"));
            return;
        }
        
        if (activePauseDialog != null) {
            activePauseDialog.dispose();
            activePauseDialog = null;
        }
        
        controllers.AudioPlayer.stopMusic();
        if (SaveManager.loadGame(gm, 0)) {
            controllers.AudioPlayer.playBattleMusic();
            getBattlePanel().startBattle();
            showPanel("Battle");
            triggerSaveNotification(models.LanguageManager.getInstance().getText("quickload_notif") + " ✔");
        } else {
            triggerSaveNotification("Error loading quicksave!");
        }
    }

    public void triggerSaveNotification(String msg) {
        if (notificationOverlay != null) {
            notificationOverlay.trigger(msg);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { /* Sistem teması bulunamazsa varsayılanı kullan */ }

        SwingUtilities.invokeLater(() -> {
            GameWindow window = new GameWindow();
            window.setVisible(true);
        });
    }
}
