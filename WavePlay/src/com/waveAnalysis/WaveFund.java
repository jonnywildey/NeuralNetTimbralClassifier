package com.waveAnalysis;

import java.util.ArrayList;
import java.util.Arrays;

import com.riff.Signal;
import com.riff.Wave;
/**
 * Class for determining the fundamental pitch of a waveform.
 * DEPRECATED, USE GETFUNDAMENTAL IN PITCH INSTEAD
 * @author Jonny Wildey
 *
 */
public class WaveFund {

	private Signal s;
	private int frame;
	private double[] signal;
	private boolean verbose;
	public int intError;
	public double percentageError;

	public WaveFund(Wave wave) {
		this.s = wave.getSignals();
		this.frame = 2560;
		this.signal = this.s.getSignal()[0]; //always pick left for now
		this.verbose = false;
		percentageError = 0.05; 
		intError = 3; 
	}
	
	public WaveFund(Signal s) {
		this.s = s;
		this.frame = 2560;
		this.signal = s.getSignal()[0]; //always pick left for now
		this.verbose = false;
		percentageError = 0.05; 
		intError = 3; 
	}

	public double samplesToHz(double avg) {
		double sampleRate = this.s.getSampleRate();
		return (sampleRate / avg) * 0.5; 
	}

	public static boolean changeSign(long a, long b) {
		if ((a < 0 && b >= 0) | (a >= 0 && b < 0)) {
			return true;
		} else {
			return false;
		}
	}
	
	
	private boolean changeSign(double a, double b) {
		if ((a < 0 && b >= 0) | (a >= 0 && b < 0)) {
			return true;
		} else {
			return false;
		}
	}

	public void verbose(boolean on) {
		this.verbose = on;
	}

	public static boolean isWithinPercentage(int a, int b, double error) {
		double la =  (a * (1 / (error + 1)));
		double lb =  (b * (1 / (error + 1)));
		double ha = a + (a * error);
		double hb = b + (b * error);		
		if ((a >= lb & a <= hb) | (b >= la & b <= ha)) {
			return true;
		} else {
			return false;
		}	
	}

	public static boolean isWithinAddition(int a, int b, int error) {
		double la =  a - error;
		double lb =  b - error;
		double ha = a + error;
		double hb = b + error;
		if ((a >= lb & a <= hb) | (b >= la & b <= ha)) {
			return true;
		} else {
			return false;
		}	
	}

	public static double getAverage(int[] array) {
		int c = 0;
		for (int i: array) {
			c += i;
		}
		return  c / array.length;
	}

	/** for fundamental period analysis principally.
	 * Finds largest, highest occurring number in a 2d array. 
	 **/
	public int getLargestHighestOccurring(Integer[][] int2d) {
		int value = 0;
		int product = 0;
		int c = 0;
		int i = 0;
		for (Integer[] ia: int2d) {
			c = ia[0] * ia[1];
			if ( c > product) {
				product = c;
				value = int2d[i][0];
			}
			i++;
		}
		if (verbose) {
			System.out.println("Generally most recurring jump:\n" + value);
		}
		return value;
	}

	/** return a 2d array with the value and its count.
	 * Can deal with multiples. Values should be in the same order as array (with 
	 * subsequent duplicate values removed). **/
	private  Integer[][] getCount(int[] array) {
		ArrayList<Integer[]> countArray = new ArrayList<Integer[]>();
		int count = 0;
		for(int i = 0; i < array.length; ++i) {
			//check if list contains current value
			boolean unique = true;
			for (Integer[] ia: countArray) {
				if (ia[0].intValue() == array[i]) {
					unique = false;
				}
			}
			//Count values
			if (unique) {
				count = 1;
				for (int j = i + 1; j <  array.length; ++j) {
					if (array[i] == array[j]) {
						count++;
					}
				}
				countArray.add(new Integer[]{array[i], count});
			}
		}
		Integer[][] intArray = new Integer[countArray.size()][];

		intArray = countArray.toArray(intArray);
		if (verbose){
			System.out.println("Index jump and frequency of jump:\n" + Arrays.deepToString(intArray));
		}
		return intArray;
	}



	public static int getMax(int[] array) {
		int max = Integer.MIN_VALUE;
		for (int i: array) {
			if(i > max) {
				max = i;
			}
		}
		return max;
	}

