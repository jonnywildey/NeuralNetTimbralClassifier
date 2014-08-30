package com.plotting;

import java.awt.*;
import javax.swing.*;
import com.DSP.waveAnalysis.FFTBox;

/**
 * Controller for generating usable DCT graphs *.
 * 
 * @author Jonny Wildey
 * @version 1.0
 */
public class DCTController extends Controller {

	/**
	 * The Class DCTGraph.
	 * 
	 * @author Jonny Wildey
	 * @version 1.0
	 */
	class DCTGraph extends FFTGraph {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		protected double[][] values;

		/**
		 * Instantiates a new dCT graph.
		 * 
		 * @param table
		 *            the table
		 * @param winSize
		 *            the win size
		 * @param axisLabels
		 *            the axis labels
		 */
		public DCTGraph(double[][] table, Dimension winSize, String[] axisLabels) {
			super(table, winSize, axisLabels);
		}

		/**
		 * converts amp value to graph value *.
		 * 
		 * @param c
		 *            the c
		 * @return the int
		 */
		@Override
		public int ampValue(double c) {
			// Log.d(c);
			return (int) (heightness - (c * maxBar));
		}
	}

	/**
	 * Instantiates a new dCT controller.
	 * 
	 * @param table
	 *            the table
	 */
	public DCTController(double[][] table) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.size = new Dimension(screenSize.width, screenSize.height / 3);
		this.table = table;
	}

	/**
	 * Instantiates a new dCT controller.
	 * 
	 * @param fftBox
	 *            the fft box
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 */
	public DCTController(FFTBox fftBox, int width, int height) {
		this.size = new Dimension(width, height);
		this.table = fftBox.getTable();
	}

	/**
	 * Creates the chart.
	 * 
	 * @param table
	 *            the table
	 * @param winSize
	 *            the win size
	 */
	@Override
	protected void createChart(double[][] table, Dimension winSize) {

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(winSize.width, 0, winSize.width, winSize.height);
		frame.setSize(winSize);
		// Variables
		String[] axisLabels = { "DB", "Frequency (log2)" };
		frame.getContentPane().add(new DCTGraph(table, winSize, axisLabels));
		// frame.pack();
		frame.setVisible(true);
	}

}
