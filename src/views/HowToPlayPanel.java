package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Nasıl Oynanır (Eğitici Slayt) Ekranı.
 * Oyuncuya oyun kurallarını, kahramanları, kartları ve düşmanları tanıtır.
 * Klavye ok tuşları, A-D, Boşluk ve ESC tuşları ile kontrol edilebilir.
 */
public class HowToPlayPanel extends JPanel {
    private GameWindow window;
    private Image bgImage;
    private int currentSlide = 0;
    private List<Slide> slides;

    // Arayüz Elemanları
    private JLabel titleLabel;
    private JPanel contentPanel;
    private JTextPane descriptionLabel;
    private JLabel pageIndicator;
    private JButton btnPrev;
    private JButton btnNext;

    // Slayt Veri Yapısı
    private static class Slide {
        String title;
        String description;
        String[] imageNames;
        int imgWidth;
        int imgHeight;

        Slide(String title, String description, String[] imageNames, int imgWidth, int imgHeight) {
            this.title = title;
            this.description = description;
            this.imageNames = imageNames;
            this.imgWidth = imgWidth;
            this.imgHeight = imgHeight;
        }
    }

    public HowToPlayPanel(GameWindow window) {
        this.window = window;
        setLayout(new BorderLayout());
        setFocusable(true);

        bgImage = window.loadImage("bg_main_menu.png");

        setupSlides();
        initComponents();
        setupKeyBindings();
    }

    private void setupSlides() {
        slides = new ArrayList<>();

        models.LanguageManager lm = models.LanguageManager.getInstance();

        // Slide 1: Giriş
        slides.add(new Slide(
            lm.getText("htp_title_1"),
            lm.getText("htp_desc_1"),
            new String[]{"bg_loading.png"},
            600, 330
        ));

        // Slide 2: Kahramanlar
        slides.add(new Slide(
            lm.getText("htp_title_2"),
            lm.getText("htp_desc_2"),
            new String[]{"hero_knight.png.png", "hero_mage.png.jpg", "hero_rogue.png.png", "hero_paladin.png.png"},
            165, 205
        ));

        // Slide 3: Kart Mekanikleri
        slides.add(new Slide(
            lm.getText("htp_title_3"),
            lm.getText("htp_desc_3"),
            new String[]{"card_strike.png.jpg", "card_block.png.jpg", "card_heal.png.jpg"},
            185, 260
        ));

        // Slide 4: Kule Bilmecesi
        slides.add(new Slide(
            lm.getText("htp_title_4"),
            lm.getText("htp_desc_4"),
            new String[]{"custom_guard.png"},
            460, 290
        ));
    }

