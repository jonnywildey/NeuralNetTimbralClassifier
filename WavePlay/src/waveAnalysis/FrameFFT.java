package waveAnalysis;

import java.util.Arrays;

import plotting.FFTController;
import riff.Signal;
import filemanager.ArrayMethods;


/**Bunch of FFTS and some utility functions for them **/
public class FrameFFT extends TransformComponent{
	
	public Signal signal;
	public int frameSize;
	public FFTBox fftBox;
	
	public FrameFFT(Signal s, int frameSize) {
		this.signal = s;
		this.frameSize = frameSize; //standard
	}
	
	public FrameFFT(Signal s) {
		this(s, 2048);
	}
	
	/** return the frame count of the signal with frame size **/
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
				amps[i] = ArrayMethods.extend(
						ArrayMethods.getSubset(
								s.getSignal()[chan], i * frame), 
						frame);
			} else { //normal case
				amps[i] = ArrayMethods.getSubset(s.getSignal()[chan], 
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
			table[i] = FFT.normalise(Complex.getMagnitudes(cs[i - 1]));
		}
		return table;
	}
	
	/** Has the FFT performed any analysis? **/
	public boolean hasAnalysed() {
		return this.fftBox != null;
	}
	
	/**Gets the results table **/
	public FFTBox getFFTBox() {
		return this.fftBox;
	}
	
	/**Quick normalised frequency response graph **/
	public void makeGraph() {
		makeGraph(40, 20000, 600, 400);
	}
	
	/**Quick normalised frequency response graph **/
	public static void makeGraph(FFTBox fftbox, Signal signal) {
		makeGraph(fftbox, signal, 40, 20000, 600, 400);
	}
	
	/**Quick normalised frequency response graph with filter options**/
	public void makeGraph(int filterFrom, int filterTo, int width, int height) {
		FrameFFT.makeGraph(this.getFFTBox(), this.signal, filterFrom, filterTo, width, height);
	}
	
	/**Quick normalised frequency response graph with filter options**/
	public static void makeGraph(FFTBox fftbox, Signal signal, int filterFrom, int filterTo, int width, int height) {
		FFTBox nb = FFTBox.convertTableToDecibels(signal, fftbox);
		nb = FFTBox.filter(nb, filterFrom, filterTo);
		nb = FFTBox.logarithmicFreq(nb);
		FFTController sc = new FFTController(nb, width, height);
		sc.makeChart();
	}

	
	/**Perform frame analysis **/
	public FFTBox analyse() {
		double[][] amps = makeAmps(this.signal, this.frameSize);
		Complex[][] cs = fftFromAmps(amps);
		double[][] table = makeTable(cs);
		this.fftBox = new FFTBox(table, this.signal);
		return this.fftBox;
	}
	
	/** Gets the loudest frequency bins (overall). Not incredibly useful**/
	public double getLoudestFreq(FFTBox fftBox) {
		//sum each
		double[][] nt = FFTBox.getSumFFTBox(fftBox).getTable();
		return ArrayMethods.getMax(nt[1]);
	}
	
	/** Filters the FFTFrame's FFTBox **/
	public FFTBox filter(int filterFrom, int filterTo){
		FFTBox nb = FFTBox.filter(this.fftBox, filterFrom, filterTo);
		this.fftBox = nb;
		return this.fftBox;
	}
	
	/**Perform frame analysis **/
	public FFTBox analyse(int filterFrom, int filterTo) {
		this.analyse();
		return this.filter(filterFrom, filterTo);
	}

	
	


}
