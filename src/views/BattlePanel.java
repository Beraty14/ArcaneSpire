package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import models.*;
import controllers.GameManager;

public class BattlePanel extends JPanel implements GameManager.GameEventListener {
    private GameWindow window;
    private GameManager gm;

    // Görseller
    private Image bgImage;
    private Image enemyImage;
    private Image playerImage;
    private Image armorIcon;
    private Image manaIcon;
    private Image heartIcon;
    private Image btnPlayIcon;

    // Animasyon ve UI State
    private List<FloatingText> floatingTexts = new ArrayList<>();
    private Timer animTimer;
    private String turnOverlayText = null;
    private int turnOverlayAlpha = 0;

    // UI Panelleri
    private JPanel bottomPanel;
    private JPanel cardPanel;
    private JButton endTurnBtn;
    private JButton btnSaveExit;
    private JLabel floorLabel;
    private JPanel popupPanel;

    public BattlePanel(GameWindow window) {
        this.window = window;
        this.gm = GameManager.getInstance();
        gm.setListener(this);

        // İkonları Yükle
        armorIcon = window.loadImage("icon_armor.png.png");
        manaIcon = window.loadImage("icon_mana.png.png");
        heartIcon = window.loadImage("icon_heart.png.png");
        btnPlayIcon = window.loadImage("btn_play.png.png");

        setLayout(new BorderLayout());
        setBackground(Color.DARK_GRAY);

        // Üst bilgi paneli
        JPanel topInfo = new JPanel(new BorderLayout());
        topInfo.setOpaque(false);
        topInfo.setBorder(BorderFactory.createEmptyBorder(5, 15, 0, 15));

        floorLabel = new JLabel("Kat 1 - Zindan Girişi");
        floorLabel.setFont(new Font("Serif", Font.BOLD, 18));
        floorLabel.setForeground(new Color(220, 180, 60));

        btnSaveExit = new JButton("⚙ " + models.LanguageManager.getInstance().getText("settings_title")) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) g2.setColor(new Color(80, 50, 120));
                else g2.setColor(new Color(40, 30, 60));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(180, 140, 50));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 10, 10);
                g2.setColor(new Color(255, 230, 150));
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2, (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
            }
        };
        btnSaveExit.setPreferredSize(new Dimension(170, 40));
        btnSaveExit.setFont(new Font("Serif", Font.BOLD, 18));
        btnSaveExit.setContentAreaFilled(false);
        btnSaveExit.setBorderPainted(false);
        btnSaveExit.setFocusPainted(false);
        btnSaveExit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSaveExit.addActionListener(e -> {
            PauseDialog pd = new PauseDialog(window);
            pd.setVisible(true);
        });

        topInfo.add(btnSaveExit, BorderLayout.WEST);
        topInfo.add(floorLabel, BorderLayout.EAST);
        add(topInfo, BorderLayout.NORTH);

        // Alt panel: Kartlar + Tur Bitir butonu
        bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        cardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        cardPanel.setOpaque(false);

        endTurnBtn = new JButton(models.LanguageManager.getInstance().getText("end_turn")) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (!isEnabled()) {
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
                }
                GradientPaint gp = new GradientPaint(0, 0, new Color(139, 32, 32), 0, getHeight(), new Color(74, 10, 10));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(204, 68, 68));
                g2.setStroke(new BasicStroke(4));
                g2.drawRect(2, 2, getWidth() - 4, getHeight() - 4);
                
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.setColor(new Color(255, 215, 0));
                g2.drawString(getText(), textX, textY);
            }
        };
        endTurnBtn.setFont(new Font("Serif", Font.BOLD, 24));
        endTurnBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        endTurnBtn.setFocusPainted(false);
        endTurnBtn.setContentAreaFilled(false);
        endTurnBtn.setBorderPainted(false);
        endTurnBtn.setPreferredSize(new Dimension(260, 70));
        endTurnBtn.addActionListener(e -> {
            gm.endPlayerTurn();
        });

        // Butonun yatayda uzun kalması ve dikeyde esnememesi için GridBagLayout sarıcısı
        JPanel btnWrapper = new JPanel(new GridBagLayout());
        btnWrapper.setOpaque(false);
        btnWrapper.add(endTurnBtn);

        bottomPanel.add(cardPanel, BorderLayout.CENTER);
        bottomPanel.add(btnWrapper, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        // Animasyon timer (30ms = ~33 fps)
        animTimer = new Timer(30, e -> {
            boolean needRepaint = false;
            if (!floatingTexts.isEmpty()) {
                updateFloatingTexts();
                needRepaint = true;
            }
            if (turnOverlayAlpha > 0) {
                turnOverlayAlpha -= 6; // Yavaşça kaybolsun (~1.3 saniye)
                if (turnOverlayAlpha < 0) turnOverlayAlpha = 0;
                needRepaint = true;
            }
            if (needRepaint) repaint();
        });
        animTimer.start();
        
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent e) {
                models.LanguageManager lm = models.LanguageManager.getInstance();
                if (btnSaveExit != null) btnSaveExit.setText("⚙ " + lm.getText("settings_title"));
                if (endTurnBtn != null) endTurnBtn.setText(lm.getText("end_turn"));
                if (gm != null && floorLabel != null) floorLabel.setText(gm.getFloorName());
            }
        });
    }

    public void resetGameManagerReference() {
        this.gm = GameManager.getInstance();
        this.gm.setListener(this);
    }

    public void startBattle() {
        Player p = gm.getPlayer();
        Enemy en = gm.getCurrentEnemy();
        playerImage = window.loadImage(p.getImagePath());
        enemyImage = window.loadImage(en.getImagePath());
        bgImage = window.loadImage(gm.getBackgroundForFloor());
        floorLabel.setText(gm.getFloorName());
        refreshCards();
        repaint();
    }

    private void refreshCards() {
        cardPanel.removeAll();
        Player p = gm.getPlayer();
        if (p == null) return;

        List<Card> hand = p.getHand();
        for (int i = 0; i < hand.size(); i++) {
            Card c = hand.get(i);
            final int index = i;
            CardButton btn = new CardButton(c, window);
            btn.setEnabled(c.canPlay(p) && gm.isPlayerTurn() && popupPanel == null);
            btn.addActionListener(e -> {
                if (gm.playCard(index)) {
                    refreshCards();
                }
            });
            cardPanel.add(btn);
        }
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    // ===== Özel Popup Sistemi (JOptionPane Yerine) =====
    private void showIntroPopup(Enemy enemy, Runnable onComplete) {
        if (popupPanel != null) remove(popupPanel);
        bottomPanel.setVisible(false);

        popupPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 240));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        popupPanel.setLayout(new BoxLayout(popupPanel, BoxLayout.Y_AXIS));
        popupPanel.setOpaque(false);

        popupPanel.add(Box.createVerticalGlue());

        JLabel floorLbl = new JLabel(gm.getFloorName(), SwingConstants.CENTER);
        floorLbl.setFont(new Font("Serif", Font.BOLD, 48));
        floorLbl.setForeground(new Color(255, 215, 0));
        floorLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        popupPanel.add(floorLbl);

        popupPanel.add(Box.createVerticalStrut(40));

        Image img = window.loadImage(enemy.getImagePath());
        if (img != null) {
            Image scaled = img.getScaledInstance(200, 240, Image.SCALE_SMOOTH);
            JLabel imgLbl = new JLabel(new ImageIcon(scaled));
            imgLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
            popupPanel.add(imgLbl);
        }

        popupPanel.add(Box.createVerticalStrut(30));

        JLabel nameLbl = new JLabel(enemy.getName(), SwingConstants.CENTER);
        nameLbl.setFont(new Font("Serif", Font.BOLD, 36));
        nameLbl.setForeground(new Color(255, 80, 80));
        nameLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        popupPanel.add(nameLbl);

        popupPanel.add(Box.createVerticalGlue());

        add(popupPanel, BorderLayout.CENTER);
        revalidate();
        repaint();

        Timer t = new Timer(2800, e -> {
            closePopup();
            if (onComplete != null) onComplete.run();
        });
        t.setRepeats(false);
        t.start();
    }

    private void showPopup(String title, String text, boolean isReward, Runnable onConfirm) {
        if (popupPanel != null) remove(popupPanel);
        bottomPanel.setVisible(false); // Kartları gizle

        final int boxW = isReward ? 720 : 540;
        final int boxH = isReward ? 480 : 300;

        popupPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Yarı saydam karanlık arka plan
                g2.setColor(new Color(0, 0, 0, 200));
                g2.fillRect(0, 0, getWidth(), getHeight());

                int boxX = (getWidth() - boxW) / 2;
                int boxY = (getHeight() - boxH) / 2;

                // Kutu tasarımı
                g2.setColor(new Color(25, 20, 35));
                g2.fillRoundRect(boxX, boxY, boxW, boxH, 20, 20);
                g2.setColor(new Color(180, 140, 50));
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(boxX, boxY, boxW, boxH, 20, 20);
            }
        };
        popupPanel.setLayout(new GridBagLayout());
        popupPanel.setOpaque(false);

        JPanel contentBox = new JPanel();
        contentBox.setLayout(new BoxLayout(contentBox, BoxLayout.Y_AXIS));
        contentBox.setOpaque(false);
        contentBox.setPreferredSize(new Dimension(boxW - 60, boxH - 40));
        contentBox.setMaximumSize(new Dimension(boxW - 60, boxH - 40));

        // Use HTML to ensure title wraps and doesn't overflow
        JLabel titleLbl = new JLabel("<html><div style='text-align: center; width: " + (boxW - 80) + "px;'>" + title + "</div></html>", SwingConstants.CENTER);
        titleLbl.setFont(new Font("Serif", Font.BOLD, 26)); // Slightly smaller font size to prevent overflow
        titleLbl.setForeground(new Color(255, 215, 0));
        titleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentBox.add(titleLbl);
        contentBox.add(Box.createVerticalStrut(15));

        if (text != null && !text.isEmpty()) {
            JTextArea textLbl = new JTextArea(text);
            textLbl.setFont(new Font("Serif", Font.PLAIN, 18));
            textLbl.setForeground(new Color(220, 220, 220));
            textLbl.setOpaque(false);
            textLbl.setEditable(false);
            textLbl.setWrapStyleWord(true);
            textLbl.setLineWrap(true);
            textLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
            textLbl.setMaximumSize(new Dimension(boxW - 80, 120));
            contentBox.add(textLbl);
            contentBox.add(Box.createVerticalStrut(15));
        }

        if (isReward) {
            JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
            cardsPanel.setOpaque(false);
            List<Card> rewards = CardLibrary.getRandomCards(3);
            for (Card c : rewards) {
                CardButton cb = new CardButton(c, window);
                cb.addActionListener(e -> {
                    gm.getPlayer().getDeck().add(c);
                    gm.getPlayer().addGuaranteedCard(c);
                    closePopup();
                    if (onConfirm != null) onConfirm.run();
                });
                cardsPanel.add(cb);
            }
            contentBox.add(cardsPanel);
        } else {
            JButton btn = new JButton(models.LanguageManager.getInstance().getText("continue_btn"));
            btn.setFont(new Font("Serif", Font.BOLD, 22));
            btn.setForeground(Color.WHITE);
            
            if (btnPlayIcon != null) {
                Image scaledBtn = btnPlayIcon.getScaledInstance(180, 60, Image.SCALE_SMOOTH);
                btn.setIcon(new ImageIcon(scaledBtn));
                btn.setHorizontalTextPosition(SwingConstants.CENTER);
                btn.setVerticalTextPosition(SwingConstants.CENTER);
                btn.setOpaque(false);
                btn.setContentAreaFilled(false);
                btn.setBorderPainted(false);
            } else {
                btn.setBackground(new Color(80, 30, 30));
                btn.setOpaque(true);
                btn.setContentAreaFilled(false);
                btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 100, 50), 2),
                    BorderFactory.createEmptyBorder(10, 30, 10, 30)
                ));
            }
            
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.addActionListener(e -> {
                closePopup();
                if (onConfirm != null) onConfirm.run();
            });
            contentBox.add(btn);
        }

        popupPanel.add(contentBox);
        add(popupPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void closePopup() {
        if (popupPanel != null) {
            remove(popupPanel);
            popupPanel = null;
        }
        bottomPanel.setVisible(true);
        refreshCards();
        revalidate();
        repaint();
    }

    // ===== Graphics2D Çizim =====
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // Arkaplan
        if (bgImage != null) {
            g2.drawImage(bgImage, 0, 0, w, h, this);
            g2.setColor(new Color(0, 0, 0, 100)); // Hafif karartma
            g2.fillRect(0, 0, w, h);
        } else {
            GradientPaint gp = new GradientPaint(0, 0, new Color(30, 15, 50), 0, h, new Color(20, 10, 30));
            g2.setPaint(gp);
            g2.fillRect(0, 0, w, h);
        }

        Player p = gm.getPlayer();
        Enemy en = gm.getCurrentEnemy();
        if (p == null || en == null) return;

        // ---- Düşman çizimi (Sağ üst-orta) ----
        int enemyW = 280;
        int enemyH = 340;
        int enemyX = w - 400;
        int enemyY = h / 2 - 250; // Yukarıda ve ortalanmış

        // Düşman Kartı Arkaplan ve Çerçeve
        g2.setColor(new Color(80, 80, 85, 230)); // Gri arkaplan
        g2.fillRoundRect(enemyX, enemyY, enemyW, enemyH, 15, 15);
        g2.setColor(new Color(140, 140, 145)); // Çerçeve
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(enemyX, enemyY, enemyW, enemyH, 15, 15);

        // Düşman ismi (Kartın içi, üstte)
        g2.setColor(new Color(30, 30, 35, 200));
        g2.fillRoundRect(enemyX + 3, enemyY + 3, enemyW - 6, 40, 12, 12);
        g2.fillRect(enemyX + 3, enemyY + 20, enemyW - 6, 23); // Alt köşeleri düzleştir

        g2.setFont(new Font("Serif", Font.BOLD, 24));
        g2.setColor(new Color(255, 120, 120));
        drawCenteredString(g2, en.getName(), enemyX, enemyY + 30, enemyW);

        // Düşman Görseli (İsmin altında)
        if (enemyImage != null) {
            g2.drawImage(enemyImage, enemyX + 10, enemyY + 45, enemyW - 20, enemyH - 55, this);
        } else {
            g2.setColor(new Color(80, 30, 30));
            g2.fillRect(enemyX + 10, enemyY + 45, enemyW - 20, enemyH - 55);
        }

        // Düşman can barı
        drawHealthBar(g2, enemyX, enemyY + enemyH + 15, enemyW, 26, en.getCurrentHp(), en.getMaxHp(), new Color(200, 40, 40), false);

        // Düşman zırhı
        if (en.getArmor() > 0) {
            if (armorIcon != null) {
                g2.drawImage(armorIcon, enemyX + enemyW + 25, enemyY + enemyH + 10, 30, 30, null);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Serif", Font.BOLD, 22));
                g2.drawString(String.valueOf(en.getArmor()), enemyX + enemyW + 60, enemyY + enemyH + 34);
            } else {
                g2.setColor(new Color(100, 150, 220));
                g2.setFont(new Font("Serif", Font.BOLD, 20));
                g2.drawString("🛡 " + en.getArmor(), enemyX + enemyW + 30, enemyY + enemyH + 35);
            }
        }

        // Oyuncu çizimi ve can barı
        int playerW = 280; // Düşman kartı ile aynı genişlik
        int playerH = 340; // Düşman kartı ile aynı uzunluk
        int playerX = 150;
        int playerY = h / 2 - 250;

        // Oyuncu Kartı Arkaplan ve Çerçeve
        g2.setColor(new Color(80, 80, 85, 230)); // Gri arkaplan
        g2.fillRoundRect(playerX, playerY, playerW, playerH, 15, 15);
        g2.setColor(new Color(140, 140, 145)); // Çerçeve
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(playerX, playerY, playerW, playerH, 15, 15);

        // Oyuncu Görseli (İsim yok, görseli tüm alana yay)
        if (playerImage != null) {
            g2.drawImage(playerImage, playerX + 8, playerY + 8, playerW - 16, playerH - 16, this);
        } else {
            g2.setColor(new Color(30, 50, 80));
            g2.fillRect(playerX + 8, playerY + 8, playerW - 16, playerH - 16);
        }
        
        // Can barını resmin HEMEN ALTINA koy (Kartın dışında)
        drawHealthBar(g2, playerX, playerY + playerH + 15, playerW, 26, p.getCurrentHp(), p.getMaxHp(), new Color(40, 180, 60), true);

        // Oyuncu zırh
        if (p.getArmor() > 0) {
            if (armorIcon != null) {
                g2.drawImage(armorIcon, playerX + playerW + 35, playerY + playerH + 10, 30, 30, null);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Serif", Font.BOLD, 22));
                g2.drawString(String.valueOf(p.getArmor()), playerX + playerW + 70, playerY + playerH + 34);
            } else {
                g2.setColor(new Color(100, 180, 255));
                g2.setFont(new Font("Serif", Font.BOLD, 20));
                g2.drawString("🛡 " + p.getArmor(), playerX + playerW + 40, playerY + playerH + 35);
            }
        }

        // Oyuncu Enerji göstergesi
        int energyY = playerY + playerH + 70;
        if (manaIcon != null) {
            g2.drawImage(manaIcon, playerX - 5, energyY - 25, 30, 30, null);
            g2.setFont(new Font("Serif", Font.BOLD, 28));
            g2.setColor(new Color(80, 160, 255));
            g2.drawString(p.getCurrentEnergy() + " / " + p.getMaxEnergy(), playerX + 35, energyY);
        } else {
            g2.setFont(new Font("Serif", Font.BOLD, 28));
            g2.setColor(new Color(80, 160, 255));
            g2.drawString("⚡ " + p.getCurrentEnergy() + " / " + p.getMaxEnergy(), playerX, energyY);
        }
        
        // Düşmanın Intent (niyet) simgesi eklenecekse buraya eklenebilir, şimdilik webdeki gibi düşman enerjisi/kartı gösterilmiyor.

        // ---- YENİ TUR OVERLAY ----
        if (turnOverlayAlpha > 0 && turnOverlayText != null) {
            g2.setFont(new Font("Serif", Font.BOLD, 80));
            // Gölge
            g2.setColor(new Color(0, 0, 0, turnOverlayAlpha));
            drawCenteredString(g2, turnOverlayText, 4, h / 2 + 4, w);
            // Yazı
            if (turnOverlayText.equals(models.LanguageManager.getInstance().getText("enemy_turn"))) g2.setColor(new Color(255, 60, 60, turnOverlayAlpha));
            else g2.setColor(new Color(80, 200, 255, turnOverlayAlpha));
            drawCenteredString(g2, turnOverlayText, 0, h / 2, w);
        }

        // ---- Floating Texts (Animasyonlu hasar yazıları) ----
        for (FloatingText ft : floatingTexts) {
            g2.setFont(new Font("Serif", Font.BOLD, ft.size));
            // Siyah çerçeve (Gölge) - Daha ince
            g2.setColor(new Color(0, 0, 0, Math.max(0, ft.alpha)));
            g2.drawString(ft.text, ft.x + 2, ft.y + 2);
            // Gerçek yazı
            g2.setColor(new Color(ft.color.getRed(), ft.color.getGreen(), ft.color.getBlue(), Math.max(0, ft.alpha)));
            g2.drawString(ft.text, ft.x, ft.y);
        }
    }

    private void drawHealthBar(Graphics2D g2, int x, int y, int width, int height, int current, int max, Color barColor, boolean isPlayer) {
        if (heartIcon != null) {
            int hx = isPlayer ? x - 25 : x + width - 5;
            g2.drawImage(heartIcon, hx, y - 2, 28, 28, null);
        }
        
        g2.setColor(new Color(20, 20, 20, 200));
        g2.fillRoundRect(x, y, width, height, 8, 8);
        double ratio = (double) current / max;
        int barW = (int) (width * ratio);
        g2.setColor(barColor);
        g2.fillRoundRect(x, y, barW, height, 8, 8);
        g2.setColor(new Color(200, 200, 200, 150));
        g2.drawRoundRect(x, y, width, height, 8, 8);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Serif", Font.BOLD, 14));
        String hpText = current + " / " + max;
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(hpText, x + (width - fm.stringWidth(hpText)) / 2, y + height - 5);
    }

    private void drawCenteredString(Graphics2D g2, String text, int x, int y, int width) {
        FontMetrics fm = g2.getFontMetrics();
        int textX = x + (width - fm.stringWidth(text)) / 2;
        g2.drawString(text, textX, y);
    }

    // ===== Floating Text Animasyonu =====
    private static class FloatingText {
        String text;
        int x, y, size, alpha;
        Color color;
        double dy;

        FloatingText(String text, int x, int y, Color color, int size) {
            this.text = text;
            this.x = x;
            this.y = y;
            this.color = color;
            this.size = size;
            this.alpha = 255;
            this.dy = -2.0; // Normal hız
        }
    }

    private void addFloatingText(String text, int baseX, int baseY, Color color) {
        // Üst üste binmemesi için rastgele X ve Y ofseti ekliyoruz (Web'deki Math.random() mantığı)
        int randX = baseX + (int)(Math.random() * 60 - 30);
        int randY = baseY + (int)(Math.random() * 40 - 20);
        floatingTexts.add(new FloatingText(text, randX, randY, color, 34)); // Yazı boyutu
    }

    private void updateFloatingTexts() {
        for (int i = floatingTexts.size() - 1; i >= 0; i--) {
            FloatingText ft = floatingTexts.get(i);
            ft.y += ft.dy;
            ft.alpha -= 5; // Normal kaybolma
            if (ft.alpha <= 0) {
                floatingTexts.remove(i);
            }
        }
    }

    // ===== GameEventListener Implementasyonu =====
    @Override
    public void onBattleStart(Player player, Enemy enemy) {
        enemyImage = window.loadImage(enemy.getImagePath());
        bgImage = window.loadImage(gm.getBackgroundForFloor());
        floorLabel.setText(gm.getFloorName());
        
        // Savaş başlamadan önce İntro Popup göster!
        showIntroPopup(enemy, null);
    }

    @Override
    public void onPlayerTurnStart() {
        endTurnBtn.setEnabled(true);
        SwingUtilities.invokeLater(this::refreshCards);
        turnOverlayText = models.LanguageManager.getInstance().getText("your_turn");
        turnOverlayAlpha = 255;
    }

    @Override
    public void onCardPlayed(Card card, int damageDealt) {
        if (card instanceof AttackCard) {
            addFloatingText("-" + damageDealt, getWidth() - 250, getHeight() - 350, new Color(255, 60, 60));
        } else if (card instanceof DefenseCard) {
            addFloatingText("+" + ((DefenseCard)card).getArmorAmount() + " Zırh", 250, getHeight() - 350, new Color(100, 180, 255));
        } else if (card instanceof MagicCard) {
            if (((MagicCard)card).getHealAmount() > 0) {
                addFloatingText("+" + ((MagicCard)card).getHealAmount() + " HP", 250, getHeight() - 350, new Color(60, 255, 100));
            }
            if (((MagicCard)card).getDamageAmount() > 0) {
                addFloatingText("-" + ((MagicCard)card).getDamageAmount(), getWidth() - 250, getHeight() - 350, new Color(255, 60, 60));
            }
        }
        SwingUtilities.invokeLater(this::refreshCards);
    }

    @Override
    public void onEnemyTurnStart(String moveName, int moveDamage) {
        endTurnBtn.setEnabled(false);
        turnOverlayText = models.LanguageManager.getInstance().getText("enemy_turn");
        turnOverlayAlpha = 255;
    }

    @Override
    public void onEnemyAttack(int damage) {
        if (damage > 0) {
            addFloatingText("-" + damage, 250, getHeight() - 350, new Color(255, 80, 80));
        } else {
            addFloatingText(models.LanguageManager.getInstance().getText("armor"), getWidth() - 250, getHeight() - 350, new Color(100, 180, 255));
        }
    }

    @Override
    public void onBattleWon(int floor) {
        models.LanguageManager lm = models.LanguageManager.getInstance();
        if (floor >= 10) {
            controllers.AudioPlayer.stopMusic();
            bgImage = window.loadImage("bg_victory.png");
            repaint();
            showPopup(lm.getText("victory"), lm.getText("victory_desc"), false, () -> {
                GameManager.resetInstance();
                gm = GameManager.getInstance();
                gm.setListener(this);
                window.showPanel("MainMenu");
            });
        } else {
            String title = lm.getText("arena_cleared"); // e.g. "Arena Geçildi! +10 HP"
            String goldMsg = String.format(lm.getText("gold_earned_msg"), gm.getGoldEarnedLastBattle());
            
            // Card reward only on even floors (2, 4, 6, 8)
            boolean offerCard = (floor % 2 == 0);
            
            // Shop only on floors 3, 6, 9
            boolean visitShop = (floor == 3 || floor == 6 || floor == 9);
            
            Runnable nextAction = () -> {
                if (visitShop) {
                    window.showPanel("Shop");
                } else {
                    gm.advanceFloor();
                }
            };
            
            if (offerCard) {
                String body = goldMsg + "\n\n" + lm.getText("choose_card");
                showPopup(title, body, true, nextAction);
            } else {
                showPopup(title, goldMsg, false, nextAction);
            }
        }
    }

    @Override
    public void onGameOver() {
        controllers.AudioPlayer.stopMusic();
        controllers.AudioPlayer.playSFX("game_over.mp4");
        models.LanguageManager lm = models.LanguageManager.getInstance();
        showPopup(lm.getText("game_over"), lm.getText("game_over_desc") + "\n\n" + lm.getText("arena_word") + gm.getFloorName() + "\n" + lm.getText("turn_word") + gm.getTurnNumber(), false, () -> {
            GameManager.resetInstance();
            gm = GameManager.getInstance();
            gm.setListener(this);
            window.showPanel("MainMenu");
        });
    }

    @Override
    public void onFloorChange(int newFloor, Enemy newEnemy) {
        enemyImage = window.loadImage(newEnemy.getImagePath());
        bgImage = window.loadImage(gm.getBackgroundForFloor());
        floorLabel.setText(gm.getFloorName());
        // Intro popup onBattleStart içinden çağrılacak.
    }

    @Override
    public void onStatsUpdated() {
        repaint();
    }
}
