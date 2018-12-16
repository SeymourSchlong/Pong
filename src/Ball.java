
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

/**
 * The main ball that must be hit around.
 * 
 * @since 10.26.2018
 * @author seymour
 */

public class Ball {
    
    public int x, y, w, h, xSpeed1 = 3, xSpeed = xSpeed1, ySpeed1 = 3, ySpeed = ySpeed1;
    public Color color;
    public String string = "rectangle";
    public boolean left, up;
    public Random random;
    
    public int rally, rallyLevel;
    
    public Ball(int xx, int yy) {
        x = xx;
        y = yy;
        
        random = new Random();
    }
    
    public Ball(int xx, int yy, int ww, int hh, Color c) {
        x = xx;
        y = yy;
        w = ww;
        h = hh;
        color = c;
    }
    
    public void setSize(int ww, int hh) {
        w = ww;
        h = hh;
    }
    
    public void setShape(String str) {
        if ("rectangle".equals(str.toLowerCase())) string = str.toLowerCase();
        else string = "circle";
    }
    
    public void setSpeed(int xSp, int ySp) {
        xSpeed = xSp;
        ySpeed = ySp;
    }
    
    public void setDirection(boolean d, boolean u) {
        left = d;
        up = u;
    }
    
    public void setPos(int xx, int yy) {
        y = yy;
        x = xx;
    }
    
    public void setColor(Color c) {
        color = c;
    }
    
    public void randSpeed() {
        int tempX = random.nextInt(2) + 3;
        int tempY = random.nextInt(2) + 3;
        
        setSpeed(tempX, tempY);
    }
    
    // Changes the colour of the ball to be random.
    public void newColor(int a) {
        //int randNum = (int) Math.floor(Math.random() * 10);
        
        switch(a) {
            case 0:
                color = Color.white;
                break;
            case 1:
                color = Color.cyan;
                break;
            case 2:
                color = Color.green;
                break;
            case 3:
                color = Color.yellow;
                break;
            case 4:
                color = Color.orange;
                break;
            case 5:
                color = Color.red;
                break;
            default:
                color = Color.magenta;
        }
    }
    
    public void move() {
        if (up) y -= ySpeed;
        else y += ySpeed;
        
        if (left) x -= xSpeed;
        else x += xSpeed;
    }
    
    public void bounceX() {
        left = !left;
        randSpeed();
        
        rally++;
        if (rally == 10) {
            rally = 0;
            rallyLevel++;
        }
        xSpeed += rallyLevel;
        ySpeed += rallyLevel;
        
        newColor(rallyLevel);
    }
    
    public void bounceX(Paddle p) {
        left = !left;
        randSpeed();
        
        rally++;
        if (rally == 10) {
            rally = 0;
            rallyLevel++;
        }
        
        xSpeed = xSpeed1 + rallyLevel;
        ySpeed = ySpeed1 + rallyLevel;
        
        if (p.dir != 0) {
            xSpeed += 1;
            ySpeed += 1;
        }
        
        newColor(rallyLevel);
    }
    
    public void bounceY() {
        up = !up;
    }
    
    public void draw(Graphics g) {
        g.setColor(color);
        if ("rectangle".equals(string)) g.fillRect(x, y, w, h);
        else g.fillOval(x, y, w, h);
    }    
}
