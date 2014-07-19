package waveProcess;

import riff.Signal;
import waveAnalysis.Statistics;
import filemanager.ArrayStuff;
import filemanager.Log;

public class Pitch {
	
	/** Pitch Shifts the audio 0 < factor < inf **/
	public static Signal pitchShift(Signal signal, double factor) {
		//set up arrays
		double[][] os = signal.getSignal();
		double[][] ns = new double[os.length][(int)((os[0].length / factor) + 1)]; //round up
		//Log.bad(os[0].length + " " + ns[0].length);
		for (int i = 0; i < ns.length;++i) {
			for (int j = 0; j < ns[i].length;++j) {
				ns[i][j] = ArrayStuff.linearApproximate(os[i], j * factor);  
				
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
				Math.atan(Math.pow(freq / 7500, 2));
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

	/** returns true if b is of similar volume to a **/
	public static boolean compareVolume(double a, double b) {
		//will put margin at 12db
		return b + 12 >= a;
	}

}
