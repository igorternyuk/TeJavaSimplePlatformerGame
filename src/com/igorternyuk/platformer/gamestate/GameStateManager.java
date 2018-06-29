package com.igorternyuk.platformer.gamestate;

import com.igorternyuk.platformer.gameplay.Game;
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
    public static final int MENU_STATE = 0;
    public static final int LEVEL_STATE = 1;
    private Game game;
    private ResourceManager resourceManger;
    private List<GameState> gameStates;
    private int currentState;
    
    public GameStateManager(Game game, ResourceManager rm){
        this.game = game;
        this.gameStates = new ArrayList<>();
        this.resourceManger = rm;
        this.gameStates.add(new MenuState(this, rm));
        this.gameStates.add(new LevelState(this, rm));
        this.currentState = MENU_STATE;
    }
    
    public Game getGame(){
        return this.game;
    }
    
    public void setCurrentState(int index){
        this.currentState = index;
        this.gameStates.get(index).init();
    }
    
    public void update(KeyboardState keyboardState, double frameTime){
        this.gameStates.get(currentState).update(keyboardState, frameTime);
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
