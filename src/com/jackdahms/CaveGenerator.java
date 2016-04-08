package com.jackdahms;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class CaveGenerator extends JPanel implements KeyListener{
	
	static int FRAME_WIDTH = 1000;
	static int FRAME_HEIGHT = 600;
	
	int width = 200;
	int height = 150;
	int cellSize = 4;
	int borderWidth = 4;
	int smoothingIterations = 5;
	
	Cave cave;
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JFrame frame = new JFrame("Cave Generation");
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT + 30);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(new CaveGenerator());
		
		frame.setVisible(true);
	}
	
	public CaveGenerator() {
		cave = new Cave(width, height);
		generate();
	}
	
	//TODO make cave a buffered image
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
		
		g.setColor(Color.white);
		g.fillRect(800, 0, 200, 600);
	}
	
	void generate() {
		cave.fillMap(0.5f);
		cave.fillBorder(borderWidth);
		for (int i = 0; i < smoothingIterations; i++) cave.simpleSmoothMap();
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) generate();
	}

}
