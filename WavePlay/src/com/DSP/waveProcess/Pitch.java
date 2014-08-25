package com.DSP.waveProcess;

import com.DSP.waveAnalysis.FFT;
import com.DSP.waveAnalysis.Statistics;
import com.riff.Signal;
import com.util.ArrayMethods;
import com.util.Log;

public class Pitch {
	
	/** Pitch Shifts the audio 0 < factor < inf **/
	public static Signal pitchShift(Signal signal, double semitones) {
		double factor = Math.pow(2, semitones / 12);
		//set up arrays
		double[][] os = signal.getSignal();
		double[][] ns = new double[os.length][(int)((os[0].length / factor) + 1)]; //round up
		//Log.bad(os[0].length + " " + ns[0].length);
		for (int i = 0; i < ns.length;++i) {
			for (int j = 0; j < ns[i].length;++j) {
				ns[i][j] = ArrayMethods.linearApproximate(os[i], j * factor);  
				
			}
		}
		return new Signal(ns, signal.getBit(), signal.getSampleRate());
	}
	
	/**Reverses |||| sesreveR**/
	public static Signal reverse(Signal signal) {
		//set up arrays
		double[][] os = signal.getSignal();
		double[][] ns = new double[os.length][os[0].length];
		for (int i = 0; i < os.length;++i) {
			for (int j = 0; j < os[0].length;++j) {
				ns[i][j] = os[i][os[i].length - 1 - j];
			}
		}
		return new Signal(ns, signal.getBit(), signal.getSampleRate());
	}
	
	/** converts frequency to Bark **/
	public static double freqToBark(double freq) {
		return 13 * Math.atan(freq * 0.00076) + 
				3.5 * Math.atan(Math.pow(freq / 7500, 2));
	}
	
	/** converts frequency row to Bark **/
	public static double[] freqToBark(double[] freqRow) {
		double[] nt = new double[freqRow.length];
		for (int i = 0; i < freqRow.length;++i) {
			nt[i] = freqToBark(freqRow[i]);
		}
		return nt;
	}
	/** converts frequency row to subdivided Bark. so 0.5 in factor will return half barks. **/
	public static double[] freqToBark(double[] freqRow, double factor) {
		double[] nt = new double[freqRow.length];
		for (int i = 0; i < freqRow.length;++i) {
			nt[i] = freqToBark(freqRow[i]) / factor;
		}
		return nt;
	}

	public static double getFundamental(double[][] table) {
		double[][] peaks = Statistics.getPeaks(table);
		double err = 0.05; //5%
		//get max
		double max = Double.NEGATIVE_INFINITY;
		double freq = 0;
		
		for (int i = 0; i < peaks[1].length; ++i) {
			if (max < peaks[1][i]) {
				max = peaks[1][i];
				freq = peaks[0][i];
			}
		}
		//does 8ve below exist?
		//Log.d("current peak" + freq);
		//Log.d(Arrays.toString(peaks[0]));
		
		for (int i = peaks[1].length - 1; i >= 0; --i) {
			if (Statistics.isValueCloseTo(freq / 2, peaks[0][i], err) | //8ve below
					Statistics.isValueCloseTo(freq / 1.5, peaks[0][i], err)) { //5th below
				if (Pitch.compareVolume(max, peaks[1][i])) {
					freq = peaks[0][i];
					break; //could not have break...
				}
				
				
			}
		}
		return freq;
	}
	
	/** Gets the fundamental of the signal. This will take ages for long wavs,
	 * don't do it. 
	 */
	public static double getFundamental(Signal s) {
		FFT fft = new FFT(s);
		double[][] t = (fft.analyse(20, 20000).getTable());
		//fft.makeGraph();
		return getFundamental(t);
	}
	
	/** Is the fundamental of the signal within the thresholds
	 * of human hearing?
	 */
	public static boolean isFundamentalHearable(Signal s) {
		double lowThreshold = 60; //or 40?
		double highThreshold = 20000;
		double fund = getFundamental(s);
		//Log.d("fund: " + fund);
		return (fund > lowThreshold & fund < highThreshold);
	}

	/** returns true if b is of similar volume to a **/
	public static boolean compareVolume(double a, double b) {
		//will put margin at 12db
		return b + 12 >= a;
	}

}
