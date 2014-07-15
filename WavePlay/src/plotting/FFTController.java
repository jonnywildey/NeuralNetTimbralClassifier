package plotting;
import java.awt.*;

import javax.swing.*;

import riff.Signal;
import waveAnalysis.FFT;


public class FFTController {
	
	private Dimension size;
	private FFT fft;
	
	public FFTController(FFT fft) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.size = new Dimension(screenSize.width, screenSize.height / 3);
		this.fft = fft;
	}
	
	public FFTController(FFT fft, int width, int height) {
		this.size = new Dimension(width, height);
		this.fft = fft;
	}
	
	public void makeChart() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createChart(fft, size);
			}
		});
	}
	
	
	private static void createChart(FFT fft, Dimension winSize) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(winSize.width, 0, winSize.width, winSize.height);
		frame.setSize(winSize);
		//Variables
		//String[] names = new String{""}; 
		String[] axisLabels = {"Energy", "Frequency"};
		frame.getContentPane().add(new FFTGraph(fft, winSize, axisLabels));
		//frame.pack();
		frame.setVisible(true);
	}



	
}
