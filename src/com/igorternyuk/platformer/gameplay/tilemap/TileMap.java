package com.igorternyuk.platformer.gameplay.tilemap;

import com.igorternyuk.platformer.gameplay.Game;
import com.igorternyuk.platformer.resourcemanager.ImageIdentifier;
import com.igorternyuk.platformer.resourcemanager.ResourceManager;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author igor
 */
public class TileMap {
    
    private double x, y;
    private int xmin, xmax, ymin, ymax;
    
    private int[][] map;
    private int tileSize;
    private int numRows, numCols;
    private int width, height;
    
    private ResourceManager resourceManager;
    private BufferedImage tileSet;
    private int numTilesX, numTilesY;
    private Tile[][] tiles;
    
    private int rowOffset, colOffset;
    private int numRowsToDraw, numColsToDraw;
    
    public TileMap(ResourceManager resourceManager, int tileSize){
        this.tileSize = tileSize;
        this.resourceManager = resourceManager;
        this.numColsToDraw = Game.WIDTH / this.tileSize;
        this.numRowsToDraw = Game.HEIGHT / this.tileSize;
    }

    public int getX() {
        return (int)this.x;
    }

    public int getY() {
        return (int)this.y;
    }

    public int getTileSize() {
        return this.tileSize;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
    
    public TileType getTileType(int row, int col){
        int val = this.map[row][col];
        int r = val / this.numTilesX;
        int c = val % this.numTilesX;
        return this.tiles[r][c].getType();
    }
    
    private TileType getTileTypeForTheTileSet(int row, int col){
        if(row == 0)
            return TileType.REGULAR;
        else 
            return TileType.BLOCKED;
    }
    
    public void setPosition(double x, double y){
        this.x += (x - this.x) * 0.1;
        this.y += (y - this.y) * 0.1;
        fixBounds();
        
        this.colOffset = (int)-this.x / this.tileSize;
        this.rowOffset = (int)-this.y / this.tileSize;
        

    }
    
    public void fixBounds(){
        if(this.x < this.xmin) this.x = this.xmin;
        if(this.x > this.xmax) this.x = this.xmax;
        if(this.y < this.ymin) this.y = this.ymin;
        if(this.y > this.ymax) this.y = this.ymax;
    }
    
    
    
    public void loadMap(String pathToMapFile){
        InputStream in = this.getClass().getResourceAsStream(pathToMapFile);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        try {
            this.numCols = Integer.parseInt(bufferedReader.readLine());
            this.numRows = Integer.parseInt(bufferedReader.readLine());
            this.width = this.numCols * this.tileSize;
            this.height = this.numRows * this.tileSize;
            this.map = new int[this.numRows][this.numCols];
            System.out.println("numRows = " + numRows);
            System.out.println("numCols = " + numCols);
            String delimeter = "\\s+";
            for(int row = 0; row < this.numRows; ++row){
                String currentLine = bufferedReader.readLine();
                String[] values = currentLine.split(delimeter);
                for(int col = 0; col < this.numCols; ++col){
                    this.map[row][col] = Integer.parseInt(values[col]);
                }
            }
            
        } catch (IOException | NumberFormatException ex) {
            Logger.getLogger(TileMap.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void loadTileSet(String pathToTileSetFile){
        if(this.resourceManager.loadImage(ImageIdentifier.TILE_SET,
                pathToTileSetFile)){
            System.out.println("Tile set was successfully loaded");
        }
        this.tileSet = this.resourceManager.getImage(ImageIdentifier.TILE_SET);
        this.numTilesX = this.tileSet.getWidth() / this.tileSize;
        this.numTilesY = this.tileSet.getHeight() / this.tileSize;
        
        this.tiles = new Tile[this.numTilesY][this.numTilesX];
        
        for(int i = 0; i < this.numTilesY; ++i){
            for(int j = 0; j < this.numTilesX; ++j){
                BufferedImage image = this.tileSet.getSubimage(j * this.tileSize,
                        i * this.tileSize, this.tileSize, this.tileSize);
                Tile tile = new Tile(image, getTileTypeForTheTileSet(i, j));
                this.tiles[i][j] = tile;
            }
        }
    }
    
    public void draw(Graphics2D g){
        for(int row = this.rowOffset; row < this.rowOffset + this.numRowsToDraw; ++row){
            if(row >= this.numRows)
                break;
            for(int col = this.colOffset; col < this.colOffset + this.numColsToDraw; ++col){
                if(col >= this.numCols)
                    break;
                int val = this.map[row][col];
                int r = val / this.numTilesX;
                int c = val / this.numTilesX;
                BufferedImage image = this.tiles[r][c].getImage();
                g.drawImage(image, (int)x + col * this.tileSize,
                        (int)y + row * this.tileSize, null);
            }
        }
    }

    
    
}
