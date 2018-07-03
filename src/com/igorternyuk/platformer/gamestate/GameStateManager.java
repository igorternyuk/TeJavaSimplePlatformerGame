package com.igorternyuk.platformer.gamestate;

import com.igorternyuk.platformer.gameplay.Game;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import com.igorternyuk.platformer.input.KeyboardState;
import com.igorternyuk.platformer.resourcemanager.ResourceManager;
import java.util.Stack;

/**
 *
 * @author igor
 */
public class GameStateManager {

    private Game game;
    private Stack<GameState> gameStates;

    public GameStateManager(Game game, ResourceManager rm) {
        this.game = game;
        this.gameStates = new Stack<>();
        this.gameStates.push(new LevelState(this, rm));
        this.gameStates.push(new MenuState(this, rm));
        this.gameStates.peek().load();
    }

    public Game getGame() {
        return this.game;
    }

    public void nextState() {
        if (this.gameStates.size() >= 2) {
            GameState currentState = this.gameStates.pop();
            currentState.unload();
            this.gameStates.peek().load();
        }
    }

    public void unloadAllGameStates() {
        while (!this.gameStates.empty()) {
            GameState currentState = this.gameStates.pop();
            currentState.unload();
        }
    }

    public void onKeyPressed(int keyCode) {
        this.gameStates.peek().onKeyPressed(keyCode);
    }

    public void onKeyReleased(int keyCode) {
        this.gameStates.peek().onKeyReleased(keyCode);
    }

    public void update(KeyboardState keyboardState, double frameTime) {
        this.gameStates.peek().update(keyboardState, frameTime);
    }

    public void draw(Graphics2D g) {
        this.gameStates.peek().draw(g);
    }
}
