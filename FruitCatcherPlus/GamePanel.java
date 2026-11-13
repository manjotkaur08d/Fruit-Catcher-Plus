import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

/** Fruit Catcher Plus: multiple fruits, five levels, and a cartoon bomb burst. */
public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private static final int WIDTH = 500;
    private static final int HEIGHT = 620;
    private static final int MAX_ITEMS = 8;
    private final Timer timer = new Timer(25, this);
    private final Basket basket = new Basket(WIDTH);
    private final Fruit[] fruits = new Fruit[MAX_ITEMS];
    private final Random random = new Random();

    private int score = 0, lives = 5, best = 0, level = 1;
    private int bannerTicks = 0, blastTicks = 0, blastX = 0, blastY = 0;
    private boolean started = false, playing = false, paused = false;
    private boolean left = false, right = false;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        addKeyListener(this);
        for (int i = 0; i < fruits.length; i++) fruits[i] = new Fruit(WIDTH);
        timer.start();
    }

    private int activeCount() {
        // More falling items with each level, up to 7 simultaneously.
        return Math.min(7, 3 + level - 1);
    }

    private void resetFruit(Fruit fruit) {
        fruit.reset(WIDTH, level);
        fruit.y = -fruit.size - random.nextInt(350);
    }

    private void newGame() {
        score = 0;
        lives = 5;
        level = 1;
        bannerTicks = 0;
        blastTicks = 0;
        basket.x = (WIDTH - basket.width) / 2;
        for (int i = 0; i < fruits.length; i++) {
            resetFruit(fruits[i]);
            fruits[i].y -= i * 100; // Stagger falling items rather than one giant clump.
        }
        started = true;
        playing = true;
        paused = false;
        left = right = false;
        requestFocusInWindow();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (blastTicks > 0) blastTicks--; // Let the burst finish even after game over.
        if (playing && !paused) {
            if (left) basket.move(-8, WIDTH);
            if (right) basket.move(8, WIDTH);
            if (bannerTicks > 0) bannerTicks--;
            for (int i = 0; i < activeCount(); i++) {
                Fruit f = fruits[i];
                f.fall(3 + level); // Simple speed increase by level.
                if (f.bounds().intersects(basket.bounds())) {
                    if (f.type == 3) {
                        lives--;
                        blastX = f.x + f.size / 2;
                        blastY = basket.y + 5;
                        blastTicks = 22;
                    } else {
                        score += (f.type == 4) ? 30 : 10;
                        if (score > best) best = score;
                        int next = Math.min(5, score / 100 + 1);
                        if (next > level) {
                            level = next;
                            bannerTicks = 55;
                        }
                    }
                    resetFruit(f);
                } else if (f.y > HEIGHT) {
                    // Missing a fruit costs one life; missing a bomb is safe.
                    if (f.type != 3) lives--;
                    resetFruit(f);
                }
                if (lives <= 0) { lives = 0; playing = false; break; }
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(227, 247, 255));
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(new Color(194, 233, 165));
        g.fillRect(0, 565, WIDTH, 55);
        g.setColor(new Color(135, 200, 118));
        g.fillRect(0, 584, WIDTH, 36);

        g.setColor(new Color(31, 80, 72));
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("FRUIT CATCHER PLUS", 18, 36);
        g.setFont(new Font("SansSerif", Font.BOLD, 16));
        g.drawString("Score: " + score, 18, 67);
        g.drawString("Lives: " + lives, 148, 67);
        g.drawString("Level: " + level, 266, 67);
        g.drawString("Best: " + best, 374, 67);
        g.setColor(new Color(180, 214, 201));
        g.drawLine(15, 84, WIDTH - 15, 84);

        if (started) {
            for (int i = 0; i < activeCount(); i++) fruits[i].draw(g);
        }
        basket.draw(g);
        if (blastTicks > 0) drawBlast(g);

        if (!started) {
            overlay(g, "FRUIT CATCHER PLUS", "Press ENTER to start", "More fruits + bomb blast + 5 levels");
        } else if (!playing) {
            overlay(g, "GAME OVER", "Final Score: " + score, "Press R to restart");
        } else if (paused) {
            overlay(g, "PAUSED", "Press P to continue", "");
        } else {
            g.setColor(new Color(54, 99, 88));
            g.setFont(new Font("SansSerif", Font.PLAIN, 13));
            g.drawString("Fruit +10  |  Gold +30  |  Bomb = -1 life  |  P Pause", 12, 608);
            if (bannerTicks > 0) {
                g.setColor(new Color(17, 67, 64, 215));
                g.fillRoundRect(120, 242, 260, 84, 22, 22);
                g.setColor(Color.WHITE);
                centered(g, "LEVEL " + level + "!", 295, new Font("SansSerif", Font.BOLD, 32));
            }
        }
    }

    // Draw a simple, harmless cartoon explosion using circles and spark lines.
    private void drawBlast(Graphics2D g) {
        int progress = 22 - blastTicks;
        int radius = 14 + progress * 3;
        g.setColor(new Color(255, 190, 30, 140));
        g.fillOval(blastX - radius, blastY - radius, radius * 2, radius * 2);
        g.setColor(new Color(255, 95, 45));
        g.fillOval(blastX - radius / 2, blastY - radius / 2, radius, radius);
        g.setStroke(new BasicStroke(4));
        for (int i = 0; i < 10; i++) {
            double angle = i * Math.PI * 2 / 10;
            int x1 = blastX + (int) (Math.cos(angle) * (radius + 5));
            int y1 = blastY + (int) (Math.sin(angle) * (radius + 5));
            int x2 = blastX + (int) (Math.cos(angle) * (radius + 18));
            int y2 = blastY + (int) (Math.sin(angle) * (radius + 18));
            g.setColor(i % 2 == 0 ? Color.ORANGE : Color.YELLOW);
            g.drawLine(x1, y1, x2, y2);
        }
        g.setStroke(new BasicStroke(1));
    }

    private void overlay(Graphics2D g, String title, String line1, String line2) {
        g.setColor(new Color(15, 45, 45, 215));
        g.fillRoundRect(46, 190, 408, 215, 22, 22);
        g.setColor(Color.WHITE);
        centered(g, title, 263, new Font("SansSerif", Font.BOLD, 27));
        centered(g, line1, 316, new Font("SansSerif", Font.BOLD, 18));
        centered(g, line2, 353, new Font("SansSerif", Font.PLAIN, 15));
    }

    private void centered(Graphics2D g, String value, int y, Font font) {
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();
        g.drawString(value, (WIDTH - metrics.stringWidth(value)) / 2, y);
    }

    @Override public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_ENTER && !started) newGame();
        if (key == KeyEvent.VK_R && started && !playing) newGame();
        if (key == KeyEvent.VK_P && playing) paused = !paused;
        if (key == KeyEvent.VK_LEFT) left = true;
        if (key == KeyEvent.VK_RIGHT) right = true;
    }
    @Override public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) left = false;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) right = false;
    }
    @Override public void keyTyped(KeyEvent e) { }
}
