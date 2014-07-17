package waveAnalysis;

import java.util.Arrays;

import riff.Signal;
import filemanager.ArrayStuff;
import filemanager.Log;


/**Bunch of FFTS
 */
public class FrameFFT{
	
	public Signal signal;
	public FFT[] ffts; //all the ffts
	public int frameSize;
	public int frameCount; //number of frames
	
	public FrameFFT(Signal s, int frameSize) {
		this.signal = s;
		this.frameSize = frameSize; //standard
		int frameCount = getFrameCount(this.signal, this.frameSize);
		this.ffts = new FFT[frameCount];
	}
	
	public FrameFFT(Signal s) {
		this(s, 2048);
	}
	
	protected static int getFrameCount(Signal s, int frameSize) {
		return (int) Math.ceil(
				(double)(s.getLength()) / (double)(frameSize));
	}
	
	/** Convert one signal into many signals, each one a frame long**/
	protected static double[][] makeAmps(Signal s, int frame) {
		int chan = 0;
		int frameCount = getFrameCount(s, frame);
		double[][] amps = new double[frameCount][frame];
		for (int i = 0; i < frameCount; ++i) {
			//last case we may have to extend the array
			if (i + 1 == frameCount) {
				amps[i] = ArrayStuff.extend(
						ArrayStuff.getSubset(
								s.getSignal()[chan], i * frame), 
						frame);
			} else { //normal case
				amps[i] = ArrayStuff.getSubset(s.getSignal()[chan], 
						i * frame, ((i + 1) * frame - 1));
			}
		}
		return amps;
	}
	
	/**Make a bunch of ffts from amplitudes **/
	protected static Complex[][] fftFromAmps(double[][] amplitudes) {
		Complex[][] cs = new Complex[amplitudes.length][];
		for (int i = 0 ; i < cs.length; ++i) {
			cs[i] = FFT.cfft(Complex.doubleToComplex(amplitudes[i]));
		}
		return cs;
	}
	
	/** Makes a frequency response table **/
	protected double[][] makeTable(Complex[][] cs) {
		double[][] table = new double[cs.length + 1][];
		table[0] = FFT.getFreqRow(this.signal, cs[0].length);
		for (int i = 1; i < table.length; ++i) {
			table[i] = Complex.getMagnitudes(cs[i - 1]);
		}
		return table;
	}
	
	/**Perform frame analysis **/
	public double[][] analyse() {
		double[][] amps = makeAmps(this.signal, this.frameSize);
		Complex[][] cs = fftFromAmps(amps);
		double[][] table = makeTable(cs);
		return table;
	}
	
	/**Perform frame analysis **/
	public double[][] analyse(int filterFrom, int filterTo) {
		double[][] table = analyse();
		return FFT.filter(table, filterFrom, filterTo);
	}
	


}
