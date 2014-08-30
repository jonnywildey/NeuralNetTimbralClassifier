package com.DSP.waveProcess;

import com.DSP.waveAnalysis.SampleRateException;
import com.riff.Signal;
import com.util.ArrayMethods;
import com.util.Log;

/**
 * Signal processes relating to amplitudes *.
 * 
 * @author Jonny Wildey
 * @version 1.0
 */
public class Gain {

	/**
	 * Adjusts amplitude. e.g. factor 2 is twice as much amplitude *
	 * 
	 * @param signal
	 *            the signal
	 * @param factor
	 *            the factor
	 * @return the signal
	 */
	public static Signal amplifyByFactor(Signal signal, double factor) {
		// set up arrays
		double[][] os = signal.getSignal();
		double[][] ns = new double[os.length][os[0].length];
		for (int i = 0; i < os.length; ++i) {
			for (int j = 0; j < os[0].length; ++j) {
				// Process
				ns[i][j] = factor * os[i][j];
				// if (j % 100 == 0) {Log.d(ns[i][j] + " " + os[i][j]);}
			}
		}
		return new Signal(ns, signal.getBit(), signal.getSampleRate());
	}

	/**
	 * Amplitude to decibel.
	 * 
	 * @param amplitude
	 *            the amplitude
	 * @return the double
	 */
	public static double amplitudeToDecibel(double amplitude) {
		return 10 * Math.log10(amplitude * amplitude);
	}

	/**
	 * converts from one bit rate to another *.
	 * 
	 * @param signal
	 *            the signal
	 * @param newBitRate
	 *            the new bit rate
	 * @return the signal
	 */
	public static Signal bitRateConvert(Signal signal, int newBitRate) {
		// set up arrays
		int obr = signal.getBit();
		if (obr == newBitRate) {
			return signal;
		} else {
			double factor = Math.pow(2, newBitRate - obr);
			Log.d("f " + factor);
			signal.setBit(newBitRate);
			return amplifyByFactor(signal, factor);
		}

	}

	/**
	 * calculates root mean square *.
	 * 
	 * @param signal
	 *            the signal
	 * @return the double
	 */
	public static double calculateRMS(Signal signal) {
		// set up arrays
		double[][] os = signal.getSignal();
		double avg = 0;
		for (int i = 0; i < os.length; ++i) {
			for (int j = 0; j < os[0].length; ++j) {
				avg += os[i][j] * os[i][j];
			}
		}
		avg /= os[0].length;
		avg = Math.pow(avg, 0.5);
		avg = avg / signal.getMaxAmplitude();
		return amplitudeToDecibel(avg);
	}

	/*
	 * public static Signal processTemplate(Signal signal) { //set up arrays
	 * double[][] os = signal.getSignal(); double[][] ns = new
	 * double[os.length][os[0].length];
	 * 
	 * for (int i = 0; i < os.length;++i) { for (int j = 0; j <
	 * os[0].length;++j) { //Process } } return new Signal(ns, signal.getBit(),
	 * signal.getSampleRate()); }
	 */

	/**
	 * Adjusts volume to distance from ceiling, -ve *.
	 * 
	 * @param signal
	 *            the signal
	 * @param dbBelowFloor
	 *            the db below floor
	 * @return the signal
	 */
	public static Signal changeGain(Signal signal, double dbBelowFloor) {
		// set up arrays
		double[][] os = signal.getSignal();
		double[][] ns = new double[os.length][os[0].length];
		double maxAmp = signal.getMaxAmplitude()
		* decibelToAmplitude(dbBelowFloor);
		double signalMax = ArrayMethods.getMaxAbs(os);
		double factor = maxAmp / signalMax;
		for (int i = 0; i < os.length; ++i) {
			for (int j = 0; j < os[0].length; ++j) {
				// Process
				ns[i][j] = factor * os[i][j];
			}
		}
		return new Signal(ns, signal.getBit(), signal.getSampleRate());
	}

	/**
	 * Decibel to amplitude.
	 * 
	 * @param decibel
	 *            the decibel
	 * @return the double
	 */
	public static double decibelToAmplitude(double decibel) {
		return Math.pow(10, (decibel / 20));
	}

	/**
	 * Decibel to power.
	 * 
	 * @param decibel
	 *            the decibel
	 * @return the double
	 */
	public static double decibelToPower(double decibel) {
		return Math.pow(10, decibel / 10);
	}

