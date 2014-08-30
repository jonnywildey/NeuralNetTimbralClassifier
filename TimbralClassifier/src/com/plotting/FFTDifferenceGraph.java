package com.plotting;

import java.awt.*;
import com.util.ArrayMethods;
import com.util.Log;

/**
 * FFT graph*.
 * 
 * @author Jonny Wildey
 * @version 1.0
 */
public class FFTDifferenceGraph extends PlotGraph {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected double[][] values;

	/**
	 * Instantiates a new fFT difference graph.
	 * 
	 * @param table
	 *            the table
	 * @param winSize
	 *            the win size
	 * @param axisLabels
	 *            the axis labels
	 */
	public FFTDifferenceGraph(double[][] table, Dimension winSize,
			String[] axisLabels) {
		super();
		this.values = table;
		this.axisLabels = axisLabels;
		size = winSize;
		offsetSize = new Dimension((int) (size.getWidth() * marginOffset),
				(int) (size.getHeight() * marginOffset));
		widthness = size.width - (offsetSize.width * 2);
		heightness = size.height - (offsetSize.height * 2);
		rColors = colorArray(values.length);
		wOffset = (winSize.getWidth() - widthness) / 2;
		hOffset = (size.getHeight() - offsetSize.getHeight()) / 2;
		wr = ((double) widthness / (double) values[0].length);
		this.half = (int) ((size.height - offsetSize.height) * 0.5);
		maxBar = ((double) (heightness - (offsetSize.height))
				/ ArrayMethods.getMaxAbs(values) * 0.5);
		Log.d("maxbar" + maxBar);
	}

	/**
	 * converts amp value to graph value *.
	 * 
	 * @param c
	 *            the c
	 * @return the int
	 */
	public int ampValue(double c) {
		// Log.d(c);
		return (int) (this.half - (c * maxBar));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.plotting.PlotGraph#drawBar(java.awt.Graphics2D)
	 */
	@Override
	public void drawBar(Graphics2D g2d) {
		whiteBackground(g2d);
		// Y axis
		g2d.drawLine(offsetSize.width, heightness, offsetSize.width,
				offsetSize.height);
		// X axis
		g2d.drawLine(offsetSize.width, heightness, size.width
				- offsetSize.width, heightness);

		g2d.drawLine(offsetSize.width, this.half,
				size.width - offsetSize.width, this.half);

		// axis labels
		drawCentredString(axisLabels[0], offsetSize.width, offsetSize.height,
				g2d);
		drawCentredString(axisLabels[1], widthness + offsetSize.width,
				heightness, g2d);
		// freq
		// drawCentredString(getHighFreq(), this.size.width - offsetSize.width,
		// (int) (this.heightness * 1.1), g2d);
		double max = values[0][values[0].length - 1];
		double min = values[0][0];
		double num = max - min;
		double wid = 0;
		String s = "";
		for (double i = 0; i <= num; ++i) {
			s = String.valueOf(Math.rint(Math.pow(2, min
					+ ((max - min) / num * i))))
					+ "hz";
			wid = offsetSize.width
			+ ((this.size.width - 2 * offsetSize.width) / num * i);
			drawCentredString(s, (int) wid, (int) (this.heightness * 1.1), g2d);
		}

		// the wavs
		// System.out.println("widthness" + widthness);
		// System.out.println("values length " + values[0].length);
		double factor = (double) (values[0].length) / (max - min);
		int c = 0;
		for (int j = 1; j < values.length; ++j) {
			double[] av = values[j];
			g2d.setColor(rColors[c]);
			for (int i = 0; i < av.length - 1; ++i) {
				// wid = 1 + ((double)(i) / (double)(av.length));
				wid = 1;
				g2d.drawLine(
						(int) (((values[0][i] - min) * wr * factor) + wOffset),
						ampValue(av[i]),
						(int) (((values[0][i + 1] - min) * wr * factor) + wOffset),
						ampValue(av[i + 1]));
				// System.out.println((i * wr) + wOffset);
				// System.out.println((int)values[i + 1] / 100);
			}
			++c;
		}

	}

	/**
	 * Gets the high freq.
	 * 
	 * @return the high freq
	 */
	private String getHighFreq() {
		return String.valueOf(Math.rint(Math.pow(2,
				ArrayMethods.getMax(this.values[0])))
				+ "hz");
	}

}
