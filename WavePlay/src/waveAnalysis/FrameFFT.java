package waveAnalysis;

import java.util.Arrays;

import plotting.FFTController;
import riff.Signal;
import waveProcess.Gain;
import waveProcess.Pitch;
import filemanager.ArrayStuff;
import filemanager.Log;


/**Bunch of FFTS
 */
public class FrameFFT{
	
	public Signal signal;
	public int frameSize;
	public double[][] table;
	
	public FrameFFT(Signal s, int frameSize) {
		this.signal = s;
		this.frameSize = frameSize; //standard
	}
	
	public FrameFFT(Signal s) {
		this(s, 2048);
	}
	
	/** Converts tables amplitude values to decibels away from maximum amplitude **/
	public static double[][] convertTableToDecibels(Signal s, double[][] table, double frame) {
		double max = Gain.amplitudeToDecibel(s.getMaxAmplitude());
		//Log.d(max);
		double[][] nt = new double[table.length][table[0].length];
		nt[0] = table[0]; //freqrrow
		for (int i = 1; i < table.length; ++i) { //skip first column
			for (int j = 0; j < table[i].length; ++j) {
				nt[i][j] = Gain.amplitudeToDecibel(table[i][j] / frame) - max;
				//Log.d(table[i][j]);
			}
		}
		return nt;
	}
	
	/** returns the table as bark subset **/
	public static double[][] getBarkedSubset(double[][] table) {
		table[0] = Pitch.freqToBark(table[0]);
		//get tables 
		int to = 0;
		int from = 0;
		int length = table.length - 1;
		int b = 20;
		double lim = 0;
		double[][] nt = new double[table.length][b];
		
		for (int i = 0;i < b; ++i) {
			nt[0][i] = i + 1;
			lim = i + 1;
			for (int j = to; j < table[0].length; ++j) {
				//Log.d(table[0][j]);
				if (table[0][j] > lim | 
						j >= table[0].length - 1) {
					//Log.d(from + " " + to + " : " + table[0][j] + " " + j);
					lim = table[0][j];
					from = to;
					to = j;
					
					for (int k = 1; k < table.length; ++k) {
						nt[k][i] = ArrayStuff.getAverageOfSubset(table[k], from, to);
						//Log.d(nt[k][i]);
					}
					break;
				}
			}
		}
		return nt;
	}
	
	/** converts frequecies to a power of two. Useful for graphs **/
	public static double[][] logarithmicFreq(double[][] table) {
		//double min = ArrayStuff.getMin(table[0]);
		double[][] nt = ArrayStuff.copy(table);
		double mlog = Math.log(2);
		for (int i = 0; i < table[0].length; ++i) {
			nt[0][i] = Math.log(table[0][i])	/ mlog;		
		}
		return nt;
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
	
	/** Has the FFT performed any analysis? **/
	public boolean hasAnalysed() {
		return this.table != null;
	}
	
	/**Gets the results table **/
	public double[][] getTable() {
		return this.table;
	}
	
	/**Quick normalised frequency response graph **/
	public void makeGraph() {
		makeGraph(40, 20000, 600, 400);
	}
	/**Quick normalised frequency response graph with filter options**/
	public void makeGraph(int filterFrom, int filterTo, int width, int height) {
		
		FFTController sc = new FFTController(FrameFFT.logarithmicFreq(FFT.filter(
						FrameFFT.convertTableToDecibels(this.signal, this.table, this.frameSize), 
						filterFrom, filterTo)), width, height);
		sc.makeChart();
	}

	
	/**Perform frame analysis **/
	public double[][] analyse() {
		double[][] amps = makeAmps(this.signal, this.frameSize);
		Complex[][] cs = fftFromAmps(amps);
		double[][] table = makeTable(cs);
		this.table = table;
		return table;
	}
	
	/** Gets the loudest frequency bins **/
	public double getLoudestFreq(double[][] table) {
		//sum each
		double[][] nt = getSumTable(table);
		return ArrayStuff.getMax(nt[1]);
	}
	
	/** returns the sum of the bins **/
	public static double[][] getSumTable(double[][] table) {
		double[][] sums = new double[2][table[0].length];
		sums[0] = table[0]; //freq row
		for (int i = 0; i < table[0].length; ++i) {
			for (int j = 1; j < table.length; ++j) {
				sums[1][i] += table[j][i];
			}
		}
		return sums;
	}
	
	public static double[][] getExponentTable(double[][] table, double exponent) {
		double[][] sums = new double[2][table[0].length];
		sums[0] = table[0]; //freq row
		for (int i = 0; i < table[0].length; ++i) {
			for (int j = 1; j < table.length; ++j) {
				sums[1][i] += table[j][i] * (Math.pow(exponent, i));
			}
		}
		return sums;
	}
	
	
	/**Perform frame analysis **/
	public double[][] analyse(int filterFrom, int filterTo) {
		double[][] table = analyse();
		return FFT.filter(table, filterFrom, filterTo);
	}

	
	


}
