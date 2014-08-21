package com.waveAnalysis;

import java.io.File;

import com.filemanager.ArrayMethods;
import com.riff.Signal;

/** Abstract class for various signal transform components like FFT and DCT **/
public abstract class TransformComponent {

	protected Signal signal;
	protected double frameSize;
	protected double[] amplitudes; //signal values to get processing from
	protected FFTBox fftBox; //where the transform signal goes

	public TransformComponent() {
		super();
	}

	/** is number a power of 2? **/
	protected static boolean powerOf2(int number) {
		double x = Math.log(number) / Math.log(2);
		return (x - Math.rint(x) == 0);
	}

	/**return signal with array length extended to a power of two **/
	public static double[] getArrayToPowerOf2(double[] signals) {
		double ls = Math.log((double)signals.length) / Math.log(2);
		if (Math.floor(ls) == ls) { //if already is a power of two
			return signals;
		} else {
			int frameSize = (int) Math.pow(2,(Math.floor(
					ls + 1)));
			return ArrayMethods.extend(signals, frameSize);
		}
	}

	/**return signal extended to a power of two **/
	public void calculateSignalSize(double[] signals) {
		double ls = Math.log((double)signals.length) / Math.log(2);
		if (Math.floor(ls) == ls) { //if already is a power of two
			this.frameSize = signals.length;
			this.amplitudes = signals;
		} else {
			this.frameSize = (int) Math.pow(2,(Math.floor(
					ls + 1)));
			this.amplitudes = ArrayMethods.extend(signals, (int) this.frameSize);
		}
	}

	public String toString() {
		if (!hasAnalysed()) {
			return "";
		}
		else {
			return this.fftBox.toString();
		}
	}
	
	

	public boolean hasAnalysed() {
		return false;
	}

	public FFTBox analyse() {
		// TODO Auto-generated method stub
		return null;
	}

	public void makeGraph() {
		// TODO Auto-generated method stub
		
	}

}