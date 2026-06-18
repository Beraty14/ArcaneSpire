package views;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import models.LanguageManager;
import models.SettingsManager;

public class SettingsPanel extends JPanel {
    private GameWindow window;
    private Image bgImage;
    private JLabel titleLabel;
    private JLabel langLabel;
    private JLabel musicLabel;
    private JLabel sfxLabel;
    private JButton btnBack;
    
    private JPanel langPanel;
    private JButton btnTr, btnEn, btnEs;
    private JSlider musicSlider;
    private JSlider sfxSlider;
    
    public SettingsPanel(GameWindow window) {
        this.window = window;
        setLayout(new GridBagLayout());
        bgImage = window.loadImage("bg_loading.png");
        
        LanguageManager lm = LanguageManager.getInstance();
        SettingsManager sm = SettingsManager.getInstance();
        
        // Ana Kutu Paneli (Yarı saydam arkaplanlı)
        JPanel boxPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(20, 15, 25, 220));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                
                g2.setColor(new Color(200, 160, 50));
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 30, 30);
            }
        };
        boxPanel.setOpaque(false);
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));
        boxPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        
        titleLabel = new JLabel();
        titleLabel.setFont(new Font("Serif", Font.BOLD, 54));
        titleLabel.setForeground(new Color(255, 215, 0));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Dil Seçimi
        langLabel = new JLabel();
        langLabel.setFont(new Font("Serif", Font.BOLD, 26));
        langLabel.setForeground(new Color(230, 230, 230));
        langLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        langPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        langPanel.setOpaque(false);
        
        btnTr = createLangButton("TR", lm);
        btnEn = createLangButton("EN", lm);
        btnEs = createLangButton("ES", lm);
        langPanel.add(btnTr);
        langPanel.add(btnEn);
        langPanel.add(btnEs);
        updateLangButtons(lm.getLanguage());
        
        // Müzik Sesi
        musicLabel = new JLabel();
        musicLabel.setFont(new Font("Serif", Font.BOLD, 26));
        musicLabel.setForeground(new Color(230, 230, 230));
        musicLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        musicSlider = new JSlider(0, 100, sm.getMusicVolume());
        musicSlider.setOpaque(false);
        musicSlider.setMaximumSize(new Dimension(350, 50));
        musicSlider.setAlignmentX(Component.CENTER_ALIGNMENT);
        musicSlider.addChangeListener(e -> {
            sm.setMusicVolume(musicSlider.getValue());
            if (!musicSlider.getValueIsAdjusting()) {
                controllers.AudioPlayer.refreshMusicVolume();
            }
        });
        
        // Efekt Sesi
        sfxLabel = new JLabel();
        sfxLabel.setFont(new Font("Serif", Font.BOLD, 26));
        sfxLabel.setForeground(new Color(230, 230, 230));
        sfxLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        sfxSlider = new JSlider(0, 100, sm.getSfxVolume());
        sfxSlider.setOpaque(false);
        sfxSlider.setMaximumSize(new Dimension(350, 50));
        sfxSlider.setAlignmentX(Component.CENTER_ALIGNMENT);
        sfxSlider.addChangeListener(e -> {
            sm.setSfxVolume(sfxSlider.getValue());
            if (!sfxSlider.getValueIsAdjusting()) {
                controllers.AudioPlayer.playSFX("thunder.mp3"); 
            }
        });
        
        // Özel Geri Butonu
        btnBack = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(new Color(120, 40, 40));
                } else {
                    g2.setColor(new Color(80, 20, 20));
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(new Color(220, 100, 100));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 15, 15);
                
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2, (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
            }
        };
        btnBack.setPreferredSize(new Dimension(220, 55));
        btnBack.setMaximumSize(new Dimension(220, 55));
        btnBack.setFont(new Font("Serif", Font.BOLD, 26));
        btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(false);
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnBack.addActionListener(e -> window.showPanel("MainMenu"));
        
        boxPanel.add(titleLabel);
        boxPanel.add(Box.createVerticalStrut(50));
        
        boxPanel.add(langLabel);
        boxPanel.add(Box.createVerticalStrut(15));
        boxPanel.add(langPanel);
        boxPanel.add(Box.createVerticalStrut(40));
        
        boxPanel.add(musicLabel);
        boxPanel.add(Box.createVerticalStrut(10));
        boxPanel.add(musicSlider);
        boxPanel.add(Box.createVerticalStrut(40));
        
        boxPanel.add(sfxLabel);
        boxPanel.add(Box.createVerticalStrut(10));
        boxPanel.add(sfxSlider);
        boxPanel.add(Box.createVerticalStrut(60));
        
        boxPanel.add(btnBack);
        
        add(boxPanel);
        
        updateTexts();
        
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                updateTexts();
                musicSlider.setValue(sm.getMusicVolume());
                sfxSlider.setValue(sm.getSfxVolume());
                updateLangButtons(lm.getLanguage());
            }
        });
    }
    
    private JButton createLangButton(String langCode, LanguageManager lm) {
        JButton btn = new JButton(langCode) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean isSelected = lm.getLanguage().equals(langCode);
                
                if (isSelected) {
                    g2.setColor(new Color(150, 40, 40));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(80, 30, 40));
                } else {
                    g2.setColor(new Color(40, 20, 30));
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                g2.setColor(isSelected ? new Color(255, 215, 0) : new Color(120, 100, 120));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 12, 12);
                
                g2.setColor(isSelected ? Color.WHITE : new Color(200, 200, 200));
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2, (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
            }
        };
        btn.setPreferredSize(new Dimension(80, 45));
        btn.setFont(new Font("Serif", Font.BOLD, 22));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> {
            lm.setLanguage(langCode);
            updateLangButtons(langCode);
            updateTexts();
        });
        return btn;
    }
    
    private void updateLangButtons(String currentLang) {
        btnTr.repaint();
        btnEn.repaint();
        btnEs.repaint();
    }
    
    private void updateTexts() {
        LanguageManager lm = LanguageManager.getInstance();
        titleLabel.setText(lm.getText("settings_title"));
        langLabel.setText(lm.getText("language"));
        musicLabel.setText(lm.getText("music_vol"));
        sfxLabel.setText(lm.getText("sfx_vol"));
        btnBack.setText("❮ " + lm.getText("back"));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bgImage != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            g2.setColor(new Color(0, 0, 0, 150)); // Arkaplanı hafif karart
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
