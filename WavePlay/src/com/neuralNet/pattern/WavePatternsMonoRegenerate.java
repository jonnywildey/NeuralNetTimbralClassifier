package com.neuralNet.pattern;

import java.io.File;
import java.util.ArrayList;

import com.neuralNet.layers.InputShell;
import com.riff.Signal;
import com.waveAnalysis.FFTBox;
import com.waveAnalysis.FrameFFT;
import com.waveProcess.FFTChain;

public class WavePatternsMonoRegenerate extends WavePatternsRegenerate {

	public WavePatternsMonoRegenerate(File filePath) {
		super(filePath);
		// TODO Auto-generated constructor stub
	}

	public WavePatternsMonoRegenerate() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected ArrayList<InputShell> reFFT(Signal signals) {
		FFTBox dd = FFTChain.monoFFTChain(signals);
		return Pattern.doubleToInputShell(dd.getValues()[0]);
	}

}
