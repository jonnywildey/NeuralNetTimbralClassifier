package plotting;
import java.awt.*;

import javax.swing.*;

import riff.Signal;


public class SignalController {
	
	private Dimension size;
	private Signal signals;
	
	public SignalController(Signal signals) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.size = new Dimension(screenSize.width, screenSize.height / 3);
		this.signals = signals;
	}
	
	public SignalController(Signal signals, int width, int height) {
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
	
	
	private static void createChart(Signal signals, Dimension winSize) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(winSize.width, 0, winSize.width, winSize.height);
		frame.setSize(winSize);
		//Variables
		//String[] names = new String{""}; 
		String[] axisLabels = {"", ""};
		frame.getContentPane().add(new SignalGraph(signals, winSize, axisLabels));
		//frame.pack();
		frame.setVisible(true);
	}



	
}
