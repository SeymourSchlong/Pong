
import java.awt.Color;
import java.awt.Graphics;

/**
 * Paddle to hit the ball. Controllable by the player
 * 
 * @since 10.29.2018
 * @author seymour
 */

public class Paddle {
    
    public int score = 0, dir = 0;
    public int x, y, w = 20, h = 75, defaultSpeed = 5, speed = defaultSpeed;
    public Color color;
    
    public Paddle(int xx, int yy) {
        x = xx;
        y = yy;
    }
    
    public void setPos(int xx, int yy) {
        x = xx;
        y = yy;
    }
    
    public void rallyUp(Ball b) {
        speed = defaultSpeed + b.rallyLevel*2;
    }
    
    public void setColor(Color c) {
        color = c;
    }
    
    public void moveUp() {
        if (y - speed > 25) y -= speed;
    }
    
    public void moveDown() {
        if (y + h + 4 + speed < 400) y += speed;
    }
    
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, w, h);
    }
}
