import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;

/** The player's basket. */
public class Basket {
    public int x;
    public final int y = 525;
    public final int width = 100;
    public final int height = 42;

    public Basket(int screenWidth) {
        x = (screenWidth - width) / 2;
    }

    public void move(int amount, int screenWidth) {
        x += amount;
        if (x < 15) x = 15;
        if (x + width > screenWidth - 15) x = screenWidth - 15 - width;
    }

    public Rectangle bounds() {
        return new Rectangle(x + 6, y, width - 12, height);
    }

    public void draw(Graphics2D g) {
        g.setColor(new Color(98, 55, 28));
        g.fillRoundRect(x + 8, y - 12, width - 16, 20, 10, 10);
        g.setColor(new Color(196, 123, 57));
        Polygon basketShape = new Polygon();
        basketShape.addPoint(x, y);
        basketShape.addPoint(x + width, y);
        basketShape.addPoint(x + width - 16, y + height);
        basketShape.addPoint(x + 16, y + height);
        g.fillPolygon(basketShape);
        g.setColor(new Color(125, 76, 37));
        for (int i = 1; i < 4; i++) {
            int lineY = y + i * 9;
            g.drawLine(x + 12, lineY, x + width - 12, lineY);
        }
        g.setColor(new Color(243, 180, 94));
        g.fillRoundRect(x - 3, y - 2, width + 6, 11, 6, 6);
    }
}
