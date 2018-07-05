package com.igorternyuk.platformer.gameplay.entities.player;

import com.igorternyuk.platformer.gameplay.entities.Entity;
import com.igorternyuk.platformer.gameplay.entities.EntityType;
import com.igorternyuk.platformer.gameplay.entities.powerups.PowerUp;
import com.igorternyuk.platformer.gameplay.entities.weapon.FireBall;
import com.igorternyuk.platformer.gamestate.LevelState;
import com.igorternyuk.platformer.graphics.animations.Animation;
import com.igorternyuk.platformer.graphics.animations.AnimationFacing;
import com.igorternyuk.platformer.graphics.animations.AnimationManager;
import com.igorternyuk.platformer.graphics.animations.AnimationPlayMode;
import com.igorternyuk.platformer.input.KeyboardState;
import com.igorternyuk.platformer.resourcemanager.ImageIdentifier;
import com.igorternyuk.platformer.resourcemanager.ResourceManager;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 *
 * @author igor
 */
public class Player extends Entity {

    private static final double FACTOR_OF_AIR_RESISTANCE = 0.001;

    private int numFires = 25;
    private int maxFires = 25;

    //Fireball attack
    private boolean firing = false;
    private boolean alreadyFired = true;
    private int fireBallDamage = 25;

    //Scratch attack
    private boolean scratching = false;
    private boolean canScratch = true;
    private int scratchDamage = 50;

    private boolean gliding = false;

    private ResourceManager resourceMananger;
    protected AnimationManager<PlayerAnimationType> animationManager;

    private PlayerIndicator indicator;

    public Player(LevelState level) {
        super(level, EntityType.PLAYER);
        this.maxVelocity = 120;
        this.horizontalAcceleration = 70;
        this.horizontalDeceleration = 40;
        this.maxFallingSpeed = 40;
        this.jumpVelocityInitial = -3;
        this.maxJumpVelocity = -6;
        this.verticalAcceleration = -0.4;
        this.gravity = 0.7;
        this.onGround = true;
        this.resourceMananger = level.getResourceManager();
        this.animationManager = new AnimationManager<>();
        loadAnimations();
        this.animationManager.setCurrentAnimation(PlayerAnimationType.IDLE);
        this.animationManager.getCurrentAnimation().start(
                AnimationPlayMode.LOOP);
        this.flinchPeriod = 3;
        this.indicator = new PlayerIndicator(this.resourceMananger.getImage(
                ImageIdentifier.HUD), this, 0, 0);
    }

    public boolean isInScratchArea(Entity entity) {
        if (!entity.collides(this)) {
            return false;
        }
        AnimationFacing currentPlayerFacing = this.animationManager.
                getCurrentAnimation().getFacing();
        if (currentPlayerFacing == AnimationFacing.LEFT && entity.right()
                > left()) {
            return true;
        } else if (currentPlayerFacing == AnimationFacing.RIGHT && entity.left()
                < right()) {
            return true;
        }
        return false;
    }

    @Override
    public void hit(int damage) {
        super.hit(damage);
    }

    public void collectPowerup(PowerUp powerup) {
        gainExtraFires(powerup.getNumFires());
        gainHealth(powerup.getHealthIncrement());
        powerup.collect();
    }

    public boolean isFiring() {
        return this.firing;
    }

    public boolean isScratching() {
        return this.scratching;
    }

    public boolean isGliding() {
        return this.gliding;
    }

    public int getFireBallDamage() {
        return this.fireBallDamage;
    }

    public int getScratchDamage() {
        return this.scratchDamage;
    }

    public int getNumFires() {
        return this.numFires;
    }

    public int getMaxFires() {
        return this.maxFires;
    }

    @Override
    public int getWidth() {
        return this.animationManager.getCurrentAnimation()
                .getCurrentFrameWidth();
    }

    @Override
    public int getHeight() {
        return this.animationManager.getCurrentAnimation()
                .getCurrentFrameHeight();
    }

    @Override
    public boolean isAlive() {
        return this.health > 0;
    }

    public void setGliding(boolean gliding) {
        this.gliding = gliding;
    }

    public void setFiring() {
        this.firing = true;
    }

    public void setScratching() {
        this.scratching = true;
    }

    public void resetAlreadyFired() {
        this.alreadyFired = false;
    }

    public boolean canFire() {
        return !isFlinching() && !this.alreadyFired && this.numFires > 0;
    }

    public void setCanScratch(boolean canScratch) {
        this.canScratch = canScratch;
    }

    public void gainHealth(int healthIncrement) {
        this.health += healthIncrement;
        if (this.health > this.maxHealth) {
            this.health = this.maxHealth;
        }
    }

    public void gainExtraFires(int numFires) {
        this.numFires += numFires;
        if (this.numFires > this.maxFires) {
            this.numFires = this.maxFires;
        }
    }

    @Override
    public void accelerateDownwards(double frameTime) {
        if (!this.onGround) {
            if (this.gliding) {
                this.velY += FACTOR_OF_AIR_RESISTANCE * gravity * frameTime;
            } else {
                this.velY += gravity * frameTime;
            }
        }
    }

    private void attackWithFireBall() {
        if (canFire()) {
            this.level.getEntities().add(new FireBall(this.level, this));
            --this.numFires;
        }
    }

    private PlayerAnimationType getCurrentAction() {
        return this.animationManager.getCurrentAnimationIdentifier();
    }

