package com.waveAnalysis;

import java.util.Arrays;

import com.filemanager.ArrayMethods;
import com.filemanager.Log;
import com.riff.Signal;

public class FrameBlurFFT extends FrameFFT{
	
	
	public FrameBlurFFT(Signal s, int frameSize) {
		super(s, frameSize);
	}

	public FrameBlurFFT(Signal s) {
		super(s);
	}
	
	/**Overload of FrameFFT**/
	protected static int getFrameCount(Signal s, int frameSize) {
		return (int) Math.ceil(
				(double)(s.getLength()) / (double)(frameSize) * 2);
	}
	
	/** Convert one signal into many signals, each one a frame long**/
	protected static double[][] makeBlurAmps(Signal s, int frame) {
		int chan = 0;
		int frameCount = getFrameCount(s, frame);
		double[][] amps = new double[frameCount][frame];
		for (int i = 0; i < frameCount; ++i) {
			//last case we may have to extend the array
			//Log.d(i + "  " + frameCount);
			if (i + 2 >= frameCount) {
				amps[i] = ArrayMethods.extend(
						ArrayMethods.getSubset(
								s.getSignal()[chan], i * frame / 2), 
						frame / 2);
			} else { //normal case
				amps[i] = ArrayMethods.getSubset(s.getSignal()[chan], 
						i * frame / 2, (((i + 1) * frame /2) - 1));
			}
		}
		return amps;
	}
	
	/**Perform frame analysis **/
	@Override
	public FFTBox analyse() {
		double[][] amps = makeBlurAmps(this.signal, this.frameSize);
		Complex[][] cs = fftFromAmps(amps);
		double[][] table = makeTable(cs);
		this.fftBox = new FFTBox(table, this.signal);
		return this.fftBox;
	}


}
