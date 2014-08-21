package com.plotting;
import java.awt.*;

import javax.swing.*;

import com.filemanager.ArrayMethods;
import com.riff.Signal;
import com.waveAnalysis.FFT;
import com.waveAnalysis.FFTBox;
import com.waveAnalysis.FrameFFT;

/** Controller for generating usable DCT graphs **/
public class DCTController {
	
	private Dimension size;
	private double[][] table;
	
	public DCTController(double[][] table) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.size = new Dimension(screenSize.width, screenSize.height / 3);
		this.table = table;
	}
	
	public DCTController(FFTBox fftBox, int width, int height) {
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
	
	
	
	private static void createChart(double[][] table, Dimension winSize) {
		class DCTGraph extends FFTGraph {
			protected double[][] values;
			public DCTGraph(double[][] table, Dimension winSize, String[] axisLabels) {
				super(table, winSize, axisLabels);
			}
			/** converts amp value to graph value **/
			public int ampValue(double c) {
				//Log.d(c);
				 return (int) (heightness - (c * maxBar));
			}
		}
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(winSize.width, 0, winSize.width, winSize.height);
		frame.setSize(winSize);
		//Variables
		String[] axisLabels = {"DB", "Frequency (log2)"};
		frame.getContentPane().add(new DCTGraph(table, winSize, axisLabels));
		//frame.pack();
		frame.setVisible(true);
	}
	
	



	
}
