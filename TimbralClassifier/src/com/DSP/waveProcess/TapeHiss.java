package com.DSP.waveProcess;

import com.riff.Signal;
import com.riff.Wave;

/**
 * static tape hiss required for noise processes *.
 *
 * @author Jonny Wildey
 * @version 1.0
 */
public class TapeHiss {
	
	public static Signal tape = new Wave("assets/wav/Tape.wav").getSignals();

	/**
	 * Instantiates a new tape hiss.
	 */
	public TapeHiss() {
	}

	
}
