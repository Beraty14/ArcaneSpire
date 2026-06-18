package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import models.*;
import controllers.GameManager;
import controllers.AudioPlayer;

public class ShopPanel extends JPanel {
    private GameWindow window;
    private GameManager gm;
    private LanguageManager lm;

    // Background & Icons
    private Image bgImage;
    private Image heartIcon;
    private Image energyIcon;

    // Components
    private JLabel shopTitleLabel;
    private JLabel welcomeLabel;
    private JLabel goldLabel;
    private JLabel hpLabel;
    private JPanel goodsPanel;
    private JButton leaveBtn;

    // Items for sale
    private List<Card> cardsForSale = new ArrayList<>();
    private boolean[] cardsBought = new boolean[3];
    
    private boolean healPotionBought = false;
    private final int healPotionCost = 40;

    private boolean energyUpgradeBought = false;
    private final int energyUpgradeCost = 65;

    public ShopPanel(GameWindow window) {
        this.window = window;
        this.gm = GameManager.getInstance();
        this.lm = LanguageManager.getInstance();

        // Load background and custom item icons
        bgImage = window.loadImage("bg_reward.png");
        heartIcon = window.loadImage("icon_health_potion.png");
        energyIcon = window.loadImage("icon_energy_upgrade.png");

        setLayout(new BorderLayout());
        setBackground(new Color(25, 20, 35));
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        // --- Top Header Panel ---
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Glassmorphism background
                g2.setColor(new Color(40, 30, 55, 200));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                // Subtle glowing border
                g2.setColor(new Color(140, 90, 220, 120));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 15, 15);
            }
        };
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JPanel titleContainer = new JPanel();
        titleContainer.setLayout(new BoxLayout(titleContainer, BoxLayout.Y_AXIS));
        titleContainer.setOpaque(false);

        shopTitleLabel = new JLabel(lm.getText("shop_title"));
        shopTitleLabel.setFont(new Font("Serif", Font.BOLD, 42));
        shopTitleLabel.setForeground(new Color(255, 215, 0)); // Gold title
        shopTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        welcomeLabel = new JLabel(lm.getText("shop_welcome"));
        welcomeLabel.setFont(new Font("Serif", Font.ITALIC, 16));
        welcomeLabel.setForeground(new Color(180, 170, 200));
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        titleContainer.add(shopTitleLabel);
        titleContainer.add(Box.createVerticalStrut(5));
        titleContainer.add(welcomeLabel);

        JPanel statusContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 10));
        statusContainer.setOpaque(false);

        goldLabel = new JLabel();
        goldLabel.setFont(new Font("Serif", Font.BOLD, 26));
        goldLabel.setForeground(new Color(255, 220, 80));

        hpLabel = new JLabel();
        hpLabel.setFont(new Font("Serif", Font.BOLD, 26));
        hpLabel.setForeground(new Color(100, 240, 120));

        statusContainer.add(goldLabel);
        statusContainer.add(hpLabel);

        headerPanel.add(titleContainer, BorderLayout.WEST);
        headerPanel.add(statusContainer, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // --- Goods Showcase Panel (Center) ---
        goodsPanel = new JPanel(new GridBagLayout());
        goodsPanel.setOpaque(false);
        add(goodsPanel, BorderLayout.CENTER);

        // --- Bottom Navigation Panel ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        leaveBtn = new JButton(lm.getText("leave_shop")) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) g2.setColor(new Color(60, 45, 100));
                else g2.setColor(new Color(40, 25, 70));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(new Color(140, 90, 220));
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 15, 15);
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2, (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
            }
        };
        leaveBtn.setFont(new Font("Serif", Font.BOLD, 26));
        leaveBtn.setForeground(Color.WHITE);
        leaveBtn.setPreferredSize(new Dimension(280, 65));
        leaveBtn.setContentAreaFilled(false);
        leaveBtn.setBorderPainted(false);
        leaveBtn.setFocusPainted(false);
        leaveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        leaveBtn.addActionListener(e -> {
            AudioPlayer.playSFX("attack.wav"); // generic click SFX
            gm.advanceFloor();
            window.showPanel("Battle");
        });
        bottomPanel.add(leaveBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void resetGameManagerReference() {
        this.gm = GameManager.getInstance();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bgImage != null) {
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            g.setColor(new Color(0, 0, 0, 160));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public void initShop() {
        lm = LanguageManager.getInstance();
        shopTitleLabel.setText(lm.getText("shop_title"));
        welcomeLabel.setText(lm.getText("shop_welcome"));
        leaveBtn.setText(lm.getText("leave_shop"));
        
        Player p = gm.getPlayer();
        if (p == null) return;

        // Reset goods
        cardsForSale = CardLibrary.getRandomCards(3);
        cardsBought = new boolean[3];
        healPotionBought = false;
        energyUpgradeBought = false;

        refreshUI();
    }

    private void refreshUI() {
        Player p = gm.getPlayer();
        if (p == null) return;

        goldLabel.setText("🪙 " + lm.getText("gold_text") + p.getGold());
        hpLabel.setText("❤️ HP: " + p.getCurrentHp() + " / " + p.getMaxHp());

        goodsPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.gridy = 0;

        // --- Render Cards for Sale ---
        for (int i = 0; i < cardsForSale.size(); i++) {
            final Card card = cardsForSale.get(i);
            final int cardIndex = i;
            final boolean alreadyBought = cardsBought[i];

            JPanel itemWrapper = new JPanel(new BorderLayout(0, 10));
            itemWrapper.setOpaque(false);
            itemWrapper.setPreferredSize(new Dimension(140, 260));

            CardButton cardBtn = new CardButton(card, window);
            itemWrapper.add(cardBtn, BorderLayout.CENTER);

            JButton buyBtn;
            if (alreadyBought) {
                buyBtn = createStyledBuyButton(lm.getText("bought"), false);
                cardBtn.setEnabled(false);
            } else {
                buyBtn = createStyledBuyButton("50 " + lm.getText("gold_text"), p.getGold() >= 50);
                buyBtn.addActionListener(e -> {
                    if (p.spendGold(50)) {
                        p.getDeck().add(card);
                        p.addGuaranteedCard(card);
                        cardsBought[cardIndex] = true; // mark as bought
                        AudioPlayer.playSFX("gain_block.mp4"); // purchase sound
                        refreshUI();
                    } else {
                        JOptionPane.showMessageDialog(this, lm.getText("insufficient_gold"));
                    }
                });
            }

            itemWrapper.add(buyBtn, BorderLayout.SOUTH);

            gbc.gridx = i;
            goodsPanel.add(itemWrapper, gbc);
        }

        // --- Render Health Potion ---
        JPanel potionWrapper = new JPanel(new BorderLayout(0, 10));
        potionWrapper.setOpaque(false);
        potionWrapper.setPreferredSize(new Dimension(140, 260));

        JButton potionGraphic = new JButton() {
            private boolean hovered = false;
            {
                addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent e) { hovered = true; repaint(); }
                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e) { hovered = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight() - 20; // reserved space for hover, same as CardButton
                int offsetY = (hovered && isEnabled()) ? 0 : 15;
                
                // Draw card-like background
                g2.setColor(new Color(35, 25, 45));
                g2.fillRoundRect(2, 2 + offsetY, w - 4, h - 4, 12, 12);
                
                Color borderColor = healPotionBought ? Color.GRAY : new Color(200, 50, 50);
                if (hovered && !healPotionBought) {
                    borderColor = new Color(255, 215, 0); // Golden shine on hover
                }
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(2, 2 + offsetY, w - 4, h - 4, 12, 12);
                
                // Draw custom-generated health potion asset
                if (heartIcon != null) {
                    g2.drawImage(heartIcon, 25, 25 + offsetY, 80, 80, null);
                } else {
                    g2.setColor(healPotionBought ? Color.DARK_GRAY : new Color(255, 80, 80));
                    g2.fillOval(30, 45 + offsetY, 70, 70);
                }

                // Add glass reflection overlay
                if (!healPotionBought) {
                    g2.setColor(new Color(255, 255, 255, 30));
                    g2.fillOval(35, 35 + offsetY, 25, 25);
                }
                
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Serif", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("+25 HP", (w - fm.stringWidth("+25 HP")) / 2, 125 + offsetY);
                
                g2.setColor(new Color(220, 200, 160));
                g2.setFont(new Font("Serif", Font.BOLD, 12));
                fm = g2.getFontMetrics();
                String title = lm.getText("heal_potion");
                g2.drawString(title, (w - fm.stringWidth(title)) / 2, 155 + offsetY);

                g2.setColor(new Color(180, 180, 200));
                g2.setFont(new Font("Serif", Font.PLAIN, 10));
                fm = g2.getFontMetrics();
                String subtitle = lm.getText("shop_restore_hp");
                g2.drawString(subtitle, (w - fm.stringWidth(subtitle)) / 2, 175 + offsetY);

                if (healPotionBought) {
                    g2.setColor(new Color(0, 0, 0, 120));
                    g2.fillRoundRect(2, 2 + offsetY, w - 4, h - 4, 12, 12);
                }
            }
        };
        potionGraphic.setPreferredSize(new Dimension(130, 210));
        potionGraphic.setMinimumSize(new Dimension(130, 210));
        potionGraphic.setMaximumSize(new Dimension(130, 210));
        potionGraphic.setContentAreaFilled(false);
        potionGraphic.setFocusPainted(false);
        potionGraphic.setBorderPainted(false);
        potionGraphic.setToolTipText(lm.getText("heal_potion_desc"));
        potionGraphic.setEnabled(!healPotionBought);
        
        potionWrapper.add(potionGraphic, BorderLayout.CENTER);

        JButton buyPotionBtn;
        if (healPotionBought) {
            buyPotionBtn = createStyledBuyButton(lm.getText("bought"), false);
        } else {
            buyPotionBtn = createStyledBuyButton(healPotionCost + " " + lm.getText("gold_text"), p.getGold() >= healPotionCost);
            buyPotionBtn.addActionListener(e -> {
                if (p.spendGold(healPotionCost)) {
                    p.heal(25);
                    healPotionBought = true;
                    AudioPlayer.playSFX("heal.mp4"); // heal sound
                    refreshUI();
                } else {
                    JOptionPane.showMessageDialog(this, lm.getText("insufficient_gold"));
                }
            });
        }
        potionWrapper.add(buyPotionBtn, BorderLayout.SOUTH);

        gbc.gridx = 3;
        goodsPanel.add(potionWrapper, gbc);

        // --- Render Energy Upgrade ---
        JPanel energyWrapper = new JPanel(new BorderLayout(0, 10));
        energyWrapper.setOpaque(false);
        energyWrapper.setPreferredSize(new Dimension(140, 260));

        JButton energyGraphic = new JButton() {
            private boolean hovered = false;
            {
                addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent e) { hovered = true; repaint(); }
                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e) { hovered = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight() - 20; // reserved space for hover
                int offsetY = (hovered && isEnabled()) ? 0 : 15;
                
                // Draw card-like background
                g2.setColor(new Color(35, 25, 45));
                g2.fillRoundRect(2, 2 + offsetY, w - 4, h - 4, 12, 12);
                
                Color borderColor = energyUpgradeBought ? Color.GRAY : new Color(60, 150, 220);
                if (hovered && !energyUpgradeBought) {
                    borderColor = new Color(255, 215, 0); // Golden shine on hover
                }
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(2, 2 + offsetY, w - 4, h - 4, 12, 12);
                
                // Draw energy icon (real image asset)
                if (energyIcon != null) {
                    g2.drawImage(energyIcon, 25, 25 + offsetY, 80, 80, null);
                } else {
                    g2.setColor(energyUpgradeBought ? Color.DARK_GRAY : new Color(80, 160, 255));
                    g2.fillOval(30, 45 + offsetY, 70, 70);
                }

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Serif", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("+1 Max Energy", (w - fm.stringWidth("+1 Max Energy")) / 2, 125 + offsetY);
                
                g2.setColor(new Color(220, 200, 160));
                g2.setFont(new Font("Serif", Font.BOLD, 11));
                fm = g2.getFontMetrics();
                String miniTitle = lm.getText("shop_energy_boost");
                g2.drawString(miniTitle, (w - fm.stringWidth(miniTitle)) / 2, 155 + offsetY);

                g2.setColor(new Color(180, 180, 200));
                g2.setFont(new Font("Serif", Font.PLAIN, 10));
                fm = g2.getFontMetrics();
                String subtitle = lm.getText("shop_max_energy_plus_1");
                g2.drawString(subtitle, (w - fm.stringWidth(subtitle)) / 2, 175 + offsetY);

                if (energyUpgradeBought) {
                    g2.setColor(new Color(0, 0, 0, 120));
                    g2.fillRoundRect(2, 2 + offsetY, w - 4, h - 4, 12, 12);
                }
            }
        };
        energyGraphic.setPreferredSize(new Dimension(130, 210));
        energyGraphic.setMinimumSize(new Dimension(130, 210));
        energyGraphic.setMaximumSize(new Dimension(130, 210));
        energyGraphic.setContentAreaFilled(false);
        energyGraphic.setFocusPainted(false);
        energyGraphic.setBorderPainted(false);
        energyGraphic.setToolTipText(lm.getText("energy_upgrade_desc"));
        energyGraphic.setEnabled(!energyUpgradeBought);

        energyWrapper.add(energyGraphic, BorderLayout.CENTER);

        JButton buyEnergyBtn;
        if (energyUpgradeBought) {
            buyEnergyBtn = createStyledBuyButton(lm.getText("bought"), false);
        } else {
            buyEnergyBtn = createStyledBuyButton(energyUpgradeCost + " " + lm.getText("gold_text"), p.getGold() >= energyUpgradeCost);
            buyEnergyBtn.addActionListener(e -> {
                if (p.spendGold(energyUpgradeCost)) {
                    p.increaseMaxEnergy(1);
                    energyUpgradeBought = true;
                    AudioPlayer.playSFX("gain_block.mp4"); // upgrade sound
                    controllers.SaveManager.saveGame(gm, gm.getCurrentSaveSlot()); // autosave the upgraded energy
                    refreshUI();
                } else {
                    JOptionPane.showMessageDialog(this, lm.getText("insufficient_gold"));
                }
            });
        }
        energyWrapper.add(buyEnergyBtn, BorderLayout.SOUTH);

        gbc.gridx = 4;
        goodsPanel.add(energyWrapper, gbc);

        goodsPanel.revalidate();
        goodsPanel.repaint();
    }

    private JButton createStyledBuyButton(String text, boolean enabled) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth();
                int h = getHeight();
                boolean hovered = getModel().isRollover() && isEnabled();
                
                // Background color or gradient
                if (!isEnabled()) {
                    g2.setColor(new Color(60, 60, 60)); // dark gray when disabled
                } else if (hovered) {
                    g2.setColor(new Color(230, 170, 0)); // bright gold on hover
                } else {
                    g2.setColor(new Color(180, 130, 20)); // rich gold normally
                }
                g2.fillRoundRect(0, 0, w, h, 10, 10);
                
                // Border
                if (isEnabled()) {
                    g2.setColor(new Color(255, 220, 80));
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawRoundRect(0, 0, w - 1, h - 1, 10, 10);
                } else {
                    g2.setColor(new Color(90, 90, 90));
                    g2.setStroke(new BasicStroke(1));
                    g2.drawRoundRect(0, 0, w - 1, h - 1, 10, 10);
                }
                
                // Text
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.setColor(isEnabled() ? Color.BLACK : new Color(150, 150, 150));
                int textX = (w - fm.stringWidth(getText())) / 2;
                int textY = (h - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(getText(), textX, textY);
            }
        };
        btn.setFont(new Font("Serif", Font.BOLD, 15));
        btn.setPreferredSize(new Dimension(130, 35));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setEnabled(enabled);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
