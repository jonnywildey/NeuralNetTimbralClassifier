package waveAnalysis;

import filemanager.ArrayStuff;
import filemanager.Log;
import riff.Signal;


public class FFT {
	
	private double sampleRate;
	private double frameSize; //must be power of two
	private double[] amplitudes;
	private Complex[] cValues;
	private double[] magnitudes;
	private double[] freqRow;
	
	public FFT() {		
	}
	
	public FFT(double[] amplitudes, double sampleRate, int frameSize) {
		this.sampleRate = sampleRate;
		this.frameSize = frameSize;
		this.amplitudes = amplitudes;
	}
	
	public FFT(Signal s) {
		this.sampleRate = s.getSampleRate();
		this.frameSize = (int) Math.pow(2,(Math.floor(Music.log(s.getSignal()[0].length, 2)) + 1));
		this.amplitudes = ArrayStuff.extend(s.getSignal()[0], (int) this.frameSize);
	}
	
	
	public double[][] analyse() {
		this.cValues = cfft(Complex.doubleToComplex(this.amplitudes));
		this.magnitudes = Complex.getMagnitudes(this.cValues);
		this.freqRow = this.getFreqRow();
		return ArrayStuff.flip(new double[][]{freqRow, magnitudes});
	}
	
	/** only returns values up to a particular frequency **/
	public double[][] analyse(int toFreq) {
		double[][] vals = this.analyse();
		int limit = 0;
		Log.d("lrng" + vals[0].length);
		for (int i = 0; i < vals.length; ++i) {
			if (vals[i][0] > toFreq) {
				limit = i;
				break;
			}
		}
		Log.d(limit);
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

}
