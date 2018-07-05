package com.igorternyuk.platformer.gameplay.entities;

import com.igorternyuk.platformer.gameplay.Game;
import com.igorternyuk.platformer.gameplay.tilemap.TileMap;
import com.igorternyuk.platformer.gameplay.tilemap.TileType;
import com.igorternyuk.platformer.gamestate.LevelState;
import com.igorternyuk.platformer.input.KeyboardState;
import java.awt.Graphics2D;

/**
 *
 * @author igor
 */
public abstract class Entity {
    protected LevelState level;
    protected EntityType type;
    
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
    protected double gravity;
    protected double maxFallingSpeed;
    protected boolean movingRight = false;
    protected boolean movingLeft = false;
    protected boolean jumping = false;
    protected boolean onGround = false;
    protected int health;
    protected int maxHealth;
    
    protected boolean flinching = false;
    protected double flinchTime;
    protected double flinchPeriod;
    protected boolean needDraw = true;
    protected int damage;

    public Entity(LevelState levelState, EntityType type) {
        this.level = levelState;
        this.type = type;
        this.tileMap = levelState.getTileMap();
        this.tileSize = this.tileMap.getTileSize();
        this.health = 100;
        this.maxHealth = 100;
    }

    public abstract int getWidth();
    public abstract int getHeight();
    
    public int getDamage(){
        return this.damage;
    }

    public EntityType getType() {
        return this.type;
    }

    public int getHealth() {
        return this.health;
    }

    public int getMaxHealth() {
        return this.maxHealth;
    }
    
    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double top() {
        return this.y;
    }

    public double bottom() {
        return this.y + getHeight();
    }

    public double left() {
        return this.x;
    }

    public double right() {
        return this.x + getWidth();
    }

    public int getMapX() {
        return this.tileMap.getCameraX();
    }

    public int getMapY() {
        return this.tileMap.getCameraY();
    }

    public int getAbsX() {
        return (int) this.x - getMapX();
    }

    public int getAbsY() {
        return (int) this.y - getMapY();
    }

    public int getAbsTop() {
        return getAbsY();
    }

    public int getAbsBottom() {
        return getAbsY() + getHeight();
    }

    public int getAbsLeft() {
        return getAbsX();
    }

    public int getAbsRight() {
        return getAbsX() + getWidth();
    }

    public boolean isOnTheScreen() {
        return getAbsRight() >= 0
                && getAbsLeft() < Game.WIDTH
                && getAbsTop() >= 0
                && getAbsBottom() < Game.HEIGHT;
    }

    public double getVelX() {
        return this.velX;
    }

    public double getVelY() {
        return this.velY;
    }

    protected boolean isFalling() {
        return !this.onGround && this.velY > gravity;
    }

    protected boolean isLifting() {
        return !this.onGround && this.velY < 0;
    }

    public final void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public final void setVelocity(double velX, double velY) {
        this.velX = velX;
        this.velY = velY;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }

    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    public void resetMovingFlags() {
        this.movingLeft = false;
        this.movingRight = false;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }
    
    protected void accelerateLeft(double frameTime) {
        this.velX -= this.horizontalAcceleration * frameTime;
        constrainHorizontalVelocity();
    }

    protected void accelerateRight(double frameTime) {
        this.velX += this.horizontalAcceleration * frameTime;
        constrainHorizontalVelocity();
    }
    
    protected void decelerate(double frameTime) {
        if (this.velX > 0) {
            this.velX -= this.horizontalDeceleration * frameTime;
            if (this.velX < 0) {
                this.velX = 0;
            }
        } else if (this.velX < 0) {
            this.velX += this.horizontalDeceleration * frameTime;
            if (this.velX > 0) {
                this.velX = 0;
            }
        }
    }
    
    public void accelerateDownwards(double frameTime) {
        this.velY += gravity * frameTime;
        contrainVerticalVelocity();
    }

    public void accelerateUpwards(double frameTime) {
        this.velY += this.verticalAcceleration * frameTime;
        contrainVerticalVelocity();
    }
    
