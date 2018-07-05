package com.igorternyuk.platformer.gameplay.entities.enemies;

import com.igorternyuk.platformer.gameplay.entities.Entity;
import com.igorternyuk.platformer.gameplay.entities.EntityType;
import com.igorternyuk.platformer.gamestate.LevelState;
import com.igorternyuk.platformer.graphics.animations.Animation;
import com.igorternyuk.platformer.graphics.animations.AnimationFacing;
import com.igorternyuk.platformer.graphics.animations.AnimationManager;
import com.igorternyuk.platformer.graphics.animations.AnimationPlayMode;
import com.igorternyuk.platformer.input.KeyboardState;
import com.igorternyuk.platformer.resourcemanager.ImageIdentifier;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author igor
 */
public class Snail extends Entity {

    private AnimationManager<SnailAnimationType> animationManager =
            new AnimationManager<>();

    public Snail(LevelState levelState, double x, double y) {
        super(levelState, EntityType.SNAIL);
        setPosition(x, y);
        setupPhysics();
        loadAnimations();
        this.animationManager.setCurrentAnimation(SnailAnimationType.CRAWLING);
        this.animationManager.getCurrentAnimation().
                start(AnimationPlayMode.LOOP);
        this.flinchPeriod = 5;
    }

    private void setupPhysics() {
        this.onGround = true;
        this.maxVelocity = 20;
        this.velX = this.maxVelocity;
        this.velY = 0;
        this.gravity = 0;
        this.health = 100;
    }

    private void loadAnimations() {
        BufferedImage spriteSheet = this.level.getResourceManager().getImage(
                ImageIdentifier.SNAIL);
        for (SnailAnimationType animType : SnailAnimationType.values()) {
            this.animationManager.addAnimation(animType, new Animation(spriteSheet,
                    animType.getAnimationSpeed(), animType.getFrames()));
        }
    }

    @Override
    public int getWidth() {
        return this.animationManager.getCurrentAnimation().
                getCurrentFrameWidth();
    }

    @Override
    public int getHeight() {
        return this.animationManager.getCurrentAnimation().
                getCurrentFrameHeight();
    }

    @Override
    protected void handleHorizontalCollision(int row, int col) {
        if (this.velX > 0) {
            this.x = col * this.tileSize - this.tileSize - 2;
        } else if (this.velX < 0) {
            this.x = col * this.tileSize + this.tileSize + 2;
        }
        this.velX *= -1;
        setProperAnimationFacing();
    }

    private void setProperAnimationFacing() {
        if (this.velX < 0) {
            this.animationManager.setAnimationsFacing(AnimationFacing.LEFT);
        } else {
            this.animationManager.setAnimationsFacing(AnimationFacing.RIGHT);
        }
    }

    @Override
    public void update(KeyboardState keyboardState, double frameTime) {
        super.update(keyboardState, frameTime);
        move(frameTime);
        this.animationManager.update(frameTime);
    }

    @Override
    public void draw(Graphics2D g) {
        if(!this.needDraw)
            return;
        this.animationManager.draw(g, getAbsX(), getAbsY(), LevelState.SCALE,
                LevelState.SCALE);
    }
}
