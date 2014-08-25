package com.plotting;
import java.awt.*;
import javax.swing.*;
import com.riff.Signal;

/**
 * Controller for plotting wave signals *.
 *
 * @author Jonny Wildey
 * @version 1.0
 */
public class SignalController extends Controller{
	
	protected Signal signals;
	
	/**
	 * Instantiates a new signal controller.
	 *
	 * @param signals the signals
	 */
	public SignalController(Signal signals) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.size = new Dimension(screenSize.width, screenSize.height / 3);
		this.signals = signals;
	}
	
	/**
	 * Instantiates a new signal controller.
	 *
	 * @param signals the signals
	 * @param width the width
	 * @param height the height
	 */
	public SignalController(Signal signals, int width, int height) {
		this.size = new Dimension(width, height);
		this.signals = signals;
	}
	
	/* (non-Javadoc)
	 * @see com.plotting.Controller#makeChart()
	 */
	public void makeChart() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createChart(signals, size);
			}
		});
	}
	
	/**
	 * Creates the chart.
	 *
	 * @param signals the signals
	 * @param winSize the win size
	 */
	protected static void createChart(Signal signals, Dimension winSize) {
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
