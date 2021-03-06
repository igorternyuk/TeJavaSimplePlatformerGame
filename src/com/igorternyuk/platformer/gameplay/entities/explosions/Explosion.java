package com.igorternyuk.platformer.gameplay.entities.explosions;

import com.igorternyuk.platformer.gameplay.entities.Entity;
import com.igorternyuk.platformer.gameplay.entities.EntityType;
import com.igorternyuk.platformer.gamestate.LevelState;
import com.igorternyuk.platformer.graphics.animations.Animation;
import com.igorternyuk.platformer.graphics.animations.AnimationManager;
import com.igorternyuk.platformer.graphics.animations.AnimationPlayMode;
import com.igorternyuk.platformer.input.KeyboardState;
import com.igorternyuk.platformer.resourcemanager.ImageIdentifier;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author igor
 */
public class Explosion extends Entity {

    private AnimationManager<ExplosionAnimationType> animationManager =
            new AnimationManager<>();

    public Explosion(LevelState levelState, double x, double y) {
        super(levelState, EntityType.EXPLOSION);
        this.x = x;
        this.y = y;
        loadAnimations();
        this.animationManager.setCurrentAnimation(
                ExplosionAnimationType.EXPLOSION);
        this.animationManager.getCurrentAnimation().
                start(AnimationPlayMode.ONCE);
    }

    private void loadAnimations() {
        BufferedImage spriteSheet = this.level.getResourceManager().getImage(
                ImageIdentifier.EXPLOSION);
        for (ExplosionAnimationType animType : ExplosionAnimationType.values()) {
            this.animationManager.addAnimation(animType, new Animation(
                    spriteSheet, animType.getAnimationSpeed(), animType.
                    getFrames()));
        }
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
        super.update(keyboardState, frameTime);
        this.animationManager.update(frameTime);
        if (this.animationManager.getCurrentAnimation().hasBeenPlayedOnce()) {
            this.health = 0;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if (!this.needDraw) {
            return;
        }
        this.animationManager.draw(g, (int) this.x, (int) this.y,
                LevelState.SCALE, LevelState.SCALE);
    }

}
