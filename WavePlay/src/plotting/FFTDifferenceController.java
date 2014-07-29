package plotting;
import java.awt.*;

import javax.swing.*;

import riff.Signal;
import waveAnalysis.FFT;
import waveAnalysis.FFTBox;
import waveAnalysis.FrameFFT;


public class FFTDifferenceController {
	
	private Dimension size;
	private double[][] table;
	
	public FFTDifferenceController(double[][] table) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.size = new Dimension(screenSize.width, screenSize.height / 3);
		this.table = table;
	}
	
	public FFTDifferenceController(FFTBox fftBox, int width, int height) {
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
