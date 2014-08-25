package com.riff;

import com.plotting.SignalController;
import com.plotting.WavController;
import com.util.Log;

/**
 * Basic data holder for signals *.
 *
 * @author Jonny Wildey
 * @version 1.0
 */
public class Signal {
	public double[][] signal;
	public int bit;
	private int sampleRate;
	
	/**
	 * Construct signal with amplitudes, bitrate and sample rate *.
	 *
	 * @param signal the signal
	 * @param bit the bit
	 * @param sampleRate the sample rate
	 */
	public Signal(double[][] signal, int bit, int sampleRate) {
		this.signal = signal;
		this.bit = bit;
		this.sampleRate = sampleRate;
	}
	
	/**
	 * returns the maximum potential amplitude
	 * for fixed point calculations.
	 *
	 * @return the max amplitude
	 */
	public double getMaxAmplitude() {
		return  Math.pow(2, this.bit - 1) - 1;
	}
	
	/**
	 * returns the minimum potential amplitude
	 * for fixed point calculations.
	 *
	 * @return the min amplitude
	 */
	public double getMinAmplitude() {
		return  Math.pow(2, this.bit - 1) * -1;
	}
	
	/**
	 * Find smallest value of signal (that isn't 0) *.
	 *
	 * @return the double
	 */
	public double findSmallestAmplitude() {
		double min = Double.MAX_VALUE;
		double abVal = 0;
		for (double[] row : this.signal) {
			for (double val : row) {
				abVal = Math.abs(val);
				if (abVal < min & abVal != 0) {
					min = abVal;
				}
			}
		}
		return min;
	}
	
	/**
	 * Make graph.
	 */
	public void makeGraph() {
		SignalController sc = new SignalController(this, 600, 400);
		sc.makeChart();
	}
	
	/**
	 * Make graph.
	 *
	 * @param width the width
	 * @param height the height
	 */
	public void makeGraph(int width, int height) {
		SignalController sc = new SignalController(this, width, height);
		sc.makeChart();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder(this.signal[0].length * 20);
		for (int i = 0; i < this.signal.length; ++i) {
			sb.append("[");
			for (int j = 0; j < this.signal[i].length; ++j) {
				sb.append(this.signal[i][j]);
				if (j < this.signal[i].length - 1) {
					sb.append(",");
				}
			}
			sb.append("]");
			if (i < this.signal.length - 1) {
				sb.append(",");
			}
		}
		return sb.toString();
	}
	

	
	/**
	 * get the amount of channels *.
	 *
	 * @return the channels
	 */
	public int getChannels() {
		return signal.length;
	}

	/**
	 * Gets the signal.
	 *
	 * @return the signal
	 */
	public double[][] getSignal() {
		return signal;
	}


	/**
	 * Gets the bit.
	 *
	 * @return the bit
	 */
	public int getBit() {
		return bit;
	}

	/**
	 * Sets the bit.
	 *
	 * @param bit the new bit
	 */
	public void setBit(int bit) {
		this.bit = bit;
	}

	/**
	 * Gets the sample rate.
	 *
	 * @return the sample rate
	 */
	public int getSampleRate() {
		return sampleRate;
	}

	/**
	 * Sets the sample rate.
	 *
	 * @param sampleRate the new sample rate
	 */
	public void setSampleRate(int sampleRate) {
		this.sampleRate = sampleRate;
	}

	/**
	 * Gets the length.
	 *
	 * @return the length
	 */
	public int getLength() {
		return this.signal[0].length;
	}

	
	
	
}
