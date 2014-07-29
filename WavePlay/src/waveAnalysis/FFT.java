package waveAnalysis;

import java.util.Arrays;

import plotting.FFTController;
import plotting.SignalController;
import filemanager.ArrayStuff;
import filemanager.Log;
import riff.Signal;

/**Basic FFT algorithm implemented with lots of 
 * help from Rosetta Code
 * @author Jonny
 *
 */
public class FFT {
	
	protected Signal signal; //initial signal
	protected double frameSize; //must be power of two
	protected double[] amplitudes; //amplitudes from signal
	protected Complex[] cValues; //FFT complex values
	protected double[] magnitudes; //Magnitudes from FFT values
	protected double[] freqRow; //frequency row
	protected double[][] table; //freqRow & magnitudes
	
	public FFT() {		
	}
	
	/** Custom frame size constructor **/
	public FFT(Signal s, int frameSize) {
		this.signal = s;
		this.frameSize = frameSize;
		this.amplitudes = s.getSignal()[0]; //assume left
	}
	
	/** Custom frame size constructor **/
	public FFT(Signal s) {
		this.signal = s;
		//find nearest bigger frame size
		calculateSignalSize(s.getSignal()[0]);
		
	}
	
	/*return signal extended to a power of two **/
	public void calculateSignalSize(double[] signals) {
		double ls = Math.log((double)signals.length) / Math.log(2);
		if (Math.floor(ls) == ls) { //if already is a power of two
			this.frameSize = signals.length;
			this.amplitudes = signals;
		} else {
			this.frameSize = (int) Math.pow(2,(Math.floor(
					ls + 1)));
			this.amplitudes = ArrayStuff.extend(signals, (int) this.frameSize);
		}
	}
	
	/*return signal extended to a power of two **/
	public static double[] getPowerArray(double[] signals) {
		double ls = Math.log((double)signals.length) / Math.log(2);
		if (Math.floor(ls) == ls) { //if already is a power of two
			return signals;
		} else {
			int frameSize = (int) Math.pow(2,(Math.floor(
					ls + 1)));
			return ArrayStuff.extend(signals, frameSize);
		}
	}
	
	
	
	/**Using the inverse FFT create a new signal **/
	public Signal invertFFTSignal() {
		double[][] s = new double[][]{
				ArrayStuff.getSubset(
						Complex.getReals(FFT.icfft(this.cValues)), 
						0, this.signal.getLength())
				};
		return new Signal(s, this.signal.getBit(), 
				(int) this.signal.getSampleRate());
	}
	
	/**Filters an FFT by frequency. Does not care about frame size
	 * (as long as to and from frequencies exist) **/
	public static double[][] filter(double[][] table, double from, double to) {
		//get min and max
		int min = 0;
		int max = 0;
		double[] magnitudes = table[1];
		double[] freqRow = table[0];
		//get array positions of to and from
		for (int i = 0; i < freqRow.length; ++i) {
			if (freqRow[i] > from) {
				min = i;
				break;
			}
		}
		for (int i = freqRow.length - 1; i >= 0; --i) {
			if (freqRow[i] < to) {
				max = i;
				break;
			}
		}
		//make new table
		double[][] newt = new double[table.length][];
		for (int i = 0; i < newt.length; ++i) {
			newt[i] = ArrayStuff.getSubset(table[i], min, max);
			
		}
		return newt;
	}
	
	/** Perform FFT analysis. get table of frequencies
	 * and magnitudes **/
	public double[][] analyse() {
		this.cValues = cfft(Complex.doubleToComplex(this.amplitudes));
		makeFromValues();
		return this.table;
	}
	
	/** is number a power of 2? **/
	protected static boolean powerOf2(int number) {
		double x = Math.log(number) / Math.log(2);
		return (x - Math.rint(x) == 0);
	}
	
	/** updates magnitudes etc. from cValues **/
	protected void makeFromValues() {
		this.magnitudes = Complex.getMagnitudes(this.cValues);
		this.freqRow = FFT.getFreqRow(this.signal, this.frameSize);
		this.table = new double[][]{this.freqRow, this.magnitudes};
	}
	
