package waveAnalysis;

import java.util.Arrays;

import riff.Signal;
import filemanager.ArrayStuff;
import filemanager.Log;

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
				amps[i] = ArrayStuff.extend(
						ArrayStuff.getSubset(
								s.getSignal()[chan], i * frame / 2), 
						frame / 2);
			} else { //normal case
				amps[i] = ArrayStuff.getSubset(s.getSignal()[chan], 
						i * frame / 2, (((i + 1) * frame /2) - 1));
			}
		}
		return amps;
	}
	
	/**Perform frame analysis **/
	@Override
	public double[][] analyse() {
		double[][] amps = makeBlurAmps(this.signal, this.frameSize);
		Complex[][] cs = fftFromAmps(amps);
		double[][] table = makeTable(cs);
		this.table = table;
		return table;
	}

	/** basic noise cancelling. Also removes first one of array 
	 * @return **/
	public double[][] sumDifference() {
		double[][] nt = new double[table.length - 1][table[0].length];
		nt[0] = table[0];
		for (int i = 2; i <= nt.length; ++i) {
			for (int j = 0; j < this.table[i].length; ++j) {
			nt[i - 1][j] = (this.table[i - 1][j] - this.table[i][j]); 
			}
		}
		//Log.d(Arrays.deepToString(nt));
		
		this.table = nt;
		return this.table;
	}


}
