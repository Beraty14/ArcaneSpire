package views;

import controllers.GameManager;
import models.Player;
import javax.swing.*;
import java.awt.*;

public class CharacterSelectPanel extends JPanel {
    private GameWindow window;
    private Image bgImage;
    private JLabel titleLabel;
    private JPanel gridPanel;

    public CharacterSelectPanel(GameWindow window) {
        this.window = window;
        setLayout(new BorderLayout());
        bgImage = window.loadImage("bg_character_select.png");

        titleLabel = new JLabel(models.LanguageManager.getInstance().getText("char_select_title"), SwingConstants.CENTER);
        Font titleFont = new Font("Serif", Font.BOLD, 38);
        java.util.Map<java.awt.font.TextAttribute, Object> attributes = new java.util.HashMap<>();
        attributes.put(java.awt.font.TextAttribute.TRACKING, 0.2);
        titleLabel.setFont(titleFont.deriveFont(attributes));
        
        titleLabel.setForeground(new Color(255, 215, 0));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(60, 0, 40, 0));
        add(titleLabel, BorderLayout.NORTH);

        gridPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 20));
        gridPanel.setOpaque(false);
        add(gridPanel, BorderLayout.CENTER);

        updateContent();

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent e) {
                updateContent();
            }
        });
    }

    private void updateContent() {
        models.LanguageManager lm = models.LanguageManager.getInstance();
        titleLabel.setText(lm.getText("char_select_title"));
        
        gridPanel.removeAll();
        gridPanel.add(createCharCard(lm.getText("hero_knight_name"), "90", "3", "5", lm.getText("hero_knight_desc"), lm.getText("hero_knight_lore"), "hero_knight.png.png", Player.HeroClass.KNIGHT));
        gridPanel.add(createCharCard(lm.getText("hero_mage_name"), "65", "4", "6", lm.getText("hero_mage_desc"), lm.getText("hero_mage_lore"), "hero_mage.png.jpg", Player.HeroClass.MAGE));
        gridPanel.add(createCharCard(lm.getText("hero_rogue_name"), "70", "3", "5", lm.getText("hero_rogue_desc"), lm.getText("hero_rogue_lore"), "hero_rogue.png.png", Player.HeroClass.ROGUE));
        gridPanel.add(createCharCard(lm.getText("hero_paladin_name"), "85", "3", "5", lm.getText("hero_paladin_desc"), lm.getText("hero_paladin_lore"), "hero_paladin.png.png", Player.HeroClass.PALADIN));
        
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private JPanel createCharCard(String name, String hp, String energy, String cards, String desc, String lore, String imgName, Player.HeroClass heroClass) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(12, 8, 25, 240));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(210, 380));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createLineBorder(new Color(184, 134, 11, 90), 2, true));

        Image img = window.loadImage(imgName);
        if (img != null) {
            Image scaled = img.getScaledInstance(110, 130, Image.SCALE_SMOOTH);
            JLabel imgLabel = new JLabel(new ImageIcon(scaled));
            imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            imgLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(15, 0, 10, 0),
                BorderFactory.createLineBorder(new Color(184, 134, 11, 65), 2)
            ));
            card.add(imgLabel);
        }

        JLabel nameLbl = new JLabel(name);
        Font nameFont = new Font("Serif", Font.BOLD, 16);
        java.util.Map<java.awt.font.TextAttribute, Object> attr = new java.util.HashMap<>();
        attr.put(java.awt.font.TextAttribute.TRACKING, 0.15);
        nameLbl.setFont(nameFont.deriveFont(attr));
        nameLbl.setForeground(new Color(255, 215, 0));
        nameLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(nameLbl);
        card.add(Box.createVerticalStrut(8));

        models.LanguageManager lm = models.LanguageManager.getInstance();
        
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        statsPanel.setOpaque(false);
        statsPanel.add(createStatBadge("❤", hp + " HP", new Color(200, 50, 50)));
        statsPanel.add(createStatBadge("⚡", energy, new Color(200, 160, 30)));
        statsPanel.add(createStatBadge("🃏", cards, new Color(80, 120, 220)));
        statsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(statsPanel);
        card.add(Box.createVerticalStrut(8));

        JTextArea descArea = new JTextArea(desc);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setOpaque(false);
        descArea.setForeground(new Color(150, 140, 170));
        descArea.setFont(new Font("Serif", Font.BOLD, 12));
        descArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        descArea.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        card.add(descArea);

        JTextArea loreArea = new JTextArea(lore);
        loreArea.setLineWrap(true);
        loreArea.setWrapStyleWord(true);
        loreArea.setOpaque(false);
        loreArea.setForeground(new Color(138, 122, 170));
        loreArea.setFont(new Font("Serif", Font.ITALIC, 11));
        loreArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        loreArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(184, 134, 11, 40)),
            BorderFactory.createEmptyBorder(8, 10, 10, 10)
        ));
        card.add(loreArea);

        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CinematicPanel cin = window.getCinematicPanel();
                if (cin != null) cin.setHeroChoice(heroClass);
                window.showPanel("Cinematic");
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0, 200), 2));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBorder(BorderFactory.createLineBorder(new Color(184, 134, 11, 100), 2));
            }
        });

        return card;
    }

    private JPanel createStatBadge(String icon, String text, Color color) {
        JPanel badge = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 40));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
            }
        };
        badge.setLayout(new FlowLayout(FlowLayout.CENTER, 4, 2));
        badge.setOpaque(false);
        
        JLabel lbl = new JLabel(icon + " " + text);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        lbl.setForeground(Color.WHITE);
        badge.add(lbl);
        return badge;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bgImage != null) {
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            g.setColor(new Color(0, 0, 0, 100));
            g.fillRect(0, 0, getWidth(), getHeight());
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
