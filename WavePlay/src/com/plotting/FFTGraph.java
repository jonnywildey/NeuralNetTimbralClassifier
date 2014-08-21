package com.plotting;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.*;

import com.filemanager.ArrayMethods;
import com.filemanager.Log;
import com.riff.Signal;
import com.waveAnalysis.FFT;
import com.waveAnalysis.FFTBox;
import com.waveAnalysis.FrameFFT;

/**
 * FFT graph. Specifically, an implementation of the plot graph using doubles
 * rather than long
 **/
public class FFTGraph extends PlotGraph {

	private static final long serialVersionUID = 4660340575824126891L;
	protected double[][] values;

	public FFTGraph(double[][] table, Dimension winSize, String[] axisLabels) {
		super();
		initialise(table, winSize, axisLabels);
	}

	public FFTGraph(FFTBox fftBox, Dimension winSize, String[] axisLabels) {
		super();
		initialise(fftBox.getTable(), winSize, axisLabels);
	}
	
	/** initialise basic values etc. **/
	protected void initialise(double[][] table, Dimension winSize,
			String[] axisLabels) {
		this.values = table;
		this.axisLabels = axisLabels;
		initDimensions(winSize);
		rColors = colorArray(values.length);
		maxBar = ((double) (heightness - (offsetSize.height)) / ArrayMethods
				.getMaxAbs(values));
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
		this.half = (int) ((size.height - offsetSize.height) * 0.5);
	}

	/** converts amp value to graph value **/
	public int ampValue(double c) {
		// Log.d(c);
		return (int) (offsetSize.height - (c * maxBar));
	}

	/** Gets the highest frequency in table **/
	private String getHighFreq() {
		return String.valueOf(Math.rint(Math.pow(2,
				ArrayMethods.getMax(this.values[0])))
				+ "hz");
	}

	@Override
	/** Main drawing method **/
	public void drawBar(Graphics2D g2d) {
		whiteBackground(g2d);
		double max = values[0][values[0].length - 1];
		double min = values[0][0];
		double num = max - min;
		// lines
		drawLines(g2d, max, min, num);
		// the wavs
		drawValues(g2d, max, min);
	}

	/** draw the actual values **/
	protected void drawValues(Graphics2D g2d, double max, double min) {
		double factor = (double) (values[0].length) / (max - min);
		int c = 0;
		for (int j = 1; j < values.length; ++j) {
			double[] av = values[j];
			g2d.setColor(rColors[c]);
			for (int i = 0; i < av.length - 1; ++i) {
				g2d.drawLine(
						(int) (((values[0][i] - min) * wr * factor) + wOffset),
						ampValue(av[i]),
						(int) (((values[0][i + 1] - min) * wr * factor) + wOffset),
						ampValue(av[i + 1]));
			}
			++c;
		}
	}

	/** Axis labels, sub bars etc. **/
	protected void drawLines(Graphics2D g2d, double max, double min, double num) {
		double wid;
		// Y X axis
		g2d.drawLine(offsetSize.width, heightness, offsetSize.width,
				offsetSize.height);
		g2d.drawLine(offsetSize.width, heightness, size.width
				- offsetSize.width, heightness);
		// axis labels
		drawCentredString(axisLabels[0], offsetSize.width, offsetSize.height,
				g2d);
		drawCentredString(axisLabels[1], widthness + offsetSize.width,
				heightness, g2d);
		// freq
		// drawCentredString(getHighFreq(), this.size.width - offsetSize.width,
		// (int) (this.heightness * 1.1), g2d);
		String s = "";
		for (double i = 0; i <= num; ++i) {
			s = String.valueOf(Math.rint(Math.pow(2, min
					+ ((max - min) / num * i))))
					+ "hz";
			wid = offsetSize.width
					+ ((this.size.width - 2 * offsetSize.width) / num * i);
			drawCentredString(s, (int) wid, (int) (this.heightness * 1.1), g2d);
		}
	}

}
