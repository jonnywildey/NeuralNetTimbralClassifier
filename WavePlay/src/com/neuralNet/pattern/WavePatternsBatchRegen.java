package com.neuralNet.pattern;

public class WavePatternsBatchRegen extends WavePatterns{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 202416054125154835L;
	public int regenCount = 15;
	
	@Override
	public WavePatterns call() throws Exception {
		this.wavReFFTBatchPattern(15, this.files);
		return this;
	}
}
