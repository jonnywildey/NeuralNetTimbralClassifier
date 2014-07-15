package waveProcess;

import java.util.Random;

import riff.Signal;

public class Noise {

	public Noise() {
		// TODO Auto-generated constructor stub
	}
	
	/**adds white noise to the signal. db should be -ve  **/
	public static Signal whiteNoise(Signal signal, double dbBelowFloor) {
		//set up arrays
		double[][] os = signal.getSignal();
		double[][] ns = new double[os.length][os[0].length];
		double maxThresh = signal.getMaxAmplitude() * Gain.decibelToAmplitude(dbBelowFloor);
		Random r = new Random();
		double v = 0;
		for (int i = 0; i < os.length;++i) {
			for (int j = 0; j < os[0].length;++j) {
				//Process
				v = (r.nextDouble() * (maxThresh * 2)) - maxThresh;
				ns[i][j] = os[i][j] + v;
			}
		}
		return new Signal(ns, signal.getBit(), signal.getSampleRate());
	}
	
	
}
