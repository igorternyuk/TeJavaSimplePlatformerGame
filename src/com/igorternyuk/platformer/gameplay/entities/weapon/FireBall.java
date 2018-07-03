package com.igorternyuk.platformer.gameplay.entities.weapon;

import com.igorternyuk.platformer.gameplay.entities.Entity;
import com.igorternyuk.platformer.gamestate.LevelState;
import com.igorternyuk.platformer.input.KeyboardState;
import java.awt.Graphics2D;

/**
 *
 * @author igor
 */
public class FireBall extends Entity {

    public FireBall(LevelState level) {
        super(level);
        loadAnimations();
    }

    private void loadAnimations() {

    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public boolean isAlive() {
        return false;
    }

    @Override
    public void update(KeyboardState keyboardState, double frameTime) {

    }

    @Override
    public void draw(Graphics2D g) {

    }

}
