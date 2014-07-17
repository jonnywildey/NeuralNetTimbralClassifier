package waveAnalysis;


import java.util.Arrays;

import filemanager.ArrayStuff;
import filemanager.Log;

public class Statistics {
	
	public static double[][] getPeaks(double[][] table) {
		//assume first row is dull.
		double[][] nt = getSignChanges(table[1]);
		for (int i = 0; i < nt[0].length; ++i) {
			nt[0][i] = table[0][(int) nt[0][i]]; //EWWW
		}
		return nt;
	}
	
	public static double getFundamental(double[][] table) {
		double[][] peaks = getPeaks(table);
		double err = 0.05; //5%
		//get max
		double max = Double.NEGATIVE_INFINITY;
		double freq = 0;
		
		for (int i = 0; i < peaks[1].length; ++i) {
			if (max < peaks[1][i]) {
				max = peaks[1][i];
				freq = peaks[0][i];
			}
		}
		//does 8ve below exist?
		//Log.d("current peak" + freq);
		//Log.d(Arrays.toString(peaks[0]));
		
		for (int i = peaks[1].length - 1; i >= 0; --i) {
			if (isValueCloseTo(freq / 2, peaks[0][i], err) | //8ve below
					isValueCloseTo(freq / 1.5, peaks[0][i], err)) { //5th below
				if (compareVolume(max, peaks[1][i])) {
					freq = peaks[0][i];
					break; //could not have break...
				}
				
				
			}
		}
		return freq;
	}
	
	/** returns true if b is of similar volume to a **/
	private static boolean compareVolume(double a, double b) {
		//will put margin at 12db
		return b + 12 >= a;
	}
	
	
	private static double[][] getSignChanges(double[] table) {
		double[][] peaks = new double[2][table.length]; 
		int counter = 0;
		boolean prev = posGrad(table[1], table[0]);
		boolean now = posGrad(table[2], table[1]);
		for (int i = 2; i < table.length; ++i) {
			now = posGrad(table[i], table[i - 1]);
			if (now == false & prev == true) { //just troughs
				peaks[0][counter] = i - 1; //i think...
				peaks[1][counter] = table[i - 1];
				counter++;
			}
			prev = now;
		}
		peaks = new double[][]{ArrayStuff.extend(peaks[0], counter - 1), 
				ArrayStuff.extend(peaks[1], counter - 1)};
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

}
