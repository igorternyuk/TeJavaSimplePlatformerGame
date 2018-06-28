package com.igorternyuk.platformer.graphics.animations;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author igor
 * @param <Identifier> Animation identifier
 */
public class AnimationManager<Identifier> {
    private Map<Identifier, Animation> animations = new HashMap<>();
    private Identifier currentAnimation;
    
    public void set(Identifier identifier){
        if(this.animations.containsKey(identifier)){
            this.currentAnimation = identifier;
        }
    }
    
    public void add(Identifier identifier, Animation animation){
        this.animations.put(identifier, animation);
    }
    
    public void remove(Identifier identifier){
        this.animations.remove(identifier);
    }
    
    public void update(double frameTime){
        this.animations.get(this.currentAnimation).update(frameTime);
    }
    
    public void draw(Graphics2D g, int destX, int destY, double scaleX,
            double scaleY){
        this.animations.get(this.currentAnimation).draw(g, destX, destY,
                scaleX, scaleY);
    }
}
