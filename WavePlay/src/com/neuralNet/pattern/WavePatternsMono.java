package com.neuralNet.pattern;

import java.util.ArrayList;

import com.neuralNet.InputShell;
import com.riff.Signal;
import com.waveAnalysis.FFTBox;
import com.waveAnalysis.FrameFFT;

public class WavePatternsMono extends WavePatterns{
	
	@Override
	protected ArrayList<InputShell> reFFT(Signal signals) {
		FrameFFT fft = new FrameFFT(signals, 4096);
		FFTBox dd = fft.analyse(20, 20000);
		dd = FFTBox.getSumTable(dd, 7);
		dd = FFTBox.getHiResBarkedSubset(dd, 1);
		dd = FFTBox.normaliseTable(dd, 10);
		return Pattern.doubleToInputShell(dd.getValues()[0]);
	}
}
