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
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
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
	
	int width = 200;
	int height = 150;
	float fillDensity = 0.48f;
				
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
		
		createGUI();
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
		int smoothingIterations = 5;
		cave.fillMap(fillDensity);
		cave.fillBorder(2);
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
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) generate();
	}
	
	private void createGUI() {
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		
		int defaultWidth = width;
		int defaultHeight = height;
		int defaultFillDensity = (int) (fillDensity * 1000);
		
		JLabel widthLabel = new JLabel("Width");
		JLabel heightLabel = new JLabel("Height");
		JSpinner widthSpinner = new JSpinner();
		JSpinner heightSpinner = new JSpinner();
		JSlider widthSlider = new JSlider();
		JSlider heightSlider = new JSlider();
		
		JLabel randomSeedLabel = new JLabel("Use Random Seed");
		JLabel seedLabel = new JLabel("Seed");
		JCheckBox seedCheckBox = new JCheckBox();
		JTextField seedField = new JTextField();
		
		JSpinner fillDensitySpinner = new JSpinner();
		JLabel fillDensityLabel = new JLabel("Fill Density");
		JSlider fillDensitySlider = new JSlider();
		
		JSpinner borderSpinner = new JSpinner();
		JLabel borderLabel = new JLabel("Border Width");
		
		JButton fillDensityButton = new JButton("Fill New Cave");
		
		JButton regenerateButton = new JButton("Generate New Cave");
		
		SpinnerNumberModel numberModel = new SpinnerNumberModel();
		numberModel.setMinimum(10);
		numberModel.setMaximum(10000);
		widthSpinner.setModel(numberModel);
		layout.putConstraint(SpringLayout.NORTH, widthSpinner, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.EAST, widthSpinner, -5, SpringLayout.EAST, this);
		add(widthSpinner);
		
		layout.putConstraint(SpringLayout.NORTH, widthLabel, 1, SpringLayout.NORTH, widthSpinner);
		layout.putConstraint(SpringLayout.WEST, widthLabel, -195, SpringLayout.EAST, this);
		add(widthLabel);
		
		widthSlider.setPreferredSize(new Dimension(190, 20));
		widthSlider.setBackground(Color.white);
		widthSlider.setMinimum(0);
		widthSlider.setMaximum((int)((SpinnerNumberModel) widthSpinner.getModel()).getMaximum());
		widthSlider.setValue(defaultWidth);
		widthSlider.addChangeListener((ChangeEvent e) -> widthSpinner.setValue(widthSlider.getValue()));
		layout.putConstraint(SpringLayout.NORTH, widthSlider, 10, SpringLayout.SOUTH, widthLabel);
		layout.putConstraint(SpringLayout.EAST, widthSlider, -5, SpringLayout.EAST, this);
		add(widthSlider);
		
		numberModel = new SpinnerNumberModel();
		numberModel.setMinimum(10);
		numberModel.setMaximum(10000);
		heightSpinner.setModel(numberModel);
		layout.putConstraint(SpringLayout.NORTH, heightSpinner, 5, SpringLayout.SOUTH, widthSlider);
		layout.putConstraint(SpringLayout.EAST, heightSpinner, -5, SpringLayout.EAST, this);
		add(heightSpinner);
		
		layout.putConstraint(SpringLayout.NORTH, heightLabel, 1, SpringLayout.NORTH, heightSpinner);
		layout.putConstraint(SpringLayout.WEST, heightLabel, -195, SpringLayout.EAST, this);
		add(heightLabel);
		
		seedCheckBox.setBackground(Color.white);
		seedCheckBox.setSelected(true);
		seedCheckBox.addActionListener((ActionEvent e) -> {
			cave.setSeed = !seedCheckBox.isSelected();
			seedField.setEnabled(cave.setSeed);
		});
		layout.putConstraint(SpringLayout.NORTH, seedCheckBox, 5, SpringLayout.SOUTH, heightSpinner);
		layout.putConstraint(SpringLayout.EAST, seedCheckBox, -5, SpringLayout.EAST, this);
		add(seedCheckBox);
		
		layout.putConstraint(SpringLayout.NORTH, randomSeedLabel, 2, SpringLayout.NORTH, seedCheckBox);
		layout.putConstraint(SpringLayout.WEST, randomSeedLabel, -195, SpringLayout.EAST, this);
		add(randomSeedLabel);
		
		seedField.setColumns(10);
		seedField.setHorizontalAlignment(JTextField.RIGHT);
		seedField.setEnabled(false);
		layout.putConstraint(SpringLayout.NORTH, seedField, 5, SpringLayout.SOUTH, seedCheckBox);
		layout.putConstraint(SpringLayout.EAST, seedField, -5, SpringLayout.EAST, this);
		add(seedField);
		
		layout.putConstraint(SpringLayout.NORTH, seedLabel, 1, SpringLayout.NORTH, seedField);
		layout.putConstraint(SpringLayout.WEST, seedLabel, -195, SpringLayout.EAST, this);
		add(seedLabel);
		
		numberModel = new SpinnerNumberModel();
		numberModel.setMinimum(0);
		numberModel.setMaximum(1000);
		fillDensitySpinner.setModel(numberModel);
		fillDensitySpinner.setValue(defaultFillDensity);
		fillDensitySpinner.addChangeListener((ChangeEvent e) -> fillDensity = (float)(int)fillDensitySpinner.getValue() / 1000f);
		layout.putConstraint(SpringLayout.NORTH, fillDensitySpinner, 10, SpringLayout.SOUTH, seedField);
		layout.putConstraint(SpringLayout.EAST, fillDensitySpinner, -5, SpringLayout.EAST, this);
		add(fillDensitySpinner);
		
		layout.putConstraint(SpringLayout.NORTH, fillDensityLabel, 1, SpringLayout.NORTH, fillDensitySpinner);
		layout.putConstraint(SpringLayout.WEST, fillDensityLabel, -195, SpringLayout.EAST, this);
		add(fillDensityLabel);
		
		fillDensitySlider.setPreferredSize(new Dimension(190, 20));
		fillDensitySlider.setBackground(Color.white);
		fillDensitySlider.setMinimum(0);
		fillDensitySlider.setMaximum(1000);
		fillDensitySlider.setValue(defaultFillDensity);
		fillDensitySlider.addChangeListener((ChangeEvent e) -> fillDensitySpinner.setValue(fillDensitySlider.getValue()));
		layout.putConstraint(SpringLayout.NORTH, fillDensitySlider, 10, SpringLayout.SOUTH, fillDensityLabel);
		layout.putConstraint(SpringLayout.EAST, fillDensitySlider, -5, SpringLayout.EAST, this);
		add(fillDensitySlider);
		
		numberModel = new SpinnerNumberModel();
		numberModel.setMinimum(0);
		numberModel.setMaximum(100);
		borderSpinner.setModel(numberModel);
		borderSpinner.setValue(2);
		layout.putConstraint(SpringLayout.NORTH, borderSpinner, 5, SpringLayout.SOUTH, fillDensitySlider);
		layout.putConstraint(SpringLayout.EAST, borderSpinner, -5, SpringLayout.EAST, this);
		add(borderSpinner);
		
		layout.putConstraint(SpringLayout.NORTH, borderLabel, 1, SpringLayout.NORTH, borderSpinner);
		layout.putConstraint(SpringLayout.WEST, borderLabel, -195, SpringLayout.EAST, this);
		add(borderLabel);
		
		fillDensityButton.setPreferredSize(new Dimension(190, 30));
		fillDensityButton.addActionListener((ActionEvent e) -> {
//			cave.seed = TODO seed string to long
			cave.fillMap(fillDensity);
			cave.fillBorder((int)borderSpinner.getValue());
			cave.mapToImage();
			repaint();
		});
		layout.putConstraint(SpringLayout.NORTH, fillDensityButton, 5, SpringLayout.SOUTH, borderSpinner);
		layout.putConstraint(SpringLayout.EAST, fillDensityButton, -5, SpringLayout.EAST, this);
		add(fillDensityButton);
		
		regenerateButton.addActionListener((ActionEvent e) -> generate());
		layout.putConstraint(SpringLayout.SOUTH, regenerateButton, -5, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.EAST, regenerateButton, -5, SpringLayout.EAST, this);
		add(regenerateButton);
	}

}
