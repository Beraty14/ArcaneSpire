package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Oyunun Açılış Animasyonu (Intro Splash Screen) Ekranı.
 * ORIZON STUDIOS sunar.
 * Ken Burns kamera hareketi, süzülen sisler, şimşek vuruşu,
 * ve logo belirmesi içerir (şok dalgası veya partikül patlaması içermez).
 */
public class IntroPanel extends JPanel {
    private GameWindow window;
    private Image bgImage;
    private Timer animationTimer;
    private int tickCount = 0;
    private int state = 1; // 1: Şimşek Çakma Anı, 2: Logo Belirme, 3: Ana Logo Bekleme
    
    // Animasyon Parametreleri
    private float textAlpha = 0f;
    private float logoAlpha = 0f;
    private float lightningFlash = 0f;
    private List<List<Point>> lightningBranches = new ArrayList<>();
    private List<Cloud> clouds = new ArrayList<>();
    private Random random = new Random();
    private boolean proceeded = false;

    // Bulut (Sis) Sınıfı
    private static class Cloud {
        double x, y;
        double speed;
        double width, height;
        float opacity;

        Cloud(double x, double y, double speed, double width, double height, float opacity) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.width = width;
            this.height = height;
            this.opacity = opacity;
        }
    }

    public IntroPanel(GameWindow window) {
        this.window = window;
        setLayout(new BorderLayout());
        setFocusable(true);

        bgImage = window.loadImage("bg_loading.png");

        setupInteractions();
    }

    private void setupInteractions() {
        // Tıklama ile geçiş
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                proceedToMainMenu();
            }
        });

        // Klavye ile geçiş
        InputMap im = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        im.put(KeyStroke.getKeyStroke("released SPACE"), "skipIntro");
        im.put(KeyStroke.getKeyStroke("released ENTER"), "skipIntro");
        im.put(KeyStroke.getKeyStroke("released ESCAPE"), "skipIntro");
        
        am.put("skipIntro", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                proceedToMainMenu();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                proceedToMainMenu();
            }
        });
    }

    private void initClouds() {
        int w = getWidth();
        if (w <= 0) w = 1920;
        int h = getHeight();
        if (h <= 0) h = 1080;

        clouds.clear();
        for (int i = 0; i < 4; i++) {
            double sizeW = 380 + random.nextInt(220);
            double sizeH = 140 + random.nextInt(80);
            double startX = random.nextInt(w + 400) - 200;
            double startY = random.nextInt((int)(h * 0.45));
            double speed = 0.2 + random.nextDouble() * 0.4;
            float opacity = 0.02f + random.nextFloat() * 0.04f;
            clouds.add(new Cloud(startX, startY, speed, sizeW, sizeH, opacity));
        }
    }

    @Override
    public void addNotify() {
        super.addNotify();
        resetIntro();
        if (animationTimer == null) {
            animationTimer = new Timer(30, e -> {
                tickCount++;
                updateIntroAnimation();
                repaint();
            });
        }
        animationTimer.start();
        requestFocusInWindow();
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        if (animationTimer != null) {
            animationTimer.stop();
        }
    }

    public void resetIntro() {
        tickCount = 0;
        state = 1;
        textAlpha = 0f;
        logoAlpha = 0f;
        lightningFlash = 0f;
        lightningBranches.clear();
        clouds.clear();
        proceeded = false;
    }

    private void updateIntroAnimation() {
        if (clouds.isEmpty() && getWidth() > 0) {
            initClouds();
        }

        // Sis hareketleri
        int w = getWidth();
        if (w <= 0) w = 1920;
        for (Cloud c : clouds) {
            c.x += c.speed;
            if (c.x > w + c.width) {
                c.x = -c.width;
                c.y = random.nextInt((int)(getHeight() * 0.45));
                c.speed = 0.2 + random.nextDouble() * 0.4;
                c.opacity = 0.02f + random.nextFloat() * 0.04f;
            }
        }

        // Şimşek parlamasını sönümlendir
        if (lightningFlash > 0f) {
            lightningFlash -= 0.04f;
            if (lightningFlash < 0f) lightningFlash = 0f;
        }

        switch (state) {
            case 1:
                triggerLightningStrike();
                state = 2;
                break;

            case 2:
                if (logoAlpha < 1.0f) {
                    logoAlpha = Math.min(1.0f, logoAlpha + 0.03f);
                } else {
                    logoAlpha = 1.0f;
                    state = 3;
                }
                break;

            case 3:
                if (tickCount > 280) {
                    proceedToMainMenu();
                }
                break;
        }
    }

    private void triggerLightningStrike() {
        int width = getWidth();
        int height = getHeight();
        if (width <= 0) width = 800;
        if (height <= 0) height = 600;

        lightningFlash = 1.0f;
        lightningBranches.clear();

        // 3-4 farklı ana şimşek damarı, ekrana tam yayılmış halde
        int numMainBolts = 3 + random.nextInt(2); 
        int regionWidth = width / numMainBolts;
        
        for (int b = 0; b < numMainBolts; b++) {
            List<Point> branch = new ArrayList<>();
            // Her şimşek ekranın farklı bir bölgesinden (sol, orta, sağ) başlar
            int curX = (b * regionWidth) + 20 + random.nextInt(Math.max(1, regionWidth - 40));
            int curY = 0;
            branch.add(new Point(curX, curY));

            int segments = 15 + random.nextInt(10); // Daha kırıklı gerçekçi yapı
            int stepY = (height / 2 + random.nextInt(height / 3)) / segments;
            
            // Ekranın solundakiler sola, sağındakiler sağa doğru eğilim göstersin (saçaklanma)
            int horizontalDrift = (curX < width / 2) ? -20 : 20;
            
            for (int i = 1; i < segments; i++) {
                curY += stepY + random.nextInt(20) - 5;
                curX += (random.nextInt(160) - 80) + horizontalDrift; // Keskin ve geniş zikzaklar
                branch.add(new Point(curX, curY));
                
                // %50 ihtimalle alt dal (çatallanma) oluştur
                if (random.nextDouble() > 0.5) {
                    List<Point> subBranch = new ArrayList<>();
                    subBranch.add(new Point(curX, curY));
                    int subX = curX;
                    int subY = curY;
                    int subSegments = random.nextInt(8) + 4;
                    for (int j = 0; j < subSegments; j++) {
                        subY += stepY + random.nextInt(15) - 5;
                        subX += (random.nextInt(240) - 120) + horizontalDrift; // Dalların yatayda iyice açılması
                        subBranch.add(new Point(subX, subY));
                    }
                    lightningBranches.add(subBranch);
                }
            }
            lightningBranches.add(branch);
        }
        
        // Gerçekçi şimşek ses efekti
        controllers.AudioPlayer.playSFX("thunder.mp3");
    }

    private void proceedToMainMenu() {
        if (tickCount < 15) return; // Terminalden enter'a basılıp açıldığında anında geçmesini önlemek için
        if (!proceeded) {
            proceeded = true;
            if (animationTimer != null) {
                animationTimer.stop();
            }
            window.showPanel("MainMenu");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();

        // 1. Ken Burns Kamera Hareketiyle Arkaplanı Çiz
        if (bgImage != null) {
            double scale = 1.07 + Math.sin(tickCount * 0.003) * 0.05;
            double dx = Math.cos(tickCount * 0.004) * 22;
            double dy = Math.sin(tickCount * 0.002) * 14;
            
            int drawW = (int)(width * scale);
            int drawH = (int)(height * scale);
            int drawX = (width - drawW) / 2 + (int)dx;
            int drawY = (height - drawH) / 2 + (int)dy;

            g2.drawImage(bgImage, drawX, drawY, drawW, drawH, this);

            // 1.2 Sis Katmanları
            for (Cloud c : clouds) {
                Graphics2D gCloud = (Graphics2D) g2.create();
                gCloud.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                java.awt.geom.Point2D center = new java.awt.geom.Point2D.Double(c.x + c.width / 2.0, c.y + c.height / 2.0);
                float radius = (float)(Math.max(c.width, c.height) / 2.0);
                float[] dist = {0.0f, 0.7f, 1.0f};
                
                Color cCenter = new Color(180, 200, 240, (int)(c.opacity * 255));
                Color cMid = new Color(180, 200, 240, (int)(c.opacity * 0.4f * 255));
                Color cEdge = new Color(180, 200, 240, 0);
                Color[] colors = {cCenter, cMid, cEdge};
                
                RadialGradientPaint rgp = new RadialGradientPaint(center, radius, dist, colors);
                gCloud.setPaint(rgp);
                
                java.awt.geom.Ellipse2D ellipse = new java.awt.geom.Ellipse2D.Double(c.x, c.y, c.width, c.height);
                gCloud.fill(ellipse);
                gCloud.dispose();
            }

            g2.setColor(new Color(5, 2, 15, 210));
            g2.fillRect(0, 0, width, height);
        } else {
            g2.setColor(new Color(5, 2, 15));
            g2.fillRect(0, 0, width, height);
        }

        // 2. Logo (ARCANE SPIRE) (Orizon Studios yazısı kaldırıldı)
        if ((state >= 2 || state == 1) && logoAlpha > 0f) {
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            String titleText = "ARCANE SPIRE";
            double scale = 1.0;
            if (state == 3) {
                scale = 1.0 + Math.sin(tickCount * 0.05) * 0.015;
            }

            g2.setFont(new Font("Serif", Font.BOLD, (int)(72 * scale)));
            FontMetrics fm = g2.getFontMetrics();
            int lx = (width - fm.stringWidth(titleText)) / 2;
            int ly = (height - fm.getHeight()) / 2 + fm.getAscent() - 50;

            g2.setColor(new Color(220, 170, 50, (int)(logoAlpha * 80)));
            g2.drawString(titleText, lx + 3, ly + 3);
            g2.drawString(titleText, lx - 3, ly - 3);

            g2.setColor(new Color(255, 215, 0, (int)(logoAlpha * 255)));
            g2.drawString(titleText, lx, ly);

            String subtitleText = "K A R T L A R I N   Ç A R P I Ş M A S I";
            g2.setFont(new Font("Serif", Font.ITALIC, (int)(18 * scale)));
            fm = g2.getFontMetrics();
            int sx = (width - fm.stringWidth(subtitleText)) / 2;
            int sy = ly + 55;
            g2.setColor(new Color(200, 200, 220, (int)(logoAlpha * 210)));
            g2.drawString(subtitleText, sx, sy);
        }

        // 3. Gerçekçi Çatallı Şimşek Çizimi (8K Kalitesinde Çok Katmanlı Parlama)
        if (state == 1 || (state == 2 && lightningFlash > 0.1f)) {
            if (!lightningBranches.isEmpty()) {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // En Dış Parlama (Mavi Glow)
                g2.setStroke(new BasicStroke(12f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.setColor(new Color(100, 200, 255, (int)(lightningFlash * 100)));
                for (List<Point> branch : lightningBranches) {
                    for (int i = 0; i < branch.size() - 1; i++) {
                        g2.drawLine(branch.get(i).x, branch.get(i).y, branch.get(i+1).x, branch.get(i+1).y);
                    }
                }
                
                // Orta Parlama (Açık Mavi/Beyaz Glow)
                g2.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.setColor(new Color(200, 240, 255, (int)(lightningFlash * 180)));
                for (List<Point> branch : lightningBranches) {
                    for (int i = 0; i < branch.size() - 1; i++) {
                        g2.drawLine(branch.get(i).x, branch.get(i).y, branch.get(i+1).x, branch.get(i+1).y);
                    }
                }

                // İç Saf Beyaz Çekirdek (Core)
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.setColor(new Color(255, 255, 255, (int)(lightningFlash * 255)));
                for (List<Point> branch : lightningBranches) {
                    for (int i = 0; i < branch.size() - 1; i++) {
                        g2.drawLine(branch.get(i).x, branch.get(i).y, branch.get(i+1).x, branch.get(i+1).y);
                    }
                }
            }
        }

        // 4. Şimşek Parlama Katmanı (Tüm ekranın beyazlaması)
        if (lightningFlash > 0f) {
            g2.setColor(new Color(255, 255, 255, (int)(lightningFlash * 150)));
            g2.fillRect(0, 0, width, height);
        }

        // 6. Kılavuz Metni
        if (state == 3 && logoAlpha >= 1.0f) {
            double pulseText = Math.sin(tickCount * 0.1) * 0.5 + 0.5;
            g2.setFont(new Font("Serif", Font.PLAIN, 16));
            String pressKeyText = "Devam etmek için ekrana tıklayın veya bir tuşa basın...";
            FontMetrics fm = g2.getFontMetrics();
            int px = (width - fm.stringWidth(pressKeyText)) / 2;
            int py = height - 100;
            g2.setColor(new Color(180, 170, 150, (int)(pulseText * 180)));
            g2.drawString(pressKeyText, px, py);
        }
    }
}
