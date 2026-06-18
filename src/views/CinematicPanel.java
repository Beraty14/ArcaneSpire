package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import controllers.GameManager;
import models.Player;

public class CinematicPanel extends JPanel {
    private GameWindow window;
    private Image bgImage;
    private Image dragonImage;
    private Player.HeroClass selectedHero;
    
    private JPanel storyPanel;
    private JPanel answerPanel;
    private JTextField answerField;
    private JLabel answerLabel;
    private JButton btnSubmit;
    private JLabel timerLabel;
    private Timer countdownTimer;
    private int timeLeft;
    private Riddle currentRiddle;

    private static class Riddle {
        int id;
        Riddle(int id) { this.id = id; }
    }

    private Riddle[] riddles = {
        new Riddle(0),
        new Riddle(1),
        new Riddle(2),
        new Riddle(3),
        new Riddle(4)
    };

    public CinematicPanel(GameWindow window) {
        this.window = window;
        setLayout(new BorderLayout());
        bgImage = window.loadImage("bg_cinematic.png");
        if (bgImage == null) bgImage = window.loadImage("bg_riddle.png");
        dragonImage = window.loadImage("enemy_dragon_boss.png.png");
        if (dragonImage == null) dragonImage = window.loadImage("boss_dragon.png.jpg");

        storyPanel = new JPanel();
        storyPanel.setOpaque(false);
        storyPanel.setLayout(new BoxLayout(storyPanel, BoxLayout.Y_AXIS));
        storyPanel.setBorder(BorderFactory.createEmptyBorder(60, 100, 20, 100));

        answerPanel = new JPanel();
        answerPanel.setOpaque(false);
        answerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));

        answerField = new JTextField(15);
        answerField.setFont(new Font("Serif", Font.PLAIN, 20));
        answerField.setBackground(new Color(30, 20, 50));
        answerField.setForeground(Color.WHITE);
        answerField.setCaretColor(Color.WHITE);

        btnSubmit = new JButton(models.LanguageManager.getInstance().getText("answer_btn"));
        btnSubmit.setFont(new Font("Serif", Font.BOLD, 18));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setBackground(new Color(150, 40, 40));
        btnSubmit.setOpaque(true);
        btnSubmit.setBorderPainted(false);
        btnSubmit.setFocusPainted(false);

        timerLabel = new JLabel("⏱ 30");
        timerLabel.setFont(new Font("Serif", Font.BOLD, 24));
        timerLabel.setForeground(Color.RED);

        ActionListener submitAction = e -> checkAnswer();

        btnSubmit.addActionListener(submitAction);
        answerField.addActionListener(submitAction);

        answerLabel = new JLabel("<html><font color='#DAD2B4' size='4'>" + models.LanguageManager.getInstance().getText("your_answer") + "</font></html>");

        answerPanel.add(timerLabel);
        answerPanel.add(answerLabel);
        answerPanel.add(answerField);
        answerPanel.add(btnSubmit);

        add(storyPanel, BorderLayout.CENTER);
        add(answerPanel, BorderLayout.SOUTH);
        answerPanel.setVisible(false);
    }

    public void setHeroChoice(Player.HeroClass heroChoice) {
        this.selectedHero = heroChoice;
    }

    public void startCinematic() {
        String videoName = null;
        if (selectedHero == Player.HeroClass.MAGE) {
            videoName = "mage_intro.mp4";
        }
        // İleride diğer karakterler için de eklenebilir:
        // else if (selectedHero == Player.HeroClass.KNIGHT) videoName = "knight_intro.mp4";

        if (videoName != null) {
            playIntroVideo(videoName, this::showRiddleSequence);
        } else {
            showRiddleSequence();
        }
    }

    private void playIntroVideo(String videoName, Runnable onFinished) {
        String basePath = window.getBasePath();
        // mp4 uzantısını atarak temel dosya adını (örn: mage_intro) alıyoruz
        String baseName = videoName.replace(".mp4", "");
        
        // Klasör ve dosya isimlerini güncelle
        java.io.File framesDir = new java.io.File(basePath + java.io.File.separator + "src" + java.io.File.separator + "videos" + java.io.File.separator + baseName + "_frames");
        java.io.File wavFile = new java.io.File(basePath + java.io.File.separator + "src" + java.io.File.separator + "videos" + java.io.File.separator + baseName + ".wav");
        
        if (!framesDir.exists() || !wavFile.exists()) {
            System.out.println("Video dosyalari bulunamadi: " + framesDir.getAbsolutePath() + " veya " + wavFile.getAbsolutePath());
            onFinished.run();
            return;
        }

        // Meta dosyasından FPS ve Frame sayısını oku
        double fps = 24.0;
        int totalFrames = 240;
        try {
            java.util.Scanner sc = new java.util.Scanner(new java.io.File(framesDir, "meta.txt"));
            fps = Double.parseDouble(sc.nextLine());
            totalFrames = Integer.parseInt(sc.nextLine());
            sc.close();
        } catch (Exception e) {}

        storyPanel.removeAll();
        answerPanel.setVisible(false);
        storyPanel.setBackground(Color.BLACK);
        storyPanel.setOpaque(true);

        // Anlık çizilecek resmi tutan dizi
        final Image[] currentImage = new Image[1];
        JPanel videoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image img = currentImage[0];
                if (img != null) {
                    g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        videoPanel.setBackground(Color.BLACK);
        videoPanel.setOpaque(true);

        storyPanel.setLayout(new BorderLayout());
        storyPanel.setBorder(null);
        storyPanel.add(videoPanel, BorderLayout.CENTER);
        
        revalidate();
        repaint();

        // Ses klibini yükle
        javax.sound.sampled.Clip clip = null;
        try {
            javax.sound.sampled.AudioInputStream ais = javax.sound.sampled.AudioSystem.getAudioInputStream(wavFile);
            clip = javax.sound.sampled.AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final javax.sound.sampled.Clip finalClip = clip;
        final double finalFps = fps;
        final int finalTotalFrames = totalFrames;

        // Oynatma iş parçacığı
        new Thread(() -> {
            if (finalClip != null) {
                finalClip.start();
            }

            long startTime = System.currentTimeMillis();
            int lastDrawnFrame = -1;

            while (true) {
                long elapsedMicroseconds;
                if (finalClip != null && finalClip.isRunning()) {
                    elapsedMicroseconds = finalClip.getMicrosecondPosition();
                } else {
                    elapsedMicroseconds = (System.currentTimeMillis() - startTime) * 1000L;
                }

                // Sese göre anlık hangi karede (frame) olmamız gerektiğini tam olarak hesaplıyoruz
                int frameIndex = (int) ((elapsedMicroseconds / 1000000.0) * finalFps);
                if (frameIndex >= finalTotalFrames) {
                    break;
                }

                if (frameIndex != lastDrawnFrame) {
                    lastDrawnFrame = frameIndex;
                    String frameName = String.format("frame_%04d.jpg", frameIndex);
                    java.io.File frameFile = new java.io.File(framesDir, frameName);
                    if (frameFile.exists()) {
                        // Resmi diske erişerek doğrudan ve hızlıca oluşturuyoruz
                        Image img = Toolkit.getDefaultToolkit().createImage(frameFile.getAbsolutePath());
                        new ImageIcon(img); // Resmin RAM'e tam olarak yüklenmesini bekletir
                        currentImage[0] = img;
                        videoPanel.repaint(); // Ekrana çizdirir
                    }
                }

                try {
                    Thread.sleep(10); // İşlemciyi yormamak için kısa bir uyku
                } catch (Exception e) {}
            }

            if (finalClip != null) {
                finalClip.stop();
                finalClip.close();
            }

            SwingUtilities.invokeLater(() -> {
                storyPanel.setOpaque(false);
                storyPanel.setLayout(new BoxLayout(storyPanel, BoxLayout.Y_AXIS));
                storyPanel.setBorder(BorderFactory.createEmptyBorder(60, 100, 20, 100));
                onFinished.run();
            });
        }).start();
    }

    private void showRiddleSequence() {
        storyPanel.removeAll();
        answerPanel.setVisible(false);
        answerField.setText("");
        currentRiddle = riddles[new Random().nextInt(riddles.length)];

        models.LanguageManager lm = models.LanguageManager.getInstance();
        
        // Update static UI elements
        if (btnSubmit != null) btnSubmit.setText(lm.getText("answer_btn"));
        if (answerLabel != null) answerLabel.setText("<html><font color='#DAD2B4' size='4'>" + lm.getText("your_answer") + "</font></html>");
        
        String heroName = lm.getText("hero_knight_name");
        if (selectedHero != null) {
            switch(selectedHero) {
                case KNIGHT: heroName = lm.getText("hero_knight_name"); break;
                case MAGE: heroName = lm.getText("hero_mage_name"); break;
                case ROGUE: heroName = lm.getText("hero_rogue_name"); break;
                case PALADIN: heroName = lm.getText("hero_paladin_name"); break;
            }
        }

        String riddleQuestion = lm.getText("riddle_" + currentRiddle.id + "_q");

        String[] storyLines = {
            lm.getText("cinematic_hero") + " " + heroName + ",",
            lm.getText("cinematic_line1"),
            "",
            lm.getText("cinematic_line2"),
            lm.getText("cinematic_line3"),
            "",
            lm.getText("cinematic_line4"),
            lm.getText("cinematic_line5"),
            "",
            riddleQuestion
        };

        for (int i = 0; i < storyLines.length; i++) {
            String line = storyLines[i];
            JLabel label = new JLabel(line, SwingConstants.CENTER);
            
            if (i == storyLines.length - 1) { // The riddle itself
                label.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 28));
                label.setForeground(new Color(255, 215, 0)); // Gold
                label.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
            } else {
                label.setFont(new Font("Serif", Font.ITALIC, 22));
                label.setForeground(new Color(220, 210, 180));
            }
            label.setMaximumSize(new Dimension(900, 50));
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            storyPanel.add(label);
            storyPanel.add(Box.createVerticalStrut(8));
        }

        revalidate();
        repaint();

        Timer revealTimer = new Timer(300, new ActionListener() {
            int count = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (count < storyPanel.getComponentCount()) {
                    storyPanel.getComponent(count).setVisible(true);
                    count++;
                } else {
                    ((Timer)e.getSource()).stop();
                    answerPanel.setVisible(true);
                    answerField.requestFocus();
                    startTimer();
                }
            }
        });
        
        for(Component c : storyPanel.getComponents()) c.setVisible(false);
        revealTimer.start();
    }

    private void startTimer() {
        timeLeft = 30;
        timerLabel.setText("⏱ " + timeLeft);
        if (countdownTimer != null) countdownTimer.stop();
        
        countdownTimer = new Timer(1000, e -> {
            timeLeft--;
            timerLabel.setText("⏱ " + timeLeft);
            if (timeLeft <= 0) {
                countdownTimer.stop();
                handleTimeout();
            }
        });
        countdownTimer.start();
    }

    private void showCustomPopup(String title, String message, boolean isError) {
        JDialog dialog = new JDialog(window, title, true);
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(0, 0, 0, 0));
        
        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(25, 20, 35, 240));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(isError ? new Color(200, 50, 50) : new Color(50, 200, 100));
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Serif", Font.BOLD, 28));
        titleLbl.setForeground(isError ? new Color(255, 100, 100) : new Color(100, 255, 150));
        titleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel msgArea = new JLabel("<html><div style='text-align: center; width: 350px;'>" + message.replace("\n", "<br>") + "</div></html>", SwingConstants.CENTER);
        msgArea.setFont(new Font("Serif", Font.PLAIN, 20));
        msgArea.setForeground(Color.WHITE);
        msgArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        msgArea.setBorder(BorderFactory.createEmptyBorder(15, 0, 20, 0));

        JButton btnOk = new JButton(models.LanguageManager.getInstance().getText("continue_btn")) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) g2.setColor(new Color(80, 80, 80));
                else g2.setColor(new Color(50, 50, 50));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(150, 150, 150));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 10, 10);
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2, (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
            }
        };
        btnOk.setPreferredSize(new Dimension(160, 45));
        btnOk.setFont(new Font("Serif", Font.BOLD, 18));
        btnOk.setContentAreaFilled(false);
        btnOk.setBorderPainted(false);
        btnOk.setFocusPainted(false);
        btnOk.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnOk.addActionListener(e -> dialog.dispose());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(btnOk);

        panel.add(titleLbl);
        panel.add(msgArea);
        panel.add(btnPanel);

        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(window);
        dialog.setVisible(true);
    }

    private void handleTimeout() {
        GameManager gm = GameManager.getInstance();
        gm.setRiddleCorrect(false);
        models.LanguageManager lm = models.LanguageManager.getInstance();
        showCustomPopup(lm.getText("time_out_title"), lm.getText("time_out_desc"), true);
        startGame();
    }

    private void checkAnswer() {
        if (countdownTimer != null) countdownTimer.stop();
        String ans = answerField.getText().trim().toLowerCase();
        boolean correct = false;
        
        models.LanguageManager lm = models.LanguageManager.getInstance();
        String[] possibleAnswers = lm.getText("riddle_" + currentRiddle.id + "_a").split(",");
        
        for (String a : possibleAnswers) {
            if (ans.contains(a.trim().toLowerCase())) { correct = true; break; }
        }

        GameManager gm = GameManager.getInstance();
        gm.setRiddleCorrect(correct);
        
        if (correct) {
            String hName = selectedHero != null ? lm.getText("hero_" + selectedHero.name().toLowerCase() + "_name") : lm.getText("hero_knight_name");
            showCustomPopup(lm.getText("correct_ans_title"), lm.getText("correct_ans_desc") + " " + hName + "...\"", false);
        } else {
            showCustomPopup(lm.getText("wrong_ans_title"), lm.getText("wrong_ans_desc"), true);
        }
        startGame();
    }

    private void startGame() {
        GameManager.getInstance().startNewGame(selectedHero != null ? selectedHero : Player.HeroClass.KNIGHT);
        controllers.AudioPlayer.playBattleMusic();
        window.getBattlePanel().startBattle();
        window.showPanel("Battle");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (bgImage != null) {
            g2.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            
            float radius = Math.max(getWidth(), getHeight()) * 0.7f;
            java.awt.geom.Point2D center = new java.awt.geom.Point2D.Float(getWidth() * 0.75f, Math.max(1, getHeight() * 0.5f));
            float[] dist = {0.0f, 1.0f};
            Color[] colors = {new Color(80, 20, 20, 50), new Color(0, 0, 0, 255)};
            RadialGradientPaint rgp = new RadialGradientPaint(center, radius, dist, colors);
            g2.setPaint(rgp);
            g2.fillRect(0, 0, getWidth(), getHeight());
        } else {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        // Removed dragon image based on user request.
    }
}
