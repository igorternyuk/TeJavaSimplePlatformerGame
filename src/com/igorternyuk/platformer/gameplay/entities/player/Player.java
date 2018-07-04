package com.igorternyuk.platformer.gameplay.entities.player;

import com.igorternyuk.platformer.gameplay.entities.Entity;
import com.igorternyuk.platformer.gameplay.entities.EntityType;
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

    private static final double FLINCH_PERIOD = 0.1;
    private static final double FACTOR_OF_AIR_RESISTANCE = 0.001;

    private int numFires;
    private int maxFire = 25;
    private boolean flinching = false;
    private double flichTime;

    //Fireball attack
    private boolean firing = false;
    private boolean canFire = true;
    private int fireBallDamage = 25;

    //Scratch attack
    private boolean scratching = false;
    private boolean canScratch = true;
    private int scratchDamage;
    private int scratchRange;

    private boolean gliding = false;

    private ResourceManager resourceMananger;
    protected AnimationManager<PlayerAnimationType> animationManager;

    public Player(LevelState level) {
        super(level, EntityType.PLAYER);
        this.maxVelocity = 120;
        this.horizontalAcceleration = 70;
        this.horizontalDeceleration = 40;
        this.maxFallingSpeed = 40;
        this.jumpVelocityInitial = -3;
        this.maxJumpVelocity = -8;
        this.verticalAcceleration = -0.5;
        this.gravity = 0.7;
        this.onGround = true;
        this.resourceMananger = level.getResourceManager();
        this.animationManager = new AnimationManager<>();
        loadAnimations();
        this.animationManager.setCurrentAnimation(PlayerAnimationType.IDLE);
        this.animationManager.getCurrentAnimation().start(
                AnimationPlayMode.LOOP);
    }

    public int getFireBallDamage() {
        return this.fireBallDamage;
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
        if(this.numFires < this.maxFire){
            this.canFire = canFire;
        }
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
        this.level.getEntities().add(new FireBall(this.level, this));
        ++this.numFires;
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

        if (this.firing && this.canFire) {
            attackWithFireBall();
        }

        //Movement
        move(frameTime);

        setProperAnimation();
        setProperAnimationFacing();
        this.animationManager.update(frameTime);

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
            if (this.animationManager.getCurrentAnimation().hasBeenPlayedOnce()) {
                this.animationManager.getCurrentAnimation().stop();
                this.scratching = false;
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
            
            this.canFire = false;
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
    
    public AnimationFacing getAnimationFacing(){
        return this.animationManager.getCurrentAnimation().getFacing();
    }

    private void setProperAnimationFacing() {
        if (this.movingLeft) {
            this.animationManager.setAnimationsFacing(AnimationFacing.LEFT);
        } else if (this.movingRight) {
            this.animationManager.setAnimationsFacing(AnimationFacing.RIGHT);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if (this.flinching) {
            if (((int) this.flichTime * 1000) % 2 == 0) {
                return;
            }
        }
        this.animationManager.draw(g, getAbsX(), getAbsY(),
                LevelState.SCALE, LevelState.SCALE);
    }

    @Override
    public boolean isAlive() {
        return this.health > 0;
    }
}
