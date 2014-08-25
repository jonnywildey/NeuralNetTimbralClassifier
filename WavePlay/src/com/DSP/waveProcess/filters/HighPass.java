package com.DSP.waveProcess.filters;



/**
 * The Class HighPass.
 *
 * @author Jonny Wildey
 * @version 1.0
 */
public class HighPass extends Filter{

	/**
	 * Instantiates a new high pass.
	 *
	 * @param sampleRate the sample rate
	 * @param centreFreq the centre freq
	 * @param resonance the resonance
	 */
	public HighPass(int sampleRate, double centreFreq, double resonance) {
		super(sampleRate, centreFreq, 1 / resonance, 0);
		
	}
	
	/* (non-Javadoc)
	 * @see com.DSP.waveProcess.filters.Filter#calculateCoefficients()
	 */
	@Override
	protected void calculateCoefficients() {
		b0 =  (1 + c)/2;
	    b1 = -(1 + c);
	    b2 =  (1 + c)/2;
	    a0 =   1 + alpha;
	    a1 =  -2*c;
	    a2 =   1 - alpha;
	    normaliseCoefficients();
	}
	

}
