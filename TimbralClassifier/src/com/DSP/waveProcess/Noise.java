package com.DSP.waveProcess;

import java.util.Random;

import com.riff.Signal;

/**
 * Noise based signal processes *.
 *
 * @author Jonny Wildey
 * @version 1.0
 */
public class Noise {

	/**
	 * Instantiates a new noise.
	 */
	public Noise() {
	}
	
	/**
	 * adds white noise to the signal. db should be -ve  *
	 *
	 * @param signal the signal
	 * @param dbBelowFloor the db below floor
	 * @return the signal
	 */
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
	
	/**
	 * adds tape hiss to the signal. db should be -ve  *
	 *
	 * @param signal the signal
	 * @param dbBelowFloor the db below floor
	 * @return the signal
	 */
	public static Signal addTapeHiss(Signal signal, double dbBelowFloor) {
		//set up arrays
		double[][] os = signal.getSignal();
		double[][] tape = TapeHiss.tape.getSignal();
		double[][] ns = new double[os.length][os[0].length];
		if (signal.getBit() != TapeHiss.tape.getBit()) {
			tape = Gain.bitRateConvert(
					TapeHiss.tape, signal.getBit()).getSignal();
		}
		double factor = Gain.decibelToAmplitude(dbBelowFloor);
		for (int i = 0; i < os.length;++i) {
			for (int j = 0; j < os[0].length;++j) {
				//Process
				ns[i][j] = os[i][j] + 
						(tape[i % tape[0].length][j % tape[0].length]) 
						* factor;
			}
		}
		return new Signal(ns, signal.getBit(), signal.getSampleRate());
	}
	
	
	
	
}
