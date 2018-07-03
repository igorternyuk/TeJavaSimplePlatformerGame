package com.igorternyuk.platformer.gameplay.entities.weapon;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author igor
 */
public enum FireBallAnimation {
    FLYING(7, 7, 15, 15, 4, 30, 0.1),
    DESTROYING(6, 34, 20, 20, 3, 30, 0.5);

    private List<Rectangle> frames = new ArrayList<>();

    private double animationSpeed;

    private FireBallAnimation(int firstFrameX, int firstFrameY, int frameWidth,
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
