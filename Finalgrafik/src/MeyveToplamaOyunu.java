import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MeyveToplamaOyunu extends JFrame implements ActionListener, KeyListener {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 650;
    private static final int SEPET_WIDTH = 150;
    private static final int MEYVE_WIDTH = 70;
    private static final int MEYVE_HEIGHT = 70;
    private static final int TIMER_DELAY = 10;

    private Timer timer;
    private int sepetX;
    private int skor;
    private List<Meyve> meyveler;
    private String[] meyveResimleri = {"out/muz.png", "out/armut.png", "out/cilek.png", "out/elma.png"};
    private JButton skorButton; // Skoru göstermek için bir buton

    public MeyveToplamaOyunu() {
        setTitle("Meyve Toplama Oyunu");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Oyun panelini oluştur ve arka plan resmini ayarla
        OyunPanel oyunPanel = new OyunPanel();
        setContentPane(oyunPanel);

        sepetX = WIDTH / 2 - SEPET_WIDTH / 2;
        skor = 0;
        meyveler = new ArrayList<>();

        // Skor butonunu oluştur
        skorButton = new JButton("Skor: " + skor);
        skorButton.setFocusable(false);
        skorButton.setEnabled(false); // Butonun tıklanamaması için
        skorButton.setBackground(Color.BLUE);

        // Butonun yerini ayarla
        skorButton.setBounds(10, 10, 120, 40);
        oyunPanel.add(skorButton);
        oyunPanel.setLayout(null);

        timer = new Timer(TIMER_DELAY, this);
        timer.start();

        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
    }

    public void paint(Graphics g) {
        super.paint(g);

        // Sepeti resim olarak çiz
        ImageIcon sepetIcon = new ImageIcon("out/sepet.png");
        g.drawImage(sepetIcon.getImage(), sepetX, HEIGHT - 50, SEPET_WIDTH, 50, null);

        // Meyveleri ve resimlerini çiz
        for (Meyve meyve : meyveler) {
            ImageIcon meyveIcon = new ImageIcon(meyve.getResimPath());
            g.drawImage(meyveIcon.getImage(), meyve.getX(), meyve.getY(), MEYVE_WIDTH, MEYVE_HEIGHT, null);
            g.setColor(Color.white);
            g.drawString(Integer.toString(meyve.getSayi()), meyve.getX() + MEYVE_WIDTH / 2 - 5, meyve.getY() + MEYVE_HEIGHT / 2 + 5);
        }

        // Skor butonunun metnini güncelle
        skorButton.setText("Skor: " + skor);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Yeni meyve ekleyerek oyunu güncelle
        ekleYeniMeyve();
        hareketEttir();
        carpismaKontrolu();
        repaint();
    }

    private void ekleYeniMeyve() {
        Random random = new Random();
        if (random.nextDouble() < 0.02) {
            int x = random.nextInt(WIDTH - MEYVE_WIDTH);
            int y = 0;
            Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            int sayi;

            // Rasgele bir meyve seç
            String resimPath = meyveResimleri[random.nextInt(meyveResimleri.length)];

            // Meyve türüne göre sayı değerini belirle
            switch (resimPath) {
                case "out/armut.png":
                    sayi = 1;
                    break;
                case "out/muz.png":
                    sayi = -1;
                    break;
                case "out/cilek.png":
                    sayi = -2;
                    break;
                case "out/elma.png":
                    sayi = 2;
                    break;
                default:
                    sayi = 0; // Tanımlı olmayan durumlar için sayı değeri sıfır olsun
                    break;
            }

            // Meyve nesnesini oluştururken rastgele bir hız belirle
            int hiz = random.nextInt(4) + 1;

            meyveler.add(new Meyve(x, y, color, sayi, resimPath, hiz));
        }
    }

    private void hareketEttir() {
        for (Meyve meyve : meyveler) {
            meyve.setY(meyve.getY() + meyve.getHiz());
        }
    }

    private void carpismaKontrolu() {
        for (int i = 0; i < meyveler.size(); i++) {
            Meyve meyve = meyveler.get(i);
            if (meyve.getY() > HEIGHT - 50 && meyve.getX() > sepetX && meyve.getX() < sepetX + SEPET_WIDTH) {
                skor += meyve.getSayi();
                meyveler.remove(i);
                i--;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT && sepetX > 0) {
            sepetX -= 10;
        } else if (key == KeyEvent.VK_RIGHT && sepetX < WIDTH - SEPET_WIDTH) {
            sepetX += 10;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MeyveToplamaOyunu oyun = new MeyveToplamaOyunu();
            oyun.setVisible(true);
        });
    }

    private class Meyve {
        private int x;
        private int y;
        private Color color;
        private int sayi;
        private String resimPath;
        private int hiz; // Meyvenin hızı

        public Meyve(int x, int y, Color color, int sayi, String resimPath, int hiz) {
            this.x = x;
            this.y = y;
            this.color = color;
            this.sayi = sayi;
            this.resimPath = resimPath;
            this.hiz = hiz;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public Color getColor() {
            return color;
        }

        public int getSayi() {
            return sayi;
        }

        public String getResimPath() {
            return resimPath;
        }

        public int getHiz() {
            return hiz;
        }
    }

    private class OyunPanel extends JPanel {
        private Image arkaPlanResmi;

        public OyunPanel() {
            // Arka plan resmini yükleyin (örneğin: "arkaplan.jpg")
            arkaPlanResmi = new ImageIcon("out/arkaplan.jpg").getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Arka plan resmini çiz
            g.drawImage(arkaPlanResmi, 0, 0, getWidth(), getHeight(), this);
        }
    }
}