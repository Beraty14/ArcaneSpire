package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import models.Card;

/**
 * Kart butonu. Kartın görselini, ismini, enerji bedelini ve açıklamasını gösterir.
 * Üzerine gelince büyüme efekti uygulanır.
 */
public class CardButton extends JButton {
    private Card card;
    private Image cardIcon;
    private boolean hovered = false;

    public CardButton(Card card, GameWindow window) {
        this.card = card;
        setPreferredSize(new Dimension(130, 210));
        setMinimumSize(new Dimension(130, 210));
        setMaximumSize(new Dimension(130, 210));
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (card != null) {
            setToolTipText(card.getDescription());
            cardIcon = window.loadImage(card.getIconPath());
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight() - 20; // reserved space for hover
        int offsetY = hovered ? 0 : 15; // Hover goes up to 0, normal is at 15

        // Kart arkaplanı ve rengi (Polymorphism / OOP kullanımı için instanceof)
        Color cardBg = new Color(40, 30, 55);
        Color borderColor = new Color(140, 110, 40);

        if (card instanceof models.AttackCard) {
            cardBg = new Color(60, 20, 20);
            borderColor = new Color(200, 50, 50);
        } else if (card instanceof models.DefenseCard) {
            cardBg = new Color(20, 30, 60);
            borderColor = new Color(50, 150, 200);
        } else if (card instanceof models.MagicCard) {
            cardBg = new Color(40, 20, 60);
            borderColor = new Color(150, 50, 200);
        }

        if (!isEnabled()) {
            cardBg = new Color(50, 50, 50);
            borderColor = new Color(80, 80, 80);
        } else if (hovered) {
            borderColor = new Color(255, 215, 0); // Üzerine gelince altın sarısı parlasın
        }

        g2.setColor(cardBg);
        g2.fillRoundRect(2, 2 + offsetY, w - 4, h - 4, 12, 12);
        g2.setStroke(new BasicStroke(2));
        g2.setColor(borderColor);
        g2.drawRoundRect(2, 2 + offsetY, w - 4, h - 4, 12, 12);

        // Enerji bedeli (sol üst köşe)
        if (card != null) {
            g2.setColor(new Color(60, 140, 255));
            g2.fillOval(8, 8 + offsetY, 28, 28);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Serif", Font.BOLD, 16));
            g2.drawString(String.valueOf(card.getEnergyCost()), 17, 28 + offsetY);
        }

        // Kart ikonu
        int iconX = 15;
        int iconY = 40 + offsetY;
        int iconW = w - 30;
        int iconH = 80;
        if (cardIcon != null) {
            g2.drawImage(cardIcon, iconX, iconY, iconW, iconH, this);
        } else {
            g2.setColor(new Color(70, 50, 90));
            g2.fillRoundRect(iconX, iconY, iconW, iconH, 8, 8);
        }

        // Kart ismi
        g2.setColor(new Color(220, 200, 160));
        g2.setFont(new Font("Serif", Font.BOLD, 12));
        FontMetrics fm = g2.getFontMetrics();
        String title = card != null ? card.getTitle() : "Satıldı / Sold";
        int textX = (w - fm.stringWidth(title)) / 2;
        g2.drawString(title, textX, 135 + offsetY);

        // Kart açıklaması
        g2.setColor(new Color(180, 180, 200));
        g2.setFont(new Font("Serif", Font.PLAIN, 11));
        fm = g2.getFontMetrics();
        String desc = card != null ? card.getDescription() : "";
        int descX = (w - fm.stringWidth(desc)) / 2;
        g2.drawString(desc, descX, 155 + offsetY);

        // Devre dışıysa karartma
        if (!isEnabled()) {
            g2.setColor(new Color(0, 0, 0, 120));
            g2.fillRoundRect(2, 2 + offsetY, w - 4, h - 4, 12, 12);
        }
    }
}
