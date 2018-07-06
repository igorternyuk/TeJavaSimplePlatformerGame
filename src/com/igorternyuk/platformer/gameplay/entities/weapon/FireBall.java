package com.igorternyuk.platformer.gameplay.entities.weapon;

import com.igorternyuk.platformer.gameplay.entities.Entity;
import com.igorternyuk.platformer.gameplay.entities.EntityType;
import com.igorternyuk.platformer.gameplay.entities.player.Player;
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
public class FireBall extends Entity {

    private boolean alive = true;
    private boolean hit = false;
    private AnimationManager<FireBallAnimationType> animationMananger =
            new AnimationManager<>();

    public FireBall(LevelState level, Player player) {
        super(level, EntityType.FIREBALL);
        loadAnimations();
        this.animationMananger.setCurrentAnimation(FireBallAnimationType.FLYING);
        this.animationMananger.getCurrentAnimation().start(
                AnimationPlayMode.LOOP);
        setPhysics(player);
        this.health = 100;
        this.maxHealth = 100;
        this.damage = player.getFireBallDamage();
    }

    private void setPhysics(Player player) {
        this.y = player.top() + player.getHeight() / 4;
        this.velY = 0;
        this.maxVelocity = 200;
        this.gravity = 0.3;
        this.maxFallingSpeed = 100;
        AnimationFacing currentPlayerFacing = player.getAnimationFacing();
        if (currentPlayerFacing == AnimationFacing.RIGHT) {
            this.x = player.right() + this.getWidth();
            this.velX = +this.maxVelocity;
        } else if (currentPlayerFacing == AnimationFacing.LEFT) {
            this.x = player.left() - this.getWidth();
            this.velX = -this.maxVelocity;
        }

    }

    private void loadAnimations() {
        BufferedImage spriteSheet = this.level.getResourceManager().getImage(
                ImageIdentifier.FIRE_BALL);
        for (FireBallAnimationType animation : FireBallAnimationType.values()) {
            this.animationMananger.addAnimation(animation,
                    new Animation(spriteSheet, animation.getAnimationSpeed(),
                            animation.getFrames()));
        }
    }

    public boolean isHit() {
        return this.hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }
    
    @Override
    public int getDamage() {
        return this.damage;
    }
    
    @Override
    public int getWidth() {
        return this.animationMananger.getCurrentAnimation().
                getCurrentFrameWidth();
    }

    @Override
    public int getHeight() {
        return this.animationMananger.getCurrentAnimation().
                getCurrentFrameHeight();
    }

    @Override
    public boolean isAlive() {
        return this.alive;
    }

    @Override
    protected void handleHorizontalCollision(int row, int col) {
        super.handleHorizontalCollision(row, col);
        this.hit = true;
    }

    @Override
    protected void handleVerticalCollision(int row, int col) {
        super.handleVerticalCollision(row, col);
        this.hit = true;
    }

    @Override
    public void update(KeyboardState keyboardState, double frameTime) {
        if (this.hit) {
            if (this.animationMananger.getCurrentAnimationIdentifier()
                    != FireBallAnimationType.DESTROYING) {
                this.animationMananger.setCurrentAnimation(
                        FireBallAnimationType.DESTROYING);
                this.animationMananger.getCurrentAnimation().start(
                        AnimationPlayMode.ONCE);
            }
            if (this.animationMananger.getCurrentAnimation().
                    hasBeenPlayedOnce()) {
                this.alive = false;
            }
        }
        accelerateDownwards(frameTime);
        move(frameTime);
        this.animationMananger.update(frameTime);
    }

    @Override
    public void draw(Graphics2D g) {
        this.animationMananger.draw(g, getAbsX(), getAbsY(), LevelState.SCALE,
                LevelState.SCALE);
    }

}
