package com.neuralNet.pattern;

import java.io.File;
import java.util.ArrayList;

import com.DSP.waveAnalysis.FFTBox;
import com.DSP.waveProcess.FFTChain;
import com.neuralNet.layers.InputShell;
import com.riff.Signal;

public class WavePatternsSplitBatchRegen extends WavePatternsBatchRegen {

	public WavePatternsSplitBatchRegen() {
		// TODO Auto-generated constructor stub
	}

	public WavePatternsSplitBatchRegen(File filePath) {
		super(filePath);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	/**FFT analysis sequence **/
	protected ArrayList<InputShell> reFFT(Signal signals) {
		FFTBox dd = FFTChain.polyFFTChainSplit(signals);
		return Pattern.doubleToInputShell(dd.getValues()[0]);
	}
	
	@Override
	/**FFT analysis sequence, returns FFT box **/
	protected FFTBox reFFTBox(Signal signals) {
		FFTBox dd = FFTChain.polyFFTChainSplit(signals);
		return dd;
	}

}
