package com.igorternyuk.platformer.gameplay.entities.player;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author igor
 */
public enum PlayerState {
        IDLE(0,1,55,25,2,30),
        WALKING(0,32,235,30,8,30),
        JUMPING(0,62,20,30,1,30),
        FALLING(0,94,60,30,2,30),
        GLIDING(getGlidingFrames()),
        FIREBALLING(getFireBallAttackFrames()),
        SCRATCHING(getScratchingAttackFrames());
        
        private int frameCount;
        private List<Rectangle> frames = new ArrayList<>();

        private PlayerState(int x, int y, int width, int height, 
                int frameCount, int step) {
            this.frameCount = frameCount;
           
            for(int i = 0; i < frameCount; ++i){
                Rectangle rect = new Rectangle(x + i * step, y, width, height);
                this.frames.add(rect);
            }
        }
        
        private PlayerState(List<Rectangle> frames){
            this.frameCount = frames.size();
            this.frames.addAll(frames);
        }

        public int getFrameCount() {
            return this.frameCount;
        }
        
        public List<Rectangle> getFrames(){
            return this.frames;
        }
        
        private static List<Rectangle> getGlidingFrames(){
            List<Rectangle> scratchingFrames = new ArrayList<>();
            scratchingFrames.add(new Rectangle(0,124, 24, 24));
            scratchingFrames.add(new Rectangle(29,124, 25, 24));
            scratchingFrames.add(new Rectangle(58,124, 26, 24));
            scratchingFrames.add(new Rectangle(89,124, 25, 24));
            return scratchingFrames;
        }
        
        private static List<Rectangle> getFireBallAttackFrames(){
            List<Rectangle> scratchingFrames = new ArrayList<>();
            scratchingFrames.add(new Rectangle(0,149, 26, 25));
            scratchingFrames.add(new Rectangle(30,149, 28, 25));
            return scratchingFrames;
        }
        
        private static List<Rectangle> getScratchingAttackFrames(){
            List<Rectangle> scratchingFrames = new ArrayList<>();
            scratchingFrames.add(new Rectangle(0,175, 33, 25));
            scratchingFrames.add(new Rectangle(34,175, 34, 25));
            scratchingFrames.add(new Rectangle(79,175, 28, 25));
            scratchingFrames.add(new Rectangle(112,175, 44, 25));
            return scratchingFrames;
        }
    }
