package com.igorternyuk.platformer.gameplay.entities;

import com.igorternyuk.platformer.gameplay.Game;
import com.igorternyuk.platformer.gameplay.tilemap.TileMap;
import com.igorternyuk.platformer.gameplay.tilemap.TileType;
import com.igorternyuk.platformer.graphics.animations.AnimationManager;
import com.igorternyuk.platformer.input.KeyboardState;
import java.awt.Graphics2D;

/**
 *
 * @author igor
 * @param <AnimationIdentifier> Animation identifier
 */
public abstract class Entity<AnimationIdentifier> {
    public static final double GRAVITY = 0.5;
    
    //Tile stuff
    protected TileMap tileMap;
    protected int tileSize;
    
    //Geometry
    
    protected double x, y;
    
    //Physics
    protected double velX, velY;
    protected double maxVelocity;
    protected double horizontalAcceleration;
    protected double horizontalDeceleration;
    protected double jumpVelocityInitial, maxJumpVelocity;
    protected double verticalAcceleration;
    protected double maxFallingSpeed;
    protected boolean movingRight = false;
    protected boolean movingLeft = false;
    protected boolean jumping = false;
    protected boolean onGround = false;
    
    //Animation
    protected AnimationManager<AnimationIdentifier> animationMananger;

    
    public Entity(TileMap tileMap){
        this.tileMap = tileMap;
        this.tileSize = this.tileMap.getTileSize();
        this.animationMananger = new AnimationManager<>();
    }
    
    public int top(){
        return (int)this.y;
    }
    
    public int bottom(){
        return (int)this.y + getHeight();
    }
    
    public int left(){
        return (int)this.x;
    }
    
    public int right(){
        return (int)this.x + getWidth();
    }
    
    public int getMapX(){
        return this.tileMap.getX();
    }
    
    public int getMapY(){
        return this.tileMap.getY();
    }
    
    public int getAbsX(){
        return getMapX() + (int)this.x;
    }
    
    public int getAbsY(){
        return getMapY() + (int)this.y;
    }
    
    public int getAbsTop(){
        return getAbsY();
    }
    
    public int getAbsBottom(){
        return getAbsY() + getHeight();
    }
    
    public int getAbsLeft(){
        return getAbsX();
    }
    
    public int getAbsRight(){
        return getAbsX() + getWidth();
    }
    
    public boolean isOnTheScreen(){
        return getAbsRight() >= 0
                && getAbsLeft() < Game.WIDTH
                && getAbsTop() >= 0
                && getAbsBottom() < Game.HEIGHT;
    }

    public int getWidth() {
        return this.animationMananger.getCurrentAnimation()
                .getCurrentFrameWidth();
    }

    public int getHeight() {
        return this.animationMananger.getCurrentAnimation()
                .getCurrentFrameHeight();
    }

    public double getVelX() {
        return this.velX;
    }

    public double getVelY() {
        return this.velY;
    }
    
    protected boolean isFalling(){
        return !this.onGround && this.velY > 0;
    }
    
    protected boolean isLifting(){
        return !this.onGround && this.velY < 0;
    }
    
    public void setPosition(double x, double y){
        this.x = x;
        this.y = y;
    }
    
    public void setVelocity(double velX, double velY){
        this.velX = velX;
        this.velY = velY;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }

    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    public void resetMoving(){
        this.movingLeft = false;
        this.movingRight = false;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }
    
    public void moveHorizontally(double frameTime){
        this.x += this.velX * frameTime;
        handleMapCollision(false);
    }
    
    public void accelerateDown(double frameTime){
        this.velY += GRAVITY * frameTime;
        if(this.velY > this.maxFallingSpeed){
            this.velY = this.maxFallingSpeed;
        }
    }
    
    public void moveVertically(double frameTime){
        if(!this.onGround){
            accelerateDown(frameTime);
            this.y += this.velY;            
            this.onGround = false;
            handleMapCollision(true);
        }
    }
    
    public boolean collides(Entity other){
        return !(this.right() < other.left()
                || this.left() > other.right()
                || this.top() > other.bottom()
                || this.bottom() < other.top());
    }
    
    public void handleMapCollision(boolean isVerticalMovement){
        //System.out.println("Collision checking for vertical movement");
        /*System.out.println("this.top()  = " + this.top());
        System.out.println("this.bottom() = " + this.bottom());
        System.out.println("this.left() = " + this.left());
        System.out.println("this.right()  = " + this.right());
        System.out.println("this.top() / this.tileSize = " + (this.top() / this.tileSize));
        System.out.println("this.bottom() / this.tileSize = " + (this.bottom() / this.tileSize));
        System.out.println("this.left() / this.tileSize = " + (this.left() / this.tileSize));
        System.out.println("this.right() / this.tileSize = " + (this.right() / this.tileSize));*/
        outer:
        for(int row = this.top() / this.tileSize; row <= (this.bottom() - 1) / this.tileSize; ++row){
            for(int col = this.left() / this.tileSize; col <= (this.right() - 1) / this.tileSize; ++col){
                if(this.tileMap.getTileType(row, col).equals(TileType.BLOCKED)){
                    if(isVerticalMovement){                        
                        if(this.velY < 0){
                            this.y = row * this.tileSize + this.tileSize;
                        } else if(this.velY > 0){
                            this.y = row * this.tileSize - this.tileSize;
                            this.onGround = true;
                            this.velY = 0;
                            
                        }
                    } else {
                        if(this.velX > 0){
                            this.x = col * this.tileSize - this.tileSize;
                        } else if(this.velX < 0){
                            this.x = col * this.tileSize + this.tileSize;
                        }
                    }
                    break outer;
                }
            }
        }
    }
    
    public abstract boolean isAlive();
    public abstract void update(KeyboardState keyboardState, double frameTime);
    public abstract void draw(Graphics2D g);
}
