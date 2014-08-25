package com.DSP.waveAnalysis;


import com.util.ArrayMethods;

/**
 * Statistical methods for DSP analysis *.
 *
 * @author Jonny Wildey
 * @version 1.0
 */
public class Statistics {
	
	/**
	 * Gets the peaks.
	 *
	 * @param table the table
	 * @return the peaks
	 */
	public static double[][] getPeaks(double[][] table) {
		//assume first row is dull.
		double[][] nt = getSignalPeaks(table[1]);
		for (int i = 0; i < nt[0].length; ++i) {
			nt[0][i] = table[0][(int) nt[0][i]]; //EWWW
		}
		return nt;
	}
	
	/**
	 * Gets the signal peaks.
	 *
	 * @param table the table
	 * @return the signal peaks
	 */
	private static double[][] getSignalPeaks(double[] table) {
		double[][] peaks = new double[2][table.length]; 
		int counter = 0;
		boolean prev = posGrad(table[1], table[0]);
		boolean now = posGrad(table[2], table[1]);
		for (int i = 2; i < table.length; ++i) {
			now = posGrad(table[i], table[i - 1]);
			if (now == false & prev == true) { //just peaks
				peaks[0][counter] = i - 1; //i think...
				peaks[1][counter] = table[i - 1];
				counter++;
			}
			prev = now;
		}
		peaks = new double[][]{ArrayMethods.extend(peaks[0], counter - 1), 
				ArrayMethods.extend(peaks[1], counter - 1)};
		return peaks;
	}
	
	/**
	 * Error is in percent *.
	 *
	 * @param is the is
	 * @param close the close
	 * @param error the error
	 * @return true, if is value close to
	 */
	public static boolean isValueCloseTo(double is, double close, double error) {
		double cdown = close - (close * error);
		double cup = close + (close * error);
		//Log.d(close + " " + cup + " " + cdown);
		return (is < cup & is > cdown);
	}
	
	
	/**
	 * Put later first!!! *.
	 *
	 * @param d the d
	 * @param e the e
	 * @return true, if successful
	 */
	private static boolean posGrad(double d, double e) {
		return d > e;
	}

	/**
	 * Round.
	 *
	 * @param x the x
	 * @param decimal the decimal
	 * @return the double
	 */
	public static double round(double x, int decimal) {
		double dec = Math.pow(10, decimal);
		return Math.round(x * dec) / dec;
	}
	
	/**
	 * Round.
	 *
	 * @param x the x
	 * @param decimal the decimal
	 * @return the double
	 */
	public static double round(float x, int decimal) {
		double dec = Math.pow(10, decimal);
		return Math.round(x * dec) / dec;
	}
	
	/**
	 * Round.
	 *
	 * @param x the x
	 * @param decimal the decimal
	 * @return the double
	 */
	public static double round(long x, int decimal) {
		double dec = Math.pow(10, decimal);
		return Math.round(x * dec) / dec;
	}
	
	/**
	 * Round.
	 *
	 * @param x the x
	 * @param decimal the decimal
	 * @return the double
	 */
	public static double round(char x, int decimal) {
		double dec = Math.pow(10, decimal);
		return Math.round(x * dec) / dec;
	}
	
	/**
	 * Round.
	 *
	 * @param x the x
	 * @param decimal the decimal
	 * @return the double
	 */
	public static double round(byte x, int decimal) {
		double dec = Math.pow(10, decimal);
		return Math.round(x * dec) / dec;
	}

	/**
	 * Round.
	 *
	 * @param x the x
	 * @param decimal the decimal
	 * @return the double
	 */
	public static double round(int x, int decimal) {
		double dec =  Math.pow(10, decimal);
		return Math.round((double)x * dec) / dec;
	}

}
