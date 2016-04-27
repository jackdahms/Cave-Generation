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
import javax.swing.SpinnerListModel;
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

	Ruleset selected;
	List<Ruleset> rulesets = new ArrayList<Ruleset>();
	List<String> rulesetNames = new ArrayList<String>();
	
	Cave cave;
	
	int width = 200;
	int height = 150;
	int borderWidth = 2;
	int smoothingIterations = 10;
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
		createPresets();
		
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
		cave = new Cave(width, height);
		cave.fillMap(fillDensity);
		cave.fillBorder(borderWidth);
		for (int i = 0; i < smoothingIterations; i++) cave.smooth(selected.mapper);
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
	
	private void createPresets() {
		rulesets.add(new Ruleset("default", 0.48f, 10, () -> {
			for (int r = 0; r < cave.height; r++) {
				for (int c = 0; c < cave.width; c++) {
					if (cave.countSurroundingWalls(r, c) < 4) cave.map[r][c] = 0;
					else if (cave.countSurroundingWalls(r, c) > 4) cave.map[r][c] = 1;
				}
			}
			return cave.map;
		}));
		
		rulesets.add(new Ruleset("chunky", 0.48f, 5, () -> {
			for (int r = 0; r < cave.height; r++) {
				for (int c = 0; c < cave.width; c++) {
					if (cave.countSurroundingWalls(r, c) < 3) cave.map[r][c] = 0;
					else if (cave.countSurroundingWalls(r, c) > 5) cave.map[r][c] = 1;
				}
			}
			return cave.map;
		}));
		
		rulesets.add(new Ruleset("third", 0.715f, 4, () -> {
			for (int r = 0; r < cave.height; r++) {
				for (int c = 0; c < cave.width; c++) {
					if (cave.countSurroundingWalls(r, c) < 5) cave.map[r][c] = 0;
					else if (cave.countSurroundingWalls(r, c) > 5) cave.map[r][c] = 1;
				}
			}
			return cave.map;
		}));
		
		//set the default ruleset as selected
		selected = rulesets.get(0);
		
		//create list of ruleset names for gui
		for (Ruleset r : rulesets) rulesetNames.add(r.name);
	}
	
	private void createGUI() {
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		
		int defaultWidth = width;
		int defaultHeight = height;
		int defaultFillDensity = (int) (fillDensity * 1000);
		int defaultBorder = borderWidth;
		int defaultIterations = smoothingIterations;
		
		JLabel widthLabel = new JLabel("Width");
		JLabel heightLabel = new JLabel("Height");
		JSpinner widthSpinner = new JSpinner();
		JSpinner heightSpinner = new JSpinner();
		JSlider widthSlider = new JSlider(); //TODO do i even need sliders
		JSlider heightSlider = new JSlider(); //note: dims set on sliders, density/iterations set on spinners
		
		JLabel randomSeedLabel = new JLabel("Use Random Seed");
		JLabel seedLabel = new JLabel("Seed");
		JCheckBox seedCheckBox = new JCheckBox();
		JTextField seedField = new JTextField();
		
		JSpinner fillDensitySpinner = new JSpinner();
		JLabel fillDensityLabel = new JLabel("Fill Density");
		JSlider fillDensitySlider = new JSlider();
		
		JSpinner borderSpinner = new JSpinner();
		JLabel borderLabel = new JLabel("Border Width");
		JSlider borderSlider = new JSlider();
		
		JButton fillDensityButton = new JButton("Fill New Cave");
		
		JLabel presetLabel = new JLabel("Ruleset");
		JSpinner presetSpinner = new JSpinner(); //TODO make this a drop down menu
		JLabel iterationsLabel = new JLabel("Smoothing Iterations");
		JSpinner iterationsSpinner = new JSpinner();
		JSlider iterationsSlider = new JSlider();
		JButton iterateButton = new JButton("Smooth Once"); //for iterating once
		JButton smoothButton = new JButton("Smooth 5 Times"); //do all iterations
		
		JButton regenerateButton = new JButton("Generate New Cave");
		
		SpinnerNumberModel numberModel = new SpinnerNumberModel();
		numberModel.setMinimum(10);
		numberModel.setMaximum(5000);
		widthSpinner.setModel(numberModel);
		widthSpinner.setValue(defaultWidth);
		widthSpinner.addChangeListener((ChangeEvent e) -> widthSlider.setValue((int) widthSpinner.getValue()));
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
		widthSlider.addChangeListener((ChangeEvent e) -> {
			width = widthSlider.getValue();
			widthSpinner.setValue(width);
		});
		layout.putConstraint(SpringLayout.NORTH, widthSlider, 10, SpringLayout.SOUTH, widthLabel);
		layout.putConstraint(SpringLayout.EAST, widthSlider, -5, SpringLayout.EAST, this);
		add(widthSlider);
		
		numberModel = new SpinnerNumberModel();
		numberModel.setMinimum(10);
		numberModel.setMaximum(5000);
		heightSpinner.setModel(numberModel);
		heightSpinner.setValue(defaultHeight);
		heightSpinner.addChangeListener((ChangeEvent e) -> heightSlider.setValue((int) heightSpinner.getValue()));
		layout.putConstraint(SpringLayout.NORTH, heightSpinner, 5, SpringLayout.SOUTH, widthSlider);
		layout.putConstraint(SpringLayout.EAST, heightSpinner, -5, SpringLayout.EAST, this);
		add(heightSpinner);
		
		layout.putConstraint(SpringLayout.NORTH, heightLabel, 1, SpringLayout.NORTH, heightSpinner);
		layout.putConstraint(SpringLayout.WEST, heightLabel, -195, SpringLayout.EAST, this);
		add(heightLabel);
		
		heightSlider.setPreferredSize(new Dimension(190, 20));
		heightSlider.setBackground(Color.white);
		heightSlider.setMinimum(0);
		heightSlider.setMaximum((int)((SpinnerNumberModel) heightSpinner.getModel()).getMaximum());
		heightSlider.setValue(defaultHeight);
		heightSlider.addChangeListener((ChangeEvent e) -> {
			height = heightSlider.getValue();
			heightSpinner.setValue(height);
		});
		layout.putConstraint(SpringLayout.NORTH, heightSlider, 10, SpringLayout.SOUTH, heightLabel);
		layout.putConstraint(SpringLayout.EAST, heightSlider, -5, SpringLayout.EAST, this);
		add(heightSlider);
		
		seedCheckBox.setBackground(Color.white);
		seedCheckBox.setSelected(true);
		seedCheckBox.addActionListener((ActionEvent e) -> {
			cave.setSeed = !seedCheckBox.isSelected();
			seedField.setEnabled(cave.setSeed);
		});
		layout.putConstraint(SpringLayout.NORTH, seedCheckBox, 5, SpringLayout.SOUTH, heightSlider);
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
		fillDensitySpinner.addChangeListener((ChangeEvent e) -> {
			fillDensity = (float)(int)fillDensitySpinner.getValue() / 1000f;
			fillDensitySlider.setValue((int)fillDensitySpinner.getValue());
		});
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
		layout.putConstraint(SpringLayout.NORTH, fillDensitySlider, 5, SpringLayout.SOUTH, fillDensitySpinner);
		layout.putConstraint(SpringLayout.EAST, fillDensitySlider, -5, SpringLayout.EAST, this);
		add(fillDensitySlider);
		
		numberModel = new SpinnerNumberModel();
		numberModel.setMinimum(0);
		numberModel.setMaximum(30);
		borderSpinner.setModel(numberModel);
		borderSpinner.setValue(2);
		borderSpinner.addChangeListener((ChangeEvent e) -> {
			borderWidth = (int) borderSpinner.getValue();
			borderSlider.setValue(borderWidth);
		});
		layout.putConstraint(SpringLayout.NORTH, borderSpinner, 5, SpringLayout.SOUTH, fillDensitySlider);
		layout.putConstraint(SpringLayout.EAST, borderSpinner, -5, SpringLayout.EAST, this);
		add(borderSpinner);
		
		layout.putConstraint(SpringLayout.NORTH, borderLabel, 1, SpringLayout.NORTH, borderSpinner);
		layout.putConstraint(SpringLayout.WEST, borderLabel, -195, SpringLayout.EAST, this);
		add(borderLabel);
		
		borderSlider.setPreferredSize(new Dimension(190, 20));
		borderSlider.setBackground(Color.white);
		borderSlider.setMinimum(0);
		borderSlider.setMaximum((int)((SpinnerNumberModel) borderSpinner.getModel()).getMaximum());
		borderSlider.setValue(defaultBorder);
		borderSlider.addChangeListener((ChangeEvent e) -> borderSpinner.setValue(borderSlider.getValue()));
		layout.putConstraint(SpringLayout.NORTH, borderSlider, 10, SpringLayout.SOUTH, borderLabel);
		layout.putConstraint(SpringLayout.EAST, borderSlider, -5, SpringLayout.EAST, this);
		add(borderSlider);
		
		fillDensityButton.setPreferredSize(new Dimension(190, 30));
		fillDensityButton.addActionListener((ActionEvent e) -> {
			cave = new Cave(width, height);
//			cave.seed = TODO seed string to long
			cave.fillMap(fillDensity);
			cave.fillBorder((int)borderSpinner.getValue());
			cave.mapToImage();
			repaint();
		});
		layout.putConstraint(SpringLayout.NORTH, fillDensityButton, 5, SpringLayout.SOUTH, borderSlider);
		layout.putConstraint(SpringLayout.EAST, fillDensityButton, -5, SpringLayout.EAST, this);
		add(fillDensityButton);
		
		SpinnerListModel listModel = new SpinnerListModel(rulesetNames);
		presetSpinner.setModel(listModel);
		presetSpinner.addChangeListener((ChangeEvent e) -> {
			String name = (String) listModel.getValue();
			for (Ruleset r : rulesets)
				if (name.equals(r.name))
					selected = r;
			fillDensitySpinner.setValue((int)(selected.fillDensity * 1000f));
			iterationsSpinner.setValue(selected.smoothingIterations);
		});
		layout.putConstraint(SpringLayout.NORTH, presetSpinner, 5, SpringLayout.SOUTH, fillDensityButton);
		layout.putConstraint(SpringLayout.EAST, presetSpinner, -5, SpringLayout.EAST, this);
		add(presetSpinner);
		
		layout.putConstraint(SpringLayout.NORTH, presetLabel, 1, SpringLayout.NORTH, presetSpinner);
		layout.putConstraint(SpringLayout.WEST, presetLabel, -195, SpringLayout.EAST, this);
		add(presetLabel);
		
		numberModel = new SpinnerNumberModel();
		numberModel.setMinimum(1);
		numberModel.setMaximum(20);
		iterationsSpinner.setModel(numberModel);
		iterationsSpinner.setPreferredSize(new Dimension(35, 20));
		iterationsSpinner.setValue(defaultIterations);
		iterationsSpinner.addChangeListener((ChangeEvent e) -> {
			smoothingIterations = (int)iterationsSpinner.getValue();
			smoothButton.setText("Smooth " + smoothingIterations + " Times");
			iterationsSlider.setValue(smoothingIterations);
		});
		layout.putConstraint(SpringLayout.NORTH, iterationsSpinner, 5, SpringLayout.SOUTH, presetSpinner);
		layout.putConstraint(SpringLayout.EAST, iterationsSpinner, -5, SpringLayout.EAST, this);
		add(iterationsSpinner);
		
		layout.putConstraint(SpringLayout.NORTH, iterationsLabel, 1, SpringLayout.NORTH, iterationsSpinner);
		layout.putConstraint(SpringLayout.WEST, iterationsLabel, -195, SpringLayout.EAST, this);
		add(iterationsLabel);
		
		iterationsSlider.setPreferredSize(new Dimension(190, 20));
		iterationsSlider.setBackground(Color.white);
		iterationsSlider.setMinimum(0);
		iterationsSlider.setMaximum(20);
		iterationsSlider.setValue(defaultIterations);
		iterationsSlider.addChangeListener((ChangeEvent e) -> iterationsSpinner.setValue(iterationsSlider.getValue()));
		layout.putConstraint(SpringLayout.NORTH, iterationsSlider, 5, SpringLayout.SOUTH, iterationsSpinner);
		layout.putConstraint(SpringLayout.EAST, iterationsSlider, -5, SpringLayout.EAST, this);
		add(iterationsSlider);
		
		iterateButton.setPreferredSize(new Dimension(190, 30));
		iterateButton.addActionListener((ActionEvent e) -> {
			cave.smooth(selected.mapper);
			cave.mapToImage();
			repaint();
		});
		layout.putConstraint(SpringLayout.NORTH, iterateButton, 5, SpringLayout.SOUTH, iterationsSlider);
		layout.putConstraint(SpringLayout.WEST, iterateButton, -195, SpringLayout.EAST, this);
		add(iterateButton);
		
		smoothButton.setPreferredSize(new Dimension(190, 30));
		smoothButton.addActionListener((ActionEvent e) -> {
			for (int i = 0; i < smoothingIterations; i++) cave.smooth(selected.mapper);
			cave.mapToImage();
			repaint();
		});
		layout.putConstraint(SpringLayout.NORTH, smoothButton, 5, SpringLayout.SOUTH, iterateButton);
		layout.putConstraint(SpringLayout.WEST, smoothButton, -195, SpringLayout.EAST, this);
		add(smoothButton);
		
		regenerateButton.setPreferredSize(new Dimension(190, 30));
		regenerateButton.addActionListener((ActionEvent e) -> generate());
		layout.putConstraint(SpringLayout.SOUTH, regenerateButton, -5, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.EAST, regenerateButton, -5, SpringLayout.EAST, this);
		add(regenerateButton);
	}

}
