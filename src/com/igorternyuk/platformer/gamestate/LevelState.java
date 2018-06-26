package com.igorternyuk.platformer.gamestate;

import java.awt.Graphics2D;
import com.igorternyuk.platformer.input.KeyboardState;
import com.igorternyuk.platformer.resourcemanager.ResourceManager;

/**
 *
 * @author igor
 */
public class LevelState extends GameState{

    public LevelState(GameStateManager gsm, ResourceManager rm) {
        super(gsm, rm);
    }

    @Override
    public void init() {
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
    }
    
}
