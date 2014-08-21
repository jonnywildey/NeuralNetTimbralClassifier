package com.waveProcess.filters;




public class BandPass extends Filter{
	
	protected double q;

	public BandPass(int sampleRate, double centreFreq, double octaveBW) {
		super(sampleRate, centreFreq, octaveBW, 0);
		
	}
	
	@Override
	protected void calculateCoefficients() {
		b0 =   s/2;
	    b1 =   0;
	    b2 =  -s/2;
	    a0 =   1 + alpha;
	    a1 =  -2*c;
	    a2 =   1 - alpha;
	    normaliseCoefficients();
	}
	

}
