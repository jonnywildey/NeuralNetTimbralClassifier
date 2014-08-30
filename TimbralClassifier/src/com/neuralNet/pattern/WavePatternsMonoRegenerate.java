package com.neuralNet.pattern;

import java.io.File;
import java.util.ArrayList;

import com.DSP.waveAnalysis.FFTBox;
import com.DSP.waveProcess.FFTChain;
import com.neuralNet.layers.InputShell;
import com.riff.Signal;

public class WavePatternsMonoRegenerate extends WavePatternsRegenerate {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WavePatternsMonoRegenerate() {
		// TODO Auto-generated constructor stub
	}

	public WavePatternsMonoRegenerate(File filePath) {
		super(filePath);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ArrayList<InputShell> reFFT(Signal signals) {
		FFTBox dd = FFTChain.monoFFTChain(signals);
		return Pattern.doubleToInputShell(dd.getValues()[0]);
	}

}
