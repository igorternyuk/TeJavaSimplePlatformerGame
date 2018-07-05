package com.igorternyuk.platformer.gamestate;

import com.igorternyuk.platformer.gameplay.Game;
import com.igorternyuk.platformer.gameplay.entities.Entity;
import com.igorternyuk.platformer.gameplay.entities.EntityType;
import com.igorternyuk.platformer.gameplay.entities.enemies.Snail;
import com.igorternyuk.platformer.gameplay.entities.enemies.Spider;
import com.igorternyuk.platformer.gameplay.entities.player.Player;
import com.igorternyuk.platformer.gameplay.entities.powerups.Powerup;
import com.igorternyuk.platformer.gameplay.entities.powerups.PowerupType;
import com.igorternyuk.platformer.gameplay.entities.weapon.FireBall;
import com.igorternyuk.platformer.gameplay.tilemap.TileMap;
import com.igorternyuk.platformer.graphics.images.Background;
import java.awt.Graphics2D;
import com.igorternyuk.platformer.input.KeyboardState;
import com.igorternyuk.platformer.resourcemanager.ImageIdentifier;
import com.igorternyuk.platformer.resourcemanager.ResourceManager;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author igor
 */
public class LevelState extends GameState {

    public static final double SCALE = 2;
    private static final int SCREEN_HALF_WIDTH = (int) (Game.WIDTH / 2 / SCALE);
    private static final int SCREEN_HALF_HEIGHT =
            (int) (Game.HEIGHT / 2 / SCALE);
    private TileMap tileMap;
    private Background background;
    private Player player;
    private List<Entity> entities = new ArrayList<>();

    public LevelState(GameStateManager gsm, ResourceManager rm) {
        super(gsm, rm);
    }

    public List<Entity> getEntities() {
        return this.entities;
    }

    public TileMap getTileMap() {
        return this.tileMap;
    }

    @Override
    public void load() {
        System.out.println("Level state loading...");
        this.resourceManager.loadImage(ImageIdentifier.LEVEL_BACKGROUND,
                "/Backgrounds/grassbg1.gif");
        this.background = new Background(this.resourceManager
                .getImage(ImageIdentifier.LEVEL_BACKGROUND));
        this.tileMap = new TileMap(this.resourceManager, Game.TILE_SIZE);
        this.tileMap.loadTileSet("/Tilesets/grasstileset.gif");
        this.tileMap.loadMap("/Maps/level1.map");
        this.resourceManager.loadImage(ImageIdentifier.PLAYER_SPRITE_SHEET,
                "/Sprites/Player/playerSpriteSheet.png");
        this.resourceManager.loadImage(ImageIdentifier.FIRE_BALL,
                "/Sprites/Player/fireball.gif");
        this.resourceManager.loadImage(ImageIdentifier.SNAIL,
                "/Sprites/Enemies/snail.gif");
        this.resourceManager.loadImage(ImageIdentifier.SPIDER,
                "/Sprites/Enemies/spider.gif");
        this.resourceManager.loadImage(ImageIdentifier.EXPLOSION,
                "/Sprites/Enemies/explosion.gif");
        this.resourceManager.loadImage(ImageIdentifier.HUD,
                "/HUD/hud.gif");
        this.resourceManager.loadImage(ImageIdentifier.POWERUPS,
                "/Sprites/Powerups/powerups.png");
        startNewGame();
    }

    private void startNewGame() {
        createPlayer();
        createEnemies();
        createPowerups();
    }

    private void createPlayer() {
        this.player = new Player(this);
        this.player.setPosition(2 * this.tileMap.getTileSize(),
                6 * this.tileMap.getTileSize());
        this.entities.add(this.player);
    }

    private void createEnemies() {
        Snail snail = new Snail(this, 6 * 30, 6 * 30 + 10);
        this.entities.add(snail);
        Spider s1 = new Spider(this, 7 * 30, 1 * 30);
        this.entities.add(s1);
        System.out.println("this.entities.size()" + this.entities.size());
    }

