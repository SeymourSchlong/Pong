
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 * bootleg pong
 * 
 * @since 10.26.2018
 * @author seymour
 */

public class Window extends javax.swing.JFrame implements KeyListener {

    // The window height and length, as well as the offset from the top of the window.
    public int winL = 600, winH = 400, yOffset = 25;
    
    // The state that the game is in...
    // -2 = DIFFICULTY SELECT
    // -1 = PAUSE GAME
    // 0 = START SCREEN
    // 1 = PLAY GAME
    // 2 = RESULTS SCREEN
    public int state, players, victor;
    public int difficulty;
    
    Ball ball;              // Creates the ball
    Paddle p1, p2;          // Creates the players/paddles
    Timer timer;            // Creates the timer
    RoboBall robo;          // Creates the robot timer
    Random random;          // Random number generation
    BufferedImage title;    // Title screen image
    
    BufferedImage bi;
    Graphics big;
    
    public Window() {
        initComponents();
        addKeyListener(this);
        setSize(winL, winH);
        
        try {
            File titleLoc = new File("./img/title.png");
            title = ImageIO.read(titleLoc);
        } catch(Exception e) {
            System.out.println(e);
        }
        
        bi = (BufferedImage)createImage(winL, winH);
        big = bi.createGraphics();
        
        ball = new Ball(0, 0);
        p1 = new Paddle(0, 0);
        p2 = new Paddle(0, 0);
        timer = new Timer();
        robo = new RoboBall();
        
        random = new Random();
        
        int tempY = random.nextInt(2), tempX = random.nextInt(2);
        ball.setDirection((tempX == 1), (tempY == 1));
        
        newGame();
        
        timer.start();
    }
    
    public void newGame() {
        // Initialize ball + information -- initial location, color, size, speed, and shape.
        ball.setPos(middle(winL, ball.w), middle(winH, ball.h));
        ball.setColor(Color.white);
        ball.setSize(16, 16);
        ball.setShape("rectangle");
        ball.setSpeed(ball.xSpeed1, ball.ySpeed1);
        //ball.randSpeed();

        p1.setPos(10, middle(winH, p1.h));
        p1.score = 0;
        
        p2.setPos(570, middle(winH, p2.h));
        p2.score = 0;
        
        state = 0;
        
        repaint();
    }
    
    public void resetPos() {
        ball.setPos(middle(winL, ball.w), middle(winH, ball.h));
        ball.setSpeed(ball.xSpeed1, ball.ySpeed1);
        //ball.randSpeed();
        ball.rally = ball.rallyLevel = 0;
        state = -1;
    }
    
    public int middle(int a, int b) {
        return (int) (a / 2) - (b / 2);
    }
    
    public void keyPressed(KeyEvent ke) {
        int key = ke.getKeyCode();
        
        // Start Screen
        if (state == 0) {
            if (key == KeyEvent.VK_1) {
            } else if (key == KeyEvent.VK_2) {
            }
            switch(key) {
                case KeyEvent.VK_1:
                    state = -2;
                    players = 1;
                    break;
                case KeyEvent.VK_2:
                    state = -1;
                    players = 2;
                    break;
            }
        }
        
        // Difficulty Selection
        if (state == -2) {
            switch(key) {
                case KeyEvent.VK_Q:
                    difficulty = 15;
                    state = -1;
                    robo.start();
                    break;
                case KeyEvent.VK_W:
                    difficulty = 14;
                    state = -1;
                    robo.start();
                    break;
                case KeyEvent.VK_E:
                    difficulty = 13;
                    state = -1;
                    robo.start();
                    break;
                case KeyEvent.VK_R:
                    difficulty = 12;
                    state = -1;
                    robo.start();
                    break;
            }
        }
        
        if (Math.abs(state) == 1) {
            switch(key) {
                // Player 1
                case KeyEvent.VK_W:
                    p1.dir = 1;
                    break;
                case KeyEvent.VK_S:
                    p1.dir = 2;
                    break;
                // Player 2
                case KeyEvent.VK_UP:
                    if (players != 1) p2.dir = 1;
                    break;
                case KeyEvent.VK_DOWN:
                    if (players != 1) p2.dir = 2;
                    break;
                case KeyEvent.VK_ESCAPE:
                    if (Math.abs(state) == 1) state *= -1;
                    break;
            }
            if (key != KeyEvent.VK_ESCAPE) state = 1;
        }
        
        if (state == 2) {
            if (key == KeyEvent.VK_R) newGame();
        }
        
        repaint();
    }
    
    public void keyTyped(KeyEvent ke) { }
    
    public void keyReleased(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_W || ke.getKeyCode() == KeyEvent.VK_S) p1.dir = 0;
        
