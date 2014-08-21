package com.waveAnalysis;

import com.util.Log;

class Complex {
	public double re;
	public double im;
	
	public Complex(double real, double imaginary) {
		this.re = real;
		this.im = imaginary;
		
	}
	
	public Complex(double real) {
		this.re = real;
		this.im = 0;
	}
	
	public boolean equals(Complex c) {
		return c.re == this.re & 
				c.im == this.im;
	}
	
	public Complex add(Complex c) {
		return new Complex(this.re + c.re, 
				this.im + c.im);
	}
	
	public Complex subtract(Complex c) {
		return new Complex(this.re - c.re, 
				this.im - c.im);
	}
	
	public Complex multiply(Complex c) {
		return new Complex(this.re * c.re - 
				this.im * c.im, 
				this.im * c.re + 
				this.re * c.im);
	}
	
	public Complex divide(Complex c) {
		return new Complex((this.re * c.re - 
				this.im * c.im) / 
				(c.re * c.re + c.im * c.im), 
				(this.im * c.re + 
				this.re * c.im) /
				(c.re * c.re + c.im * c.im));
	}
	
	/** e to the power of this **/
	public Complex exp() {
		double er = Math.exp(this.re);
		return new Complex(er * Math.cos(this.im), 
					er * Math.sin(this.im));
	}
	
	public Complex log() {
		return new Complex(Math.log(this.getDistance()), this.getArg());
	}
	
	
	public Complex pow(Complex x) {
		return this.log().multiply(x).exp();
	}
	
	
	public double getDistance() {
		return Math.pow((this.im * this.im) + 
				(this.re * this.re), 0.5);
	}
	
	public double getArg() {
		return Math.atan(this.im / this.re);
	}
	
	public String toString() {
		return (this.re + "re +" + this.im + "im\t");
	}
	
	public static Complex[] doubleToComplex(double[] values) {
		Complex[] cs = new Complex[values.length];
		for (int i = 0; i < values.length; ++i) {
			cs[i] = new Complex(values[i]);
		}
		return cs;
	}
	
	/** return all maginutes (distance) of a complex array **/
	public static double[] getMagnitudes(Complex[] values) {
		double[] cs = new double[values.length];
		for (int i = 0; i < values.length; ++i) {
			cs[i] = values[i].getDistance();
		}
		return cs;
	}
	
	/**Get a subset of array **/
	public static Complex[] getSubset(Complex[] object, int start, int end) {
		Complex[] newBytes = new Complex[(end + 1) - start];
		for (int i = 0; i <= (end - start); ++i) {
			newBytes[i] = object[start + i];
		}
		return newBytes;
	}
	
	/** return all reals of a complex array **/
	public static double[] getReals(Complex[] values) {
		double[] cs = new double[values.length];
		for (int i = 0; i < values.length; ++i) {
			cs[i] = values[i].re;
		}
		return cs;
	}
	
	/** return all imaginaries of a complex array **/
	public static double[] getImaginary(Complex[] values) {
		double[] cs = new double[values.length];
		for (int i = 0; i < values.length; ++i) {
			cs[i] = values[i].im;
		}
		return cs;
	}
	
	
	
}