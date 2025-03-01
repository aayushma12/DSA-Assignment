import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Queue;
import java.util.Stack;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List; // Explicitly use java.util.List
import javax.swing.Timer; // Explicitly use javax.swing.Timer

public class TetrisGame extends JPanel {
    // Constants
    final int BOARD_WIDTH = 10;
    final int BOARD_HEIGHT = 20;
    final int BLOCK_SIZE = 30;

    // Block shapes (2D arrays)
    final int[][][] BLOCK_SHAPES = {
            {{1, 1, 1, 1}}, // I shape
            {{1, 1}, {1, 1}}, // O shape
            {{1, 1, 0}, {0, 1, 1}}, // S shape
            {{0, 1, 1}, {1, 1, 0}}, // Z shape
            {{1, 1, 1}, {0, 1, 0}}, // T shape
            {{1, 1, 0}, {1, 0, 0}}, // L shape
            {{0, 1, 1}, {0, 0, 1}}  // J shape
    };

    final Color[] BLOCK_COLORS = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.PINK, Color.ORANGE, Color.CYAN};

    private Queue<Block> blockQueue = new LinkedList<>();
    private Stack<Block> gameBoard = new Stack<>();
    private Block currentBlock;
    private boolean gameOver = false;
    private int score = 0;
    private int time = 0; // Time in seconds
    private Timer timer;

    public TetrisGame() {
        setPreferredSize(new Dimension(BOARD_WIDTH * BLOCK_SIZE + 2, BOARD_HEIGHT * BLOCK_SIZE + 2)); // Adjusted size for border
        setBackground(Color.BLACK);

        // Initialize game
        enqueueBlock();
        currentBlock = blockQueue.poll();

        // Game update timer
        timer = new Timer(500, e -> gameLoop());
        timer.start();

        // Time update timer
        Timer timeUpdater = new Timer(1000, e -> {
            if (!gameOver) {
                time++; // Increment time every second
                repaint(); // Repaint to show updated time
            }
        });
        timeUpdater.start();

        // Key listener for moving and rotating blocks
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (gameOver) return;

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        moveBlockLeft();
                        break;
                    case KeyEvent.VK_RIGHT:
                        moveBlockRight();
                        break;
                    case KeyEvent.VK_UP:
                        rotateBlock();
                        break;
                }
                repaint();
            }
        });
        setFocusable(true);
    }

    // Enqueue a new random block into the blockQueue
    private void enqueueBlock() {
        int shapeIndex = (int) (Math.random() * BLOCK_SHAPES.length);
        Block newBlock = new Block(BLOCK_SHAPES[shapeIndex], BLOCK_COLORS[shapeIndex]);
        blockQueue.add(newBlock);
    }

    // Move block left
    private void moveBlockLeft() {
        if (!gameOver && canMoveToPosition(currentBlock.x - 1, currentBlock.y, currentBlock.shape)) {
            currentBlock.x--;
        }
    }

    // Move block right
    private void moveBlockRight() {
        if (!gameOver && canMoveToPosition(currentBlock.x + 1, currentBlock.y, currentBlock.shape)) {
            currentBlock.x++;
        }
    }

    // Rotate the block 90 degrees clockwise
    private void rotateBlock() {
        if (gameOver) return;
        currentBlock.rotate();
        if (!canMoveToPosition(currentBlock.x, currentBlock.y, currentBlock.shape)) {
            currentBlock.rotate(); // Undo rotation if it collides
        }
    }

    // Check if the block can move to a given position (x, y)
    private boolean canMoveToPosition(int x, int y, int[][] shape) {
        for (int dy = 0; dy < shape.length; dy++) {
            for (int dx = 0; dx < shape[dy].length; dx++) {
                if (shape[dy][dx] == 1) {
                    int newX = x + dx;
                    int newY = y + dy;
                    if (newX < 0 || newX >= BOARD_WIDTH || newY >= BOARD_HEIGHT || newY < 0 || getBlockAt(newX, newY) != null) {
                        return false; // Collision with boundary or existing blocks
                    }
                }
            }
        }
        return true;
    }

    // Return the block at the specified (x, y) position
    private Block getBlockAt(int x, int y) {
        for (Block block : gameBoard) {
            for (int dy = 0; dy < block.shape.length; dy++) {
                for (int dx = 0; dx < block.shape[dy].length; dx++) {
                    if (block.shape[dy][dx] == 1 && block.x + dx == x && block.y + dy == y) {
                        return block;
                    }
                }
            }
        }
        return null;
    }

    // Game loop: Check for collisions, place the block, and clear full rows
    private void gameLoop() {
        if (gameOver) return;

        // Check if the current block can move down
        if (!canMoveDown(currentBlock)) {
            placeBlock(currentBlock);
            clearFullRows();
            enqueueBlock();
            currentBlock = blockQueue.poll();
            
            // Check if the new block can be placed at the top
            if (!canMoveToPosition(currentBlock.x, currentBlock.y, currentBlock.shape)) {
                gameOver = true; // Game over if the block can't be placed at the top
            }
        } else {
            currentBlock.y++; // Move the block down
        }

        repaint();
    }

    // Check if the block can move down
    private boolean canMoveDown(Block block) {
        return canMoveToPosition(block.x, block.y + 1, block.shape);
    }

    // Place the block on the game board
    private void placeBlock(Block block) {
        gameBoard.push(block);
    }

    // Clear full rows
    private void clearFullRows() {
        List<Block> toRemove = new ArrayList<>();
        for (int y = 0; y < BOARD_HEIGHT; y++) {
            boolean fullRow = true;
            for (int x = 0; x < BOARD_WIDTH; x++) {
                if (getBlockAt(x, y) == null) {
                    fullRow = false;
                    break;
                }
            }
            if (fullRow) {
                toRemove.addAll(removeRow(y));
                score += 100; // Increase score for clearing a row
            }
        }
        gameBoard.removeAll(toRemove); // Remove full rows
    }

    // Remove a row by shifting down blocks
    private List<Block> removeRow(int row) {
        List<Block> removedBlocks = new ArrayList<>();
        for (Block block : gameBoard) {
            for (int dy = 0; dy < block.shape.length; dy++) {
                for (int dx = 0; dx < block.shape[dy].length; dx++) {
                    if (block.shape[dy][dx] == 1 && block.y + dy == row) {
                        removedBlocks.add(block);
                    }
                }
            }
        }
        return removedBlocks;
    }

    // Block class to represent a Tetris block with its shape, color, and position
    class Block {
        int[][] shape;
        Color color;
        int x, y;

        Block(int[][] shape, Color color) {
            this.shape = shape;
            this.color = color;
            this.x = BOARD_WIDTH / 2 - shape[0].length / 2; // Center the block
            this.y = 0; // Start at the top
        }

        void rotate() {
            int n = shape.length;
            int m = shape[0].length;
            int[][] rotated = new int[m][n];

            // Rotate 90 degrees clockwise
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    rotated[j][n - 1 - i] = shape[i][j];
                }
            }

            shape = rotated;
        }
    }

    // Paint the game board and blocks
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the game board blocks
        for (Block block : gameBoard) {
            for (int dy = 0; dy < block.shape.length; dy++) {
                for (int dx = 0; dx < block.shape[dy].length; dx++) {
                    if (block.shape[dy][dx] == 1) {
                        g.setColor(block.color);
                        g.fillRect((block.x + dx) * BLOCK_SIZE, (block.y + dy) * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                        g.setColor(Color.BLACK);
                        g.drawRect((block.x + dx) * BLOCK_SIZE, (block.y + dy) * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                    }
                }
            }
        }

        // Draw current block
        for (int dy = 0; dy < currentBlock.shape.length; dy++) {
            for (int dx = 0; dx < currentBlock.shape[dy].length; dx++) {
                if (currentBlock.shape[dy][dx] == 1) {
                    g.setColor(currentBlock.color);
                    g.fillRect((currentBlock.x + dx) * BLOCK_SIZE, (currentBlock.y + dy) * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                    g.setColor(Color.BLACK);
                    g.drawRect((currentBlock.x + dx) * BLOCK_SIZE, (currentBlock.y + dy) * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }

        // Draw game over message
        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over", BOARD_WIDTH * BLOCK_SIZE / 4, BOARD_HEIGHT * BLOCK_SIZE / 2);
        }

        // Draw score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("Score: " + score, 10, 20);

        // Draw time
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("Time: " + time + "s", BOARD_WIDTH * BLOCK_SIZE - 100, 20);

        // Draw the border around the game area
        g.setColor(Color.WHITE);
        g.drawRect(0, 0, BOARD_WIDTH * BLOCK_SIZE, BOARD_HEIGHT * BLOCK_SIZE); // Outer border
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tetris Game");
        TetrisGame gamePanel = new TetrisGame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gamePanel);
        frame.pack();
        frame.setVisible(true);
    }
}
