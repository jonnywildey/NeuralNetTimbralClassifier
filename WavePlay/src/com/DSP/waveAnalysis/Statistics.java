package com.DSP.waveAnalysis;


import java.util.Arrays;

import com.util.ArrayMethods;
import com.util.Log;

public class Statistics {
	
	public static double[][] getPeaks(double[][] table) {
		//assume first row is dull.
		double[][] nt = getSignalPeaks(table[1]);
		for (int i = 0; i < nt[0].length; ++i) {
			nt[0][i] = table[0][(int) nt[0][i]]; //EWWW
		}
		return nt;
	}
	
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
	
	/**Error is in percent **/
	public static boolean isValueCloseTo(double is, double close, double error) {
		double cdown = close - (close * error);
		double cup = close + (close * error);
		//Log.d(close + " " + cup + " " + cdown);
		return (is < cup & is > cdown);
	}
	
	
	/** Put later first!!! **/
	private static boolean posGrad(double d, double e) {
		return d > e;
	}

	public static double round(double x, int decimal) {
		double dec = Math.pow(10, decimal);
		return Math.round(x * dec) / dec;
	}
	
	public static double round(float x, int decimal) {
		double dec = Math.pow(10, decimal);
		return Math.round(x * dec) / dec;
	}
	
	public static double round(long x, int decimal) {
		double dec = Math.pow(10, decimal);
		return Math.round(x * dec) / dec;
	}
	
	public static double round(char x, int decimal) {
		double dec = Math.pow(10, decimal);
		return Math.round(x * dec) / dec;
	}
	
	public static double round(byte x, int decimal) {
		double dec = Math.pow(10, decimal);
		return Math.round(x * dec) / dec;
	}

	public static double round(int x, int decimal) {
		double dec =  Math.pow(10, decimal);
		return Math.round((double)x * dec) / dec;
	}

}
