package com.jackdahms;

import java.util.Random;

public class Cave {
	
	int width, height;
	
	boolean setSeed = false;
	long seed = 0;
	
	int[][] map; 
	
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
			}
		}
	}
	
	public void simpleSmoothMap() {
		for (int r = 0; r < height; r++) {
			for (int c = 0; c < width; c++) {
				
			}
		}
	}

}
