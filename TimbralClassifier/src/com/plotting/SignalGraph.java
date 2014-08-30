package com.plotting;

import java.awt.*;
import com.riff.Signal;

/**
 * Graph for plotting signals. Based upon the plot graph but uses the signal
 * object for normalising etc.
 * 
 * @author Jonny Wildey
 * @version 1.0
 */
public class SignalGraph extends PlotGraph {

	private static final long serialVersionUID = 1956916433882671200L;
	protected double[][] values;

	/**
	 * Instantiates a new signal graph.
	 * 
	 * @param signals
	 *            the signals
	 * @param winSize
	 *            the win size
	 * @param axisLabels
	 *            the axis labels
	 */
	public SignalGraph(Signal signals, Dimension winSize, String[] axisLabels) {
		super();
		initialise(signals, winSize, axisLabels);
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
	/** Main draw function **/
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
	 * Draw the actual signal *.
	 * 
	 * @param g2d
	 *            the g2d
	 * @return the int
	 */
	protected int drawValues(Graphics2D g2d) {
		int c = 0;
		for (double[] av : values) {
			g2d.setColor(rColors[c]);
			for (int i = 0; i < av.length - 1; ++i) {
				g2d.drawLine((int) ((i * wr) + wOffset), ampValue(av[i]),
						(int) (((i + 1) * wr) + wOffset), ampValue(av[i + 1]));
			}
			++c;
		}
		return c;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.plotting.PlotGraph#initDimensions(java.awt.Dimension)
	 */
	@Override
	protected void initDimensions(Dimension winSize) {
		size = winSize;
		offsetSize = new Dimension((int) (size.getWidth() * marginOffset),
				(int) (size.getHeight() * marginOffset));
		widthness = size.width - (offsetSize.width * 2);
		heightness = size.height - (offsetSize.height * 2);
		wOffset = (winSize.getWidth() - widthness) / 2;
		hOffset = (size.getHeight() - offsetSize.getHeight()) / 2;
		wr = ((double) widthness / (double) values[0].length);
	}

	/**
	 * Initialise.
	 * 
	 * @param signals
	 *            the signals
	 * @param winSize
	 *            the win size
	 * @param axisLabels
	 *            the axis labels
	 */
	protected void initialise(Signal signals, Dimension winSize,
			String[] axisLabels) {
		this.values = signals.getSignal();
		this.axisLabels = axisLabels;
		initDimensions(winSize);
		rColors = colorArray(values.length);
		this.half = (int) ((size.height - offsetSize.height) * 0.5);
		maxBar = ((double) (heightness - (offsetSize.height))
				/ signals.getMaxAmplitude() * 0.5);
		// Log.d(maxBar);
	}

}
