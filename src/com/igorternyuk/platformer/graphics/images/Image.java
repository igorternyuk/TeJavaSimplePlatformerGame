package com.igorternyuk.platformer.graphics.images;

import com.igorternyuk.platformer.gameplay.Game;
import com.igorternyuk.platformer.input.KeyboardState;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author igor
 */
public class Image {

    protected BufferedImage image;
    protected double x;
    protected double y;
    protected double dx;
    protected double dy;

    public Image(BufferedImage image, double x, double y, double dx,
            double dy) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }

    public Image(BufferedImage image) {
        this(image, 0, 0, 0, 0);
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setSpeed(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void update(KeyboardState keyBoardState, double frameTime) {
        this.x += this.dx;
        this.y += this.dy;
    }

    public void draw(Graphics2D g) {
        g.drawImage(image, (int) this.x, (int) this.y, null);
    }

}
