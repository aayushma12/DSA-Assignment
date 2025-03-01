import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Tetris extends JPanel implements KeyListener {
    private static final long serialVersionUID = 1L;

    private final Point[][][] Tetraminos = {
        { 
            { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) }, // I
            { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) },
            { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
            { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) }
        },
        { 
            { new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) }, // T
            { new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
            { new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) },
            { new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2) }
        },
        { 
            { new Point(0, 1), new Point(1, 1), new Point(1, 0), new Point(2, 0) }, // L
            { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2) },
            { new Point(0, 1), new Point(1, 1), new Point(1, 2), new Point(2, 2) },
            { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 2) }
        },
        { 
            { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 0) }, // J
            { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 2) },
            { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 2) },
            { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2) }
        },
        { 
            { new Point(0, 1), new Point(1, 1), new Point(1, 0), new Point(1, 2) }, // S
            { new Point(0, 1), new Point(1, 1), new Point(1, 2), new Point(2, 2) }
        },
        { 
            { new Point(0, 1), new Point(1, 1), new Point(1, 0), new Point(1, 2) }, // Z
            { new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) }
        },
        { 
            { new Point(0, 1), new Point(1, 1), new Point(1, 0), new Point(1, 2) }, // O
            { new Point(0, 1), new Point(1, 1), new Point(1, 0), new Point(1, 2) }
        }
    };

    private final Color[] tetraminoColors = { Color.cyan, Color.blue, Color.orange, Color.yellow, Color.green, Color.pink, Color.red };
    private Point pieceOrigin;
    private int currentPiece, rotation;
    private Queue<Integer> nextPieces = new LinkedList<>();
    private long score, gameTime;
    private Color[][] well;
    private boolean gameOver = false;
    private javax.swing.Timer gameTimer, dropTimer;
    private JButton leftButton, rightButton;

    private void init() {
        well = new Color[12][24];
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 23; j++) {
                well[i][j] = Color.BLACK; // Set all cells to black initially
            }
        }
        // Set boundaries to Color.GRAY
        for (int i = 0; i < 12; i++) {
            well[i][23] = Color.GRAY; // Bottom boundary
        }
        for (int j = 0; j < 24; j++) {
            well[0][j] = Color.GRAY; // Left boundary
            well[11][j] = Color.GRAY; // Right boundary
        }

        newPiece();
        startTimers();
    }

    private void startTimers() {
        gameTime = 0;
        gameTimer = new javax.swing.Timer(1000, e -> {
            if (!gameOver) gameTime++;
        });
        gameTimer.start();

        dropTimer = new javax.swing.Timer(500, e -> {
            if (!gameOver) speedDrop();
        });
        dropTimer.start();
    }

    private void newPiece() {
        pieceOrigin = new Point(5, 2);
        rotation = 0;
        if (nextPieces.isEmpty()) {
            java.util.List<Integer> pieces = java.util.Arrays.asList(0, 1, 2, 3, 4, 5, 6);
            java.util.Collections.shuffle(pieces);
            nextPieces.addAll(pieces);
        }
        currentPiece = nextPieces.poll();
        if (collidesAt(pieceOrigin.x, pieceOrigin.y, rotation)) {
            gameOver = true;
            gameTimer.stop();
            dropTimer.stop();
        }
        repaint();
    }

    private boolean collidesAt(int x, int y, int rotation) {
        for (Point p : Tetraminos[currentPiece][rotation]) {
            if (well[p.x + x][p.y + y] != Color.BLACK) return true;
        }
        return false;
    }

    private void move(int i) {
        if (!collidesAt(pieceOrigin.x + i, pieceOrigin.y, rotation)) pieceOrigin.x += i;
        repaint();
    }

    private void rotate(int i) {
        int newRotation = (rotation + i) % 4;
        if (newRotation < 0) newRotation = 3;
        
        // Check for boundary collision after rotating
        if (!collidesAt(pieceOrigin.x, pieceOrigin.y, newRotation)) {
            rotation = newRotation;
        } else {
            // Try to adjust position if rotation causes collision
            if (!collidesAt(pieceOrigin.x - 1, pieceOrigin.y, newRotation)) {
                pieceOrigin.x -= 1;  // Move left if collision detected
                rotation = newRotation;
            } else if (!collidesAt(pieceOrigin.x + 1, pieceOrigin.y, newRotation)) {
                pieceOrigin.x += 1;  // Move right if collision detected
                rotation = newRotation;
            }
        }
        repaint();
    }

    private void dropDown() {
        while (!collidesAt(pieceOrigin.x, pieceOrigin.y + 1, rotation)) pieceOrigin.y += 1;
        fixToWell();
        repaint();
    }

    private void speedDrop() {
        if (!collidesAt(pieceOrigin.x, pieceOrigin.y + 1, rotation)) {
            pieceOrigin.y += 1;
        } else {
            fixToWell();
        }
        repaint();
    }

    private void fixToWell() {
        for (Point p : Tetraminos[currentPiece][rotation]) {
            well[pieceOrigin.x + p.x][pieceOrigin.y + p.y] = tetraminoColors[currentPiece];
        }
        clearRows();
        newPiece();
    }

    private void clearRows() {
        int numClears = 0;
        for (int j = 21; j > 0; j--) {
            boolean full = true;
            for (int i = 1; i < 11; i++) {
                if (well[i][j] == Color.BLACK) {
                    full = false;
                    break;
                }
            }
            if (full) {
                System.out.println("Row " + j + " is full, clearing it!");
                for (int row = j; row > 0; row--) {
                    for (int col = 1; col < 11; col++) {
                        well[col][row] = well[col][row - 1];
                    }
                }
                numClears++;
            }
        }
        System.out.println("Cleared " + numClears + " rows.");
        score += numClears * 100;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.fillRect(0, 0, 26 * 12, 26 * 23);
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 23; j++) {
                g.setColor(well[i][j]);
                g.fillRect(26 * i, 26 * j, 25, 25);
            }
        }
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 5, 25);
        g.drawString("Time: " + gameTime + "s", 5, 45);
        if (gameOver) g.drawString("GAME OVER!", 100, 200);
        else drawPiece(g);
    }

    private void drawPiece(Graphics g) {
        g.setColor(tetraminoColors[currentPiece]);
        for (Point p : Tetraminos[currentPiece][rotation]) {
            g.fillRect((p.x + pieceOrigin.x) * 26, (p.y + pieceOrigin.y) * 26, 25, 25);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver) return;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                move(-1);
                break;
            case KeyEvent.VK_RIGHT:
                move(1);
                break;
            case KeyEvent.VK_DOWN:
                speedDrop();
                break;
            case KeyEvent.VK_UP:
                rotate(1);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame f = new JFrame("Tetris");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(12 * 26 + 10, 26 * 23 + 60);
        f.setLayout(new BorderLayout());

        final Tetris game = new Tetris();
        game.init();
        f.add(game, BorderLayout.CENTER);

        JPanel controls = new JPanel();
        game.leftButton = new JButton("Left");
        game.leftButton.addActionListener(e -> game.move(-1));
        controls.add(game.leftButton);

        game.rightButton = new JButton("Right");
        game.rightButton.addActionListener(e -> game.move(1));
        controls.add(game.rightButton);

        f.add(controls, BorderLayout.SOUTH);

        // Add the KeyListener to the game panel
        game.addKeyListener(game);
        game.setFocusable(true);  // Allow the panel to receive keyboard focus
        f.setVisible(true);
    }
}
