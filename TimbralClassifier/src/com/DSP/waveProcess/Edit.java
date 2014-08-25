package com.DSP.waveProcess;

import com.DSP.waveAnalysis.SampleRateException;
import com.riff.Signal;
import com.util.Log;

/**
 * Time based editing of signals *.
 *
 * @author Jonny Wildey
 * @version 1.0
 */
public class Edit {

	/**
	 * Instantiates a new edits the.
	 */
	public Edit() {
	}
	
	/**
	 * receive a subset of the signal. Does not deal with
	 * stereo and mono well  *
	 *
	 * @param signal the signal
	 * @param sampleFrom the sample from
	 * @param sampleTo the sample to
	 * @return the signal
	 */
	public static Signal crop(Signal signal, int sampleFrom, int sampleTo) {
		//set up arrays
		double[][] os = signal.getSignal();
		double[][] ns = new double[os.length][sampleTo - sampleFrom];
				
		for (int i = 0; i < os.length;++i) {
			for (int j = sampleFrom; j < sampleTo; ++j) {
				//Process
				ns[i][j - sampleFrom] = os[i][j];
			}
		}
		return new Signal(ns, signal.getBit(), signal.getSampleRate());
	}
	
	/**
	 * Stitches one signal to the end of another*.
	 *
	 * @param signals the signals
	 * @return the signal
	 * @throws SampleRateException the sample rate exception
	 */
	public static Signal concat(Signal... signals) throws SampleRateException {
		//set up arrays
		double[][][] os = new double[signals.length][][];
		int length = 0; 
		int bit = 0;
		int sr = signals[0].getSampleRate();
		int chan = 1; //max channels
		for (int i = 0; i < signals.length; ++i) {
			os[i] = signals[i].getSignal();
			length += os[i][0].length;
			chan = (signals[i].getChannels() > chan) ? signals[i].getChannels() : chan;
			bit = (signals[i].getBit() > bit) ? signals[i].getBit(): bit;
			if (signals[i].getSampleRate() != sr) {
				throw new SampleRateException();
			}
		}
		double[][] ns = new double[chan][length];	
		//Log.d(signals.length + " " + bit + " " + length);
		int c = 0;
		for (int k = 0; k < os.length; ++k) { //each signal
			os[k] = Gain.bitRateConvert(signals[k], bit).getSignal();
			for (int i = 0; i < ns.length;++i) { //pan
				for (int j = 0; j < os[k][i].length;++j) {
				//Process
					ns[i][j + c] = os[k][i][j];
				}
			}
			c += os[k][0].length;
		}
		return new Signal(ns, bit, sr);
	}

}
