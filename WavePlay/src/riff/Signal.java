package riff;

import plotting.SignalController;
import plotting.WavController;

/**Basic data holder for signals **/
public class Signal {
	public double[][] signal;
	public int bit;
	private int sampleRate;
	
	public Signal(double[][] signal, int bit, int sampleRate) {
		this.signal = signal;
		this.bit = bit;
		this.sampleRate = sampleRate;
	}
	
	/**returns the maximum potential amplitude
	 * for fixed point calculations
	 */
	public double getMaxAmplitude() {
		return  Math.pow(2, this.bit - 1) - 1;
	}
	
	/**returns the minimum potential amplitude
	 * for fixed point calculations
	 */
	public double getMinAmplitude() {
		return  Math.pow(2, this.bit - 1) * -1;
	}
	
	public void makeGraph() {
		SignalController sc = new SignalController(this, 600, 400);
		sc.makeChart();
	}
	
	public void makeGraph(int width, int height) {
		SignalController sc = new SignalController(this, width, height);
		sc.makeChart();
	}
	
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
	

	
	/** get the amount of channels **/
	public int getChannels() {
		return signal.length;
	}

	public double[][] getSignal() {
		return signal;
	}


	public int getBit() {
		return bit;
	}

	public void setBit(int bit) {
		this.bit = bit;
	}

	public int getSampleRate() {
		return sampleRate;
	}

	public void setSampleRate(int sampleRate) {
		this.sampleRate = sampleRate;
	}

	public int getLength() {
		return this.signal[0].length;
	}

	
	
	
}
