package com.igorternyuk.platformer.gameplay.entities;

import com.igorternyuk.platformer.gameplay.tilemap.TileMap;
import com.igorternyuk.platformer.gameplay.tilemap.TileType;

/**
 *
 * @author igor
 */
public abstract class Entity {
    //Tile stuff
    protected TileMap tileMap;
    protected int tileSize;
    
    //Geometry
    
    protected double relX, relY, absX, absY;
    protected int width, height;
    
    //Physics
    protected double velX, velY;
    protected double jumpVelocity;
    protected boolean isMovingRight = false;
    protected boolean isMovingLeft = false;
    protected boolean isJumping = false;
    protected boolean isFalling = false;
    protected boolean isOnGround = false;
    
    protected boolean isAlive = true;
    
    public Entity(TileMap tileMap){
        this.tileMap = tileMap;
        this.tileSize = this.tileMap.getTileSize();
    }
    
    public int top(){
        return (int)this.relY;
    }
    
    public int bottom(){
        return (int)this.relY + this.height;
    }
    
    public int left(){
        return (int)this.relX;
    }
    
    public int right(){
        return (int)this.relX + this.width;
    }
    
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
                            this.relY = row * this.tileSize + this.tileSize;
                        } else if(this.velY > 0){
                            this.relY = row * this.tileSize - this.tileSize;
                            this.isOnGround = true;
                            this.velY = 0;
                        }
                    } else {
                        if(this.velX > 0){
                            this.relX = col * this.tileSize - this.tileSize;
                        } else if(this.velX < 0){
                            this.relX = col * this.tileSize + this.tileSize;
                        }
                    }
                }
            }
        }
    }
}
