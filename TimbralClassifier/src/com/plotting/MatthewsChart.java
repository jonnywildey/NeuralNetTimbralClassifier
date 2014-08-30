package com.plotting;

import java.awt.*;
import javax.swing.*;
import com.DSP.waveAnalysis.Statistics;

/**
 * Create a plot graph of Matthews coefficients *.
 * 
 * @author Jonny Wildey
 * @version 1.0
 */
public class MatthewsChart extends Controller {

	/**
	 * The Class MatthewsGraph.
	 * 
	 * @author Jonny Wildey
	 * @version 1.0
	 */
	class MatthewsGraph extends PlotGraph {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		double[][] values;

		/**
		 * Instantiates a new matthews graph.
		 * 
		 * @param values
		 *            the values
		 * @param winSize
		 *            the win size
		 * @param axisLabels
		 *            the axis labels
		 */
		public MatthewsGraph(double[][] values, Dimension winSize,
				String[] axisLabels) {
			this.values = values;
			this.axisLabels = axisLabels;
			size = winSize;
			offsetSize = new Dimension((int) (size.getWidth() * marginOffset),
					(int) (size.getHeight() * marginOffset));
			// dims of the actual chart part
			widthness = size.width - (offsetSize.width * 2);
			heightness = size.height - (offsetSize.height * 2);
			// System.out.println("max b " + maxBar);
			rColors = colorArray(values.length);
			wOffset = (winSize.getWidth() - widthness) / 2;
			hOffset = (size.getHeight() - offsetSize.getHeight()) / 2;
			wr = ((double) widthness / (double) values[0].length);
			this.half = (int) ((size.height - offsetSize.height) * 0.5);
			// get bar value - height of chart ratio (so biggest value is at top
			// of chart)
			this.max = 1d;
			maxBar = ((heightness - offsetSize.height) / 1d);

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.plotting.PlotGraph#drawBar(java.awt.Graphics2D)
		 */
		@Override
		public void drawBar(Graphics2D g2d) {
			whiteBackground(g2d);
			drawLines(g2d);
			drawGuides(g2d, 5);
			int c = 0;
			for (double[] av : values) {
				g2d.setColor(rColors[c]);
				for (int i = 0; i < av.length - 1; ++i) {
					g2d.drawLine((int) ((i * wr) + wOffset), heightness
							- (int) (av[i] * maxBar),
							(int) (((i + 1) * wr) + wOffset), heightness
							- (int) (av[i + 1] * maxBar));
				}
				++c;
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.plotting.PlotGraph#drawGuides(java.awt.Graphics2D, int)
		 */
		@Override
		protected void drawGuides(Graphics2D g2d, int number) {
			for (int i = 0; i <= number; ++i) {
				g2d.drawLine(
						(int) (offsetSize.width * 0.95),
						(int) (offsetSize.height + (((heightness - offsetSize.height)
								/ ((double) (number)) * (double) i))),
								offsetSize.width,
								(int) (offsetSize.height + (((heightness - offsetSize.height)
										/ ((double) (number)) * (double) i))));
				// label
				drawCentredString(
						Double.toString(Statistics
								.round((max - ((double) i * (max / (double) (number)))),
										2)),
										(int) (offsetSize.width * 0.8),
										(int) ((offsetSize.height * 1.3) + (((heightness - offsetSize.height) / (double) (number)) * (double) i)),
										g2d);
				// x axis
				g2d.drawLine(
						(int) (offsetSize.width + ((widthness / (double) (number)) * (double) i)),
						(int) (heightness * 1.02),
						(int) (offsetSize.width + ((widthness / (double) (number)) * (double) i)),
						heightness);
				drawCentredString(
						Integer.toString((int) (((double) i * ((double) (this.values[0].length) / (double) (number))))),
						(int) (offsetSize.width + ((widthness / (double) (number)) * (double) i)),
						(int) (heightness * 1.08), g2d);
			}
		}

	}

	/**
	 * Instantiates a new matthews chart.
	 * 
	 * @param signals
	 *            the signals
	 */
	public MatthewsChart(double[][] signals) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.size = new Dimension(screenSize.width, screenSize.height / 3);
		this.table = signals;
	}

	/**
	 * Instantiates a new matthews chart.
	 * 
	 * @param signals
	 *            the signals
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 */
	public MatthewsChart(double[][] signals, int width, int height) {
		this.size = new Dimension(width, height);
		this.table = signals;
	}

	/**
	 * Make the chart *.
	 * 
	 * @param values
	 *            the values
	 * @param winSize
	 *            the win size
	 */
	@Override
	protected void createChart(double[][] values, Dimension winSize) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(winSize.width, 0, winSize.width, winSize.height);
		frame.setSize(winSize);
		// Variables
		// String[] names = new String{""};
		String[] axisLabels = { "Matthews", "Epochs" };
		frame.getContentPane().add(
				new MatthewsGraph(values, winSize, axisLabels));
		// frame.pack();
		frame.setVisible(true);
	}

}
