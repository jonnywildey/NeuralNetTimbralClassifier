package com.plotting;
import java.awt.*;

import javax.swing.*;

import com.matrix.ConfusionMatrix;
import com.riff.Signal;
import com.util.ArrayMethods;
import com.waveAnalysis.FFT;
import com.waveAnalysis.FFTBox;
import com.waveAnalysis.FrameFFT;

/** Controller for generating readable Confusion Matrixes graphs **/
public class ConfusionMatrixController {
	
	private Dimension size;
	private ConfusionMatrix confusionMatrix;
	
	public ConfusionMatrixController(ConfusionMatrix confusionMatrix) {
		//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.size = new Dimension(600, 560);
		this.confusionMatrix = confusionMatrix;
	}
	
	public ConfusionMatrixController(ConfusionMatrix confusionMatrix, int width, int height) {
		this.size = new Dimension(width, height);
		this.confusionMatrix = confusionMatrix;
	}
	
	public void makeChart() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createChart(confusionMatrix, size);
			}
		});
	}
	
	
	
	private static void createChart(ConfusionMatrix confusionMatrix, Dimension winSize) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(winSize.width, 0, winSize.width, winSize.height);
		frame.setSize(winSize);
		//Variables
		frame.getContentPane().add(new ConfusionMatrixGraph(confusionMatrix, winSize));
		//frame.pack();
		frame.setVisible(true);
	}
	
	



	
}
