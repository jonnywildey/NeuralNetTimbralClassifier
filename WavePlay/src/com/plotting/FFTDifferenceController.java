package com.plotting;
import java.awt.*;
import javax.swing.*;
import com.DSP.waveAnalysis.FFTBox;


public class FFTDifferenceController extends Controller{
	
	
	public FFTDifferenceController(double[][] table) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.size = new Dimension(screenSize.width, screenSize.height / 3);
		this.table = table;
	}
	
	public FFTDifferenceController(FFTBox fftBox, int width, int height) {
		this.size = new Dimension(width, height);
		this.table = fftBox.getTable();
	}
	
	
	protected static void createChart(double[][] table, Dimension winSize) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(winSize.width, 0, winSize.width, winSize.height);
		frame.setSize(winSize);
		//Variables
		//String[] names = new String{""}; 
		String[] axisLabels = {"DB", "Frequency (log2)"};
		frame.getContentPane().add(new FFTDifferenceGraph(table, winSize, axisLabels));
		//frame.pack();
		frame.setVisible(true);
	}



	
}
