package com.igorternyuk.platformer.graphics.animations;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author igor
 */
public class Animation {
    private double currentFrameTime;
    private int currentFrame = 0;
    private double speed;
    private int frameCount;
    private BufferedImage image;
    private List<Rectangle> frames;
    private List<Rectangle> flippedFrames;
    private boolean isPlaying = false;
    private boolean loopAnimation = true;
    private boolean flipped = false;
    
    public Animation(BufferedImage image, double speed,
            int x, int y, int width, int height,
            int frameCount, int frameStep){
        this.image = image;
        this.speed = speed;
        this.frameCount = frameCount;
        this.frames = new ArrayList<>(this.frameCount);
        this.flippedFrames = new ArrayList<>(this.frameCount);
    
        for(int i = 0; i < frameCount; ++i){
            Rectangle rect = new Rectangle(x + i * frameStep, y, width, height);
            this.frames.add(rect);
            Rectangle flippedRect = new Rectangle(x + i * frameStep + width, y,
                    -width, height);
            this.flippedFrames.add(flippedRect);
        }
    }
    
    public Animation(BufferedImage image, double speed, List<Rectangle> frames){
        this.image = image;
        this.speed = speed;
        this.frameCount = frames.size();
        this.frames = new ArrayList<>(this.frameCount);
        this.flippedFrames = new ArrayList<>(this.frameCount);
        this.frames.addAll(frames); 
        frames.stream().map((frame) -> new Rectangle(frame.x + frame.width,
                frame.y, -frame.width, frame.height)).forEachOrdered((flippedFrame) -> {
                    this.flippedFrames.add(flippedFrame);
        });
    }

    public boolean isFlipped() {
        return flipped;
    }

    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }
    
    public Rectangle getCurrentRect(){
        return this.flipped
                ? this.flippedFrames.get(this.currentFrame)
                : this.frames.get(this.currentFrame);
    }
    
    public int getCurrentFrameWidth(){
        return getCurrentRect().width;
    }
    
    public int getCurrentFrameHeight(){
        return getCurrentRect().height;
    }
    
    public void start(boolean loop){
        this.isPlaying = true;
        this.currentFrame = 0;
        this.currentFrameTime = 0;
        this.loopAnimation = loop;
    }
    
    public void stop(){
        this.isPlaying = false;
        this.currentFrame = 0;
        this.currentFrameTime = 0;
    }
    
    public void update(double frameTime){
        if(!this.isPlaying)
            return;
        this.currentFrameTime += frameTime;
        if(this.currentFrameTime >= speed){
            ++this.currentFrame;
            if(this.currentFrame >= this.frames.size()){
                this.currentFrame = 0;
                if(!this.loopAnimation){
                    this.isPlaying = false;
                }
            }
            this.currentFrameTime = 0;
        }
    }
    
    public void draw(Graphics2D g, int destX, int destY, double scaleX,
            double scaleY){
        Rectangle currentRect = this.frames.get(this.currentFrame);
        int dx1 = (int)(destX * scaleX);
        int dy1 = (int)(destY * scaleY);
        int dx2 = dx1 + (int)(currentRect.width * scaleX);
        int dy2 = dy1 + (int)(currentRect.height * scaleY);
        g.drawImage(this.image, dx1, dy1, dx2, dy2
                , currentRect.x, currentRect.y
                , currentRect.x + currentRect.width
                , currentRect.y + currentRect.height
                , null);
        
    }
}
