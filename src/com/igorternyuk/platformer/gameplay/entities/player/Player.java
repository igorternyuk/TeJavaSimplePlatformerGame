package com.igorternyuk.platformer.gameplay.entities.player;

import com.igorternyuk.platformer.gameplay.entities.Entity;
import com.igorternyuk.platformer.gameplay.entities.weapon.FireBall;
import com.igorternyuk.platformer.gameplay.tilemap.TileMap;
import com.igorternyuk.platformer.gamestate.LevelState;
import com.igorternyuk.platformer.graphics.animations.Animation;
import com.igorternyuk.platformer.graphics.animations.AnimationFacing;
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
public class Player extends Entity<PlayerAction>{
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
    
    public Player(TileMap tileMap, ResourceManager rm) {
        super(tileMap);
        this.resourceMananger = rm;
        this.maxVelocity = 120;
        this.horizontalAcceleration = 70;
        this.horizontalDeceleration = 40;
        this.maxFallingSpeed = 40;
        this.jumpVelocityInitial = -3;
        this.maxJumpVelocity = -9;
        this.verticalAcceleration = -0.75;
        this.gravity = 0.7;
        this.onGround = true;
        loadAnimations();
        this.animationMananger.setCurrentAnimation(PlayerAction.IDLE);
        this.animationMananger.getCurrentAnimation().start(AnimationPlayMode.LOOP);
    }
       
    private void loadAnimations(){
        BufferedImage spriteSheet = this.resourceMananger.getImage(
                ImageIdentifier.PLAYER_SPRITE_SHEET);
        this.animationMananger.addAnimation(
                PlayerAction.IDLE,
                new Animation(spriteSheet
                        , PlayerAction.IDLE.getSpeed()
                        , PlayerAction.IDLE.getFrames()));
        this.animationMananger.addAnimation(
                PlayerAction.WALKING,
                new Animation(spriteSheet
                        , PlayerAction.WALKING.getSpeed()
                        , PlayerAction.WALKING.getFrames()));
        this.animationMananger.addAnimation(
                PlayerAction.JUMPING,
                new Animation(spriteSheet
                        , PlayerAction.JUMPING.getSpeed()
                        , PlayerAction.JUMPING.getFrames()));
        this.animationMananger.addAnimation(
                PlayerAction.FALLING,
                new Animation(spriteSheet
                        , PlayerAction.FALLING.getSpeed()
                        , PlayerAction.FALLING.getFrames()));
        this.animationMananger.addAnimation(
                PlayerAction.GLIDING,
                new Animation(spriteSheet
                        , PlayerAction.GLIDING.getSpeed()
                        , PlayerAction.GLIDING.getFrames()));
        this.animationMananger.addAnimation(
                PlayerAction.FIREBALLING,
                new Animation(spriteSheet
                        , PlayerAction.FIREBALLING.getSpeed()
                        , PlayerAction.FIREBALLING.getFrames()));
        this.animationMananger.addAnimation(
                PlayerAction.SCRATCHING,
                new Animation(spriteSheet
                        , PlayerAction.SCRATCHING.getSpeed()
                        , PlayerAction.SCRATCHING.getFrames()));
    }
    
    public void setGliding(boolean gliding){
        this.gliding = gliding;
    }
    
    public void setFiring(){
        this.firing = true;
    }
    
    public void setScratching(){
        this.scratching = true;
    }

    public void setCanFire(boolean canFire) {
        this.canFire = canFire;
    }

    public void setCanScratch(boolean canScratch) {
        this.canScratch = canScratch;
    }
    
    protected void accelerateLeft(double frameTime){
        this.velX -= this.horizontalAcceleration * frameTime;
        if(this.velX < -this.maxVelocity){
            this.velX = -this.maxVelocity;
        }
    }
    
    protected void accelerateRight(double frameTime){
        this.velX += this.horizontalAcceleration * frameTime;
        if(this.velX > this.maxVelocity){
            this.velX = this.maxVelocity;
        }
    }
    
    @Override
    public void accelerateDown(double frameTime){
        if(!this.onGround){
            if(this.gliding){
                this.velY += FACTOR_OF_AIR_RESISTANCE * gravity * frameTime;
            } else {
                this.velY += gravity * frameTime;
            }
        }        
    }
    
    private void decelerate(double frameTime){
        if(this.velX > 0){
            this.velX -= this.horizontalDeceleration * frameTime;
            if(this.velX < 0){
                this.velX = 0;
            }
        } else if(this.velX < 0){
            this.velX += this.horizontalDeceleration * frameTime;
            if(this.velX > 0){
                this.velX = 0;
            }
        }
    }
   
    private PlayerAction getCurrentAction(){
        return this.animationMananger.getCurrentAnimationIdentifier();
    }
    
