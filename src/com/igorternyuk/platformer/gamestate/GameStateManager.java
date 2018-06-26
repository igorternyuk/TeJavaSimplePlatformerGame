package com.igorternyuk.platformer.gamestate;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import com.igorternyuk.platformer.input.KeyboardState;
import com.igorternyuk.platformer.resourcemanager.ResourceManager;

/**
 *
 * @author igor
 */
public class GameStateManager {
    private static final int MENU_STATE = 0;
    private static final int LEVEL_STATE = 1;
    private ResourceManager resourceManger;
    private List<GameState> gameStates;
    private int currentState;
    
    public GameStateManager(ResourceManager rm){
        this.gameStates = new ArrayList<>();
        this.resourceManger = rm;
        this.gameStates.add(new MenuState(this, this.resourceManger));
        this.currentState = MENU_STATE;
    }
    
    public void setCurrentState(int index){
        this.currentState = index;
        this.gameStates.get(index).init();
    }
    
    public void update(KeyboardState keyboardState){
        this.gameStates.get(currentState).update(keyboardState);
    }
    
    public void onKeyPressed(int keyCode){
        this.gameStates.get(currentState).onKeyPressed(keyCode);
    }
    
    public void onKeyReleased(int keyCode){
        this.gameStates.get(currentState).onKeyReleased(keyCode);
    }
    
    public void draw(Graphics2D g){
        this.gameStates.get(currentState).draw(g);
    }
}
