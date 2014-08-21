package com.plotting;
import java.awt.*;
import javax.swing.*;

/** Controller for WavGraph **/
public class WavController {
	
	private Dimension size;
	private long[][] signals;
	
	public WavController(long[][] signals) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.size = new Dimension(screenSize.width, screenSize.height / 3);
		this.signals = signals;
	}
	
	public WavController(long[][] signals, int width, int height) {
		this.size = new Dimension(width, height);
		this.signals = signals;
	}
	
	public void makeChart() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createChart(signals, size);
			}
		});
	}
	
	
	private static void createChart(long[][] values, Dimension winSize) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(winSize.width, 0, winSize.width, winSize.height);
		frame.setSize(winSize);
		//Variables
		//String[] names = new String{""}; 
		String[] axisLabels = {"", ""};
		frame.getContentPane().add(new WavGraph(values, winSize, axisLabels));
		//frame.pack();
		frame.setVisible(true);
	}



	
}