	/**
	 * Gets the mid.
	 * 
	 * @param signal
	 *            the signal
	 * @return the mid
	 */
	public static Signal getMid(Signal signal) {
		if (signal.getChannels() == 1) {
			return signal;
		}
		double[][] os = signal.getSignal();
		double[][] ns = new double[1][os[0].length];
		for (int i = 0; i < os.length; ++i) {
			for (int j = 0; j < os[0].length; ++j) {
				ns[0][j] += os[i][j] / 2;
			}
		}
		return new Signal(ns, signal.getBit(), signal.getSampleRate());
	}

	/**
	 * Gets the side.
	 * 
	 * @param signal
	 *            the signal
	 * @return the side
	 */
	public static Signal getSide(Signal signal) {
		if (signal.getChannels() == 1) {
			return signal;
		}
		double[][] os = signal.getSignal();
		double[][] ns = new double[1][os[0].length];
		for (int j = 0; j < os[0].length; ++j) {
			ns[0][j] += os[0][j] - os[1][j];
		}
		return new Signal(ns, signal.getBit(), signal.getSampleRate());
	}

	/**
	 * Inverts signal *.
	 * 
	 * @param signal
	 *            the signal
	 * @return the signal
	 */
	public static Signal invert(Signal signal) {
		// set up arrays
		double[][] os = signal.getSignal();
		double[][] ns = new double[os.length][os[0].length];

		for (int i = 0; i < os.length; ++i) {
			for (int j = 0; j < os[0].length; ++j) {
				// Process
				ns[i][j] = os[i][j] * -1;
			}
		}
		return new Signal(ns, signal.getBit(), signal.getSampleRate());
	}

	/**
	 * Mid side encode.
	 * 
	 * @param signal
	 *            the signal
	 * @return the signal
	 */
	public static Signal midSideEncode(Signal signal) {
		double[][] ns = new double[][] { getMid(signal).getSignal()[0],
				getSide(signal).getSignal()[0] };
		return new Signal(ns, signal.getBit(), signal.getSampleRate());
	}

	/**
	 * left to mono *.
	 * 
	 * @param signal
	 *            the signal
	 * @return the signal
	 */
	public static Signal monoLeft(Signal signal) {
		// set up arrays
		if (signal.getChannels() == 1) {
			return signal;
		} else {
			double[] left = signal.getSignal()[0];
			return new Signal(new double[][] { left }, signal.getBit(),
					signal.getSampleRate());
		}
	}

	/**
	 * left to mono *.
	 * 
	 * @param signal
	 *            the signal
	 * @return the signal
	 */
	public static Signal monoRight(Signal signal) {
		if (signal.getChannels() == 1) {
			return signal;
		} else {
			double[] left = signal.getSignal()[0];
			return new Signal(new double[][] { left }, signal.getBit(),
					signal.getSampleRate());
		}
	}

	/**
	 * converts to stereo *.
	 * 
	 * @param signal
	 *            the signal
	 * @return the signal
	 */
	public static Signal monoToStereo(Signal signal) {
		// set up arrays
		double[][] os = signal.getSignal();
		double[][] ns = new double[2][os[0].length];

		for (int i = 0; i < os.length; ++i) {
			for (int j = 0; j < os[0].length; ++j) {
				ns[i][j] = os[0][j];
			}
		}
		return new Signal(ns, signal.getBit(), signal.getSampleRate());
	}

	/**
	 * Adjusts volume to distance from ceiling, -ve *.
	 * 
	 * @param signal
	 *            the signal
	 * @return the signal
	 */
	public static Signal normalise(Signal signal) {
		return changeGain(signal, 0);
	}

	/**
	 * Power to decibel.
	 * 
	 * @param power
	 *            the power
	 * @return the double
	 */
	public static double powerToDecibel(double power) {
		return 10 * (Math.log10(power));
	}

