package com.plotting;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import com.DSP.waveAnalysis.FFTBox;

/**
 * Basic controller for creating graphs *.
 *
 * @author Jonny Wildey
 * @version 1.0
 */
public class Controller {
	
	protected Dimension size;
	protected double[][] table;
	
	/**
	 * Instantiates a new controller.
	 */
	public Controller() {
	}
	
	/**
	 * Instantiates a new controller.
	 *
	 * @param table the table
	 */
	public Controller(double[][] table) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.size = new Dimension(screenSize.width, screenSize.height / 3);
		this.table = table;
	}
	
	/**
	 * Instantiates a new controller.
	 *
	 * @param fftBox the fft box
	 * @param width the width
	 * @param height the height
	 */
	public Controller(FFTBox fftBox, int width, int height) {
		this.size = new Dimension(width, height);
		this.table = fftBox.getTable();
	}
	
	/**
	 * Make chart.
	 */
	public void makeChart() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createChart(table, size);
			}
		});
	}
	
	/**
	 * Creates the chart.
	 *
	 * @param table the table
	 * @param winSize the win size
	 */
	protected void createChart(double[][] table, Dimension winSize) {
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
