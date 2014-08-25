package com.DSP.waveProcess.filters;


/** basic template for filter. Just need to change the coefficients **/
public class Filter {
	
	protected double sampleRate;
	protected double centreFreq;
	protected double octaveBW;
	protected double gain;
	
	protected double bigA;
	protected double w0;   
	protected double c; 
	protected double s; 
	protected double alpha;
	
	protected double a0;
	protected double a1;
	protected double a2;
	protected double b0;
	protected double b1;
	protected double b2;
	
	double xmem1, xmem2, ymem1, ymem2;
	
	public Filter(int sampleRate, double centreFreq, double octaveBW,
			double gain) {
		this.sampleRate = sampleRate;
		this.centreFreq = centreFreq;
		this.octaveBW = octaveBW;
		this.gain = gain;
		this.calculateMiddles();
		this.calculateCoefficients();
		this.reset();
	}
	
	protected void calculateMiddles() {
		this.bigA = Math.pow(10, gain / 20);
		this.w0 = 2 * Math.PI * this.centreFreq / this.sampleRate;
		this.c = Math.cos(w0);
		this.s = Math.sin(w0);
		this.alpha = s * Math.sinh( 0.34657359028 * this.octaveBW * w0 / this.s ); 
	}
	
	
	
	protected void calculateCoefficients() {
	}
	
	public void reset() {
	   xmem1 = xmem2 = ymem1 = ymem2 = 0;
	}
	
	protected void normaliseCoefficients() {
		b0 /= a0;
	    b1 /= a0;
	    b2 /= a0;
	    a1 /= a0;
	    a2 /= a0;
	}
	
	public double process(double x ) {
	   double y = b0*x + b1*xmem1 + b2*xmem2 - a1*ymem1 - a2*ymem2;
	   xmem2 = xmem1;
	   xmem1 = x;
	   ymem2 = ymem1;
	   ymem1 = y;
	   return y;
	}

	public double getSampleRate() {
		return sampleRate;
	}

	public void setSampleRate(double sampleRate) {
		this.sampleRate = sampleRate;
	}

	public double getCentreFreq() {
		return centreFreq;
	}

	public void setCentreFreq(double centreFreq) {
		this.centreFreq = centreFreq;
		this.calculateMiddles();
		this.calculateCoefficients();
		//this.reset();
	}

	public double getOctaveBW() {
		return octaveBW;
	}

	public void setOctaveBW(double octaveBW) {
		this.octaveBW = octaveBW;
	}

	public double getGain() {
		return gain;
	}

	public void setGain(double gain) {
		this.gain = gain;
	}
	
	


}