    @Override
    public void update(KeyboardState keyboardState, double frameTime) {
        this.animationMananger.update(frameTime);
        resetMoving();
        if(keyboardState.isKeyPressed(KeyEvent.VK_LEFT)
                || keyboardState.isKeyPressed(KeyEvent.VK_A)){
            accelerateLeft(frameTime);
            this.movingLeft = true;
        } else if(keyboardState.isKeyPressed(KeyEvent.VK_RIGHT)
                || keyboardState.isKeyPressed(KeyEvent.VK_D)){
            accelerateRight(frameTime);
            this.movingRight = true;
        } else {
            decelerate(frameTime);            
        }
        
        if(this.canFire){
            this.firing = keyboardState.isKeyPressed(KeyEvent.VK_F);
        }
        
        if(this.canScratch){
            this.scratching = keyboardState.isKeyPressed(KeyEvent.VK_R);
        }
        
        //Cannot moveHorizontally while attacking except in the air
        if(!this.onGround && (getCurrentAction() == PlayerAction.FIREBALLING
                || getCurrentAction() == PlayerAction.SCRATCHING)){
            this.velX = 0;
        }
        
        if(keyboardState.isKeyPressed(KeyEvent.VK_UP)
                || keyboardState.isKeyPressed(KeyEvent.VK_W)){
            this.jumping = this.onGround;
        }
        
        if(keyboardState.isKeyPressed(KeyEvent.VK_E)){
            if(isFalling()){
                this.gliding = true;
            }
        } else {
            this.gliding = false;
        }
        
        if(this.jumping){
            if(this.onGround){
                this.velY = this.jumpVelocityInitial;
                this.onGround = false;
            } else {
                this.velY += this.verticalAcceleration * frameTime;
            }
        }
        
        //Movement
        moveHorizontally(frameTime);
        moveVertically(frameTime);
        
        updateAnimation();
        /*if(this.flinching){
            this.flichTime += frameTime;
            if(this.flichTime >= FLINCH_PERIOD){
                this.flichTime = 0;
            }
        }*/
    }

    private void updateAnimation(){
        if(this.scratching){
            if(getCurrentAction() != PlayerAction.SCRATCHING){
                this.animationMananger
                        .setCurrentAnimation(PlayerAction.SCRATCHING);
                this.animationMananger
                        .getCurrentAnimation().start(AnimationPlayMode.ONCE);
            } else {
                if(this.animationMananger.getCurrentAnimation()
                        .hasBeenPlayedOnce()){
                    this.animationMananger.getCurrentAnimation().stop();
                    this.scratching = false;
                    this.canScratch = false;
                    setIdleAnimation();
                }
            }
        } else if(this.firing){
            if(getCurrentAction() != PlayerAction.FIREBALLING){
                this.animationMananger
                        .setCurrentAnimation(PlayerAction.FIREBALLING);
                this.animationMananger.getCurrentAnimation()
                        .start(AnimationPlayMode.ONCE);
            } else {
                if(this.animationMananger.getCurrentAnimation()
                        .hasBeenPlayedOnce()){
                    this.animationMananger.getCurrentAnimation().stop();
                    this.firing = false;
                    this.canFire = false;
                    setIdleAnimation();
                }
            }
        } else if(isFalling()){
            if(this.gliding){
                if(getCurrentAction() != PlayerAction.GLIDING){
                    this.animationMananger
                            .setCurrentAnimation(PlayerAction.GLIDING);
                    this.animationMananger.getCurrentAnimation()
                            .start(AnimationPlayMode.LOOP);
                }
            } else {
                if(getCurrentAction() != PlayerAction.FALLING){
                    this.animationMananger
                            .setCurrentAnimation(PlayerAction.FALLING);
                    this.animationMananger.getCurrentAnimation()
                            .start(AnimationPlayMode.LOOP);
                }
            }
        } else if(isLifting()){
            if(getCurrentAction() != PlayerAction.JUMPING){
                this.animationMananger
                        .setCurrentAnimation(PlayerAction.JUMPING);
                this.animationMananger.getCurrentAnimation()
                        .start(AnimationPlayMode.LOOP);
            }
        } else if(this.movingLeft || this.movingRight){
            //System.out.println("Setting WALKING");
            if(getCurrentAction() != PlayerAction.WALKING){
                this.animationMananger
                        .setCurrentAnimation(PlayerAction.WALKING);
                this.animationMananger.getCurrentAnimation()
                        .start(AnimationPlayMode.LOOP);
            }
        } else {
            setIdleAnimation();
        }
        
        setCorrectSnimationFacing();
        
        //System.out.println("playerX = " + this.x + " playerY = " + this.y);
    }
    
    private void setIdleAnimation(){
        if(getCurrentAction() != PlayerAction.IDLE){
            this.animationMananger.setCurrentAnimation(PlayerAction.IDLE);
            this.animationMananger.getCurrentAnimation()
                    .start(AnimationPlayMode.LOOP);
        }
    }
    
    private void setCorrectSnimationFacing(){
        if(this.movingLeft){
            this.animationMananger.setAnimationsFacing(AnimationFacing.LEFT);
        } else if(this.movingRight){
            this.animationMananger.setAnimationsFacing(AnimationFacing.RIGHT);
        }
    }
    
    @Override
    public void draw(Graphics2D g) {
        /*if(this.flinching){
            
        }*/
        this.animationMananger.draw(g, getAbsX(), getAbsY(),
                    LevelState.SCALE, LevelState.SCALE);
    }

    @Override
    public boolean isAlive() {
        return this.health > 0;
    }
}
