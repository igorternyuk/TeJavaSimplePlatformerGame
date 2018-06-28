package com.igorternyuk.platformer.gamestate;

import com.igorternyuk.platformer.gameplay.Game;
import com.igorternyuk.platformer.gameplay.tilemap.TileMap;
import com.igorternyuk.platformer.graphics.Background;
import java.awt.Graphics2D;
import com.igorternyuk.platformer.input.KeyboardState;
import com.igorternyuk.platformer.resourcemanager.ImageIdentifier;
import com.igorternyuk.platformer.resourcemanager.ResourceManager;

/**
 *
 * @author igor
 */
public class LevelState extends GameState{
    private TileMap tileMap;
    private Background background;
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
        this.tileMap.setPosition(0, 0);
        this.resourceManager.loadImage(ImageIdentifier.PLAYER_SPRITE_SHEET,
                "/Sprites/Player/playerSpriteSheet.png");
    }

    @Override
    public void update(KeyboardState keyboardState) {
    }

    @Override
    public void onKeyPressed(int keyCode) {
    }

    @Override
    public void onKeyReleased(int keyCode) {
    }

    @Override
    public void draw(Graphics2D g) {
        if(this.background != null){
            this.background.draw(g);
        }
        
        if(this.tileMap != null){
            this.tileMap.draw(g);
        }
    }
    
}
