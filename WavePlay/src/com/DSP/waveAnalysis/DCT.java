package com.DSP.waveAnalysis;

import com.plotting.DCTController;
import com.plotting.FFTController;
import com.riff.Signal;
import com.util.ArrayMethods;
import com.util.Log;

/**
 * Direct cosine transform *.
 *
 * @author Jonny Wildey
 * @version 1.0
 */
public class DCT extends TransformComponent{
	
	/**
	 * Custom frame size constructor *.
	 *
	 * @param s the s
	 * @param frameSize the frame size
	 */
	public DCT(Signal s, int frameSize) {
		this.signal = s;
		this.frameSize = frameSize;
		this.amplitudes = s.getSignal()[0]; //assume left
	}
	
	/**
	 * Uses smallest frame size that can cover entire signal *.
	 *
	 * @param s the s
	 */
	public DCT(Signal s) {
		this.signal = s;
		//find nearest bigger frame size
		calculateSignalSize(s.getSignal()[0]);
	}
	
	
	
	/**
	 * DCT algorithm *.
	 *
	 * @param signal the signal
	 * @return the double[]
	 */
	public static double[] dct(double[] signal) {
		//generate new signal
		double[] ns = alterRow(signal);
		Complex[] fft = FFT.cfft(Complex.doubleToComplex(ns));
		//remove half
		fft = Complex.getSubset(fft, 0, (fft.length / 2) - 1);
		double[] dct = newDCT(fft);
		//dct = ArrayStuff.normalizeDouble(dct, 100000000);
		return dct;
	}
	
	/**
	 * perfoms transform and returns it in a FFTBox *.
	 *
	 * @return the fFT box
	 */
	@Override
	public FFTBox analyse() {
		double[] amps = FFT.getArrayToPowerOf2(signal.getSignal()[0]);
		double[] freqRow = ArrayMethods.extend(DCT.getFreqRow(signal, amps.length), amps.length / 2);
		amps = dct(amps);
		double[][] nd = new double[][]{freqRow, amps};
		this.fftBox = new FFTBox(nd, signal);
		return this.fftBox;
	}
	
	/**
	 * Transforms and filters *.
	 *
	 * @param from the from
	 * @param to the to
	 * @return the fFT box
	 */
	public FFTBox analyse(int from, int to) {
		this.analyse();
		return this.filter(from, to);
	}
	
	/**
	 * Filters the FFTBox *.
	 *
	 * @param from the from
	 * @param to the to
	 * @return the fFT box
	 */
	public FFTBox filter(int from, int to) {
		if (hasAnalysed()) {
			this.fftBox = FFTBox.filter(this.fftBox, from, to);
			return  this.fftBox;
		}
		else {
			return null;
		}
	}
	
	/**
	 * alter the row to allow for DCT *.
	 *
	 * @param signal the signal
	 * @return the double[]
	 */
	private static double[] alterRow(double[] signal) {
		double[] ns = new double[signal.length];
		for  (int i = 0; i < signal.length / 2; ++i) {
				ns[i] = signal[2 * i];
				ns[signal.length - 1 - i] = signal[2 * i + 1];
		}
		return ns;
	}
	
	/**
	 * return the frequency row. Slightly different than FFT maybe *
	 *
	 * @param s the s
	 * @param frameSize the frame size
	 * @return the freq row
	 */
	public static double[] getFreqRow(Signal s, double frameSize) {
		double sr = s.getSampleRate() / frameSize / 2;
		double[] fr = new double[(int) frameSize];
		for (int i = 0; i < frameSize; ++i) {
			fr[i] = i * sr;
		}
		return fr;
	}
	
	/**
	 * do the * e to the power part *.
	 *
	 * @param fft the fft
	 * @return the double[]
	 */
	private static double[] newDCT(Complex[] fft) {
		double[] dct = new double[fft.length];
		double e = 0;
		for (int i = 0; i < fft.length; ++i) {
			e = Math.pow(Math.E, -1d * (double)i * Math.PI / (double)(fft.length * 2));
			//Log.d( + " " + fft[i].re);
			dct[i] = e  * fft[i].re;
		}
		return dct;
	}
	
	/**
	 * do the * e to the power part *.
	 *
	 * @param s the s
	 * @return the complex[]
	 */
	private static Complex[] newIDCT(double[] s) {
		Complex[] dct = new Complex[s.length];
		double e = 0;
		for (int i = 0; i < s.length; ++i) {
			e = Math.pow(Math.E, -1d * (double)i * Math.PI / (double)(s.length * 2));
			//Log.d( + " " + fft[i].re);
			dct[i] = new Complex((double)s[i] / e);
		}
		return dct;
	}
	
	/**
	 * inverse operation of alter row *.
	 *
	 * @param vals the vals
	 * @return the double[]
	 */
	private static double[] irearrange(double[] vals) {
		double[] ns = new double[vals.length];
		for (int i = 0; i < vals.length / 2; ++i) {
			ns[2 * i] = vals[i];
			ns[2 * i + 1] = vals[vals.length - 1 - i];
		}
		return ns;
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
	 * @param filterFrom the filter from
	 * @param filterTo the filter to
	 * @param width the width
	 * @param height the height
	 */
	public void makeGraph(int filterFrom, int filterTo, int width, int height) {
		FFTBox fb = flipAndRemove(this.fftBox); 
		DCTController sc = new DCTController(FFTBox.logarithmicFreq(FFTBox.filter(
						fb, 
						filterFrom, filterTo)), width, height);
		sc.makeChart();
	}
	
	/**
	 * Inverse DCT *.
	 *
	 * @param s the s
	 * @return the double[]
	 */
	public static double[] idct(double[] s) {
		Complex[] s2 = newIDCT(s);
		double[] s3 = Complex.getReals(FFT.icfft(s2));
		s3 = irearrange(s3);
		return s3;
	}
	
	/**
	 * Simplifies DCT by only using the negative component
	 * and flipping it into positive.
	 *
	 * @param dctBox the dct box
	 * @return the fFT box
	 */
	public static FFTBox flipAndRemove(FFTBox dctBox) {
		double[][] vals = dctBox.getTable();
		double[][] newVals = new double[vals.length][vals[0].length];
		newVals[0] = vals[0];
		for (int i = 1; i < vals.length; ++i) {
			for (int j = 0; j < vals[i].length; ++j) {
				newVals[i][j] = (vals[i][j] > 0) ? 0 : vals[i][j] * -1; 
			}
		}
		return new FFTBox(newVals, dctBox);
	}
	
}
