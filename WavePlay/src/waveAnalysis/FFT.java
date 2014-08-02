package waveAnalysis;

import java.util.Arrays;

import plotting.FFTController;
import plotting.SignalController;
import filemanager.ArrayMethods;
import filemanager.Log;
import riff.Signal;

/**Basic FFT algorithm implemented with lots of 
 * help from Rosetta Code
 * @author Jonny
 *
 */
public class FFT extends TransformComponent {
	
	protected Complex[] cValues; //complex values of FFT (unnormalised)
	
	public FFT() {		
	}
	
	/** Custom frame size constructor **/
	public FFT(Signal s, int frameSize) {
		this.signal = s;
		this.frameSize = frameSize;
		this.amplitudes = s.getSignal()[0]; //assume left
	}
	
	/** Uses smallest frame size that can cover entire signal **/
	public FFT(Signal s) {
		this.signal = s;
		//find nearest bigger frame size
		calculateSignalSize(s.getSignal()[0]);
	}
	
	/**Using the inverse FFT create a new signal **/
	public Signal invertFFTSignal() {
		double[][] s = new double[][]{
				ArrayMethods.getSubset(
						Complex.getReals(FFT.icfft(this.cValues)), 
						0, this.signal.getLength())
				};
		return new Signal(s, this.signal.getBit(), 
				(int) this.signal.getSampleRate());
	}
	
	/** Perform FFT analysis. get table of frequencies
	 * and magnitudes **/
	@Override
	public FFTBox analyse() {
		this.cValues = cfft(Complex.doubleToComplex(this.amplitudes));
		makeFromValues();
		return this.fftBox;
	}
	
	/** updates magnitudes etc. from cValues **/
	protected void makeFromValues() {
		this.fftBox = new FFTBox(
				new double[][]{FFT.getFreqRow(this.signal, this.frameSize), 
						normalise(Complex.getMagnitudes(this.cValues))}, 
						this.signal);
	}
	
	/** analyses, then filters. This affects the table in the FFT **/
	public FFTBox analyse(int fromFreq, int toFreq) {
		FFTBox vals = this.analyse();
		vals = FFTBox.filter(vals, fromFreq, toFreq);
		this.fftBox = vals;
		this.fftBox.setFrameSize(this.frameSize); //just in case
		return this.fftBox;
	}
	
	/** return the frequency row based on sample rate and frame size **/
	public static double[] getFreqRow(Signal s, double frameSize) {
		double sr = s.getSampleRate() / frameSize;
		double[] fr = new double[(int) frameSize];
		for (int i = 0; i < frameSize; ++i) {
			fr[i] = i * sr;
		}
		return fr;
	}
	
	/**Quick normalised frequency response graph **/
	@Override
	public void makeGraph() {
		makeGraph(40, 20000, 800, 600);
	}
	/**Quick normalised frequency response graph with filter options**/
	public void makeGraph(int filterFrom, int filterTo, int width, int height) {
		FFTController sc = new FFTController(FFTBox.logarithmicFreq(FFTBox.filter(
						FFTBox.convertTableToDecibels(this.signal, this.fftBox), 
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
	
	/** Normalise fft amplitudes to  1/N **/
	public static double[] normalise(double[] amplitudes) {
		double l = amplitudes.length;
		double[] na = new double[amplitudes.length];
		for (int i = 0; i < amplitudes.length; ++i) {
			na[i] = amplitudes[i] / l;
		}
		return na;
	}
	
	/** Normalise fft amplitudes to  1/N **/
	public static double[] normalise(Complex[] amplitudes) {
		double l = amplitudes.length;
		double[] na = new double[amplitudes.length];
		for (int i = 0; i < amplitudes.length; ++i) {
			na[i] = amplitudes[i].re / l;
		}
		return na;
	}
	

	/** Invert fft algorithm **/
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
	
	@Override
	public boolean hasAnalysed() {
		return this.cValues != null;
	}
	
	
	public FFTBox getFFTBox() {
		return this.fftBox;
	}
	
	


}
