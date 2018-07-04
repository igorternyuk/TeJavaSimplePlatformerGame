package com.igorternyuk.platformer.gameplay.entities.explosions;

import com.igorternyuk.platformer.gameplay.entities.Entity;
import com.igorternyuk.platformer.gamestate.LevelState;
import com.igorternyuk.platformer.graphics.animations.AnimationManager;
import com.igorternyuk.platformer.input.KeyboardState;
import java.awt.Graphics2D;

/**
 *
 * @author igor
 */
public class Explosion extends Entity {

    private AnimationManager<ExplosionAnimationType> animationManager =
            new AnimationManager<>();

    public Explosion(LevelState levelState, double x, double y) {
        super(levelState);
        this.x = x;
        this.y = y;
    }
    
    private void loadAnimations(){
        
    }

    @Override
    public int getWidth() {
        return this.animationManager.getCurrentAnimation().
                getCurrentFrameWidth();
    }

    @Override
    public int getHeight() {
        return this.animationManager.getCurrentAnimation().
                getCurrentFrameHeight();
    }

    @Override
    public void update(KeyboardState keyboardState, double frameTime) {
        this.animationManager.update(frameTime);
    }

    @Override
    public void draw(Graphics2D g) {
        this.animationManager.draw(g, (int) this.x, (int) this.y,
                LevelState.SCALE, LevelState.SCALE);
    }

}
