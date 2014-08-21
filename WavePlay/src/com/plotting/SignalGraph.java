package com.plotting;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.*;

import com.filemanager.ArrayMethods;
import com.filemanager.Log;
import com.riff.Signal;

/**
 * Graph for plotting signals. Based upon the plot graph but uses the signal
 * object for normalising etc.
 **/
public class SignalGraph extends PlotGraph {

	private static final long serialVersionUID = 1956916433882671200L;
	protected double[][] values;

	public SignalGraph(Signal signals, Dimension winSize, String[] axisLabels) {
		super();
		initialise(signals, winSize, axisLabels);
	}

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

	/** converts amp value to graph value **/
	public int ampValue(double c) {
		// Log.d(c);
		return (int) (this.half - (c * maxBar));

	}

	@Override
	/** Main draw function **/
	public void drawBar(Graphics2D g2d) {
		whiteBackground(g2d);
		drawLines(g2d);
		// the wavs
		drawValues(g2d);
	}

	/** Draw the actual signal **/
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

	/** Draw axis and labels **/
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

}
