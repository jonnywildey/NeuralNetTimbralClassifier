package com.neuralNet.pattern;

import java.io.File;

import com.DSP.waveAnalysis.FFTBox;
import com.DSP.waveProcess.FFTChain;
import com.riff.Signal;
import com.util.Log;

public class WavePatternsRegenRewriteMono extends WavePatternsRegenRewrite {

	public WavePatternsRegenRewriteMono() {
		// TODO Auto-generated constructor stub
	}

	public WavePatternsRegenRewriteMono(File filePath) {
		super(filePath);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	/**FFT analysis sequence, returns FFT box **/
	protected FFTBox reFFTBox(Signal signals) {
		FFTBox dd = FFTChain.monoFFTChain(signals);
		return dd;
	}

}
