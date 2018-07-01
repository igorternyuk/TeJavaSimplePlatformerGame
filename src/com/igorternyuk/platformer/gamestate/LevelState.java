package com.igorternyuk.platformer.gamestate;

import com.igorternyuk.platformer.gameplay.Game;
import com.igorternyuk.platformer.gameplay.entities.player.Player;
import com.igorternyuk.platformer.gameplay.tilemap.TileMap;
import com.igorternyuk.platformer.graphics.Background;
import java.awt.Graphics2D;
import com.igorternyuk.platformer.input.KeyboardState;
import com.igorternyuk.platformer.resourcemanager.ImageIdentifier;
import com.igorternyuk.platformer.resourcemanager.ResourceManager;
import java.awt.event.KeyEvent;

/**
 *
 * @author igor
 */
public class LevelState extends GameState{
    public static final double SCALE = 2;
    private static final int SCREEN_HALF_WIDTH = (int)(Game.WIDTH / 2 / SCALE);
    private static final int SCREEN_HALF_HEIGHT = (int)(Game.HEIGHT / 2 / SCALE);
    private TileMap tileMap;
    private Background background;
    private Player player;
    public LevelState(GameStateManager gsm, ResourceManager rm) {
        super(gsm, rm);        
    }

    @Override
    public void init() {
        System.out.println("Level state initialization...");
        this.resourceManager.loadImage(ImageIdentifier.PLAY_BACKGROUND,
                "/Backgrounds/grassbg1.gif");
        this.background = new Background(this.resourceManager
                .getImage(ImageIdentifier.PLAY_BACKGROUND));
        this.tileMap = new TileMap(this.resourceManager, Game.TILE_SIZE);
        this.tileMap.loadTileSet("/Tilesets/grasstileset.gif");
        this.tileMap.loadMap("/Maps/level1.map");
        this.resourceManager.loadImage(ImageIdentifier.PLAYER_SPRITE_SHEET,
                "/Sprites/Player/playerSpriteSheet.png");
        this.player = new Player(this.tileMap, this.resourceManager);
        
        this.player.setPosition(2 * this.tileMap.getTileSize(),
                6 * this.tileMap.getTileSize());
    }

    @Override
    public void update(KeyboardState keyboardState, double frameTime) {
        if(this.player != null){
            this.player.update(keyboardState, frameTime);
            scrollTileMapCamera();
        }
        
    }
    
    private void scrollTileMapCamera(){
        if(this.tileMap != null){
            if(this.player.getX() > SCREEN_HALF_WIDTH
               && this.tileMap.getWidth() - this.player.getX() > SCREEN_HALF_WIDTH){
                int dx = this.player.getX() - SCREEN_HALF_WIDTH;
                this.tileMap.setCameraPositionX(dx);
            }
            
            if(this.player.getY() > SCREEN_HALF_HEIGHT
               && this.tileMap.getHeight() - this.player.getY() > SCREEN_HALF_HEIGHT){
                int dy = this.player.getY() - SCREEN_HALF_HEIGHT;
                this.tileMap.setCameraPositionY(dy);
            }            
        }
    }

    @Override
    public void onKeyPressed(int keyCode) {
    }

    @Override
    public void onKeyReleased(int keyCode) {
        if(keyCode == KeyEvent.VK_F){
            this.player.setCanFire(true);
        } else if(keyCode == KeyEvent.VK_R){
            this.player.setCanScratch(true);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if(this.background != null){
            this.background.draw(g);
        }
        
        if(this.tileMap != null){
            this.tileMap.draw(g);
        }
        
        if(this.player != null){
            this.player.draw(g);
        }
    }
    
}