    private void createPowerups() {
        Powerup p1 = new Powerup(this, EntityType.POWERUP,
                PowerupType.EXTRA_HEALTH, 16 * 30, 2 * 30);
        Powerup p2 = new Powerup(this, EntityType.POWERUP,
                PowerupType.EXTRA_FIRES, 5 * 30, 5 * 30);
        this.entities.add(p1);
        this.entities.add(p2);
    }

    @Override
    public void unload() {
        this.player = null;
        this.resourceManager.unloadImage(ImageIdentifier.POWERUPS);
        this.resourceManager.unloadImage(ImageIdentifier.HUD);
        this.resourceManager.unloadImage(ImageIdentifier.EXPLOSION);
        this.resourceManager.unloadImage(ImageIdentifier.SPIDER);
        this.resourceManager.unloadImage(ImageIdentifier.SNAIL);
        this.resourceManager.unloadImage(ImageIdentifier.FIRE_BALL);
        this.resourceManager.unloadImage(ImageIdentifier.PLAYER_SPRITE_SHEET);
        this.resourceManager.unloadImage(ImageIdentifier.LEVEL_BACKGROUND);
        this.tileMap = null;
    }

    @Override
    public void update(KeyboardState keyboardState, double frameTime) {
        if (this.player == null) {
            return;
        }
        //Remove the dead entities
        this.entities.removeIf(e -> e != this.player && !e.isAlive());

        //Update all entitites
        for (int i = this.entities.size() - 1; i >= 0; --i) {
            this.entities.get(i).update(keyboardState, frameTime);
        }

        checkCollisions();

        scrollTileMapCamera();
    }

    private List<Entity> getEnemies() {
        return this.entities.stream().filter(
                e -> e.getType() == EntityType.SNAIL
                || e.getType() == EntityType.SPIDER).
                collect(Collectors.toList());
    }

    private List<FireBall> getFireBalls() {
        return this.entities.stream().filter(e ->
                e.getType() == EntityType.FIREBALL).map(e -> (FireBall) e).
                collect(Collectors.toList());
    }

    private List<Powerup> getPowerups() {
        return this.entities.stream().filter(e -> e.getType()
                == EntityType.POWERUP).map(e -> (Powerup) e).collect(
                Collectors.toList());
    }

    private void checkCollisions() {
        checkFireBallEnemyCollision();
        
        
    }
    
    private void checkFireBallEnemyCollision(){
        List<Entity> enemies = getEnemies();
        List<FireBall> fireballs = getFireBalls();
        for(int i = 0; i < fireballs.size(); ++i){
            for(int j = 0; j < enemies.size(); ++j){
                FireBall fireball = fireballs.get(i);
                Entity enemy = enemies.get(j);
                if(!enemy.isFlinching() && fireball.collides(enemy)){
                    enemy.hit(fireball.getDamage());
                    fireball.setHit(true);
                    break;
                }
            }
        }
    }
    
    private void handlePlayerEnemy(Player player, Entity enemy) {

    }

    private void scrollTileMapCamera() {
        if (this.tileMap != null) {
            if (this.player.getX() > SCREEN_HALF_WIDTH
                    && this.tileMap.getWidth() - this.player.getX()
                    > SCREEN_HALF_WIDTH) {
                int dx = (int) this.player.getX() - SCREEN_HALF_WIDTH;
                this.tileMap.setCameraPositionX(dx);
            }

            if (this.player.getY() > SCREEN_HALF_HEIGHT
                    && this.tileMap.getHeight() - this.player.getY()
                    > SCREEN_HALF_HEIGHT) {
                int dy = (int) this.player.getY() - SCREEN_HALF_HEIGHT;
                this.tileMap.setCameraPositionY(dy);
            }
        }
    }

    @Override
    public void onKeyPressed(int keyCode) {
    }

    @Override
    public void onKeyReleased(int keyCode) {
        if (keyCode == KeyEvent.VK_F) {
            this.player.setCanFire(true);
        } else if (keyCode == KeyEvent.VK_R) {
            this.player.setCanScratch(true);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if (this.background != null) {
            this.background.draw(g);
        }

        if (this.tileMap != null) {
            this.tileMap.draw(g);
        }

        for (int i = this.entities.size() - 1; i >= 0; --i) {
            this.entities.get(i).draw(g);
        }
    }

}
