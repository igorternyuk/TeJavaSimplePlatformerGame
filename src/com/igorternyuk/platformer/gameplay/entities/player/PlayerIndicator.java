package com.igorternyuk.platformer.gameplay.entities.player;

import com.igorternyuk.platformer.gamestate.LevelState;
import com.igorternyuk.platformer.graphics.images.Image;
import com.igorternyuk.platformer.input.KeyboardState;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author igor
 */
public class PlayerIndicator extends Image {

    private static final Font FONT = new Font("Tahoma", Font.ITALIC, 22);
    private Player player;
    private String healthText = "";
    private String fireText = "";

    public PlayerIndicator(BufferedImage image, Player player,
            double posOnTheScreenX, double posOnTheScreenY) {
        super(image, posOnTheScreenX, posOnTheScreenY, 0, 0);
        this.player = player;
    }

    @Override
    public void update(KeyboardState keyBoardState, double frameTime) {
        this.healthText = this.player.getHealth() + " / " + this.player.
                getMaxHealth();
        this.fireText = this.player.getNumFires() + " / " + this.player.
                getMaxFire();
    }

    @Override
    public void draw(Graphics2D g) {
        //g.drawImage(image, (int) this.x, (int) this.y, null);
        g.drawImage(this.image, (int) (this.x * LevelState.SCALE), (int) (this.y
                * LevelState.SCALE), (int) (this.image.getWidth()
                * LevelState.SCALE), (int) (this.image.getHeight()
                * LevelState.SCALE), null);
        g.setColor(Color.white);
        g.setFont(FONT);
        g.drawString(healthText, (int) (17 * LevelState.SCALE), (int) (13
                * LevelState.SCALE));
        g.drawString(fireText, (int) (17 * LevelState.SCALE), (int) (35
                * LevelState.SCALE));
    }
}