    private void handleUserInput(KeyboardState keyboardState) {
        if (keyboardState.isKeyPressed(KeyEvent.VK_LEFT)
                || keyboardState.isKeyPressed(KeyEvent.VK_A)) {
            this.movingLeft = true;
        } else if (keyboardState.isKeyPressed(KeyEvent.VK_RIGHT)
                || keyboardState.isKeyPressed(KeyEvent.VK_D)) {
            this.movingRight = true;
        }

        if (!this.alreadyFired) {
            this.firing = keyboardState.isKeyPressed(KeyEvent.VK_F);
        }

        if (this.canScratch) {
            this.scratching = keyboardState.isKeyPressed(KeyEvent.VK_R);
        }

        if (keyboardState.isKeyPressed(KeyEvent.VK_UP)
                || keyboardState.isKeyPressed(KeyEvent.VK_W)) {
            this.jumping = this.onGround;
        }

        if (keyboardState.isKeyPressed(KeyEvent.VK_E)) {
            if (isFalling()) {
                this.gliding = true;
            }
        } else {
            this.gliding = false;
        }
    }

    private void correctMovement(double frameTime) {
        if (this.movingLeft) {
            accelerateLeft(frameTime);
        } else if (this.movingRight) {
            accelerateRight(frameTime);
        } else {
            decelerate(frameTime);
        }

        //Cannot moveHorizontally while attacking except in the air
        resetVelocityIfCannotMove();

        if (this.jumping) {
            jump(frameTime);
        }
    }

    private void attackIfHasTo() {
        if (this.firing) {
            attackWithFireBall();
        }
    }

    private void updateAnimations(double frameTime) {
        setProperAnimation();
        setProperAnimationFacing();
        this.animationManager.update(frameTime);
    }

    @Override
    public void update(KeyboardState keyboardState, double frameTime) {
        super.update(keyboardState, frameTime);
        this.indicator.update(keyboardState, frameTime);
        resetMovingFlags();
        handleUserInput(keyboardState);
        correctMovement(frameTime);
        move(frameTime);
        attackIfHasTo();
        updateAnimations(frameTime);
        
    }

    private void resetVelocityIfCannotMove() {
        if (getCurrentAction() == PlayerAnimationType.FIREBALLING
                || getCurrentAction() == PlayerAnimationType.SCRATCHING) {
            this.velX = 0;
        }
    }

    private void setProperAnimation() {
        if (this.scratching) {
            setAnimation(PlayerAnimationType.SCRATCHING, AnimationPlayMode.ONCE);
            if (this.animationManager.getCurrentAnimation().hasBeenPlayedOnce()) {
                this.animationManager.getCurrentAnimation().stop();
                this.canScratch = false;
                setAnimation(PlayerAnimationType.IDLE, AnimationPlayMode.LOOP);
            }
        } else if (this.firing) {
            setAnimation(PlayerAnimationType.FIREBALLING, AnimationPlayMode.ONCE);
            if (this.animationManager.getCurrentAnimation()
                    .hasBeenPlayedOnce()) {
                this.animationManager.getCurrentAnimation().stop();
                this.firing = false;
                setAnimation(PlayerAnimationType.IDLE, AnimationPlayMode.LOOP);
            }
            this.alreadyFired = true;
        } else if (isFalling()) {
            if (this.gliding) {
                setAnimation(PlayerAnimationType.GLIDING, AnimationPlayMode.LOOP);
            } else {
                setAnimation(PlayerAnimationType.FALLING, AnimationPlayMode.LOOP);
            }
        } else if (isLifting()) {
            setAnimation(PlayerAnimationType.JUMPING, AnimationPlayMode.LOOP);
        } else if (this.movingLeft || this.movingRight) {
            setAnimation(PlayerAnimationType.WALKING, AnimationPlayMode.LOOP);
        } else {
            setAnimation(PlayerAnimationType.IDLE, AnimationPlayMode.LOOP);
        }
    }

    private void setAnimation(PlayerAnimationType animationType,
            AnimationPlayMode playMode) {
        if (getCurrentAction() != animationType) {
            this.animationManager.setCurrentAnimation(animationType);
            this.animationManager.getCurrentAnimation().start(playMode);
        }
    }

    public AnimationFacing getAnimationFacing() {
        return this.animationManager.getCurrentAnimation().getFacing();
    }

    private void setProperAnimationFacing() {
        if (this.movingLeft) {
            this.animationManager.setAnimationsFacing(AnimationFacing.LEFT);
        } else if (this.movingRight) {
            this.animationManager.setAnimationsFacing(AnimationFacing.RIGHT);
        }
    }

    private void loadAnimations() {
        BufferedImage spriteSheet = this.resourceMananger.getImage(
                ImageIdentifier.PLAYER_SPRITE_SHEET);
        for (PlayerAnimationType animationType : PlayerAnimationType.values()) {
            this.animationManager.addAnimation(
                    animationType,
                    new Animation(spriteSheet,
                            animationType.getSpeed(),
                            animationType.getFrames()));
        }
    }

    @Override
    public void draw(Graphics2D g) {
        this.indicator.draw(g);
        if (!this.needDraw) {
            return;
        }
        this.animationManager.draw(g, getAbsX(), getAbsY(),
                LevelState.SCALE, LevelState.SCALE);
    }
}
