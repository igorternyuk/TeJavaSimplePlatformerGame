package com.igorternyuk.platformer.gameplay;

import com.igorternyuk.platformer.input.KeyboardState;
import com.igorternyuk.platformer.graphics.Display;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.igorternyuk.platformer.gamestate.GameStateManager;
import com.igorternyuk.platformer.resourcemanager.ResourceManager;
import com.igorternyuk.platformer.utils.Time;

/**
 *
 * @author igor
 */
public class Game implements Runnable {

    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;
    public static final int TILE_SIZE = 30;
    private static final String TITLE = "JPlatformer";
    private static final int CLEAR_COLOR = 0xff000000;
    private static final int NUM_BUFFERS = 4;
    private static final float FPS = 60.0f;
    private static final float FRAME_TIME = Time.SECOND / FPS;
    private static final float FRAME_TIME_IN_SECONDS = 1 / FPS;
    private static final long IDLE_TIME = 1;

    private boolean running = false;
    private Thread gameThread;
    private Display display;
    private Graphics2D graphics;
    private KeyboardState keyboardState;
    private ResourceManager resourceManager;
    private GameStateManager gameStateManager;

    public Game() {
        this.display = Display.create(WIDTH, HEIGHT, TITLE, NUM_BUFFERS,
                CLEAR_COLOR);
        this.graphics = this.display.getGraphics();
        this.keyboardState = new KeyboardState();
        this.display.addInputListener(this.keyboardState);
        this.display.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                gameStateManager.onKeyPressed(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                gameStateManager.onKeyReleased(e.getKeyCode());
            }
        });
        this.display.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onWindowCloseRequest();
            }
        });
        this.resourceManager = new ResourceManager(); //TODO Should be singleton
        this.gameStateManager = new GameStateManager(this, this.resourceManager);
    }

    public void onWindowCloseRequest() {
        stop();
        System.exit(0);
    }

    public synchronized void start() {
        if (this.running) {
            return;
        }
        this.running = true;
        this.gameThread = new Thread(this);
        this.gameThread.start();
    }

    public synchronized void stop() {
        if (!this.running) {
            return;
        }
        this.running = false;
        try {
            this.gameThread.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        cleanUp();
    }

    public void update() {
        this.gameStateManager.update(this.keyboardState, FRAME_TIME_IN_SECONDS);
    }

    public void render() {
        this.display.clear();
        this.gameStateManager.draw(graphics);
        this.display.swapBuffers();
    }

    @Override
    public void run() {
        int fps = 0;
        int updates = 0;
        int auxillaryUpdates = 0;
        long auxillaryTimer = 0;
        long timeSinceLastUpdate = 0;
        long lastTime = Time.get();
        System.out.println("FrameTime = " + FRAME_TIME);

        while (this.running) {
            long currentTime = Time.get();
            long elapsedTime = currentTime - lastTime;
            lastTime = currentTime;
            timeSinceLastUpdate += elapsedTime;
            auxillaryTimer += elapsedTime;
            boolean needToRender = false;
            while (timeSinceLastUpdate > FRAME_TIME) {
                timeSinceLastUpdate -= FRAME_TIME;
                update();
                ++updates;
                if (needToRender) {
                    ++auxillaryUpdates;
                } else {
                    needToRender = true;
                }
            }

            if (needToRender) {
                render();
                ++fps;
            } else {
                try {
                    Thread.sleep(IDLE_TIME);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Game.class.getName()).log(Level.SEVERE,
                            null, ex);
                }
            }

            if (auxillaryTimer >= Time.SECOND) {
                this.display.setTitle(TITLE + " || FPS: " + fps + " | Upd: "
                        + updates + " | Updl: " + auxillaryUpdates);
                fps = 0;
                updates = 0;
                auxillaryUpdates = 0;
                auxillaryTimer = 0;
            }
        }
    }

    public void cleanUp() {
        this.gameStateManager.unloadAllGameStates();
        this.display.destroy();
    }
}
