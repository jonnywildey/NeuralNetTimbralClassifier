package waveProcess;

import riff.Signal;

public class Edit {

	public Edit() {
		// TODO Auto-generated constructor stub
	}
	
	/**receive a subset of the signal  **/
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

}
