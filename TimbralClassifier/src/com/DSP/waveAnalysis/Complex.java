package com.DSP.waveAnalysis;

/**
 * Complex number class for transforms *.
 * 
 * @author Jonny Wildey
 * @version 1.0
 */
class Complex {
	/**
	 * Double to complex.
	 * 
	 * @param values
	 *            the values
	 * @return the complex[]
	 */
	public static Complex[] doubleToComplex(double[] values) {
		Complex[] cs = new Complex[values.length];
		for (int i = 0; i < values.length; ++i) {
			cs[i] = new Complex(values[i]);
		}
		return cs;
	}
	/**
	 * return all imaginaries of a complex array *.
	 * 
	 * @param values
	 *            the values
	 * @return the imaginary
	 */
	public static double[] getImaginary(Complex[] values) {
		double[] cs = new double[values.length];
		for (int i = 0; i < values.length; ++i) {
			cs[i] = values[i].im;
		}
		return cs;
	}

	/**
	 * return all magnitudes (distance) of a complex array *.
	 * 
	 * @param values
	 *            the values
	 * @return the magnitudes
	 */
	public static double[] getMagnitudes(Complex[] values) {
		double[] cs = new double[values.length];
		for (int i = 0; i < values.length; ++i) {
			cs[i] = values[i].getDistance();
		}
		return cs;
	}

	/**
	 * return all reals of a complex array *.
	 * 
	 * @param values
	 *            the values
	 * @return the reals
	 */
	public static double[] getReals(Complex[] values) {
		double[] cs = new double[values.length];
		for (int i = 0; i < values.length; ++i) {
			cs[i] = values[i].re;
		}
		return cs;
	}

	/**
	 * Get a subset of array *.
	 * 
	 * @param object
	 *            the object
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 * @return the subset
	 */
	public static Complex[] getSubset(Complex[] object, int start, int end) {
		Complex[] newBytes = new Complex[(end + 1) - start];
		for (int i = 0; i <= (end - start); ++i) {
			newBytes[i] = object[start + i];
		}
		return newBytes;
	}

	public double re;

	public double im;

	/**
	 * Instantiates a new complex.
	 * 
	 * @param real
	 *            the real
	 */
	public Complex(double real) {
		this.re = real;
		this.im = 0;
	}

	/**
	 * Instantiates a new complex.
	 * 
	 * @param real
	 *            the real
	 * @param imaginary
	 *            the imaginary
	 */
	public Complex(double real, double imaginary) {
		this.re = real;
		this.im = imaginary;

	}

	/**
	 * Adds the.
	 * 
	 * @param c
	 *            the c
	 * @return the complex
	 */
	public Complex add(Complex c) {
		return new Complex(this.re + c.re, this.im + c.im);
	}

	/**
	 * Divide.
	 * 
	 * @param c
	 *            the c
	 * @return the complex
	 */
	public Complex divide(Complex c) {
		return new Complex((this.re * c.re - this.im * c.im)
				/ (c.re * c.re + c.im * c.im),
				(this.im * c.re + this.re * c.im) / (c.re * c.re + c.im * c.im));
	}

	/**
	 * Equals.
	 * 
	 * @param c
	 *            the c
	 * @return true, if successful
	 */
	public boolean equals(Complex c) {
		return c.re == this.re & c.im == this.im;
	}

	/**
	 * e to the power of this *.
	 * 
	 * @return the complex
	 */
	public Complex exp() {
		double er = Math.exp(this.re);
		return new Complex(er * Math.cos(this.im), er * Math.sin(this.im));
	}

	/**
	 * Gets the arg.
	 * 
	 * @return the arg
	 */
	public double getArg() {
		return Math.atan(this.im / this.re);
	}

	/**
	 * Gets the distance.
	 * 
	 * @return the distance
	 */
	public double getDistance() {
		return Math.pow((this.im * this.im) + (this.re * this.re), 0.5);
	}

	/**
	 * Log.
	 * 
	 * @return the complex
	 */
	public Complex log() {
		return new Complex(Math.log(this.getDistance()), this.getArg());
	}

	/**
	 * Multiply.
	 * 
	 * @param c
	 *            the c
	 * @return the complex
	 */
	public Complex multiply(Complex c) {
		return new Complex(this.re * c.re - this.im * c.im, this.im * c.re
				+ this.re * c.im);
	}

	/**
	 * Pow.
	 * 
	 * @param x
	 *            the x
	 * @return the complex
	 */
	public Complex pow(Complex x) {
		return this.log().multiply(x).exp();
	}

	/**
	 * Subtract.
	 * 
	 * @param c
	 *            the c
	 * @return the complex
	 */
	public Complex subtract(Complex c) {
		return new Complex(this.re - c.re, this.im - c.im);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return (this.re + "re +" + this.im + "im\t");
	}

}