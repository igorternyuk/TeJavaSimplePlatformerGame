package com.igorternyuk.platformer.gameplay.entities.explosions;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author igor
 */
public enum ExplosionAnimationType {
    EXPLOSION(0,0,30,30,6,30,0.3);
    
    private List<Rectangle> frames = new ArrayList<>();

    private double animationSpeed;

    private ExplosionAnimationType(int firstFrameX, int firstFrameY, int frameWidth,
            int frameHeight, int frameCount, int frameStep,
            double animationSpeed) {
        for (int i = 0; i < frameCount; ++i) {
            Rectangle rect = new Rectangle(
                    firstFrameX + frameStep * i,
                    firstFrameY,
                    frameWidth,
                    frameHeight
            );
            this.frames.add(rect);
        }
        this.animationSpeed = animationSpeed;
    }

    public List<Rectangle> getFrames() {
        return this.frames;
    }

    public double getAnimationSpeed() {
        return this.animationSpeed;
    }
}
