package com.DSP.waveProcess.filters;



public class LowPass extends Filter{

	public LowPass(int sampleRate, double centreFreq, double resonance) {
		super(sampleRate, centreFreq, 1 / resonance, 0);
		
	}
	
	@Override
	protected void calculateCoefficients() {
		a0 =   1 + alpha;
		b0 =  (1 - c)/2;
	    b1 = (1 - c);
	    b2 =  ((1 - c)/2);
	    a1 =  (-2*c);
	    a2 =   (1 - alpha);
	    normaliseCoefficients();
	}
	

}
