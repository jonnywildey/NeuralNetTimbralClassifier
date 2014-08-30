package com.DSP.waveAnalysis;

import com.riff.Signal;
import com.util.ArrayMethods;

/**
 * staggered FFT window analysis *.
 * 
 * @author Jonny Wildey
 * @version 1.0
 */
public class FrameBlurFFT extends FrameFFT {

	/**
	 * Overload of FrameFFT*.
	 * 
	 * @param s
	 *            the s
	 * @param frameSize
	 *            the frame size
	 * @return the frame count
	 */
	protected static int getFrameCount(Signal s, int frameSize) {
		return (int) Math.ceil((double) (s.getLength()) / (double) (frameSize)
				* 2);
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
	protected static double[][] makeBlurAmps(Signal s, int frame) {
		int chan = 0;
		int frameCount = getFrameCount(s, frame);
		double[][] amps = new double[frameCount][frame];
		for (int i = 0; i < frameCount; ++i) {
			// last case we may have to extend the array
			// Log.d(i + "  " + frameCount);
			if (i + 2 >= frameCount) {
				amps[i] = ArrayMethods.extend(
						ArrayMethods.getSubset(s.getSignal()[chan], i * frame
								/ 2), frame / 2);
			} else { // normal case
				amps[i] = ArrayMethods.getSubset(s.getSignal()[chan], i * frame
						/ 2, (((i + 1) * frame / 2) - 1));
			}
		}
		return amps;
	}

	/**
	 * Instantiates a new frame blur fft.
	 * 
	 * @param s
	 *            the s
	 */
	public FrameBlurFFT(Signal s) {
		super(s);
	}

	/**
	 * Instantiates a new frame blur fft.
	 * 
	 * @param s
	 *            the s
	 * @param frameSize
	 *            the frame size
	 */
	public FrameBlurFFT(Signal s, int frameSize) {
		super(s, frameSize);
	}

	/**
	 * Perform frame analysis *.
	 * 
	 * @return the fFT box
	 */
	@Override
	public FFTBox analyse() {
		double[][] amps = makeBlurAmps(this.signal, this.frameSize);
		Complex[][] cs = fftFromAmps(amps);
		double[][] table = makeTable(cs);
		this.fftBox = new FFTBox(table, this.signal);
		return this.fftBox;
	}

}
