package waveProcess;

import filemanager.Log;
import riff.Signal;

public class EQFilter {

	/**High Pass **/
	public static Signal highPassFilter(Signal signal, 
			double centreFreq, double resonance) {
		//set up arrays
		double[][] os = signal.getSignal();
		double[][] ns = new double[os.length][os[0].length];
		HighPass bf = new HighPass(signal.getSampleRate(), centreFreq, resonance);
				
		for (int i = 0; i < os.length;++i) {
			for (int j = 0; j < os[0].length;++j) {
				//Process
				ns[i][j] = bf.process(os[i][j]);
				//Log.d(os[i][j] + " " + ns[i][j]);
			}
			bf.reset();
		}
		return new Signal(ns, signal.getBit(), signal.getSampleRate());
	}
	
	/**Low Pass **/
	public static Signal lowPassFilter(Signal signal, 
			double centreFreq, double resonance) {
		//set up arrays
		double[][] os = signal.getSignal();
		double[][] ns = new double[os.length][os[0].length];
		LowPass bf = new LowPass(signal.getSampleRate(), centreFreq, resonance);
				
		for (int i = 0; i < os.length;++i) {
			for (int j = 0; j < os[0].length;++j) {
				//Process
				ns[i][j] = bf.process(os[i][j]);
				//Log.d(os[i][j] + " " + ns[i][j]);
			}
			bf.reset();
		}
		return new Signal(ns, signal.getBit(), signal.getSampleRate());
	}
	
	/**Band Pass **/
	public static Signal bandPassFilter(Signal signal, 
			double centreFreq, double octaveBW) {
		//set up arrays
		double[][] os = signal.getSignal();
		double[][] ns = new double[os.length][os[0].length];
		BandPass bf = new BandPass(signal.getSampleRate(), centreFreq, octaveBW);
				
		for (int i = 0; i < os.length;++i) {
			for (int j = 0; j < os[0].length;++j) {
				//Process
				ns[i][j] = bf.process(os[i][j]);
				//Log.d(os[i][j] + " " + ns[i][j]);
			}
			bf.reset();
		}
		return new Signal(ns, signal.getBit(), signal.getSampleRate());
	}
	
	/**Band Pass **/
	public static Signal notch(Signal signal, 
			double centreFreq, double octaveBW) {
		//set up arrays
		double[][] os = signal.getSignal();
		double[][] ns = new double[os.length][os[0].length];
		Notch bf = new Notch(signal.getSampleRate(), centreFreq, octaveBW);
				
		for (int i = 0; i < os.length;++i) {
			for (int j = 0; j < os[0].length;++j) {
				//Process
				ns[i][j] = bf.process(os[i][j]);
				//Log.d(os[i][j] + " " + ns[i][j]);
			}
			bf.reset();
		}
		return new Signal(ns, signal.getBit(), signal.getSampleRate());
	}
	
	/**Standard EQ **/
	public static Signal EQ(Signal signal, 
			double centreFreq, double octaveBW, double gain) {
		//set up arrays
		double[][] os = signal.getSignal();
		double[][] ns = new double[os.length][os[0].length];
		PeakEQ bf = new PeakEQ(signal.getSampleRate(), centreFreq, octaveBW, gain);
				
		for (int i = 0; i < os.length;++i) {
			for (int j = 0; j < os[0].length;++j) {
				//Process
				ns[i][j] = bf.process(os[i][j]);
				//Log.d(os[i][j] + " " + ns[i][j]);
			}
			bf.reset();
		}
		return new Signal(ns, signal.getBit(), signal.getSampleRate());
	}
	
	/**Filter sweep. recommend low BW (~0.3) high gain (> 6) **/
	public static Signal EQSweep(Signal signal, 
			double centreFreq, double to, double octaveBW, double gain) {
		//set up arrays
		double[][] os = signal.getSignal();
		double[][] ns = new double[os.length][os[0].length];
		PeakEQ bf = new PeakEQ(signal.getSampleRate(), centreFreq, octaveBW, gain);
		double freq = centreFreq;
		int dt = 10; //how often to change freq. 
		for (int i = 0; i < os.length;++i) {
			for (int j = 0; j < os[0].length;++j) {
				//Process
				if (j % dt == 0) {
					freq = centreFreq + ((j + 1) / (double)(os[0].length + 1)) * (to - centreFreq);
					bf.setCentreFreq(freq);
				}	
				ns[i][j] = bf.process(os[i][j]);
				//Log.d(os[i][j] + " " + ns[i][j]);
			}
			bf.reset();
		}
		return new Signal(ns, signal.getBit(), signal.getSampleRate());
	}


}
