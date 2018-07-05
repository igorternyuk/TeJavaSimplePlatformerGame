package com.igorternyuk.platformer.gameplay.entities.powerups;

import com.igorternyuk.platformer.gameplay.entities.Entity;
import com.igorternyuk.platformer.gameplay.entities.EntityType;
import com.igorternyuk.platformer.gamestate.LevelState;
import com.igorternyuk.platformer.graphics.images.Sprite;
import com.igorternyuk.platformer.input.KeyboardState;
import com.igorternyuk.platformer.resourcemanager.ImageIdentifier;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 *
 * @author igor
 */
public class PowerUp extends Entity {

    private PowerupType powerupType;
    private Sprite sprite;
    private int numFires;
    private int healthIncrement;

    public PowerUp(LevelState levelState, EntityType type,
            PowerupType powerupType, double x, double y) {
        super(levelState, type);
        this.powerupType = powerupType;
        setPosition(x, y);
        setupBonuses();
        loadSprites();
        setProperSourceRect();
        setProperDestRect();
    }

    public int getNumFires() {
        return this.numFires;
    }

    public int getHealthIncrement() {
        return this.healthIncrement;
    }

    private void setupBonuses() {
        Random random = new Random();
        if (powerupType == PowerupType.EXTRA_HEALTH) {
            this.numFires = 0;
            this.healthIncrement = random.nextInt(45) + 5;
        } else if (powerupType == PowerupType.EXTRA_FIRES) {
            this.numFires = random.nextInt(7) + 3;
            this.healthIncrement = 0;
        }
    }

    public PowerupType getPowerupType() {
        return this.powerupType;
    }

    private void loadSprites() {
        BufferedImage image = this.level.getResourceManager().getImage(
                ImageIdentifier.POWERUPS);
        this.sprite = new Sprite(image, this.x, this.y);
    }
    
    private void setProperSourceRect(){
        Rectangle sourceRect = this.sprite.getSourceRect();
        if (this.powerupType == PowerupType.EXTRA_FIRES) {
            sourceRect.x = 15;
        } else {
            sourceRect.x = 0;
        }
        sourceRect.y = 0;
        sourceRect.width = 15;
        sourceRect.height = 16;
    }
    
    private void setProperDestRect(){
        Rectangle destRect = this.sprite.getDestRect();
        destRect.width = 15;
        destRect.height = 16;
    }

    public void collect() {
        this.health = 0;
    }

    @Override
    public int getWidth() {
        return this.sprite.getWidth();
    }

    @Override
    public int getHeight() {
        return this.sprite.getHeight();
    }
    
    @Override
    public void update(KeyboardState keyboardState, double frameTime){
        this.sprite.setPosition(getAbsX(), getAbsY());
    }

    @Override
    public void draw(Graphics2D g) {
        //System.out.println("draw powerup x = " + x + " y = " + y);
        this.sprite.draw(g);
    }
}