    protected void constrainHorizontalVelocity(){
        if (this.velX < -this.maxVelocity) {
            this.velX = -this.maxVelocity;
        }
        
        if (this.velX > this.maxVelocity) {
            this.velX = this.maxVelocity;
        }
    }
    
    protected void contrainVerticalVelocity(){
        if (this.velY > this.maxFallingSpeed) {
            this.velY = this.maxFallingSpeed;
        }
        if(this.velY < this.maxJumpVelocity){
            this.velY = this.maxJumpVelocity;
        }
    }
    
    public void jump(double frameTime) {
        if (this.onGround) {
            this.velY = this.jumpVelocityInitial;
            this.onGround = false;
        } else {
            accelerateUpwards(frameTime);
        }
    }
    
    public void move(double frameTime){
        /*It it is very important to call moveVertically method first because 
        when the player tries to fire or scratch running it will be swept away from the map.
        The cause is horizontal collision handler and continues tries to fall
        in order to determine if the player is on the ground or not and further
        y coordinate correction*/
        moveVertically(frameTime);
        moveHorizontally(frameTime);
    }

    public void moveHorizontally(double frameTime) {
        this.x += this.velX * frameTime;
        handleMapCollision(Direction.HORIZONTAL);
    }

    public void moveVertically(double frameTime) {
        if (!this.onGround) {
            accelerateDownwards(frameTime);
        }
        /*The player should constantly try to fall in order to determine
        if it is on the ground or not*/
        this.y += this.velY;
        if (this.y < 0) {
            this.y = 0;
            this.velY = gravity;
        }
        this.onGround = false;
        handleMapCollision(Direction.VERTICAL);
    }

    public boolean collides(Entity other) {
        return !(this.right() < other.left()
                || this.left() > other.right()
                || this.top() > other.bottom()
                || this.bottom() < other.top());
    }

    protected void handleHorizontalCollision(int row, int col) {
        if (this.velX > 0) {
            this.x = col * this.tileSize - this.tileSize - 2;
        } else if (this.velX < 0) {
            this.x = col * this.tileSize + this.tileSize + 2;
        }
        this.velX = 0;
    }

    protected void handleVerticalCollision(int row, int col) {
        if (this.velY < 0) {
            this.y = row * this.tileSize + this.tileSize + 1;
            this.velY = gravity;

        } else if (this.velY > 0) {
            this.y = row * this.tileSize - this.tileSize;
            this.onGround = true;
            this.jumping = false;
            this.velY = 0;
        }
    }

    protected void handleMapCollision(Direction direction) {
        final int rowMin = (int) this.top() / this.tileSize;
        final int rowMax = (int) (this.bottom() - 1) / this.tileSize;
        final int colMin = (int) this.left() / this.tileSize;
        final int colMax = (int) (this.right() - 1) / this.tileSize;

        outer:
        for (int row = rowMin; row <= rowMax; ++row) {
            for (int col = colMin; col <= colMax; ++col) {
                if (this.tileMap.getTileType(row, col).equals(TileType.BLOCKED)) {
                    if (direction == Direction.VERTICAL) {
                        handleVerticalCollision(row, col);
                    } else if (direction == Direction.HORIZONTAL) {
                        handleHorizontalCollision(row, col);
                    }
                    //If we've got a collision we can terminate the further checking
                    break outer;
                }
            }
        }
    }
    
    public void hit(int damage){
        this.health -= damage;
        setFlinching(true);
    }

    public boolean isFlinching() {
        return this.flinching;
    }

    public void setFlinching(boolean flinching) {
        this.flinching = flinching;
    }

    public boolean isAlive() {
        return this.health > 0;
    }

    public void update(KeyboardState keyboardState, double frameTime){
        if (this.flinching) {
            this.flinchTime += frameTime;
            int number = (int) (this.flinchTime * 1000);
            this.needDraw = number % 10 == 0;  
            if (this.flinchTime >= this.flinchPeriod) {
                this.flinchTime = 0;
                this.flinching = false;            
            }
        } else {
            this.needDraw = true;
        }
    }

    public abstract void draw(Graphics2D g);
}
