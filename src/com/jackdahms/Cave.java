package com.jackdahms;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Cave {
	
	int width, height;
	
	//in countSurroundingWalls, whether outside of the map should be treated as walls or space
	boolean treatEdgesAsWalls = true; 
	
	boolean setSeed = false;
	long seed = 0;
	
	int[][] map; 
	
	BufferedImage image;
	
	/**
	 * Create a new cave.
	 * @param width		width of cave in pixels
	 * @param height	height of cave in pixels
	 */
	public Cave(int width, int height) {
		this.width = width;
		this.height = height;
		
		map = new int[height][width]; //[rows][columns]
	}
	
	/**
	 * Set seed for cave generation.
	 * @param seed
	 */
	public void setSeed(long seed) {
		setSeed = true; //flag needed so they can set seed = 0 if they want
		this.seed = seed;
	}
	
	/**
	 * Fill each index in the cave randomly based on seed
	 * @param fillPercent Between 0.0 and 1.0, the approximate percentage of walls in the cave,
	 */
	public void fillMap(float fillPercent) {
		Random ayn; //ayn rand!
		if (setSeed) ayn = new Random(seed);
		else ayn = new Random();
		
		for (int r = 0; r < height; r++) {
			for (int c = 0; c < width; c++) {
				if (ayn.nextFloat() < fillPercent) map[r][c] = 1;
				else map[r][c] = 0; //need an else so map can be regenerated w/o new cave object
			}
		}
	}
	
	//cave formations seem to point in combined vectors of row and column
	public void smooth(Generatable g) {
		map = g.generate();
	}
	
	public void mapToImage() {
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g = image.createGraphics();
		for (int r = 0; r < height; r++) {
			for (int c = 0; c < width; c++) {
				if (map[r][c] == 1) g.setColor(Color.black);
				else g.setColor(Color.white);
				g.fillRect(c, r, 1, 1);
			}
		}
		g.dispose();
	}
	
	public int countSurroundingWalls(int r, int c) {
		int count = 0;
		for (int i = r - 1; i < r + 2; i++) {
			for (int k = c - 1; k < c + 2; k++) {
				if (i != r || k != c) { //true for surrounding tiles but not the tile itself
					if (treatEdgesAsWalls) {
						if (i > -1 && i < map.length && k > -1 && k < map[0].length) { //true if tile is inside map
							if (map[i][k] == 1) count++;
						} else {
							count++; //count invalid tiles as walls
						}
					} else {
						try {
							if (map[i][k] == 1) count++;
						} catch (Exception e) {} //if not treating edge as walls, don't count edges 
					}
				}
			}
		}
		return count;
	}
	
	/**
	 * Fills the border of the map to make the edges walls
	 * @param width number of cells wide to make the border
	 */
	public void fillBorder(int width) {
		//top wall
		for (int r = 0; r < width; r++) {
			for (int c = 0; c < this.width; c++) {
				map[r][c] = 1;
			}
		}
		
		//left wall
		for (int r = 0; r < height; r++) {
			for (int c = 0; c < width; c++) {
				map[r][c] = 1;
			}
		}
		
		//right wall
		for (int r = 0; r < height; r++) {
			for (int c = this.width - width; c < this.width; c++) {
				map[r][c] = 1;
			}
		}
		
		//bottom wall
		for (int r = height - width; r < height; r++) {
			for (int c = 0; c < this.width; c++) {
				map[r][c] = 1;
			}
		}
	}

}
