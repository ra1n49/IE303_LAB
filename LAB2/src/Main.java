import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class Main extends JFrame {
    public Main() {
        setTitle("Flappy Bird");
        setSize(360, 640);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        GamePanel panel = new GamePanel();
        setContentPane(panel);

        setVisible(true);
    }
    public static void main(String[] args) {
        new Main();
    }
}

class GamePanel extends JPanel implements ActionListener, KeyListener {
    private Image backgroundImage;
    private Image birdImage;
    private Image pipeTopImage;
    private Image pipeBottomImage;

    private int birdX = 100;
    private int birdY = 250;
    private int birdWidth = 40;
    private int birdHeight = 40;
    private int velocity = 0;
    private final int gravity = 1;
    private final int jump = -12;

    private Timer timer;

    private class Pipe {
        int x;
        int height;
        final int width = 60;
        final int gap = 150;
        boolean passed = false; // Dùng để tính điểm khi chim bay qua

        public Pipe(int x, int height) {
            this.x = x;
            this.height = height;
        }
    }

    private ArrayList<Pipe> pipes = new ArrayList<>();
    private int pipeSpeed = 4;
    private int frameCount = 0;

    private Random rand = new Random();

    private int score = 0;
    private boolean gameOver = false;

    public GamePanel() {
        backgroundImage = new ImageIcon("flappybirdbg.png").getImage();
        birdImage = new ImageIcon("flappybird.png").getImage();
        pipeTopImage = new ImageIcon("toppipe.png").getImage();
        pipeBottomImage = new ImageIcon("bottompipe.png").getImage();

        timer = new Timer(30, this);
        timer.start();

        setFocusable(true);
        addKeyListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        g.drawImage(birdImage, birdX, birdY, birdWidth, birdHeight, this);

        for (Pipe pipe : pipes) {
            g.drawImage(pipeTopImage, pipe.x, 0, pipe.width, pipe.height, this);
            int bottomY = pipe.height + pipe.gap;
            int bottomHeight = getHeight() - bottomY;
            g.drawImage(pipeBottomImage, pipe.x, bottomY, pipe.width, bottomHeight, this);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Score: " + score, 20, 30);

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("GAME OVER", 80, 300);
            g.setFont(new Font("Arial", Font.PLAIN, 18));
            g.drawString("Press R to Restart", 100, 340);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) return;

        velocity += gravity;
        birdY += velocity;

        frameCount++;
        if (frameCount % 70 == 0) {
            int height = rand.nextInt(200) + 100;
            pipes.add(new Pipe(getWidth(), height));
        }

        for (Pipe pipe : pipes) {
            pipe.x -= pipeSpeed;

            if (!pipe.passed && pipe.x + pipe.width < birdX) {
                score++;
                pipe.passed = true;
            }

            Rectangle birdRect = new Rectangle(birdX, birdY, birdWidth, birdHeight);
            Rectangle topRect = new Rectangle(pipe.x, 0, pipe.width, pipe.height);
            Rectangle bottomRect = new Rectangle(pipe.x, pipe.height + pipe.gap, pipe.width, getHeight() - (pipe.height + pipe.gap));
            if (birdRect.intersects(topRect) || birdRect.intersects(bottomRect)) {
                gameOver = true;
                timer.stop();
            }
        }

        pipes.removeIf(pipe -> pipe.x + pipe.width < 0);

        if (birdY + birdHeight >= getHeight()) {
            gameOver = true;
            timer.stop();
        }

        if (birdY < 0) {
            birdY = 0;
            velocity = 0;
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER) {
                velocity = jump;
            }
        } else {
            if (e.getKeyCode() == KeyEvent.VK_R) {
                restartGame();
            }
        }
    }

    public void restartGame() {
        birdY = 250;
        velocity = 0;
        pipes.clear();
        frameCount = 0;
        score = 0;
        gameOver = false;
        timer.start();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}
