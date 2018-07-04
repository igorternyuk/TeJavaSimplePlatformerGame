package com.igorternyuk.platformer.gamestate;

import com.igorternyuk.platformer.gameplay.Game;
import com.igorternyuk.platformer.gameplay.entities.Entity;
import com.igorternyuk.platformer.gameplay.entities.enemies.Snail;
import com.igorternyuk.platformer.gameplay.entities.enemies.Spider;
import com.igorternyuk.platformer.gameplay.entities.player.Player;
import com.igorternyuk.platformer.gameplay.tilemap.TileMap;
import com.igorternyuk.platformer.graphics.images.Background;
import java.awt.Graphics2D;
import com.igorternyuk.platformer.input.KeyboardState;
import com.igorternyuk.platformer.resourcemanager.ImageIdentifier;
import com.igorternyuk.platformer.resourcemanager.ResourceManager;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

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
        startNewGame();
    }
    
    private void startNewGame(){
        createPlayer();
        createEnemies();
    }
    
    private void createPlayer(){
        this.player = new Player(this);
        this.player.setPosition(2 * this.tileMap.getTileSize(),
                6 * this.tileMap.getTileSize());
        this.entities.add(this.player);
    }
    
    private void createEnemies(){
        Snail snail = new Snail(this, 6 * 30, 6 * 30 + 10);
        this.entities.add(snail);
        Spider s1 = new Spider(this, 7 * 30, 1 * 30);
        this.entities.add(s1);
        System.out.println("this.entities.size()" + this.entities.size());
    }

    @Override
    public void unload() {
        this.player = null;
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
        if (this.player != null) {
            //Remove the dead entities
            this.entities.removeIf(e -> e != this.player && !e.isAlive());

            //Update all entitites
            for (int i = this.entities.size() - 1; i >= 0; --i) {
                this.entities.get(i).update(keyboardState, frameTime);
            }
            scrollTileMapCamera();
        }
    }

    private void scrollTileMapCamera() {
        if (this.tileMap != null) {
            if (this.player.getX() > SCREEN_HALF_WIDTH
                    && this.tileMap.getWidth() - this.player.getX() > SCREEN_HALF_WIDTH) {
                int dx = (int) this.player.getX() - SCREEN_HALF_WIDTH;
                this.tileMap.setCameraPositionX(dx);
            }

            if (this.player.getY() > SCREEN_HALF_HEIGHT
                    && this.tileMap.getHeight() - this.player.getY() > SCREEN_HALF_HEIGHT) {
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