    private void initComponents() {
        // Üst panel (Başlık)
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setOpaque(false);
        northPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 10, 0));

        titleLabel = new JLabel(slides.isEmpty() ? "" : slides.get(0).title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 42));
        titleLabel.setForeground(new Color(255, 215, 0)); // Altın sarısı
        northPanel.add(titleLabel, BorderLayout.CENTER);
        add(northPanel, BorderLayout.NORTH);

        // Orta panel (Slayt Kartı - Glassmorphism kutusu)
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        JPanel slideCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Glassmorphism arka plan
                g2.setColor(new Color(15, 10, 25, 230));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                // Parlak kenarlık
                g2.setColor(new Color(180, 140, 50, 120));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 24, 24);
            }
        };
        slideCard.setLayout(new BorderLayout());
        slideCard.setPreferredSize(new Dimension(860, 620));
        slideCard.setOpaque(false);
        slideCard.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Slayt Görsel Alanı
        contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        slideCard.add(contentPanel, BorderLayout.CENTER);

        // Slayt Metin Alanı
        descriptionLabel = new JTextPane();
        descriptionLabel.setContentType("text/html");
        descriptionLabel.setEditable(false);
        descriptionLabel.setOpaque(false);
        descriptionLabel.setFocusable(false);
        descriptionLabel.setBorder(null);
        descriptionLabel.setMargin(new Insets(0, 0, 0, 0));
        descriptionLabel.setPreferredSize(new Dimension(800, 200));
        slideCard.add(descriptionLabel, BorderLayout.SOUTH);

        centerWrapper.add(slideCard);
        add(centerWrapper, BorderLayout.CENTER);

        // Alt Panel (Navigasyon)
        JPanel southPanel = new JPanel();
        southPanel.setOpaque(false);
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 40, 0));

        // Sayfa Göstergesi
        pageIndicator = new JLabel("● ○ ○ ○", SwingConstants.CENTER);
        pageIndicator.setFont(new Font("Serif", Font.PLAIN, 24));
        pageIndicator.setForeground(new Color(180, 140, 50));
        pageIndicator.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Butonlar Paneli
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        buttonsPanel.setOpaque(false);

        btnPrev = createNavButton(models.LanguageManager.getInstance().getText("prev_btn"));
        btnPrev.addActionListener(e -> prevSlide());

        JButton btnBackToMenu = createNavButton(models.LanguageManager.getInstance().getText("menu_btn_esc"));
        btnBackToMenu.addActionListener(e -> exitToMenu());

        btnNext = createNavButton(models.LanguageManager.getInstance().getText("next_btn"));
        btnNext.addActionListener(e -> nextSlide());

        buttonsPanel.add(btnPrev);
        buttonsPanel.add(btnBackToMenu);
        buttonsPanel.add(btnNext);

        // Klavye Kılavuzu Etiketi
        JLabel helperLabel = new JLabel("Klavye Kontrolleri: [Sol Ok / A] Önceki  |  [Sağ Ok / D / Boşluk] Sonraki  |  [ESC] Ana Menü", SwingConstants.CENTER);
        helperLabel.setFont(new Font("Serif", Font.ITALIC, 13));
        helperLabel.setForeground(new Color(150, 140, 170));
        helperLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        helperLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        southPanel.add(pageIndicator);
        southPanel.add(Box.createVerticalStrut(10));
        southPanel.add(buttonsPanel);
        southPanel.add(helperLabel);

        add(southPanel, BorderLayout.SOUTH);
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Serif", Font.BOLD, 16));
        btn.setForeground(new Color(230, 220, 200));
        btn.setBackground(new Color(35, 25, 50));
        btn.setOpaque(true);
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 140, 50), 1),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (btn.isEnabled()) {
                    btn.setBackground(new Color(75, 45, 110));
                    btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(255, 215, 0), 1),
                        BorderFactory.createEmptyBorder(8, 20, 8, 20)
                    ));
                }
            }
            public void mouseExited(MouseEvent e) {
                if (btn.isEnabled()) {
                    btn.setBackground(new Color(35, 25, 50));
                    btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(180, 140, 50), 1),
                        BorderFactory.createEmptyBorder(8, 20, 8, 20)
                    ));
                }
            }
        });
        return btn;
    }

    private void setupKeyBindings() {
        InputMap im = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        // Sağ ok / D / Space -> Sonraki
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "nextSlide");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "nextSlide");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "nextSlide");
        am.put("nextSlide", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextSlide();
            }
        });

        // Sol ok / A -> Önceki
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "prevSlide");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "prevSlide");
        am.put("prevSlide", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prevSlide();
            }
        });

        // ESC -> Ana Menü
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "exitToMenu");
        am.put("exitToMenu", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitToMenu();
            }
        });
    }

    private void updateSlideContent() {
        if (currentSlide < 0 || currentSlide >= slides.size()) return;

        Slide slide = slides.get(currentSlide);

        // Başlık
        titleLabel.setText(slide.title);

        // Açıklama Metni
        descriptionLabel.setText(slide.description);

        // Görseller
        contentPanel.removeAll();
        for (String imgName : slide.imageNames) {
            Image img = window.loadImage(imgName);
            if (img != null) {
                Image scaled = img.getScaledInstance(slide.imgWidth, slide.imgHeight, Image.SCALE_SMOOTH);
                JLabel imgLabel = new JLabel(new ImageIcon(scaled));
                imgLabel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(180, 140, 50, 80), 2),
                    BorderFactory.createEmptyBorder(2, 2, 2, 2)
                ));
                contentPanel.add(imgLabel);
            }
        }

        btnPrev.setText(models.LanguageManager.getInstance().getText("prev_btn"));
        Component[] comps = btnNext.getParent().getComponents();
        if (comps.length > 1 && comps[1] instanceof JButton) {
            ((JButton)comps[1]).setText(models.LanguageManager.getInstance().getText("menu_btn_esc"));
        }
        
        // Navigasyon Buton Aktiflikleri
        btnPrev.setEnabled(currentSlide > 0);
        if (currentSlide == slides.size() - 1) {
            btnNext.setText(models.LanguageManager.getInstance().getText("start_btn_htp"));
        } else {
            btnNext.setText(models.LanguageManager.getInstance().getText("next_btn"));
        }

        // Sayfa Göstergesi Güncelleme
        StringBuilder dots = new StringBuilder();
        for (int i = 0; i < slides.size(); i++) {
            if (i == currentSlide) {
                dots.append("● ");
            } else {
                dots.append("○ ");
            }
        }
        pageIndicator.setText(dots.toString().trim());

        revalidate();
        repaint();
    }

    private void nextSlide() {
        if (currentSlide < slides.size() - 1) {
            currentSlide++;
            updateSlideContent();
        } else {
            // Son slaytta "BAŞLA" butonuna basınca kahraman seçme ekranına gitsin
            window.showPanel("CharacterSelect");
        }
    }

    private void prevSlide() {
        if (currentSlide > 0) {
            currentSlide--;
            updateSlideContent();
        }
    }

    private void exitToMenu() {
        window.showPanel("MainMenu");
    }

    public void resetAndFocus() {
        setupSlides(); // Yeniden dilleri çek
        currentSlide = 0;
        updateSlideContent();
        requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if (bgImage != null) {
            g2.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            
            // Koyu bir filtre/radial degrade ekleyelim ki slayt içeriği daha rahat okunsun
            float radius = Math.max(getWidth(), getHeight()) * 0.8f;
            java.awt.geom.Point2D center = new java.awt.geom.Point2D.Float(getWidth() / 2f, getHeight() * 0.5f);
            float[] dist = {0.0f, 1.0f};
            Color[] colors = {new Color(15, 10, 30, 160), new Color(5, 2, 12, 240)};
            RadialGradientPaint rgp = new RadialGradientPaint(center, radius, dist, colors);
            g2.setPaint(rgp);
            g2.fillRect(0, 0, getWidth(), getHeight());
        } else {
            g2.setColor(new Color(10, 5, 20));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
