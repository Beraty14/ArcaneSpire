package views;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OrizonSplashScreen extends JWindow {

    public OrizonSplashScreen(Runnable onComplete) {
        // Çerçevesiz, temiz bir pencere
        setSize(1024, 576);
        setLocationRelativeTo(null); 
        
        SplashPanel panel = new SplashPanel(this, onComplete);
        add(panel);
    }
    
    // Main.java ile uyumlu olması için eklendi
    public void startAnimation() {
        setVisible(true);
    }
}

class SplashPanel extends JPanel implements ActionListener {
    private Timer timer;
    private long startTime;
    private JWindow parent;
    private Runnable onComplete;

    // Renk Paleti (Karanlık tema, neon mor ve siyan detaylar)
    private final Color BG_COLOR = new Color(10, 10, 14);
    private final Color NEON_PURPLE = new Color(114, 9, 183);
    private final Color NEON_CYAN = new Color(76, 201, 240);

    public SplashPanel(JWindow parent, Runnable onComplete) {
        this.parent = parent;
        this.onComplete = onComplete;
        setBackground(BG_COLOR);
        
        // 60 FPS hedeflenmiş zamanlayıcı (~16ms)
        timer = new Timer(16, this);
        startTime = System.currentTimeMillis();
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
        long elapsed = System.currentTimeMillis() - startTime;
        
        // Toplam animasyon süresi: 7 Saniye (7000 ms)
        if (elapsed > 7000) { 
            timer.stop();
            parent.dispose(); // Açılış ekranını kapat
            if (onComplete != null) {
                onComplete.run(); // Oyunu başlat
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // 8K seviyesi keskinlik için Anti-Aliasing (Vektörel Pürüzsüzleştirme) aktif ediliyor
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        long elapsed = System.currentTimeMillis() - startTime;
        float time = elapsed / 1000f; // Saniye cinsinden geçen süre

        int cx = getWidth() / 2;
        int cy = getHeight() / 2 - 30;

        // 1. GLOBAL FADE IN & FADE OUT YÖNETİMİ
        float globalAlpha = 1.0f;
        if (time < 1.0f) {
            globalAlpha = time; // İlk 1 saniye yavaşça aydınlan
        } else if (time > 5.5f) {
            globalAlpha = 1.0f - ((time - 5.5f) / 1.5f); // Son 1.5 saniye karar
        }
        globalAlpha = Math.max(0f, Math.min(1f, globalAlpha));
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, globalAlpha));

        // 2. UFUK ÇİZGİSİ ANİMASYONU (The Horizon Line)
        float lineWidth = 0;
        if (time > 0.5f) {
            lineWidth = Math.min(500f, (time - 0.5f) * 400f);
        }
        
        if (lineWidth > 0) {
            Paint gradient = new LinearGradientPaint(
                cx - 250, cy, cx + 250, cy,
                new float[]{0f, 0.5f, 1f},
                new Color[]{
                    new Color(NEON_CYAN.getRed(), NEON_CYAN.getGreen(), NEON_CYAN.getBlue(), 0), 
                    NEON_PURPLE, 
                    new Color(NEON_CYAN.getRed(), NEON_CYAN.getGreen(), NEON_CYAN.getBlue(), 0)
                }
            );
            g2d.setPaint(gradient);
            g2d.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.draw(new Line2D.Float(cx - lineWidth / 2, cy, cx + lineWidth / 2, cy));
        }

        // 3. DOĞAN GÜNEŞ / YARIM AY (Orizon'un O'su)
        float arcAlpha = 0;
        if (time > 1.5f) {
            arcAlpha = Math.min(1.0f, (time - 1.5f) * 1.5f);
        }
        
        if (arcAlpha > 0) {
            float combinedArcAlpha = Math.max(0f, Math.min(1f, globalAlpha * arcAlpha));
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, combinedArcAlpha));
            
            g2d.setColor(NEON_PURPLE);
            g2d.setStroke(new BasicStroke(6f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
            // Sadece ufkun üstünde kalan kusursuz yarım daire
            g2d.draw(new Arc2D.Float(cx - 45, cy - 45, 90, 90, 0, 180, Arc2D.OPEN));
        }

        // 4. METİN ANİMASYONU ("O R I Z O N")
        float textAlpha = 0;
        if (time > 2.5f) {
            textAlpha = Math.min(1.0f, (time - 2.5f) * 1.2f);
        }
        
        if (textAlpha > 0) {
            float combinedTextAlpha = Math.max(0f, Math.min(1f, globalAlpha * textAlpha));
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, combinedTextAlpha));
            
            String mainText = "O R I Z O N";
            g2d.setFont(new Font("SansSerif", Font.BOLD, 42));
            FontMetrics fm = g2d.getFontMetrics();
            int tx = cx - fm.stringWidth(mainText) / 2;
            int ty = cy + 70;
            
            // Neon Siyan Gölge/Parlama Efekti
            g2d.setColor(new Color(NEON_CYAN.getRed(), NEON_CYAN.getGreen(), NEON_CYAN.getBlue(), 120));
            g2d.drawString(mainText, tx, ty + 3);
            
            // Ana Beyaz Metin
            g2d.setColor(Color.WHITE);
            g2d.drawString(mainText, tx, ty);

            // Alt Başlık ("S T U D I O S")
            String subText = "S T U D I O S";
            g2d.setFont(new Font("SansSerif", Font.PLAIN, 14));
            FontMetrics fm2 = g2d.getFontMetrics();
            int tx2 = cx - fm2.stringWidth(subText) / 2;
            
            g2d.setColor(new Color(150, 150, 155));
            g2d.drawString(subText, tx2, ty + 35);
        }
    }
}
