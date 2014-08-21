package com.neuralNet.pattern;

import java.io.File;


/** allows multithreaded batch reanalysis **/
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
	
	@Override
	public WavePatterns call() throws Exception {
		this.wavReFFTPattern(this.files);
		return this;
	}
	


}
