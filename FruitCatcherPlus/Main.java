import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/** Starts the game window. */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame window = new JFrame("Fruit Catcher - Java Swing Game");
                GamePanel game = new GamePanel();
                window.add(game);
                window.pack();
                window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                window.setResizable(false);
                window.setLocationRelativeTo(null);
                window.setVisible(true);
                game.requestFocusInWindow();
            }
        });
    }
}
