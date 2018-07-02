package com.igorternyuk.platformer.gameplay.entities.weapon;

import com.igorternyuk.platformer.gameplay.entities.Entity;
import com.igorternyuk.platformer.gameplay.tilemap.TileMap;
import com.igorternyuk.platformer.input.KeyboardState;
import java.awt.Graphics2D;

/**
 *
 * @author igor
 */
public class FireBall extends Entity{

    public FireBall(TileMap tileMap) {
        super(tileMap);
        loadAnimations();
    }
    
    private void loadAnimations(){
        
    }

    @Override
    public boolean isAlive() {
        return false;
    }

    @Override
    public void update(KeyboardState keyboardState, double frameTime) {
        
    }

    @Override
    public void draw(Graphics2D g) {
        
    }
    
}
