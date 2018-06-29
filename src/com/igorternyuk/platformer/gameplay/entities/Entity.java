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
    //Tile stuff
    protected TileMap tileMap;
    protected int tileSize;
    
    //Geometry
    
    protected double x, y;
    protected int width, height;
    
    //Physics
    protected double velX, velY;
    protected double velocity, maxVelocity;
    protected double jumpVelocity, maxJumpVelocity;
    protected double gravity;
    protected double deceleration;
    protected double fallingSpeed;
    protected double maxFallingSpeed;
    protected boolean movingRight = false;
    protected boolean movingLeft = false;
    protected boolean movingUp = false;
    protected boolean movingDown = false;
    protected boolean jumping = false;
    protected boolean isFalling = false;
    protected boolean isOnGround = false;
    
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
        return this.animationMananger.getCurrentAnimation().getCurrentFrameWidth();
    }

    public int getHeight() {
        return this.animationMananger.getCurrentAnimation().getCurrentFrameHeight();
    }

    public double getVelX() {
        return this.velX;
    }

    public double getVelY() {
        return this.velY;
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

    public void setMovingUp(boolean movingUp) {
        this.movingUp = movingUp;
    }

    public void setMovingDown(boolean movingDown) {
        this.movingDown = movingDown;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }
    
    public void update(KeyboardState keyboardState, float frameTime){
        this.x += this.velX * frameTime;
        handleMapCollision(false);
        if(!this.isOnGround){
            this.velY += gravity * frameTime;
            this.y += this.velY;
            this.isOnGround = false;
            handleMapCollision(true);
        }
        this.velX = 0;
    }
    
    /*
       void update(float time)
   {	

	 rect.left += dx * time;	
	 Collision(0);

	 if (!onGround) dy=dy+0.0005*time;
	 rect.top += dy*time;
	 onGround=false;
     Collision(1);
  
	 
	  currentFrame += 0.005*time;
	  if (currentFrame > 6) currentFrame -=6 ;

	  if (dx>0) sprite.setTextureRect(IntRect(40*int(currentFrame),244,40,50));
	  if (dx<0) sprite.setTextureRect(IntRect(40*int(currentFrame)+40,244,-40,50));
	 

	  sprite.setPosition(rect.left - offsetX, rect.top - offsetY);

	  dx=0;
   }
    */
    
    public boolean collides(Entity other){
        return !(this.right() < other.left()
                || this.left() > other.right()
                || this.top() > other.bottom()
                || this.bottom() < other.top());
    }
    
    public void handleMapCollision(boolean isVerticalMovement){
        for(int row = this.top() / this.tileSize; row < this.bottom() / this.tileSize; ++row){
            for(int col = this.left(); col < this.right() / this.tileSize; ++col){
                if(this.tileMap.getTileType(row, col).equals(TileType.BLOCKED)){
                    if(isVerticalMovement){
                        if(this.velY < 0){
                            this.y = row * this.tileSize + this.tileSize;
                        } else if(this.velY > 0){
                            this.y = row * this.tileSize - this.tileSize;
                            this.isOnGround = true;
                            this.isFalling = false;
                            this.velY = 0;
                        }
                    } else {
                        if(this.velX > 0){
                            this.x = col * this.tileSize - this.tileSize;
                        } else if(this.velX < 0){
                            this.x = col * this.tileSize + this.tileSize;
                        }
                    }
                }
            }
        }
    }
    
    public abstract boolean isAlive();
    public abstract void draw(Graphics2D g);
}
