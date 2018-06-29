package com.igorternyuk.platformer.gamestate;

import java.awt.Graphics2D;
import com.igorternyuk.platformer.input.KeyboardState;
import com.igorternyuk.platformer.resourcemanager.ResourceManager;

/**
 *
 * @author igor
 */
public abstract class GameState {
    protected GameStateManager gameStateManager;
    protected ResourceManager resourceManager;
    public GameState(GameStateManager gsm, ResourceManager rm){
        this.gameStateManager = gsm;
        this.resourceManager = rm;
    }
    public abstract void init();
    public abstract void update(KeyboardState keyboardState, double frameTime);
    public abstract void onKeyPressed(int keyCode);
    public abstract void onKeyReleased(int keyCode);
    public abstract void draw(Graphics2D g);
    
}
