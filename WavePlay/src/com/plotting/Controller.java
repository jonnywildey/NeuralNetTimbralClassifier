package com.plotting;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import com.DSP.waveAnalysis.FFTBox;

public class Controller {
	
	protected Dimension size;
	protected double[][] table;
	
	public Controller() {
	}
	
	public Controller(double[][] table) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.size = new Dimension(screenSize.width, screenSize.height / 3);
		this.table = table;
	}
	
	public Controller(FFTBox fftBox, int width, int height) {
		this.size = new Dimension(width, height);
		this.table = fftBox.getTable();
	}
	
	public void makeChart() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createChart(table, size);
			}
		});
	}
	
	protected static void createChart(double[][] table, Dimension winSize) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(winSize.width, 0, winSize.width, winSize.height);
		frame.setSize(winSize);
		//Variables
		String[] axisLabels = {"DB", "Frequency (log2)"};
		frame.getContentPane().add(new FFTGraph(table, winSize, axisLabels));
		frame.setVisible(true);
	}
	
	
}
