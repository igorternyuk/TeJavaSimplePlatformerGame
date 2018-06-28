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
    private BufferedImage image;
    private List<Rectangle> frames = new ArrayList<>();
    private boolean isPlaying = false;
    private boolean loopAnimation = true;
    
    public Animation(BufferedImage image, double speed,
            int x, int y, int width, int height,
            int frameCount, int frameStep){
        this.image = image;
        this.speed = speed;
        
        for(int i = 0; i < frameCount; ++i){
            Rectangle rect = new Rectangle(x + i * frameStep, y, width, height);
            this.frames.add(rect);
        }
    }
    
    public Animation(BufferedImage image, double speed, List<Rectangle> frames){
        this.image = image;
        this.speed = speed;
        this.frames.addAll(frames);        
    }
    
    public Rectangle getCurrentRect(){
        return this.frames.get(this.currentFrame);
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
        g.drawImage(this.image, destX, destY, (int)(currentRect.width * scaleX),
                (int)(currentRect.height * scaleY), currentRect.x,
                currentRect.y, currentRect.width, currentRect.height, null);
        
    }
}