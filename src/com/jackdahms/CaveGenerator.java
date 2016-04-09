package com.jackdahms;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class CaveGenerator extends JPanel implements KeyListener{
		
	static int FRAME_WIDTH = 1000;
	static int FRAME_HEIGHT = 600;
	
	List<Cave> presets = new ArrayList<Cave>();
				
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
		Cave cave = new Cave(200, 150);
	}
	
	@Override
	public void paintComponent(Graphics g) {
//		g.drawImage(cave.image, 0, 0, 800, 600, null);
		
		g.setColor(Color.white);
		g.fillRect(800, 0, 200, 600);
		
		g.setColor(Color.black);
	}
	
	void generate() {
//		for (int i = 0; i < smoothingIterations; i++) cave.simpleSmoothMap(() -> {
//			for (int r = 0; r < height; r++) {
//				for (int c = 0; c < width; c++) {
//					if (cave.countSurroundingWalls(r, c) < 4) cave.map[r][c] = 0;
//					else if (cave.countSurroundingWalls(r, c) > 4) cave.map[r][c] = 1;
//				}
//			}
//			return cave.map;
//		});
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
