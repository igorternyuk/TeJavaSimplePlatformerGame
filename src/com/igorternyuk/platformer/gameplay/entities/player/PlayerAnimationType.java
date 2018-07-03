package com.igorternyuk.platformer.gameplay.entities.player;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author igor
 */
public enum PlayerAnimationType {
    IDLE(0, 1, 25, 30, 2, 30, 0.2),
    WALKING(0, 31, 25, 30, 8, 30, 0.25),
    JUMPING(0, 62, 20, 30, 1, 30, 0.25),
    FALLING(0, 94, 30, 30, 2, 30, 0.25),
    GLIDING(getGlidingFrames(), 0.25),
    FIREBALLING(getFireBallAttackFrames(), 0.1),
    SCRATCHING(getScratchingAttackFrames(), 0.1);

    private List<Rectangle> frames = new ArrayList<>();
    private int frameCount;
    private double speed;

    private PlayerAnimationType(int x, int y, int width, int height,
            int frameCount, int step, double speed) {

        for (int i = 0; i < frameCount; ++i) {
            Rectangle rect = new Rectangle(x + i * step, y, width, height);
            this.frames.add(rect);
        }

        this.frameCount = frameCount;
        this.speed = speed;
    }

    private PlayerAnimationType(List<Rectangle> frames, double speed) {
        this.frames.addAll(frames);
        this.frameCount = frames.size();
        this.speed = speed;
    }

    public int getFrameCount() {
        return this.frameCount;
    }

    public List<Rectangle> getFrames() {
        return this.frames;
    }

    public double getSpeed() {
        return this.speed;
    }

    private static List<Rectangle> getGlidingFrames() {
        List<Rectangle> scratchingFrames = new ArrayList<>();
        scratchingFrames.add(new Rectangle(0, 124, 24, 24));
        scratchingFrames.add(new Rectangle(29, 124, 25, 24));
        scratchingFrames.add(new Rectangle(58, 124, 26, 24));
        scratchingFrames.add(new Rectangle(89, 124, 25, 24));
        return scratchingFrames;
    }

    private static List<Rectangle> getFireBallAttackFrames() {
        List<Rectangle> scratchingFrames = new ArrayList<>();
        scratchingFrames.add(new Rectangle(0, 149, 26, 25));
        scratchingFrames.add(new Rectangle(30, 149, 28, 25));
        return scratchingFrames;
    }

    private static List<Rectangle> getScratchingAttackFrames() {
        List<Rectangle> scratchingFrames = new ArrayList<>();
        scratchingFrames.add(new Rectangle(0, 175, 33, 25));
        scratchingFrames.add(new Rectangle(34, 175, 34, 25));
        scratchingFrames.add(new Rectangle(79, 175, 28, 25));
        scratchingFrames.add(new Rectangle(112, 175, 44, 25));
        return scratchingFrames;
    }
}
