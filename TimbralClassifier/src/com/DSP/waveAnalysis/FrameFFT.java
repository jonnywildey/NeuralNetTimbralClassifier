package com.DSP.waveAnalysis;

import com.plotting.FFTController;
import com.riff.Signal;
import com.util.ArrayMethods;

/**
 * Windowed FFTs and some utility functions for them *.
 * 
 * @author Jonny Wildey
 * @version 1.0
 */
public class FrameFFT extends TransformComponent {

	/**
	 * Make a bunch of ffts from amplitudes *.
	 * 
	 * @param amplitudes
	 *            the amplitudes
	 * @return the complex[][]
	 */
	protected static Complex[][] fftFromAmps(double[][] amplitudes) {
		Complex[][] cs = new Complex[amplitudes.length][];
		for (int i = 0; i < cs.length; ++i) {
			cs[i] = FFT.cfft(Complex.doubleToComplex(amplitudes[i]));
		}
		return cs;
	}
	/**
	 * return the frame count of the signal with frame size *.
	 * 
	 * @param s
	 *            the s
	 * @param frameSize
	 *            the frame size
	 * @return the frame count
	 */
	protected static int getFrameCount(Signal s, int frameSize) {
		return (int) Math.ceil((double) (s.getLength()) / (double) (frameSize));
	}
	/**
	 * Convert one signal into many signals, each one a frame long*.
	 * 
	 * @param s
	 *            the s
	 * @param frame
	 *            the frame
	 * @return the double[][]
	 */
	protected static double[][] makeAmps(Signal s, int frame) {
		int chan = 0;
		int frameCount = getFrameCount(s, frame);
		double[][] amps = new double[frameCount][frame];
		for (int i = 0; i < frameCount; ++i) {
			// last case we may have to extend the array
			if (i + 1 == frameCount) {
				amps[i] = ArrayMethods.extend(
						ArrayMethods.getSubset(s.getSignal()[chan], i * frame),
						frame);
			} else { // normal case
				amps[i] = ArrayMethods.getSubset(s.getSignal()[chan],
						i * frame, ((i + 1) * frame - 1));
			}
		}
		return amps;
	}

	/**
	 * Quick normalised frequency response graph *.
	 * 
	 * @param fftbox
	 *            the fftbox
	 * @param signal
	 *            the signal
	 */
	public static void makeGraph(FFTBox fftbox, Signal signal) {
		makeGraph(fftbox, signal, 40, 20000, 600, 400);
	}

	/**
	 * Quick normalised frequency response graph with filter options*.
	 * 
	 * @param fftbox
	 *            the fftbox
	 * @param signal
	 *            the signal
	 * @param filterFrom
	 *            the filter from
	 * @param filterTo
	 *            the filter to
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 */
	public static void makeGraph(FFTBox fftbox, Signal signal, int filterFrom,
			int filterTo, int width, int height) {
		FFTBox nb = FFTBox.convertTableToDecibels(signal, fftbox);
		nb = FFTBox.filter(nb, filterFrom, filterTo);
		nb = FFTBox.logarithmicFreq(nb);
		FFTController sc = new FFTController(nb, width, height);
		sc.makeChart();
	}

	public Signal signal;

	public int frameSize;

	public FFTBox fftBox;

	/**
	 * Instantiates a new frame fft.
	 * 
	 * @param s
	 *            the s
	 */
	public FrameFFT(Signal s) {
		this(s, 2048);
	}

	/**
	 * Instantiates a new frame fft.
	 * 
	 * @param s
	 *            the s
	 * @param frameSize
	 *            the frame size
	 */
	public FrameFFT(Signal s, int frameSize) {
		this.signal = s;
		this.frameSize = frameSize; // standard
	}

	/**
	 * Perform frame analysis *.
	 * 
	 * @return the fFT box
	 */
	@Override
	public FFTBox analyse() {
		double[][] amps = makeAmps(this.signal, this.frameSize);
		Complex[][] cs = fftFromAmps(amps);
		double[][] table = makeTable(cs);
		this.fftBox = new FFTBox(table, this.signal);
		return this.fftBox;
	}

	/**
	 * Perform frame analysis *.
	 * 
	 * @param filterFrom
	 *            the filter from
	 * @param filterTo
	 *            the filter to
	 * @return the fFT box
	 */
	public FFTBox analyse(int filterFrom, int filterTo) {
		this.analyse();
		return this.filter(filterFrom, filterTo);
	}

	/**
	 * Filters the FFTFrame's FFTBox *.
	 * 
	 * @param filterFrom
	 *            the filter from
	 * @param filterTo
	 *            the filter to
	 * @return the fFT box
	 */
	public FFTBox filter(int filterFrom, int filterTo) {
		FFTBox nb = FFTBox.filter(this.fftBox, filterFrom, filterTo);
		this.fftBox = nb;
		return this.fftBox;
	}

	/**
	 * Gets the results table *.
	 * 
	 * @return the fFT box
	 */
	public FFTBox getFFTBox() {
		return this.fftBox;
	}

	/**
	 * Gets the loudest frequency bins (overall). Not incredibly useful*
	 * 
	 * @param fftBox
	 *            the fft box
	 * @return the loudest freq
	 */
	public double getLoudestFreq(FFTBox fftBox) {
		// sum each
		double[][] nt = FFTBox.getSumFFTBox(fftBox).getTable();
		return ArrayMethods.getMax(nt[1]);
	}

	/**
	 * Has the FFT performed any analysis? *.
	 * 
	 * @return true, if successful
	 */
	@Override
	public boolean hasAnalysed() {
		return this.fftBox != null;
	}

	/**
	 * Quick normalised frequency response graph *.
	 */
	@Override
	public void makeGraph() {
		makeGraph(40, 20000, 600, 400);
	}

	/**
	 * Quick normalised frequency response graph with filter options*.
	 * 
	 * @param filterFrom
	 *            the filter from
	 * @param filterTo
	 *            the filter to
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 */
	public void makeGraph(int filterFrom, int filterTo, int width, int height) {
		FrameFFT.makeGraph(this.getFFTBox(), this.signal, filterFrom, filterTo,
				width, height);
	}

	/**
	 * Makes a frequency response table *.
	 * 
	 * @param cs
	 *            the cs
	 * @return the double[][]
	 */
	protected double[][] makeTable(Complex[][] cs) {
		double[][] table = new double[cs.length + 1][];
		table[0] = FFT.getFreqRow(this.signal, cs[0].length);
		for (int i = 1; i < table.length; ++i) {
			table[i] = FFT.normalise(Complex.getMagnitudes(cs[i - 1]));
		}
		return table;
	}

}
