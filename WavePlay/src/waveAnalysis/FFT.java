package waveAnalysis;

import java.util.Arrays;

import plotting.FFTController;
import plotting.SignalController;
import filemanager.ArrayStuff;
import filemanager.Log;
import riff.Signal;


public class FFT {
	
	private double sampleRate;
	private double frameSize; //must be power of two
	private double[] amplitudes;
	private Complex[] cValues;
	protected double[] magnitudes;
	private double[] freqRow;
	protected double[][] table;
	
	public FFT() {		
	}
	
	public FFT(double[] amplitudes, double sampleRate, int frameSize) {
		this.sampleRate = sampleRate;
		this.frameSize = frameSize;
		this.amplitudes = amplitudes;
	}
	
	/**Using the inverse FFT create a new signal **/
	public Signal synthesiseSignal() {
		double[][] s = new double[][]{
				Complex.getReals(FFT.slow(this.cValues))};
		return new Signal(s, 24, (int) this.sampleRate);
	}
	
	
	
	
	public FFT(Signal s) {
		this.sampleRate = s.getSampleRate();
		//find nearest bigger frame size
		this.frameSize = (int) Math.pow(2,(Math.floor(
				Music.log(s.getSignal()[0].length, 2)) + 1));
		this.amplitudes = ArrayStuff.extend(s.getSignal()[0], (int) this.frameSize);
	}
	
	public double[][] filter(double from, double to) {
		//get min and max
		int min = 0;
		int max = 0;
		for (int i = 0; i < this.magnitudes.length; ++i) {
			if (this.freqRow[i] > from) {
				min = i;
				break;
			}
		}
		for (int i = this.magnitudes.length - 1; i >= 0; --i) {
			if (this.freqRow[i] < to) {
				max = i;
				break;
			}
		}
		this.freqRow = ArrayStuff.getSubset(this.freqRow, min, max);
		this.magnitudes = ArrayStuff.getSubset(this.magnitudes, min, max);
		this.table = new double[][]{this.freqRow, this.magnitudes};
		return this.table;
	}
	
	
	public double[][] analyse() {
		this.cValues = cfft(Complex.doubleToComplex(this.amplitudes));
		this.magnitudes = Complex.getMagnitudes(this.cValues);
		this.freqRow = this.getFreqRow();
		this.table = new double[][]{this.freqRow, this.magnitudes};
		return this.table;
		//return ArrayStuff.flip(new double[][]{freqRow, magnitudes});
	}
	
	/** only returns values up to a particular frequency **/
	public double[][] analyse(int toFreq) {
		double[][] vals = this.analyse();
		int limit = 0;
		for (int i = 0; i < vals.length; ++i) {
			if (vals[i][0] > toFreq) {
				limit = i;
				break;
			}
		}
		return ArrayStuff.extend(vals, limit);
	}
	
	public double[] getFreqRow() {
		double sr = this.sampleRate / this.frameSize;
		double[] fr = new double[this.amplitudes.length];
		for (int i = 0; i < this.amplitudes.length; ++i) {
			fr[i] = i * sr;
		}
		return fr;
	}
	
	public void makeChart() {
		FFTController sc = new FFTController(this, 600, 400);
		sc.makeChart();
	}

	private static Complex[] cfft(Complex[] amplitudes) {
		double bigN = amplitudes.length;
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
	
	public static Complex[] slow(Complex[] amplitudes) {
		double n = amplitudes.length;
		//conjugate...
		for (int i = 0; i < n; ++i) {
			amplitudes[i].im *= -1;
		}
		//apply transform
		amplitudes = cfft(amplitudes);
		Complex[] as = new Complex[(int) (2 * n)];
		for(int i = 0; i < n * 2; ++i)
		{
			//conjugate again
			as[i] = new Complex(amplitudes[i / 2].re, amplitudes[i / 2].im * -1);
			//scale
			as[i].re /= (n * 2);
			as[i].im /= (n * 2);
		}
		return as;
	}
	
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
