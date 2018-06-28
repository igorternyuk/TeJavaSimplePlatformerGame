package com.igorternyuk.platformer.gameplay.entities.player;

import com.igorternyuk.platformer.gameplay.entities.Entity;
import com.igorternyuk.platformer.gameplay.entities.weapon.FireBall;
import com.igorternyuk.platformer.gameplay.tilemap.TileMap;
import com.igorternyuk.platformer.graphics.animations.Animation;
import com.igorternyuk.platformer.resourcemanager.ImageIdentifier;
import com.igorternyuk.platformer.resourcemanager.ResourceManager;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author igor
 */
public class Player extends Entity<PlayerState>{
    private int health;
    private int maxHealth;
    private int fire;
    private int maxFire;
    private boolean flinching = false;
    private long flichTime;
    
    //Fireball attack
    private boolean firing = false;
    private int fireCost;
    private List<FireBall> fireBalls = new ArrayList<>();
    
    //Scratch attack
    private boolean scratching = false;
    
    private boolean gliding = false;
    private ResourceManager resourceMananger;
    private PlayerState playerState;
    
    public Player(TileMap tileMap, ResourceManager rm) {
        super(tileMap);
        this.resourceMananger = rm;
        this.playerState = PlayerState.IDLE;
        loadAnimations();
    }
       
    private void loadAnimations(){
        BufferedImage spriteSheet = this.resourceMananger.getImage(
                ImageIdentifier.PLAYER_SPRITE_SHEET);
        this.animationMananger.addAnimation(
                PlayerState.IDLE,
                new Animation(spriteSheet, 0.25, PlayerState.IDLE.getFrames()));
        this.animationMananger.addAnimation(
                PlayerState.WALKING,
                new Animation(spriteSheet, 0.25, PlayerState.WALKING.getFrames()));
        this.animationMananger.addAnimation(
                PlayerState.JUMPING,
                new Animation(spriteSheet, 0.25, PlayerState.JUMPING.getFrames()));
        this.animationMananger.addAnimation(
                PlayerState.FALLING,
                new Animation(spriteSheet, 0.25, PlayerState.FALLING.getFrames()));
        this.animationMananger.addAnimation(
                PlayerState.GLIDING,
                new Animation(spriteSheet, 0.25, PlayerState.GLIDING.getFrames()));
        this.animationMananger.addAnimation(
                PlayerState.FIREBALLING,
                new Animation(spriteSheet, 0.1, PlayerState.FIREBALLING.getFrames()));
        this.animationMananger.addAnimation(
                PlayerState.SCRATCHING,
                new Animation(spriteSheet, 0.1, PlayerState.SCRATCHING.getFrames()));
    }

    @Override
    public void draw(Graphics2D g) {
    }

    @Override
    public boolean isAlive() {
        return this.health > 0;
    }
    
}
