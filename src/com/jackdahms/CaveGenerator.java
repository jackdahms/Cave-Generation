package com.jackdahms;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class CaveGenerator extends JPanel{
	
	static int width = 800;
	static int height = 600;
	
	Cave cave;
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Cave Generation");
		frame.setSize(width, height);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(new CaveGenerator());
		
		frame.setVisible(true);
	}
	
	public CaveGenerator() {
		cave = new Cave(width, height);
		cave.fillMap(0.5f);
		for (int i = 0; i < 5; i++) cave.simpleSmoothMap();
//		cave.simpleSmoothMap();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		for (int r = 0; r < height; r++) {
			for (int c = 0; c < width; c++) {
				Color col;
				if (cave.map[r][c] == 1) col = Color.black;
				else col = Color.white;
				g.setColor(col);
				g.fillRect(c, r, 1, 1);
			}
		}
	}

}
