package com.plotting;
import java.awt.*;
import javax.swing.*;
import com.DSP.waveAnalysis.FFTBox;

/**
 * Controller for creating FFT Difference graph *.
 *
 * @author Jonny Wildey
 * @version 1.0
 */
public class FFTDifferenceController extends Controller{
	
	
	/**
	 * Instantiates a new fFT difference controller.
	 *
	 * @param table the table
	 */
	public FFTDifferenceController(double[][] table) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.size = new Dimension(screenSize.width, screenSize.height / 3);
		this.table = table;
	}
	
	/**
	 * Instantiates a new fFT difference controller.
	 *
	 * @param fftBox the fft box
	 * @param width the width
	 * @param height the height
	 */
	public FFTDifferenceController(FFTBox fftBox, int width, int height) {
		this.size = new Dimension(width, height);
		this.table = fftBox.getTable();
	}
	
	
	/**
	 * Creates the chart.
	 *
	 * @param table the table
	 * @param winSize the win size
	 */
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
