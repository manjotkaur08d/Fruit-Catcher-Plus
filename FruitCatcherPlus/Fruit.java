import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;

/** One falling fruit: apple, orange, or plum. */
public class Fruit {
    public int x;
    public int y;
    public final int size = 35;
    public int type;
    private final Random random = new Random();

    public Fruit(int screenWidth) {
        reset(screenWidth);
    }

    public void reset(int screenWidth) {
        x = 20 + random.nextInt(screenWidth - size - 40);
        y = -size;
        type = random.nextInt(3);
    }

    // Bombs begin in Level 2, rare golden bonus fruits in Level 3.
    public void reset(int screenWidth, int level) {
        reset(screenWidth);
        int roll = random.nextInt(100);
        if (level >= 2 && roll < 18) type = 3;
        else if (level >= 3 && roll < 30) type = 4;
    }

    public void fall(int speed) {
        y += speed;
    }

    public Rectangle bounds() {
        return new Rectangle(x + 3, y + 3, size - 6, size - 6);
    }

    public void draw(Graphics2D g) {
        Color fruitColor;
        if (type == 0) fruitColor = new Color(239, 68, 68);       // apple
        else if (type == 1) fruitColor = new Color(255, 164, 47); // orange
        else if (type == 2) fruitColor = new Color(153, 98, 213);
        else if (type == 3) fruitColor = new Color(42, 48, 60); // bomb
        else fruitColor = new Color(255, 209, 45); // golden fruit                 // plum

        g.setColor(fruitColor);
        g.fillOval(x, y, size, size);
        g.setColor(new Color(255, 255, 255, 90));
        g.fillOval(x + 6, y + 5, 10, 9);
        if (type == 3) {
            g.setColor(new Color(245, 88, 70));
            g.fillOval(x + 15, y - 7, 7, 10);
            g.setColor(Color.WHITE);
            g.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 22));
            g.drawString("!", x + 14, y + 27);
            return;
        }
        g.setColor(new Color(92, 60, 34));
        g.fillRoundRect(x + 16, y - 5, 4, 10, 3, 3);
        g.setColor(new Color(56, 148, 76));
        g.fillOval(x + 18, y - 8, 12, 7);
    }
}
