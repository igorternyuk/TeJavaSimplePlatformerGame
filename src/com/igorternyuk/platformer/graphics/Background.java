package com.igorternyuk.platformer.graphics;

import com.igorternyuk.platformer.gameplay.Game;
import com.igorternyuk.platformer.input.KeyboardState;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author igor
 */
public class Background {
    private static final int SCROLLING_SPEED = 5;
    private BufferedImage image;
    private double x, y, dx, dy; 
    
    public Background(BufferedImage image, double x, double y, double dx,
            double dy){
        this.image = image;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }
    
    public Background(BufferedImage image){
        this(image, 0, 0, 0, 0);
    }
    
    public void setPosition(double x, double y){
        this.x = x;
        this.y = y;
    }
    
    public void setSpeed(double dx, double dy){
        this.dx = dx;
        this.dy = dy;
    }
    
    public void update(KeyboardState keyBoardState, double frameTime){
        this.x += this.dx * SCROLLING_SPEED;
        this.y += this.dy * SCROLLING_SPEED;
        
        if(this.x < -Game.WIDTH || this.x > Game.WIDTH){
            setPosition(0, this.y);
        }
        
        if(this.y < -Game.HEIGHT || this.y > Game.HEIGHT){
            setPosition(0, this.y);
        }
        
    }
    
    public void draw(Graphics2D g){
        g.drawImage(image, (int)this.x, (int)this.y, Game.WIDTH,
                    Game.HEIGHT, null);
        if(this.x < 0){
            g.drawImage(image, (int)this.x + Game.WIDTH, (int)this.y, Game.WIDTH,
                    Game.HEIGHT, null);
        }
        
        if(this.x > 0){
            g.drawImage(image, (int)this.x - Game.WIDTH, (int)this.y, Game.WIDTH,
                    Game.HEIGHT, null);
        }
    }
}
