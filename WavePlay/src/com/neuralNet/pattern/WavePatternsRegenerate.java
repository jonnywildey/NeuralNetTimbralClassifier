package com.neuralNet.pattern;

import java.io.File;

import com.riff.Wave;


/** allows multithreaded batch reanalysis of waves **/
public class WavePatternsRegenerate extends WavePatterns{


	/**
	 * 
	 */
	private static final long serialVersionUID = -3699427030486108477L;

	public WavePatternsRegenerate(File filePath) {
		super(filePath);
		// TODO Auto-generated constructor stub
	}
	
	public WavePatternsRegenerate() {
		super();
	}
	
	/** turns a set of waves to a pattern. by using FFT analysis (input analysis). Does NOT
	 * write to the Wave **/
	public void wavReFFTPattern(File[] files) {
		//patterns
		if (files == null) {
			files = getFilesFromDirectory();
		}
		this.patterns = new WavePattern[files.length];
		String[] instrs = new String[files.length];
		Wave wave = null;
		for (int i = 0; i < files.length; ++i) {
			wave = new Wave(files[i]);
			patterns[i] = new WavePattern(i, wave); //make pattern
			patterns[i].setInputArray(reFFT(wave.getSignals()));
			//get instrumental outputs
			instrs[i] = WavePattern.getInstrumentalOutputs(wave);
			patterns[i].instrument = instrs[i];
		}
		this.instrs = instrs;
		getOutputs(instrs);
	}
	
	@Override
	public WavePatterns call() throws Exception {
		this.wavReFFTPattern(this.files);
		return this;
	}
	


}
