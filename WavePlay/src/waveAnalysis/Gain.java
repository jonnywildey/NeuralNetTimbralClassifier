package waveAnalysis;

import riff.Signal;

public class Gain {
	
	public static double decibelToPower(double decibel) {
		return  Math.pow(10, decibel / 10);
	}
	
	public static double decibelToAmplitude(double decibel) {
		return  Math.pow(10, (decibel / 20));
	}
	
	public static double amplitudeToDecibel(double amplitude) {
		return 10 * Math.log10(amplitude * amplitude);
	}
	
	public static double powerToDecibel(double power) {
		return 10 * (Math.log10(power));
	}
	
	/**TEMPLATE **/
	public static Signal processTemplate(Signal signal) {
		//set up arrays
		double[][] os = signal.getSignal();
		double[][] ns = new double[os.length][os[0].length];
				
		for (int i = 0; i < os.length;++i) {
			for (int j = 0; j < os[0].length;++j) {
				//Process
			}
		}
		return new Signal(ns, signal.getBit(), signal.getSampleRate());
	}
	
	/**Adjusts volume in terms of decibels **/
	public static Signal volume(Signal signal, double db) {
		//set up arrays
		double[][] os = signal.getSignal();
		double[][] ns = new double[os.length][os[0].length];
		double amp = decibelToAmplitude(db);
		for (int i = 0; i < os.length;++i) {
			for (int j = 0; j < os[0].length;++j) {
				//Process
				ns[i][j] = amp * os[i][j];
			}
		}
		return new Signal(ns, signal.getBit(), signal.getSampleRate());
	}
	
	/**Adjusts volume in terms of decibels **/
	public static Signal swapChannels(Signal signal) {
		if (signal.getChannels() != 2) {
			return null;
		}
		//set up arrays
		double[][] os = signal.getSignal();
		double[][] ns = new double[os.length][os[0].length];
		for (int i = 0; i < os.length;++i) {
			for (int j = 0; j < os[0].length;++j) {
				//Process
				ns[i][j] = os[(i + 1) % 2][j];
			}
		}
		return new Signal(ns, signal.getBit(), signal.getSampleRate());
	}
	
	
}
