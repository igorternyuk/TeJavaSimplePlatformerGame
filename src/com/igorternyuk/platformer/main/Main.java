package com.igorternyuk.platformer.main;

import com.igorternyuk.platformer.gameplay.Game;
import javax.swing.JOptionPane;

/**
 *
 * @author igor
 */
public class Main {
    private static void startGame(){
        try {
            Game game = new Game();
            game.start();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Could not to start the game"
                    + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        startGame();
    }
}
