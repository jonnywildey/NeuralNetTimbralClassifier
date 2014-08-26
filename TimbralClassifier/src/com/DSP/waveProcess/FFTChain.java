package com.DSP.waveProcess;

import com.DSP.waveAnalysis.FFTBox;
import com.DSP.waveAnalysis.FrameFFT;
import com.riff.Signal;
import com.util.ArrayMethods;
import com.util.Log;

/**
 * FFT analysis chains *.
 *
 * @author Jonny Wildey
 * @version 1.0
 */
public class FFTChain {

	/**
	 * Instantiates a new fFT chain.
	 */
	public FFTChain() {
	}
	
	/**
	 * Poly fft chain.
	 *
	 * @param signals the signals
	 * @return the fFT box
	 */
	public static FFTBox polyFFTChain(Signal signals) {
		FrameFFT fft = new FrameFFT(signals, 4096);
		FFTBox dd = fft.analyse(20, 20000);
		//fft.makeGraph();
		dd = FFTBox.getSumTable(dd, 7);
		dd = FFTBox.getHiResBarkedSubset(dd, 0.5);
		dd = FFTBox.normaliseTable(dd, 10);
		return dd;
	}
	
	/**
	 * Poly fft chain with split.
	 *
	 * @param signals the signals
	 * @return the fFT box
	 */
	public static FFTBox polyFFTChainSplit(Signal signals) {
		FrameFFT fft = new FrameFFT(signals, 4096);
		FFTBox dd = fft.analyse(20, 20000);
		FFTBox ee = new FFTBox(dd);
		//fft.makeGraph();
		ee = FFTBox.getSumTable(dd, 0, 2);
		dd = FFTBox.getSumTable(dd, 2, 7);
		ee = FFTBox.getHiResBarkedSubset(ee, 1);
		dd = FFTBox.getHiResBarkedSubset(dd, 1);
		double[] vals = ArrayMethods.concat(ee.getValues()[0], dd.getValues()[0]);
		double[] fr = ArrayMethods.concat(ee.getFreqRow(), dd.getFreqRow());
		double[][] nt = new double[][]{fr, vals};
		FFTBox n = new FFTBox(nt);
		n = FFTBox.normaliseTable(n, 10);
		//Log.d(n.toString());
		return n;
	}
	
	/**
	 * Mono fft chain.
	 *
	 * @param signals the signals
	 * @return the fFT box
	 */
	public static FFTBox monoFFTChain(Signal signals) {
		FrameFFT fft = new FrameFFT(signals, 4096);
		FFTBox dd = fft.analyse(20, 20000);
		//fft.makeGraph();
		dd = FFTBox.getSumTable(dd, 7);
		dd = FFTBox.getHiResBarkedSubset(dd, 1);
		dd = FFTBox.normaliseTable(dd, 10);
		return dd;
	}

}
