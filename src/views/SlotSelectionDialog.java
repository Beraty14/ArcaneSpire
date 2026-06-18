package views;
 
import controllers.GameManager;
import controllers.SaveManager;
import models.LanguageManager;
import models.Player;
 
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
 
/**
 * A beautiful, custom-designed slot selection modal dialog.
 * Replaces generic option dialogs with high-fidelity, detailed card profiles
 * featuring character avatars, gameplay statistics, play time trackers, and delete buttons.
 */
public class SlotSelectionDialog extends JDialog {
    public enum Mode {
        NEW_GAME,
        CONTINUE,
        SAVE,
        LOAD
    }
 
    private GameWindow window;
    private Mode mode;
    private LanguageManager lm;
    private JPanel slotsPanel;
 
    public SlotSelectionDialog(GameWindow window, Mode mode) {
        super(window, mode == Mode.SAVE ? "Oyunu Kaydet" : (mode == Mode.LOAD ? "Oyunu Yükle" : "Kayıt Yuvası Seçimi"), true);
        this.window = window;
        this.mode = mode;
        this.lm = LanguageManager.getInstance();
 
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
 
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(25, 20, 35, 248));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(new Color(180, 140, 50));
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 20, 20);
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
 
        // Dialog Title
        String titleText = lm.getText("save_slot");
        if (mode == Mode.SAVE) titleText = lm.getText("save_title");
        else if (mode == Mode.LOAD || mode == Mode.CONTINUE) titleText = lm.getText("load_title");
        else if (mode == Mode.NEW_GAME) titleText = lm.getText("new_game");
 
        JLabel titleLbl = new JLabel(titleText);
        titleLbl.setFont(new Font("Serif", Font.BOLD, 30));
        titleLbl.setForeground(new Color(255, 215, 0));
        titleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLbl);
        mainPanel.add(Box.createVerticalStrut(20));
 
        // Container for Slot Cards
        slotsPanel = new JPanel();
        slotsPanel.setOpaque(false);
        slotsPanel.setLayout(new BoxLayout(slotsPanel, BoxLayout.Y_AXIS));
        mainPanel.add(slotsPanel);
 
        // Cancel/Back Button
        JButton btnCancel = new JButton(lm.getText("back")) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) g2.setColor(new Color(120, 40, 40));
                else g2.setColor(new Color(90, 30, 30));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(250, 100, 100));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 10, 10);
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2, (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
            }
        };
        btnCancel.setPreferredSize(new Dimension(180, 42));
        btnCancel.setMaximumSize(new Dimension(180, 42));
        btnCancel.setFont(new Font("Serif", Font.BOLD, 18));
        btnCancel.setContentAreaFilled(false);
        btnCancel.setBorderPainted(false);
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCancel.addActionListener(e -> dispose());
        
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(btnCancel);
 
        add(mainPanel);
        buildSlots();
        
        // Window sizing constraints
        setSize(780, 680);
        setLocationRelativeTo(window);
    }
 
    private void buildSlots() {
        slotsPanel.removeAll();
        for (int slot = 1; slot <= 5; slot++) {
            slotsPanel.add(createSlotCard(slot));
            slotsPanel.add(Box.createVerticalStrut(12));
        }
        slotsPanel.revalidate();
        slotsPanel.repaint();
    }
 
    private JPanel createSlotCard(final int slot) {
        final SaveManager.SaveDetails details = SaveManager.getSaveDetails(slot);
        
        JPanel cardWrapper = new JPanel(new BorderLayout(15, 0));
        cardWrapper.setOpaque(false);
        cardWrapper.setMaximumSize(new Dimension(720, 95));
        cardWrapper.setPreferredSize(new Dimension(720, 95));
 
        // Main slot click area
        class ClickableCardPanel extends JPanel {
            private boolean rollover = false;
            
            public void setRollover(boolean r) {
                this.rollover = r;
                repaint();
            }
            
            public boolean isRollover() {
                return rollover;
            }
 
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                boolean isLoadable = details.exists || mode == Mode.NEW_GAME || mode == Mode.SAVE;
                
                if (!isLoadable) {
                    g2.setColor(new Color(40, 40, 45, 100)); // dimmed out
                } else if (rollover) {
                    g2.setColor(new Color(48, 35, 70));
                } else {
                    g2.setColor(new Color(30, 22, 45));
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                Color border = isLoadable ? (rollover ? new Color(255, 215, 0) : new Color(180, 140, 50, 150)) : new Color(80, 80, 80, 80);
                g2.setColor(border);
                g2.setStroke(new BasicStroke(2.0f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 12, 12);
            }
        }
 
        final ClickableCardPanel cardContent = new ClickableCardPanel();
        cardContent.setLayout(new GridBagLayout());
        cardContent.setOpaque(false);
        cardContent.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        boolean isActiveSlot = details.exists || mode == Mode.NEW_GAME || mode == Mode.SAVE;
        if (!isActiveSlot) {
            cardContent.setCursor(Cursor.getDefaultCursor());
        } else {
            // Roll-over highlight listeners
            cardContent.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    cardContent.setRollover(true);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    cardContent.setRollover(false);
                }
                @Override
                public void mouseClicked(MouseEvent e) {
                    handleSlotSelection(slot, details);
                }
            });
        }
 
        // Fill GridBagLayout contents
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(8, 15, 8, 15);
 
        // 1. Avatar (Hero Portrait)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        
        JLabel avatarLbl = new JLabel();
        avatarLbl.setPreferredSize(new Dimension(55, 65));
        avatarLbl.setBorder(BorderFactory.createLineBorder(new Color(180, 140, 50, 100), 2));
        if (details.exists && details.heroClass != null) {
            String imgName = "hero_knight.png.png";
            switch (Player.HeroClass.valueOf(details.heroClass)) {
                case MAGE: imgName = "hero_mage.png.jpg"; break;
                case ROGUE: imgName = "hero_rogue.png.png"; break;
                case PALADIN: imgName = "hero_paladin.png.png"; break;
            }
            Image img = window.loadImage(imgName);
            if (img != null) {
                avatarLbl.setIcon(new ImageIcon(img.getScaledInstance(55, 65, Image.SCALE_SMOOTH)));
            }
        } else {
            avatarLbl.setOpaque(true);
            avatarLbl.setBackground(new Color(40, 35, 50));
            avatarLbl.setHorizontalAlignment(SwingConstants.CENTER);
            avatarLbl.setForeground(new Color(100, 100, 120));
            avatarLbl.setText("?");
            avatarLbl.setFont(new Font("Serif", Font.BOLD, 22));
        }
        cardContent.add(avatarLbl, gbc);
 
        // 2. Info Container (Hero Class & Floor info)
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        
        JLabel slotTitle = new JLabel(lm.getLanguage().equals("TR") ? "Yuva " + slot : (lm.getLanguage().equals("ES") ? "Ranura " + slot : "Slot " + slot));
        slotTitle.setFont(new Font("Serif", Font.BOLD, 17));
        slotTitle.setForeground(new Color(255, 215, 0));
        infoPanel.add(slotTitle);
        infoPanel.add(Box.createVerticalStrut(4));
 
        if (details.exists) {
            String classKey = "hero_" + details.heroClass.toLowerCase() + "_name";
            String heroName = lm.getText(classKey);
            JLabel statsLbl = new JLabel(heroName + " - " + lm.getText("floor") + " " + details.floor);
            statsLbl.setFont(new Font("Serif", Font.BOLD, 14));
            statsLbl.setForeground(new Color(230, 220, 200));
            infoPanel.add(statsLbl);
        } else {
            JLabel emptyLbl = new JLabel(lm.getText("empty_slot"));
            emptyLbl.setFont(new Font("Serif", Font.ITALIC, 14));
            emptyLbl.setForeground(new Color(120, 120, 130));
            infoPanel.add(emptyLbl);
        }
        cardContent.add(infoPanel, gbc);
 
        // 3. Stats & Playtime Container
        gbc.gridx = 2;
        gbc.weightx = 0.8;
        JPanel statsPanel = new JPanel();
        statsPanel.setOpaque(false);
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        
        if (details.exists) {
            JLabel hpLbl = new JLabel("❤  HP: " + details.hp + "/" + (details.heroClass.equals("MAGE") ? 65 : (details.heroClass.equals("ROGUE") ? 70 : (details.heroClass.equals("PALADIN") ? 85 : 90))));
            hpLbl.setFont(new Font("SansSerif", Font.BOLD, 12));
            hpLbl.setForeground(new Color(220, 80, 80));
            
            JLabel goldLbl = new JLabel("🪙  " + lm.getText("gold_text") + details.gold);
            goldLbl.setFont(new Font("SansSerif", Font.BOLD, 12));
            goldLbl.setForeground(new Color(220, 180, 60));
            
            JLabel timeLbl = new JLabel("⏱  " + formatPlayTime(details.playTimeMs));
            timeLbl.setFont(new Font("SansSerif", Font.BOLD, 12));
            timeLbl.setForeground(new Color(150, 180, 220));
            
            statsPanel.add(hpLbl);
            statsPanel.add(Box.createVerticalStrut(2));
            statsPanel.add(goldLbl);
            statsPanel.add(Box.createVerticalStrut(2));
            statsPanel.add(timeLbl);
        }
        cardContent.add(statsPanel, gbc);
 
        // 4. Save metadata (Timestamp and type badge)
        gbc.gridx = 3;
        gbc.weightx = 0.8;
        JPanel metaPanel = new JPanel();
        metaPanel.setOpaque(false);
        metaPanel.setLayout(new BoxLayout(metaPanel, BoxLayout.Y_AXIS));
        metaPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        if (details.exists) {
            String typeText = lm.getText("autosave_notif");
            Color badgeColor = new Color(74, 112, 139);
            if ("MANUAL".equals(details.saveType)) {
                typeText = lm.getText("save_title");
                badgeColor = new Color(139, 90, 43);
            } else if ("QUICK".equals(details.saveType)) {
                typeText = lm.getText("quicksave_notif");
                badgeColor = new Color(90, 60, 120);
            }
            // Clean up the text (strip spin icons or dots)
            typeText = typeText.replace("...", "").replace(" ✔", "").trim();
 
            final String finalTypeText = typeText;
            final Color finalBadgeColor = badgeColor;
            JPanel badge = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(finalBadgeColor);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                }
            };
            badge.setOpaque(false);
            badge.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 3));
            JLabel badgeLbl = new JLabel(finalTypeText);
            badgeLbl.setFont(new Font("SansSerif", Font.BOLD, 10));
            badgeLbl.setForeground(Color.WHITE);
            badge.add(badgeLbl);
            badge.setMaximumSize(new Dimension(100, 20));
            
            JLabel savedTimeLbl = new JLabel(details.lastSaved != null ? details.lastSaved : "");
            savedTimeLbl.setFont(new Font("SansSerif", Font.PLAIN, 10));
            savedTimeLbl.setForeground(new Color(140, 130, 160));
            savedTimeLbl.setAlignmentX(Component.RIGHT_ALIGNMENT);
 
            metaPanel.add(badge);
            metaPanel.add(Box.createVerticalStrut(6));
            metaPanel.add(savedTimeLbl);
        }
        cardContent.add(metaPanel, gbc);
 
        cardWrapper.add(cardContent, BorderLayout.CENTER);
 
        // 5. Delete slot button (Crimson Red styled) on the right edge of occupied slots
        if (details.exists) {
            JButton btnDelete = new JButton(lm.getText("delete_btn")) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    if (getModel().isRollover()) g2.setColor(new Color(180, 40, 40));
                    else g2.setColor(new Color(110, 25, 25));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2.setColor(new Color(255, 100, 100));
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 10, 10);
                    g2.setColor(Color.WHITE);
                    g2.setFont(getFont());
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2, (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
                }
            };
            btnDelete.setPreferredSize(new Dimension(80, 95));
            btnDelete.setFont(new Font("Serif", Font.BOLD, 14));
            btnDelete.setContentAreaFilled(false);
            btnDelete.setBorderPainted(false);
            btnDelete.setFocusPainted(false);
            btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnDelete.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(this,
                    lm.getText("delete_confirm"),
                    lm.getText("delete_btn"),
                    JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    SaveManager.deleteSaveGame(slot);
                    // Dynamically refresh the UI slots panel
                    buildSlots();
                }
            });
            cardWrapper.add(btnDelete, BorderLayout.EAST);
        }
 
        return cardWrapper;
    }
 
    private void handleSlotSelection(int slot, SaveManager.SaveDetails details) {
        GameManager gm = GameManager.getInstance();
        
        if (mode == Mode.NEW_GAME) {
            if (details.exists) {
                int confirm = JOptionPane.showConfirmDialog(this,
                    lm.getLanguage().equals("TR") ? "Bu yuva zaten dolu. Üzerine yazmak istiyor musunuz?" :
                    (lm.getLanguage().equals("ES") ? "¿Esta ranura ya está ocupada. ¿Desea sobrescribirla?" : "This slot is already occupied. Do you want to overwrite it?"),
                    lm.getText("new_game"),
                    JOptionPane.YES_NO_OPTION
                );
                if (confirm != JOptionPane.YES_OPTION) return;
            }
            gm.setCurrentSaveSlot(slot);
            dispose();
            window.showPanel("CharacterSelect");
            
        } else if (mode == Mode.SAVE) {
            if (details.exists) {
                int confirm = JOptionPane.showConfirmDialog(this,
                    lm.getLanguage().equals("TR") ? "Seçilen yuvanın üzerine kaydetmek istiyor musunuz?" :
                    (lm.getLanguage().equals("ES") ? "¿Desea sobrescribir la ranura seleccionada?" : "Do you want to overwrite the selected slot?"),
                    lm.getText("save_title"),
                    JOptionPane.YES_NO_OPTION
                );
                if (confirm != JOptionPane.YES_OPTION) return;
            }
            gm.setCurrentSaveSlot(slot);
            SaveManager.saveGame(gm, slot, "MANUAL");
            dispose();
            
        } else if (mode == Mode.LOAD || mode == Mode.CONTINUE) {
            if (!details.exists) return;
            
            // Check for unsaved warning if loading inside active gameplay
            if (mode == Mode.LOAD && gm.isGameActive()) {
                int confirm = JOptionPane.showConfirmDialog(this,
                    lm.getText("unsaved_warning"),
                    lm.getText("load_title"),
                    JOptionPane.YES_NO_OPTION
                );
                if (confirm != JOptionPane.YES_OPTION) return;
            }
 
            controllers.AudioPlayer.stopMusic();
            gm.setCurrentSaveSlot(slot);
            if (SaveManager.loadGame(gm, slot)) {
                // Refresh local GameManager references inside active panels
                window.getBattlePanel().resetGameManagerReference();
                if (window.getShopPanel() != null) {
                    window.getShopPanel().resetGameManagerReference();
                }
 
                controllers.AudioPlayer.playBattleMusic();
                window.getBattlePanel().startBattle();
                dispose();
                window.showPanel("Battle");
            } else {
                JOptionPane.showMessageDialog(this, lm.getLanguage().equals("TR") ? "Kayıt yüklenirken hata oluştu!" : "Error loading save file!");
            }
        }
    }
 
    private String formatPlayTime(long ms) {
        long totalSecs = ms / 1000;
        long hours = totalSecs / 3600;
        long minutes = (totalSecs % 3600) / 60;
        long seconds = totalSecs % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
