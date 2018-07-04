package com.igorternyuk.platformer.gameplay.entities.player;

import com.igorternyuk.platformer.gameplay.entities.Entity;
import com.igorternyuk.platformer.gameplay.entities.weapon.FireBall;
import com.igorternyuk.platformer.gameplay.tilemap.TileMap;
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
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author igor
 */
public class Player extends Entity {

    private static final double FLINCH_PERIOD = 0.1;
    private static final double FACTOR_OF_AIR_RESISTANCE = 0.001;

    private int health;
    private int maxHealth;
    private int fire;
    private int maxFire;
    private boolean flinching = false;
    private double flichTime;

    //Fireball attack
    private boolean firing = false;
    private boolean canFire = true;
    private int fireCost;
    private List<FireBall> fireBalls = new ArrayList<>();

    //Scratch attack
    private boolean scratching = false;
    private boolean canScratch = true;
    private int scratchDamage;
    private int scratchRange;

    private boolean gliding = false;

    private ResourceManager resourceMananger;
    protected AnimationManager<PlayerAnimationType> animationMananger;

    public Player(LevelState level) {
        super(level);
        this.maxVelocity = 120;
        this.horizontalAcceleration = 70;
        this.horizontalDeceleration = 40;
        this.maxFallingSpeed = 40;
        this.jumpVelocityInitial = -3;
        this.maxJumpVelocity = -9;
        this.verticalAcceleration = -0.75;
        this.gravity = 0.7;
        this.onGround = true;
        this.resourceMananger = level.getResourceManager();
        this.animationMananger = new AnimationManager<>();
        loadAnimations();
        this.animationMananger.setCurrentAnimation(PlayerAnimationType.IDLE);
        this.animationMananger.getCurrentAnimation().start(
                AnimationPlayMode.LOOP);
    }

    @Override
    public int getWidth() {
        return this.animationMananger.getCurrentAnimation()
                .getCurrentFrameWidth();
    }

    @Override
    public int getHeight() {
        return this.animationMananger.getCurrentAnimation()
                .getCurrentFrameHeight();
    }

    private void loadAnimations() {
        BufferedImage spriteSheet = this.resourceMananger.getImage(
                ImageIdentifier.PLAYER_SPRITE_SHEET);
        for (PlayerAnimationType animationType : PlayerAnimationType.values()) {
            this.animationMananger.addAnimation(
                    animationType,
                    new Animation(spriteSheet,
                            animationType.getSpeed(),
                            animationType.getFrames()));
        }
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

    public void setCanFire(boolean canFire) {
        this.canFire = canFire;
    }

    public void setCanScratch(boolean canScratch) {
        this.canScratch = canScratch;
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

    }

    private PlayerAnimationType getCurrentAction() {
        return this.animationMananger.getCurrentAnimationIdentifier();
    }

    private void handleUserInput(KeyboardState keyboardState) {
        if (keyboardState.isKeyPressed(KeyEvent.VK_LEFT)
                || keyboardState.isKeyPressed(KeyEvent.VK_A)) {
            this.movingLeft = true;
        } else if (keyboardState.isKeyPressed(KeyEvent.VK_RIGHT)
                || keyboardState.isKeyPressed(KeyEvent.VK_D)) {
            this.movingRight = true;
        }

        if (this.canFire) {
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

    @Override
    public void update(KeyboardState keyboardState, double frameTime) {
        resetMovingFlags();
        handleUserInput(keyboardState);

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

        if (this.firing) {
            attackWithFireBall();
        }

        //Movement
        move(frameTime);

        setProperAnimation();
        setProperAnimationFacing();
        this.animationMananger.update(frameTime);

        if (this.flinching) {
            this.flichTime += frameTime;
            if (this.flichTime >= FLINCH_PERIOD) {
                this.flichTime = 0;
                this.flinching = false;
            }
        }
    }

    private void resetVelocityIfCannotMove() {
        if (!this.onGround && (getCurrentAction() == PlayerAnimationType.FIREBALLING
                || getCurrentAction() == PlayerAnimationType.SCRATCHING)) {
            this.velX = 0;
        }
    }

    private void setProperAnimation() {
        if (this.scratching) {
            setAnimation(PlayerAnimationType.SCRATCHING, AnimationPlayMode.ONCE);
            if (this.animationMananger.getCurrentAnimation().hasBeenPlayedOnce()) {
                this.animationMananger.getCurrentAnimation().stop();
                this.scratching = false;
                this.canScratch = false;
                setAnimation(PlayerAnimationType.IDLE, AnimationPlayMode.LOOP);
            }
        } else if (this.firing) {
            setAnimation(PlayerAnimationType.FIREBALLING, AnimationPlayMode.ONCE);
            if (this.animationMananger.getCurrentAnimation()
                    .hasBeenPlayedOnce()) {
                this.animationMananger.getCurrentAnimation().stop();
                this.firing = false;
                this.canFire = false;
                setAnimation(PlayerAnimationType.IDLE, AnimationPlayMode.LOOP);
            }
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
            this.animationMananger.setCurrentAnimation(animationType);
            this.animationMananger.getCurrentAnimation().start(playMode);
        }
    }
    
    public AnimationFacing getAnimationFacing(){
        return this.animationMananger.getCurrentAnimation().getFacing();
    }

    private void setProperAnimationFacing() {
        if (this.movingLeft) {
            this.animationMananger.setAnimationsFacing(AnimationFacing.LEFT);
        } else if (this.movingRight) {
            this.animationMananger.setAnimationsFacing(AnimationFacing.RIGHT);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if (this.flinching) {
            if (((int) this.flichTime * 1000) % 2 == 0) {
                return;
            }
        }
        this.animationMananger.draw(g, getAbsX(), getAbsY(),
                LevelState.SCALE, LevelState.SCALE);
    }

    @Override
    public boolean isAlive() {
        return this.health > 0;
    }
}
