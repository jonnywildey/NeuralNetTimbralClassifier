package com.waveProcess;

import com.riff.Signal;
import com.waveAnalysis.FFTBox;
import com.waveAnalysis.FrameFFT;

public class FFTChain {

	public FFTChain() {
		// TODO Auto-generated constructor stub
	}
	
	public static FFTBox polyFFTChain(Signal signals) {
		FrameFFT fft = new FrameFFT(signals, 4096);
		FFTBox dd = fft.analyse(20, 20000);
		//fft.makeGraph();
		dd = FFTBox.getSumTable(dd, 7);
		dd = FFTBox.getHiResBarkedSubset(dd, 0.5);
		dd = FFTBox.normaliseTable(dd, 10);
		return dd;
	}
	
	public static FFTBox monoFFTChain(Signal signals) {
		FrameFFT fft = new FrameFFT(signals, 4096);
		FFTBox dd = fft.analyse(20, 20000);
		//fft.makeGraph();
		dd = FFTBox.getSumTable(dd, 7);
		dd = FFTBox.getHiResBarkedSubset(dd, 1);
		dd = FFTBox.normaliseTable(dd, 10);
		return dd;
	}

}