	private ArrayList<Integer> getZeroCrossings() {
		ArrayList<Integer> zeroX = new ArrayList<Integer>(frame); //can't be more zero crossing than samples
		double prev = signal[0];
		for (int i = 1; i < frame; ++i) {
			if (changeSign(prev, signal[i])) {
				zeroX.add(i);
			}
			prev = signal[i];
		}
		if (verbose) {
			System.out.println("Zero crossings:\n" + zeroX.toString());
		}
		return zeroX;
	}
	
	private ArrayList<Integer> getSignChanges() {
		ArrayList<Integer> zeroX = new ArrayList<Integer>(frame); //can't be more zero crossing than samples
		double prev = posGrad(signal[0], 0);
		double now = 0;
		for (int i = 1; i < frame; ++i) {
			now = posGrad(signal[i], signal[i - 1]);
			if (changeSign(prev, now)) {
				zeroX.add(i);
			}
			prev = now;
		}
		if (verbose) {
			System.out.println("Zero crossings:\n" + zeroX.toString());
		}
		return zeroX;
	}
	
	

	
	/** Put later first!!! **/
	private double posGrad(double d, double e) {
		if (d > e) {
			return 1;
		} else {
			return -1;
		}
	}

	/** determines fundamental frequency using zero crossing analysis **/
	public double zeroCross() {
		double fundamental = 0;
		if (verbose) {
			System.out.println("Determining fundamental frequency using "
					+ "zero cross detection with frame size " + this.frame);
		}
		//get zero crossing array
		//ArrayList<Integer> zeroX = getZeroCrossings(); CHANGED
		ArrayList<Integer> zeroX = getSignChanges();
		//get distance between sample and next.
		int[] zeroDif = getSampleDistance(zeroX);
		//find each sample distance's nearest close match
		int[] simDistance = nearMatch(zeroDif);	
		//to determine fundamental period (rather than a harmonic period)
		//we need to find the longest, generally recurring index sample
		//distance
		Integer[][] counts = getCount(simDistance);
		int fundPer = getLargestHighestOccurring(counts);
		//generate period in samples
		int[] samplePeriods = generateSamplePeriods(zeroDif, fundPer);
		//average
		double avg = getAverage(samplePeriods);
		if (verbose) {
			System.out.println("average sample period: " +avg);
		}
		//and convert
		fundamental = samplesToHz(avg);
		if(verbose) {
			System.out.println("Fundamental frequency of Signal: " + 
					fundamental + "HZ");
		}
		return fundamental;
	}

	private int[] generateSamplePeriods(int[] zeroDif, int fundPer) {
		int it = 0;
		int[] fav = new int[zeroDif.length - fundPer];
		for (int fa: fav) {
			fa = 0;
			for (int i = 0; i < fundPer; ++i) {
				fa += zeroDif[i + it];
			}
			fav[it] = fa;
			it++;
		}
		if (verbose) {
			System.out.println("Sample periods:\n" + Arrays.toString(fav));
		}
		return fav;
	}

	/**Looks in array for nearest (to the right) number. 
	 * Uses both additive and percentage errors.
	 */
	private int[] nearMatch(int[] zeroDif) {
		int[] simDistance = new int[zeroDif.length - 1];

		for (int j = 0; j < simDistance.length; ++j) {
			simDistance[j] = 0;
			for (int i = j + 1; i < zeroDif.length; ++i) {
				if (isWithinAddition(zeroDif[j] , zeroDif[i], intError) | 
						isWithinPercentage(zeroDif[j], zeroDif[i], percentageError)) {
					simDistance[j] = i - j;
					break;
				}
			}
		}
		if (verbose){
			System.out.println("Near match index distance:\n" + Arrays.toString(simDistance));
		}
		return simDistance;
	}

	/** when given an arraylist of sample indexes returns an array of the distance
	 * between one sample and the next.
	 */
	private int[] getSampleDistance(ArrayList<Integer> zeroX) {
		int[] zeroDif = new int[zeroX.size() - 1];
		for (int i = 1; i < zeroDif.length + 1; ++i) {
			zeroDif[i - 1] = zeroX.get(i) - zeroX.get(i - 1);
		}
		if (verbose) {
			System.out.println("Sample distance:\n" + Arrays.toString(zeroDif));
		}
		return zeroDif;
	}



}