	/**
	 * Add signals together. very useful. Stereo. Uses the highest bit rate.
	 * throws an exception if sample rates are bad. Use sumMono for mono summing
	 * 
	 * @param signals
	 *            the signals
	 * @return the signal
	 * @throws SampleRateException
	 *             *
	 */
	public static Signal sum(Signal... signals) throws SampleRateException {
		// set up arrays
		double[][][] os = new double[signals.length][][];
		int max = 0;
		int bit = 0;
		int sr = signals[0].getSampleRate();
		for (int i = 0; i < signals.length; ++i) {
			os[i] = signals[i].getSignal();
			max = (os[i][0].length > max) ? os[i][0].length : max;
			bit = (signals[i].getBit() > bit) ? signals[i].getBit() : bit;
			if (signals[i].getSampleRate() != sr) {
				throw new SampleRateException();
			}
		}
		double[][] ns = new double[2][max];
		// Log.d(signals.length + " " + bit + " " + max);
		for (int k = 0; k < os.length; ++k) { // each signal
			os[k] = bitRateConvert(signals[k], bit).getSignal();
			for (int i = 0; i < ns.length; ++i) { // pan
				for (int j = 0; j < os[k][i].length; ++j) {
					// Process
					ns[i][j] += os[k][i][j];
				}
			}
		}
		return new Signal(ns, bit, sr);
	}

	/**
	 * Add signals together. very useful. Mono. Uses the highest bit rate.
	 * throws an exception if sample rates are bad.
	 * 
	 * @param signals
	 *            the signals
	 * @return the signal
	 * @throws SampleRateException
	 *             *
	 */
	public static Signal sumMono(Signal... signals) throws SampleRateException {
		// set up arrays
		double[][] os = new double[signals.length][];
		int max = 0;
		int bit = 0;
		int sr = signals[0].getSampleRate();
		for (int i = 0; i < signals.length; ++i) {
			os[i] = signals[i].getSignal()[0];
			max = (os[i].length > max) ? os[i].length : max;
			bit = (signals[i].getBit() > bit) ? signals[i].getBit() : bit;
			if (signals[i].getSampleRate() != sr) {
				throw new SampleRateException();
			}
		}
		double[][] ns = new double[1][max];
		// Log.d(signals.length + " " + bit + " " + max);
		for (int k = 0; k < os.length; ++k) { // each signal
			os[k] = bitRateConvert(signals[k], bit).getSignal()[0];
			for (int j = 0; j < os[k].length; ++j) {
				// Process
				ns[0][j] += os[k][j];
			}

		}
		return new Signal(ns, bit, sr);
	}

	/**
	 * Adjusts volume in terms of decibels *.
	 * 
	 * @param signal
	 *            the signal
	 * @return the signal
	 */
	public static Signal swapChannels(Signal signal) {
		if (signal.getChannels() != 2) {
			return null;
		}
		// set up arrays
		double[][] os = signal.getSignal();
		double[][] ns = new double[os.length][os[0].length];
		for (int i = 0; i < os.length; ++i) {
			for (int j = 0; j < os[0].length; ++j) {
				// Process
				ns[i][j] = os[(i + 1) % 2][j];
			}
		}
		return new Signal(ns, signal.getBit(), signal.getSampleRate());
	}

	/**
	 * converts channels sort of ok *.
	 * 
	 * @param signal
	 *            the signal
	 * @param channel
	 *            the channel
	 * @return the signal
	 */
	public static Signal toChannels(Signal signal, int channel) {
		if (signal.getChannels() == channel) {
			return signal;
		} else if (signal.getChannels() == 1 & channel == 2) {
			return monoToStereo(signal);
		} else { // 2 -> 1
			return getMid(signal);
		}
	}

	/**
	 * Adjusts volume in terms of decibels *.
	 * 
	 * @param signal
	 *            the signal
	 * @param db
	 *            the db
	 * @return the signal
	 */
	public static Signal volume(Signal signal, double db) {
		// set up arrays
		double[][] os = signal.getSignal();
		double[][] ns = new double[os.length][os[0].length];
		double amp = decibelToAmplitude(db);
		for (int i = 0; i < os.length; ++i) {
			for (int j = 0; j < os[0].length; ++j) {
				// Process
				ns[i][j] = amp * os[i][j];
			}
		}
		return new Signal(ns, signal.getBit(), signal.getSampleRate());
	}

	/**
	 * changes the volume to an rms value (e.g -12). Does NOT check if this will
	 * clip *
	 * 
	 * @param signal
	 *            the signal
	 * @param rms
	 *            the rms
	 * @return the signal
	 */
	public static Signal volumeRMS(Signal signal, double rms) {
		// Log.d("a");
		double error = 0.05;
		// set up arrays
		double current = calculateRMS(signal);
		double factor = decibelToAmplitude(rms - current);
		Signal s = amplifyByFactor(signal, factor);
		// double a = calculateRMS(s);
		// Log.d(current + " " + a + (Math.abs(a) - Math.abs(current)));
		return s;
	}

}
