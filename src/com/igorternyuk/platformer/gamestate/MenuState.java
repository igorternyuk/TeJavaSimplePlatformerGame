package com.igorternyuk.platformer.gamestate;

import com.igorternyuk.platformer.gameplay.Game;
import com.igorternyuk.platformer.graphics.Background;
import java.awt.Graphics2D;
import com.igorternyuk.platformer.input.KeyboardState;
import com.igorternyuk.platformer.resourcemanager.ImageIdentifier;
import com.igorternyuk.platformer.resourcemanager.ResourceManager;
import com.igorternyuk.platformer.utils.Painter;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;

/**
 *
 * @author igor
 */
public class MenuState extends GameState{
    private static final Color COLOR_MENU_TITLE = Color.red.darker().darker();
    private static final Color COLOR_MENU_ITEM = Color.blue;
    private static final Color COLOR_CURRENT_CHOICE = Color.yellow;
    private static final Font FONT_MENU_TITLE = new Font("Verdana", Font.BOLD, 48);
    private static final Font FONT_MENU_ITEM = new Font("Tahoma", Font.PLAIN, 36);
    
    private Background background;
    private int currentChoice = 0;
    private String[] options;

    public MenuState(GameStateManager gsm, ResourceManager rm) {
        super(gsm, rm);
        this.background = new Background(rm.getImage(ImageIdentifier.MENU_BACKGROUND));
        this.background.setPosition(0, 0);
        this.background.setSpeed(0.1, 0);
        this.options = new String[]{"Play", "Help", "Quit"};
    }

    @Override
    public void init() {
    }

    @Override
    public void update(KeyboardState keyboardState, double frameTime) {
        this.background.update(keyboardState, frameTime);
    }
    
    private void correctIndex(){
        if(this.currentChoice < 0){
            this.currentChoice = this.options.length - 1;
        } else if(this.currentChoice >= this.options.length){
            this.currentChoice = 0;
        }
    }
    
    private void select(int index){
        switch(index){
            case 0:
                this.gameStateManager.setCurrentState(GameStateManager.LEVEL_STATE);
                break;
            case 1:
                break;
            case 2:
                this.gameStateManager.getGame().onWindowCloseRequest();
                break;
        }
    }

    @Override
    public void onKeyPressed(int keyCode) {
    }

    @Override
    public void onKeyReleased(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
                --this.currentChoice;
                break;
            case KeyEvent.VK_DOWN:
                ++this.currentChoice;
                break;
            case KeyEvent.VK_ENTER:
                select(this.currentChoice);
                break;
            default:
                break;
        }
        correctIndex();
    }

    @Override
    public void draw(Graphics2D g) {
        this.background.draw(g);
        Painter.drawCenteredString(g, "JPlatformer", FONT_MENU_TITLE,
                COLOR_MENU_TITLE, Game.HEIGHT / 4);
        for(int i = 0; i < this.options.length; ++i){
            Color color = (i == currentChoice)
                    ? COLOR_CURRENT_CHOICE
                    : COLOR_MENU_ITEM; 
            Painter.drawCenteredString(g, this.options[i], FONT_MENU_ITEM,
                color, Game.HEIGHT / 3 + (i + 1) * Game.HEIGHT / 12);
        }
    }    
}
