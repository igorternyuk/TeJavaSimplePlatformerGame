package com.igorternyuk.platformer.gameplay.entities.enemies;

import com.igorternyuk.platformer.gameplay.entities.Entity;
import com.igorternyuk.platformer.gameplay.entities.EntityType;
import com.igorternyuk.platformer.gamestate.LevelState;
import com.igorternyuk.platformer.graphics.images.Sprite;
import com.igorternyuk.platformer.input.KeyboardState;
import com.igorternyuk.platformer.resourcemanager.ImageIdentifier;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author igor
 */
public class Spider extends Entity{
    private Sprite sprite;
    
    public Spider(LevelState levelState, double x, double y) {
        super(levelState, EntityType.SPIDER);
        setPosition(x, y);
        setupPhysics();
        loadSprite();
    }
    
    private void setupPhysics(){
        this.velX = 0;
        this.velY = 0;
        this.gravity = 0.8;
        this.maxFallingSpeed = 40;
        this.jumpVelocityInitial = -3;
        this.maxJumpVelocity = -9;
        this.verticalAcceleration = -1.3;
        this.onGround = false;
        this.health = 100;
    }
    
    private void loadSprite(){
        BufferedImage image = this.level.getResourceManager().getImage(
                ImageIdentifier.SPIDER);
        this.sprite = new Sprite(image, this.x, this.y);
    }

    @Override
    public int getWidth() {
        return this.sprite.getWidth();
    }
    
    @Override
    public int getHeight(){
        return this.sprite.getHeight();
    }
    
    @Override
    protected void handleVerticalCollision(int row, int col) {
        if (this.velY < 0) {
            this.y = row * this.tileSize + this.tileSize + 2;
            this.velY = gravity;

        } else if (this.velY > 0) {
            this.y = row * this.tileSize - this.tileSize - 2;
            //this.onGround = true;
            this.velY = this.verticalAcceleration;
        }
    }

    @Override
    public void update(KeyboardState keyboardState, double frameTime) {
        if(this.isLifting()){
            accelerateUpwards(frameTime);
        }
        moveVertically(frameTime);
        this.sprite.setPosition(getAbsX(), getAbsY());        
    }

    @Override
    public void draw(Graphics2D g) {
        //draw the spider's web
        g.setColor(Color.black);
        g.drawLine((int)((getAbsX() + getWidth() / 2) * LevelState.SCALE),
                (int)(0 * LevelState.SCALE),
                (int)((getAbsX() + getWidth() / 2) * LevelState.SCALE),
                (int)((getAbsTop() + getHeight() / 2) * LevelState.SCALE));
        this.sprite.draw(g);
    }

}
