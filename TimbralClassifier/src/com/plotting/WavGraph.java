package com.plotting;

import java.awt.*;
import com.util.ArrayMethods;

/**
 * Graph for plotting wave amplitudes. Automatically normalises *
 * 
 * @author Jonny Wildey
 * @version 1.0
 */
public class WavGraph extends PlotGraph {

	private static final long serialVersionUID = -3355221550223089425L;

	/**
	 * Instantiates a new wav graph. Normalises values
	 * 
	 * @param values
	 *            the values
	 * @param winSize
	 *            the win size
	 * @param axisLabels
	 *            the axis labels
	 */
	public WavGraph(long[][] values, Dimension winSize, String[] axisLabels) {
		super(values, winSize, axisLabels);
		maxBar = (((heightness - offsetSize.height) / (double) (ArrayMethods
				.getMax(values))) * 0.5);
	}

	/**
	 * converts amp value to graph value *.
	 * 
	 * @param c
	 *            the c
	 * @return the int
	 */
	public int ampValue(double c) {
		// System.out.println((int) (half - (c * maxBar)));
		return (int) (this.half - (c * maxBar));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.plotting.PlotGraph#drawBar(java.awt.Graphics2D)
	 */
	@Override
	/**Main draw Method **/
	public void drawBar(Graphics2D g2d) {
		whiteBackground(g2d);
		drawLines(g2d);
		// the wavs
		drawValues(g2d);
	}

	/**
	 * Draw axis and labels *.
	 * 
	 * @param g2d
	 *            the g2d
	 */
	@Override
	protected void drawLines(Graphics2D g2d) {
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
	}

	/**
	 * Draw actual values *.
	 * 
	 * @param g2d
	 *            the g2d
	 */
	protected void drawValues(Graphics2D g2d) {
		int c = 0;
		for (long[] av : values) {
			g2d.setColor(rColors[c]);
			for (int i = 0; i < av.length - 1; ++i) {
				g2d.drawLine((int) ((i * wr) + wOffset), ampValue(av[i]),
						(int) (((i + 1) * wr) + wOffset), ampValue(av[i + 1]));
			}
			++c;
		}
	}

}
