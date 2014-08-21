package com.waveProcess.filters;



public class PeakEQ extends Filter{

	public PeakEQ(int sampleRate, double centreFreq, double octaveBW, double gain) {
		super(sampleRate, centreFreq, octaveBW, gain);
		
	}
	
	@Override
	protected void calculateCoefficients() {
		b0 =   1 + alpha*bigA;
		b1 =  -2*c;
        b2 =   1 - alpha*bigA;
        a0 =   1 + alpha/bigA;
        a1 =  -2*c;
        a2 =   1 - alpha/bigA;
	    normaliseCoefficients();
	}
	

}
