package com.neuralNet.pattern;

import java.io.File;

import com.DSP.waveAnalysis.FFTBox;
import com.riff.Signal;
import com.riff.Wave;
import com.util.Log;

public class WavePatternsRegenRewrite extends WavePatterns{

	public WavePatternsRegenRewrite() {
	}

	public WavePatternsRegenRewrite(File filePath) {
		super(filePath);
	}
	
	@Override
	public WavePatterns call() throws Exception {
		this.wavRewrite(this.files);
		return this;
	}
	
	/** turns a set of waves to a pattern. by using FFT analysis (input analysis). 
	 * Then rewrites data chunks of Wave **/
	public void wavRewrite(File[] files) {
		//patterns
		if (files == null) {
			files = getFilesFromDirectory();
		}
		this.patterns = new WavePattern[files.length];
		String[] instrs = new String[files.length];
		Signal signal = null;
		Wave wave = null;
		for (int i = 0; i < files.length; ++i) {
			wave = new Wave(files[i]);
			patterns[i] = new WavePattern(i, wave); //make pattern
			signal = wave.getSignals();
			FFTBox fftBox = reFFTBox(signal);
			patterns[i].setInputArray(Pattern.doubleToInputShell(
					fftBox.getValues()[0]));
			//get instrumental outputs
			try {
				instrs[i] = WavePattern.getInstrumentalOutputs(wave);
			} catch (Exception e) {
				Log.d("no target in wave, assuming parent directory is target");
				instrs[i] = wave.getFilePath().getParentFile().getName();
			}
			patterns[i].instrument = instrs[i];
			WavePattern.addMetaData(signal, fftBox, "IAS8", patterns[i].instrument);
		}
		this.instrs = instrs;
		getOutputs(instrs);
	}
	
	
	

}
