package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Ana menü ekranı. Arkaplan resmi + şık butonlar.
 */
public class MainMenuPanel extends JPanel {
    private GameWindow window;
    private Image bgImage;
    private JButton btnLoad;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JButton btnPlay;
    private JButton btnHowToPlay;
    private JButton btnSettings;
    private JButton btnExit;

    public MainMenuPanel(GameWindow window) {
        this.window = window;
        setLayout(new GridBagLayout());
        bgImage = window.loadImage("bg_main_menu.png");

        // Buton paneli
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // Oyun Başlığı
        titleLabel = new JLabel(models.LanguageManager.getInstance().getText("title"));
        titleLabel.setFont(new Font("Serif", Font.BOLD, 56));
        titleLabel.setForeground(new Color(220, 180, 60));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        subtitleLabel = new JLabel(models.LanguageManager.getInstance().getText("subtitle"));
        subtitleLabel.setFont(new Font("Serif", Font.ITALIC, 24));
        subtitleLabel.setForeground(new Color(200, 200, 220));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Butonlar
        btnPlay = createMenuButton(models.LanguageManager.getInstance().getText("new_game"));
        btnPlay.addActionListener(e -> showSlotSelectionDialog(true));

        btnLoad = createMenuButton(models.LanguageManager.getInstance().getText("continue"));
        btnLoad.addActionListener(e -> showSlotSelectionDialog(false));
        
        btnLoad.setEnabled(controllers.SaveManager.hasAnySaveGame());
        
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                btnLoad.setEnabled(controllers.SaveManager.hasAnySaveGame());
                updateTexts();
            }
        });

        btnHowToPlay = createMenuButton(models.LanguageManager.getInstance().getText("how_to_play"));
        btnHowToPlay.addActionListener(e -> window.showPanel("HowToPlay"));

        btnSettings = createMenuButton(models.LanguageManager.getInstance().getText("settings"));
        btnSettings.addActionListener(e -> window.showPanel("Settings"));

        btnExit = createMenuButton(models.LanguageManager.getInstance().getText("exit"));
        btnExit.addActionListener(e -> showCustomExitDialog());

        centerPanel.add(titleLabel);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(subtitleLabel);
        centerPanel.add(Box.createVerticalStrut(50));
        centerPanel.add(btnPlay);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(btnLoad);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(btnHowToPlay);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(btnSettings);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(btnExit);

        add(centerPanel);
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Serif", Font.BOLD, 22));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(40, 30, 60));
        btn.setOpaque(true);
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 140, 50), 2),
            BorderFactory.createEmptyBorder(12, 40, 12, 40)
        ));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(320, 55));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!btn.isEnabled()) return;
                btn.setBackground(new Color(80, 50, 120));
                btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(255, 200, 80), 2),
                    BorderFactory.createEmptyBorder(12, 40, 12, 40)
                ));
            }
            public void mouseExited(MouseEvent e) {
                if (!btn.isEnabled()) return;
                btn.setBackground(new Color(40, 30, 60));
                btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(180, 140, 50), 2),
                    BorderFactory.createEmptyBorder(12, 40, 12, 40)
                ));
            }
        });
        return btn;
    }

    private void updateTexts() {
        models.LanguageManager lm = models.LanguageManager.getInstance();
        titleLabel.setText(lm.getText("title"));
        subtitleLabel.setText(lm.getText("subtitle"));
        btnPlay.setText(lm.getText("new_game"));
        btnLoad.setText(lm.getText("continue"));
        btnHowToPlay.setText(lm.getText("how_to_play"));
        btnSettings.setText(lm.getText("settings"));
        btnExit.setText(lm.getText("exit"));
    }

    private void showCustomExitDialog() {
        JDialog dialog = new JDialog(window, "Çıkış", true);
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(0, 0, 0, 0));
        
        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(25, 20, 35, 240));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(new Color(180, 140, 50));
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel lbl = new JLabel(models.LanguageManager.getInstance().getText("exit_confirm"));
        lbl.setFont(new Font("Serif", Font.BOLD, 24));
        lbl.setForeground(Color.WHITE);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        btnPanel.setOpaque(false);
        
        JButton btnYes = new JButton(models.LanguageManager.getInstance().getText("yes")) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) g2.setColor(new Color(200, 50, 50));
                else g2.setColor(new Color(150, 40, 40));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(new Color(255, 100, 100));
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 15, 15);
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2, (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
            }
        };
        btnYes.setPreferredSize(new Dimension(160, 50));
        btnYes.setFont(new Font("Serif", Font.BOLD, 22));
        btnYes.setContentAreaFilled(false);
        btnYes.setBorderPainted(false);
        btnYes.setFocusPainted(false);
        btnYes.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnYes.addActionListener(e -> System.exit(0));
        
        JButton btnNo = new JButton(models.LanguageManager.getInstance().getText("no")) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) g2.setColor(new Color(60, 130, 200));
                else g2.setColor(new Color(40, 100, 160));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(new Color(100, 180, 255));
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 15, 15);
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2, (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
            }
        };
        btnNo.setPreferredSize(new Dimension(160, 50));
        btnNo.setFont(new Font("Serif", Font.BOLD, 22));
        btnNo.setContentAreaFilled(false);
        btnNo.setBorderPainted(false);
        btnNo.setFocusPainted(false);
        btnNo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNo.addActionListener(e -> dialog.dispose());

        btnPanel.add(btnYes);
        btnPanel.add(btnNo);

        panel.add(lbl);
        panel.add(Box.createVerticalStrut(30));
        panel.add(btnPanel);

        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(window);
        dialog.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if (bgImage != null) {
            g2.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            
            float radius = Math.max(getWidth(), getHeight()) * 0.8f;
            java.awt.geom.Point2D center = new java.awt.geom.Point2D.Float(getWidth() / 2f, getHeight() * 0.25f);
            float[] dist = {0.0f, 1.0f};
            Color[] colors = {new Color(40, 20, 80, 140), new Color(5, 2, 12, 240)};
            RadialGradientPaint rgp = new RadialGradientPaint(center, radius, dist, colors);
            g2.setPaint(rgp);
            g2.fillRect(0, 0, getWidth(), getHeight());
        } else {
            // Gradient fallback
            GradientPaint gp = new GradientPaint(0, 0, new Color(20, 10, 40), 0, getHeight(), new Color(50, 20, 80));
            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private void showSlotSelectionDialog(boolean isNewGame) {
        SlotSelectionDialog ssd = new SlotSelectionDialog(window, isNewGame ? SlotSelectionDialog.Mode.NEW_GAME : SlotSelectionDialog.Mode.CONTINUE);
        ssd.setVisible(true);
    }
}
