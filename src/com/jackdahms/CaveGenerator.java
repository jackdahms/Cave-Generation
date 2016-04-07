package com.jackdahms;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class CaveGenerator extends JPanel implements KeyListener{
	
	static int width = 800;
	static int height = 600;
	
	int cellSize = 4;
	int borderWidth = 5;
	int smoothingIterations = 5;
	
	Cave cave;
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Cave Generation");
		frame.setSize(width, height + 30);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(new CaveGenerator());
		
		frame.setVisible(true);
	}
	
	public CaveGenerator() {
		cave = new Cave(width / cellSize, height / cellSize);
		generate();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		for (int r = 0; r < cave.height; r++) {
			for (int c = 0; c < cave.width; c++) {
				Color col;
				if (cave.map[r][c] == 1) col = Color.black;
				else col = Color.white;
				g.setColor(col);
				g.fillRect(c * cellSize, r * cellSize, cellSize, cellSize);
			}
		}
	}
	
	void generate() {
		cave.fillMap(0.5f);
		cave.fillBorder(borderWidth);
		for (int i = 0; i < smoothingIterations; i++) cave.simpleSmoothMap();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) generate();
	}

}
