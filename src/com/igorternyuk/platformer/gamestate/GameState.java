package com.igorternyuk.platformer.gamestate;

import java.awt.Graphics2D;
import com.igorternyuk.platformer.input.KeyboardState;

/**
 *
 * @author igor
 */
public abstract class GameState {
    protected GameStateManager gameStateManager;
    public GameState(GameStateManager gsm){
        this.gameStateManager = gsm;
    }
    public abstract void init();
    public abstract void update(KeyboardState keyboardState);
    public abstract void onKeyPressed(int keyCode);
    public abstract void onKeyReleased(int keyCode);
    public abstract void draw(Graphics2D g);
    
}
