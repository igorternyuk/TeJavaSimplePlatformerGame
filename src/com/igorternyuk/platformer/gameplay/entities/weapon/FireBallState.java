package com.igorternyuk.platformer.gameplay.entities.weapon;

/**
 *
 * @author igor
 */
public enum FireBallState {
    FLYING,
    DESTROYING;
}

/*
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

        private PlayerAction(int x, int y, int width, int height, 
                int frameCount, int step, double speed) {

            for(int i = 0; i < frameCount; ++i){
                Rectangle rect = new Rectangle(x + i * step, y, width, height);
                this.frames.add(rect);
            }
            
            this.frameCount = frameCount;
            this.speed = speed;
        }
*/