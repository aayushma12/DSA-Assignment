// Algorithm Explanation
// This Java program implements a simple Tetris game using Swing for graphical rendering and 
// AWT for event handling. The game board is a 10x20 grid, where different shaped blocks (Tetrominoes) 
// fall from the top. The game initializes with an empty board and generates random blocks using a 
// queue mechanism (nextBlocks). The current block moves downward automatically using a Timer with a 
// normal speed of 500 milliseconds per step. The player can control the block using keyboard inputs 
// (left, right, rotate, and fast drop). Collision detection prevents blocks from moving out of bounds
// or overlapping placed blocks. When a block reaches an obstacle, it is placed permanently on the board,
// and a new block is introduced. The program checks for full lines, and when a row is completely filled,
// it is removed, increasing the score. If a block cannot be placed at the initial position (top of the board), 
// the game ends, and a Game Over message displays with the elapsed time and score. The game also features a
// next block preview, a score display, and a timer. Additionally, a button panel at the bottom allows block
// movement using GUI buttons. The rendering is handled in the paintComponent method, drawing the grid, placed
// blocks, active block, and sidebar information. The game efficiently manages blocks
// using a stack for placed blocks and a queue for upcoming blocks, ensuring smooth gameplay

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class TetrisGame extends JPanel implements ActionListener {
    private final int BOARD_WIDTH = 10;
    private final int BOARD_HEIGHT = 20;
    private final int BLOCK_SIZE = 30;
    private Timer timer;
    private boolean gameOver = false;
    private int score = 0;
    private long startTime;
    private int[][] board = new int[BOARD_HEIGHT][BOARD_WIDTH];
    private int[][] currentBlock;
    private int currentX = 4, currentY = 0;
    private Stack<int[][]> placedBlocks = new Stack<>();
    private Queue<int[][]> nextBlocks = new LinkedList<>();
    private final int NORMAL_SPEED = 500;
    private final int FAST_SPEED = 100;

    private final int[][][] SHAPES = {
        {{1, 1, 1, 1}},
        {{1, 1}, {1, 1}},
        {{1, 1, 0}, {0, 1, 1}},
        {{0, 1, 1}, {1, 1, 0}},
        {{1, 1, 1}, {0, 1, 0}},
        {{1, 1, 1}, {1, 0, 0}},
        {{1, 1, 1}, {0, 0, 1}}
    };

    public TetrisGame() {
        setPreferredSize(new Dimension(BOARD_WIDTH * BLOCK_SIZE + 150, BOARD_HEIGHT * BLOCK_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        requestFocusInWindow();

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e.getKeyCode());
            }

            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    timer.setDelay(NORMAL_SPEED);
                }
            }
        });

        for (int i = 0; i < 3; i++) nextBlocks.add(getRandomBlock());
        currentBlock = nextBlocks.poll();
        nextBlocks.add(getRandomBlock());

        timer = new Timer(NORMAL_SPEED, this);
        startTime = System.currentTimeMillis();
        timer.start();
    }

    private int[][] getRandomBlock() {
        return SHAPES[(int) (Math.random() * SHAPES.length)];
    }

    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            currentY++;
            if (collision()) {
                currentY--;
                placeBlock();
                checkFullLines();
                currentBlock = nextBlocks.poll();
                nextBlocks.add(getRandomBlock());
                currentX = 4;
                currentY = 0;
                if (collision()) {
                    gameOver = true;
                    timer.stop();
                    showGameOverDialog();
                }
            }
            repaint();
        }
    }

    private void handleKeyPress(int keyCode) {
        if (keyCode == KeyEvent.VK_LEFT) moveLeft();
        if (keyCode == KeyEvent.VK_RIGHT) moveRight();
        if (keyCode == KeyEvent.VK_UP) rotateBlock();
        if (keyCode == KeyEvent.VK_DOWN) timer.setDelay(FAST_SPEED);
    }

    private void moveLeft() {
        currentX--;
        if (collision()) currentX++;
        repaint();
    }

    private void moveRight() {
        currentX++;
        if (collision()) currentX--;
        repaint();
    }

    private void rotateBlock() {
        int[][] rotated = new int[currentBlock[0].length][currentBlock.length];
        for (int row = 0; row < currentBlock.length; row++) {
            for (int col = 0; col < currentBlock[row].length; col++) {
                rotated[col][currentBlock.length - 1 - row] = currentBlock[row][col];
            }
        }
        int[][] temp = currentBlock;
        currentBlock = rotated;
        if (collision()) currentBlock = temp;
        repaint();
    }

    private boolean collision() {
        for (int row = 0; row < currentBlock.length; row++) {
            for (int col = 0; col < currentBlock[row].length; col++) {
                if (currentBlock[row][col] == 1) {
                    int x = currentX + col;
                    int y = currentY + row;
                    if (x < 0 || x >= BOARD_WIDTH || y >= BOARD_HEIGHT || board[y][x] == 1) return true;
                }
            }
        }
        return false;
    }

    private void placeBlock() {
        for (int row = 0; row < currentBlock.length; row++) {
            for (int col = 0; col < currentBlock[row].length; col++) {
                if (currentBlock[row][col] == 1) {
                    board[currentY + row][currentX + col] = 1;
                }
            }
        }
        placedBlocks.push(currentBlock);
    }

    private void checkFullLines() {
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            boolean fullLine = true;
            for (int col = 0; col < BOARD_WIDTH; col++) {
                if (board[row][col] == 0) {
                    fullLine = false;
                    break;
                }
            }
            if (fullLine) {
                score += 100;
                for (int r = row; r > 0; r--) {
                    System.arraycopy(board[r - 1], 0, board[r], 0, BOARD_WIDTH);
                }
                repaint();
            }
        }
    }

    private void showGameOverDialog() {
        long totalTime = (System.currentTimeMillis() - startTime) / 1000;
        JOptionPane.showMessageDialog(this, "Game Over!\nTime: " + totalTime + " sec\nScore: " + score, "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }
    

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw Grid
        g.setColor(Color.GRAY);
        for (int x = 0; x <= BOARD_WIDTH * BLOCK_SIZE; x += BLOCK_SIZE) {
            g.drawLine(x, 0, x, BOARD_HEIGHT * BLOCK_SIZE);
        }
        for (int y = 0; y <= BOARD_HEIGHT * BLOCK_SIZE; y += BLOCK_SIZE) {
            g.drawLine(0, y, BOARD_WIDTH * BLOCK_SIZE, y);
        }
        
        // Draw Placed Blocks
        g.setColor(Color.GREEN);
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                if (board[row][col] == 1) {
                    g.fillRect(col * BLOCK_SIZE, row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }
    
        // Draw Current Block
        g.setColor(Color.RED);
        for (int row = 0; row < currentBlock.length; row++) {
            for (int col = 0; col < currentBlock[row].length; col++) {
                if (currentBlock[row][col] == 1) {
                    g.fillRect((currentX + col) * BLOCK_SIZE, (currentY + row) * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }
    
        // Sidebar area start position
        int sidebarX = BOARD_WIDTH * BLOCK_SIZE + 10;
        
        // Display Score with spacing
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, sidebarX, 50);
        
        // Display Elapsed Time with spacing
        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
        g.drawString("Time: " + elapsedTime + " sec", sidebarX, 80);
        
        // Draw Next Block Preview slightly lower
        g.drawString("Next Block:", sidebarX, 120);
        int[][] nextBlock = nextBlocks.peek();
        if (nextBlock != null) {
            int nextBlockOffsetY = 130; // Lowering the position of next block preview
            for (int row = 0; row < nextBlock.length; row++) {
                for (int col = 0; col < nextBlock[row].length; col++) {
                    if (nextBlock[row][col] == 1) {
                        g.fillRect(sidebarX + col * BLOCK_SIZE, nextBlockOffsetY + row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                    }
                }
            }
        }
    }
    

    

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tetris Game");
        TetrisGame game = new TetrisGame();
        frame.add(game, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton leftButton = new JButton("Left");
        JButton rightButton = new JButton("Right");
        JButton rotateButton = new JButton("Rotate");

        leftButton.addActionListener(e -> game.moveLeft());
        rightButton.addActionListener(e -> game.moveRight());
        rotateButton.addActionListener(e -> game.rotateBlock());

        buttonPanel.add(leftButton);
        buttonPanel.add(rotateButton);
        buttonPanel.add(rightButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}