        if (ke.getKeyCode() == KeyEvent.VK_UP || ke.getKeyCode() == KeyEvent.VK_DOWN) p2.dir = 0;
    }
    
    public boolean collision(Ball b, Paddle p) {
        int middleX = b.x + b.w/2;
        int middleY = b.y + b.h/2;
        
        if (middleX > p.x) {
            if (middleX < p.x+p.w) {
                if (middleY > p.y) {
                    if (middleY < p.y+p.h) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    public void paint(Graphics g) {
        // Clear the window
        big.clearRect(0, 0, winL, winH);
        
        Font font = new Font("Comic Sans MS", Font.PLAIN, 18);
        Font bigFont = new Font("Comic Sans MS", Font.BOLD, 50);
        big.setFont(font);
        
        // Draw the black background
        big.setColor(Color.black);
        big.fillRect(0, 0, winL, winH);
        
        big.setColor(Color.white);
        
        // Start Screen
        if (state == 0) {
            big.drawImage(title, 178, 96, this);
            big.drawString("Press 1 for one player.", 210, 280);
            big.drawString("Press 2 for two players.", 205, 300);
        }
        
        // Difficulty Select
        if (state == -2) {
            big.drawImage(title, 178, 96, this);
            big.drawString("Press Q for easy.", 210, 280);
            big.drawString("Press W for medium.", 210, 300);
            big.drawString("Press E for hard.", 210, 320);
            big.drawString("Press R for impossible.", 210, 340);
        }
        
        // Paused / Playing
        if (Math.abs(state) == 1) {
            // Middle line
            int lineW = 10, lineH = 40;
            big.setColor(Color.lightGray);
            for (int i = 0; i < winH/50; i++) {
                int yPos = 60*i + 10;
                big.fillRect(middle(winL, lineW), yPos, lineW, lineH);
            }
            big.setColor(Color.white);
            // Display Players' scores
            big.drawString(String.valueOf(p1.score), 270, 50);
            big.drawString(String.valueOf(p2.score), 320, 50);

            // Draw ball
            ball.draw(big);

            // Draw paddles
            p1.draw(big);
            p2.draw(big);
        }
        
        // Results Screen
        if (state == 2) {
            big.setColor(Color.white);
            big.setFont(bigFont);
            big.drawString("Player " + victor + " wins!", 120, 200);
            big.setFont(font);
            big.drawString("Press R to restart.", 220, 250);
        }
        
        g.drawImage(bi, 0, 0, this);
    }
    
    public void angleBounce(Ball b, Paddle p) {
        double ballCenter = b.y + b.h/2;
        double paddleCenter = p.y + p.h/2;
        
        double offsetNum = ballCenter - paddleCenter;
        double offsetRatio = offsetNum / paddleCenter;
        
        int newSpeed = b.ySpeed1 + (int) (offsetRatio*13);
        
        b.setSpeed(b.xSpeed, newSpeed);
    }
    
    public class RoboBall extends Thread {
        public void run() {
            while(true) {
                if (players == 1) {
                    int ballC = ball.y + ball.h/2;
                    int paddleC = p2.y + p2.h/2;
                    int p1c = p1.y + p1.h/2;
                    if (ball.x + ball.w > winL/2 + 8 && ball.x + ball.w > winL/2 - 8) {
                        if (paddleC != ballC) {
                            if (ballC < paddleC) p2.moveUp();
                            else p2.moveDown();
                        }
                    }
//                    else {
//                        if (p1c != ballC) {
//                            if (ballC < p1c) p1.moveUp();
//                            else p1.moveDown();
//                        }
//                    }
                }
                try {
                    Thread.sleep(difficulty);
                } catch(Exception e) {
                    System.out.println("Thread error!");
                }
            }
        }
    }
    
    public class Timer extends Thread {

        public void run() {
            while(true) {
                // If the game's state is "play"
                ball.newColor(ball.rallyLevel);
                if (state == 1) {
                    p1.rallyUp(ball);
                    p2.rallyUp(ball);
                    
                    if (p1.dir == 1) p1.moveUp();
                    else if (p1.dir == 2) p1.moveDown();

                    if (p2.dir == 1) p2.moveUp();
                    else if (p2.dir == 2) p2.moveDown();

                    ball.move();

                    // If the ball goes out of bounds (offscreen left/right) add one to the opposing player's points
                    if (ball.x <= 0) {
                        p2.score++;
                        resetPos();
                    } else if (ball.x + ball.w >= winL) {
                        p1.score++;
                        resetPos();
                    }

                    // If the ball hits one of the paddles, bounce it off.
                    if (collision(ball, p1)) {
                        //ball.bounceX(p1);
                        ball.bounceX();
                        angleBounce(ball, p1);
                    }
                    if (collision(ball, p2)) {
                        //ball.bounceX(p2);
                        ball.bounceX();
                        angleBounce(ball, p2);
                    }

                    // If the ball hits the top or bottom of the screen, bounce it off.
                    if (ball.y <= yOffset || ball.y + ball.h >= winH) ball.bounceY();
                    
                    // If either score is 10
                    if (p1.score == 10) {
                        state = 2;
                        victor = 1;
                    }
                    else if (p2.score == 10) {
                        state = 2;
                        victor = 2;
                    }
                }
                try {
                    Thread.sleep(10);
                } catch(Exception e) {
                    System.out.println("Thread error!");
                }
                repaint();
            }
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc=" Default settings ">
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Window().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    //</editor-fold>
}
