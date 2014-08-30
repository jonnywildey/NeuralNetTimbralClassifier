package com.DSP.waveAnalysis;

import com.plotting.FFTDifferenceController;
import com.util.ArrayMethods;

/**
 * Object for analysing the difference in values between successive FFT windows.
 * 
 * @author Jonny
 */
public class FFTDifference extends TransformComponent {

	/**
	 * Obtain the difference table *.
	 * 
	 * @param table
	 *            the table
	 * @param frames
	 *            the frames
	 * @return the difference table
	 */
	public static double[][] getDifferenceTable(double[][] table, int frames) {
		// array is one less than table (because of difference)
		double[][] dif = new double[frames + 1][table[0].length];
		dif[0] = table[0]; // freq rows are the same
		for (int i = 2; i < frames + 1; ++i) {
			// Log.d(table[i].length);
			for (int j = 0; j < table[0].length; ++j) {
				dif[i - 1][j] = (table[i - 1][j] - table[i][j]);
				// Log.d(dif[i-1][j]);
			}

		}
		return dif;
	}

	/**
	 * split the frequency bins into x domains and average them *.
	 * 
	 * @param table
	 *            the table
	 * @param bands
	 *            the bands
	 * @return the double[][]
	 */
	public static double[][] splitAndAverage(double[][] table, int bands) {
		double length = table[0].length;
		int bw = (int) (length / (double) bands);
		double[][] nb = new double[table.length][bands];
		// relabel
		for (int i = 0; i < bands; ++i) {
			nb[0][i] = table[0][i * bw];
		}
		// get averages
		for (int i = 1; i < nb.length; ++i) {
			for (int j = 0; j < bands; ++j) {
				nb[i][j] = ArrayMethods.getAverageOfSubset(table[i], j * bw,
						(j + 1) * bw);
			}
		}
		return nb;
	}

	protected FrameFFT frameFFT;

	/**
	 * Instantiates a new fFT difference.
	 * 
	 * @param frameFFT
	 *            the frame fft
	 */
	public FFTDifference(FrameFFT frameFFT) {
		this.frameFFT = frameFFT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.DSP.waveAnalysis.TransformComponent#analyse()
	 */
	@Override
	public FFTBox analyse() {
		double[][] table = getDifferenceTable(this.frameFFT.getFFTBox()
				.getTable(), this.frameFFT.getFFTBox().getTable().length - 1);
		return new FFTBox(table, this.frameFFT.signal);
	}

	/**
	 * Analyse.
	 * 
	 * @param frames
	 *            the frames
	 * @return the fFT box
	 */
	public FFTBox analyse(int frames) {
		double[][] table = getDifferenceTable(this.frameFFT.getFFTBox()
				.getTable(), frames);
		return new FFTBox(table, this.frameFFT.signal);
	}

	/**
	 * Quick normalised frequency response graph *.
	 */
	@Override
	public void makeGraph() {
		makeGraph(40, 20000, 800, 600);
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
		FFTDifferenceController sc = new FFTDifferenceController(
				FFTBox.logarithmicFreq(FFTBox.filter(
						// FrameFFT.convertTableToDecibels(this.frameFFT.signal,
						// this.table, this.frameFFT.frameSize),
						this.fftBox, filterFrom, filterTo)), width, height);
		sc.makeChart();
	}
}
