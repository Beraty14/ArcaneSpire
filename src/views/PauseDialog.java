package views;
 
import controllers.GameManager;
import controllers.SaveManager;
import models.LanguageManager;
import models.SettingsManager;
 
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
 
/**
 * In-game pause menu (ESC menu). Styled with golden frames, semi-transparent overlays,
 * and custom control buttons. Includes volume sliders directly inside the pause menu.
 */
public class PauseDialog extends JDialog {
    private GameWindow window;
    private SettingsManager sm;
    private LanguageManager lm;
 
    public PauseDialog(GameWindow window) {
        super(window, "Oyun Durduruldu", true);
        this.window = window;
        this.sm = SettingsManager.getInstance();
        this.lm = LanguageManager.getInstance();
 
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0)); // transparent background structure
 
        // Main panel setup
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Dark translucent violet/black background
                g2.setColor(new Color(25, 20, 35, 240));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                // Thick gold fantasy border
                g2.setColor(new Color(180, 140, 50));
                g2.setStroke(new BasicStroke(3.5f));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 20, 20);
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 45, 30, 45));
 
        // Pause Title
        JLabel titleLbl = new JLabel(lm.getText("pause_menu"));
        titleLbl.setFont(new Font("Serif", Font.BOLD, 36));
        titleLbl.setForeground(new Color(255, 215, 0));
        titleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLbl);
        panel.add(Box.createVerticalStrut(30));
 
        // Buttons
        JButton btnResume = createStyledMenuButton(lm.getText("resume_btn"), new Color(40, 30, 60));
        btnResume.addActionListener(e -> dispose());
 
        JButton btnSave = createStyledMenuButton(lm.getText("save_title"), new Color(40, 30, 60));
        btnSave.addActionListener(e -> {
            setVisible(false);
            SlotSelectionDialog ssd = new SlotSelectionDialog(window, SlotSelectionDialog.Mode.SAVE);
            ssd.setVisible(true);
            // Refresh dialog state after slot dialog closes
            if (GameManager.getInstance().isGameActive()) {
                setVisible(true);
            } else {
                dispose();
            }
        });
 
        JButton btnLoad = createStyledMenuButton(lm.getText("load_title"), new Color(40, 30, 60));
        btnLoad.addActionListener(e -> {
            setVisible(false);
            SlotSelectionDialog ssd = new SlotSelectionDialog(window, SlotSelectionDialog.Mode.LOAD);
            ssd.setVisible(true);
            if (GameManager.getInstance().isGameActive()) {
                setVisible(true);
            } else {
                dispose();
            }
        });
 
        // Volume Control Section
        JPanel volumePanel = new JPanel();
        volumePanel.setOpaque(false);
        volumePanel.setLayout(new BoxLayout(volumePanel, BoxLayout.Y_AXIS));
        volumePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 140, 50, 80), 1),
                " " + lm.getText("settings_title") + " ",
                0, 0,
                new Font("Serif", Font.BOLD, 16),
                new Color(220, 180, 60)
            ),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
 
        JLabel musicLbl = new JLabel(lm.getText("music_vol"));
        musicLbl.setFont(new Font("Serif", Font.BOLD, 15));
        musicLbl.setForeground(new Color(220, 220, 220));
        musicLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        JSlider musicSlider = new JSlider(0, 100, sm.getMusicVolume());
        musicSlider.setOpaque(false);
        musicSlider.setPreferredSize(new Dimension(220, 30));
        musicSlider.setAlignmentX(Component.CENTER_ALIGNMENT);
        musicSlider.addChangeListener(e -> {
            sm.setMusicVolume(musicSlider.getValue());
            if (!musicSlider.getValueIsAdjusting()) {
                controllers.AudioPlayer.refreshMusicVolume();
            }
        });
 
        JLabel sfxLbl = new JLabel(lm.getText("sfx_vol"));
        sfxLbl.setFont(new Font("Serif", Font.BOLD, 15));
        sfxLbl.setForeground(new Color(220, 220, 220));
        sfxLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        JSlider sfxSlider = new JSlider(0, 100, sm.getSfxVolume());
        sfxSlider.setOpaque(false);
        sfxSlider.setPreferredSize(new Dimension(220, 30));
        sfxSlider.setAlignmentX(Component.CENTER_ALIGNMENT);
        sfxSlider.addChangeListener(e -> {
            sm.setSfxVolume(sfxSlider.getValue());
            if (!sfxSlider.getValueIsAdjusting()) {
                controllers.AudioPlayer.playSFX("attack.wav");
            }
        });
 
        volumePanel.add(musicLbl);
        volumePanel.add(Box.createVerticalStrut(4));
        volumePanel.add(musicSlider);
        volumePanel.add(Box.createVerticalStrut(10));
        volumePanel.add(sfxLbl);
        volumePanel.add(Box.createVerticalStrut(4));
        volumePanel.add(sfxSlider);
 
        // Main Menu button (Exit)
        JButton btnMainMenu = createStyledMenuButton(lm.getText("exit_menu_btn"), new Color(80, 20, 20));
        btnMainMenu.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                lm.getText("exit_confirm") + "\n(" + lm.getText("autosave_notif") + ")",
                lm.getText("exit_menu_btn"),
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                // Autosave active state
                GameManager gm = GameManager.getInstance();
                SaveManager.saveGame(gm, gm.getCurrentSaveSlot(), "AUTO");
                
                // Reset game instance and transition
                controllers.AudioPlayer.stopMusic();
                GameManager.resetInstance();
                
                // Reinitialize listeners on the new instance
                window.getBattlePanel().resetGameManagerReference();
                if (window.getShopPanel() != null) {
                    window.getShopPanel().resetGameManagerReference();
                }
 
                dispose();
                window.showPanel("MainMenu");
            }
        });
 
        // Assemble layout
        panel.add(btnResume);
        panel.add(Box.createVerticalStrut(12));
        panel.add(btnSave);
        panel.add(Box.createVerticalStrut(12));
        panel.add(btnLoad);
        panel.add(Box.createVerticalStrut(20));
        panel.add(volumePanel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(btnMainMenu);
 
        add(panel);
        pack();
        setLocationRelativeTo(window);
    }
 
    private JButton createStyledMenuButton(String text, Color baseBg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(baseBg.brighter().brighter());
                } else {
                    g2.setColor(baseBg);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                Color border = getModel().isRollover() ? new Color(255, 200, 80) : new Color(180, 140, 50);
                g2.setColor(border);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 10, 10);
                
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2, (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
            }
        };
        btn.setPreferredSize(new Dimension(280, 48));
        btn.setMaximumSize(new Dimension(280, 48));
        btn.setFont(new Font("Serif", Font.BOLD, 18));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        return btn;
    }
}
