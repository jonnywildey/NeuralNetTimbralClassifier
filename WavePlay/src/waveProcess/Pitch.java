package waveProcess;

import riff.Signal;
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

}
