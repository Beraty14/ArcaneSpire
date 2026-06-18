package views;
 
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
/**
 * Click-through global overlay that draws a beautiful spinning crest
 * and localized notification banner in the top-right corner when the game is saved.
 */
public class SaveNotificationOverlay extends JPanel {
    private static final int FADE_IN = 0;
    private static final int DISPLAY = 1;
    private static final int FADE_OUT = 2;
 
    private float alpha = 0.0f;
    private float angle = 0.0f;
    private String message = "";
    private int state = FADE_IN;
    private int visibleTimeRemaining = 1500; // ms
 
    private Timer animTimer;
 
    public SaveNotificationOverlay() {
        setOpaque(false);
        setVisible(false);
 
        animTimer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (state == FADE_IN) {
                    alpha += 0.1f;
                    if (alpha >= 1.0f) {
                        alpha = 1.0f;
                        state = DISPLAY;
                    }
                } else if (state == DISPLAY) {
                    visibleTimeRemaining -= 30;
                    if (visibleTimeRemaining <= 0) {
                        state = FADE_OUT;
                    }
                } else if (state == FADE_OUT) {
                    alpha -= 0.08f;
                    if (alpha <= 0.0f) {
                        alpha = 0.0f;
                        setVisible(false);
                        animTimer.stop();
                    }
                }
                angle += 0.15f;
                repaint();
            }
        });
    }
 
    public void trigger(String msg) {
        this.message = msg;
        this.alpha = 0.0f;
        this.angle = 0.0f;
        this.visibleTimeRemaining = 1800; // Display for 1.8 seconds
        this.state = FADE_IN;
        
        setVisible(true);
        if (!animTimer.isRunning()) {
            animTimer.start();
        }
    }
 
    @Override
    public boolean contains(int x, int y) {
        // Return false to make this overlay completely click-through,
        // allowing the player to interact with the game underneath while saving.
        return false;
    }
 
    @Override
    protected void paintComponent(Graphics g) {
        if (alpha <= 0.0f) return;
 
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
 
        int boxW = 280;
        int boxH = 50;
        int paddingRight = 20;
        int paddingTop = 20;
        
        int boxX = getWidth() - boxW - paddingRight;
        int boxY = paddingTop;
 
        // Draw capsule background with fade
        g2.setColor(new Color(20, 15, 30, (int) (alpha * 220)));
        g2.fillRoundRect(boxX, boxY, boxW, boxH, 15, 15);
 
        // Draw golden border with fade
        g2.setColor(new Color(180, 140, 50, (int) (alpha * 255)));
        g2.setStroke(new BasicStroke(2.0f));
        g2.drawRoundRect(boxX, boxY, boxW, boxH, 15, 15);
 
        // Draw spinning loading crest on the left of capsule
        int spinnerX = boxX + 15;
        int spinnerY = boxY + 15;
        int spinnerSize = 20;
 
        Graphics2D gSpinner = (Graphics2D) g2.create();
        gSpinner.rotate(angle, spinnerX + spinnerSize / 2.0, spinnerY + spinnerSize / 2.0);
        gSpinner.setColor(new Color(255, 215, 0, (int) (alpha * 255)));
        gSpinner.setStroke(new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        gSpinner.drawArc(spinnerX, spinnerY, spinnerSize, spinnerSize, 0, 270);
        gSpinner.dispose();
 
        // Draw message text
        g2.setColor(new Color(240, 235, 220, (int) (alpha * 255)));
        g2.setFont(new Font("Serif", Font.BOLD, 15));
        FontMetrics fm = g2.getFontMetrics();
        int textX = spinnerX + spinnerSize + 15;
        int textY = boxY + (boxH - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(message, textX, textY);
 
        g2.dispose();
    }
}