	/** analyses, then filters. This affects the table in the FFT **/
	public double[][] analyse(int fromFreq, int toFreq) {
		double[][] vals = this.analyse();
		vals = FFT.filter(vals, fromFreq, toFreq);
		this.table = vals;
		return vals;
	}
	
	/** return the frequencies **/
	public static double[] getFreqRow(Signal s, double frameSize) {
		double sr = s.getSampleRate() / frameSize;
		double[] fr = new double[(int) frameSize];
		for (int i = 0; i < frameSize; ++i) {
			fr[i] = i * sr;
		}
		return fr;
	}
	
	/**Quick normalised frequency response graph **/
	public void makeGraph() {
		makeGraph(40, 20000, 800, 600);
	}
	/**Quick normalised frequency response graph with filter options**/
	public void makeGraph(int filterFrom, int filterTo, int width, int height) {
		FFTController sc = new FFTController(FrameFFT.logarithmicFreq(FFT.filter(
						FrameFFT.convertTableToDecibels(this.signal, this.table, this.frameSize), 
						filterFrom, filterTo)), width, height);
		sc.makeChart();
	}
	
	
	/** the cfft algorithm **/
	protected static Complex[] cfft(Complex[] amplitudes) {
		double bigN = amplitudes.length;
		//Log.d("BN" + bigN);
		if (bigN <= 1) { //base case
			return amplitudes;
		}
		double halfN = bigN / 2;
		Complex[] evens = new Complex[(int) halfN];
		Complex[] odds = new Complex[(int) halfN];
		//this doesn't work for odd values, 
		//as this is happening recursively 
		//I assume this is why frame has to be a 
		//value of 2...
		//divide
		for (int i = 0; i < halfN; ++i ) {
			evens[i] = amplitudes[i*2];
			odds[i] = amplitudes[i*2 + 1]; 
		}
		//conquer
		evens = cfft(evens);
		odds = cfft(odds);
		double a = -2*Math.PI; //
		//combine
		for (int i = 0; i < halfN; ++i) {
			double p = i/bigN;
			Complex t = new Complex(0, a * p).exp();
			t = t.multiply(odds[i]); //could be wrong way around...
			amplitudes[i] = t.add(evens[i]); //same
			amplitudes[(int) (i + halfN)] = evens[i].subtract(t);
		}
		return amplitudes;
	}
	
	public static Complex[] icfft(Complex[] amplitudes) {
		double n = amplitudes.length;
		//conjugate...
		for (int i = 0; i < n; ++i) {
			amplitudes[i].im *= -1;
		}
		//apply transform
		amplitudes = cfft(amplitudes);
		for(int i = 0; i < n; ++i)
		{
			//conjugate again
			amplitudes[i].im *= -1;
			//scale
			amplitudes[i].re /= n;
			amplitudes[i].im /= n;
		}
		return amplitudes;
	}
	
	/*	EXAMPLE OF PITCH SHIFTING
	 * public static Complex[] slow(Complex[] amplitudes) {
		double slow = 2;
		double n = amplitudes.length;
		//conjugate...
		for (int i = 0; i < n; ++i) {
			amplitudes[i].im *= -1;
		}
		//apply transform
		amplitudes = cfft(amplitudes);
		Complex[] as = new Complex[(int) (slow * n)];
		double[] reals = Complex.getReals(amplitudes);
		double[] ims = Complex.getImaginary(amplitudes);
		for(int i = 0; i < n * slow; ++i)
		{
			//conjugate again
			as[i] = new Complex(ArrayStuff.linearApproximate(reals, (double)(i) / slow), 
					ArrayStuff.linearApproximate(ims, (double)(i) / slow) * -1);
			 
			//scale
			as[i].re /= n;
			as[i].im /= n;
		}
		return as;
	}*/
	
	public boolean hasAnalysed() {
		return this.cValues != null;
	}
	
	
	public String toString() {
		if (!hasAnalysed()) {
			return "";
		}
		StringBuilder sb = new StringBuilder(20 * this.cValues.length);
		for (int i = 0; i < this.magnitudes.length; ++i) {
			sb.append("\n");
			sb.append(this.freqRow[i] + "hz: ");
			sb.append(this.magnitudes[i]);
		}
		return sb.toString();
	}
	
	

	public double[][] getTable() {
		return this.table;
	}
	


}
