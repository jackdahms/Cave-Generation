package com.jackdahms;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class CaveGenerator extends JPanel implements KeyListener{
		
	static int FRAME_WIDTH = 1000;
	static int FRAME_HEIGHT = 600;
	
	List<Cave> presets = new ArrayList<Cave>();
	
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
		cave = new Cave(200, 150);
		generate();
		
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		
		int fillDensity = 48;

		JTextField fillDensityField = new JTextField();
		JLabel fillDensityLabel = new JLabel("Initial Fill Density");
		JSlider fillDensitySlider = new JSlider();
		
		JButton regenerateButton = new JButton("Regenerate");
		
		fillDensityField.setText("" + fillDensity);
		fillDensityField.setAlignmentX(JTextField.RIGHT_ALIGNMENT);
		fillDensityField.setColumns(3);
		fillDensityField.addCaretListener((CaretEvent e) -> {
			try {
				fillDensitySlider.setValue(Integer.parseInt(fillDensityField.getText()));
			} catch (Exception e1) {
//				cannot mutate in notification
//				fillDensityField.setText("" + fillDensitySlider);
				//they didn't enter numbers
			}
		});
		layout.putConstraint(SpringLayout.NORTH, fillDensityField, 10, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.EAST, fillDensityField, -10, SpringLayout.EAST, this);
		add(fillDensityField);
		
		layout.putConstraint(SpringLayout.NORTH, fillDensityLabel, 10, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.EAST, fillDensityLabel, -20, SpringLayout.WEST, fillDensityField);
		add(fillDensityLabel);
		
		fillDensitySlider.setValue(fillDensity);
		fillDensitySlider.addChangeListener((ChangeEvent e) -> {
			fillDensityField.setText("" + fillDensitySlider.getValue());
		});
//		fillDensitySlider.setMaximumSize(new Dimension(50, fillDensitySlider.getBounds().height));
		fillDensitySlider.setBackground(Color.white);
		fillDensitySlider.setMaximum(100);
		fillDensitySlider.setMinimum(0);
		layout.putConstraint(SpringLayout.NORTH, fillDensitySlider, 5, SpringLayout.SOUTH, fillDensityLabel);
		layout.putConstraint(SpringLayout.EAST, fillDensitySlider, -5, SpringLayout.EAST, this);
		add(fillDensitySlider);
		
		regenerateButton.addActionListener((ActionEvent e) -> {
			System.out.println("hey");
			generate();
		});
		layout.putConstraint(SpringLayout.SOUTH, regenerateButton, -5, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.EAST, regenerateButton, -5, SpringLayout.EAST, this);
		add(regenerateButton);
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		int borderBuffer = 2;
		
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth() - 200 + borderBuffer, getHeight());

		g.drawImage(cave.image, 0, 0, 800, 600, null);
		
		g.fillRect(getWidth() - 200 + borderBuffer, 0, 200 + borderBuffer, getHeight());
		
		g.setColor(Color.black);
		g.drawLine(getWidth() - 200 + borderBuffer, 0, getWidth() - 200 + borderBuffer, getHeight());
	}
	
	void generate() {
		int width = 200;
		int height = 150;
		int smoothingIterations = 5;
		cave.fillMap(0.48f);
		cave.fillBorder(4);
		for (int i = 0; i < smoothingIterations; i++) cave.simpleSmoothMap(() -> {
			for (int r = 0; r < height; r++) {
				for (int c = 0; c < width; c++) {
					if (cave.countSurroundingWalls(r, c) < 4) cave.map[r][c] = 0;
					else if (cave.countSurroundingWalls(r, c) > 4) cave.map[r][c] = 1;
				}
			}
			return cave.map;
		});
		cave.mapToImage();
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
