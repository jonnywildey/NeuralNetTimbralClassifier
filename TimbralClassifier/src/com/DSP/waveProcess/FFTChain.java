package com.DSP.waveProcess;

import com.DSP.waveAnalysis.FFTBox;
import com.DSP.waveAnalysis.FrameFFT;
import com.riff.Signal;

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
