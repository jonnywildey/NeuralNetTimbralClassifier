package libraryclasses;
import libraryclasses.LogWin.*;
public class ArrayFunctions {

	public static double getMax(double[] array) {
		assert array.length > 0;
		double max = Double.MIN_VALUE;
		for (int i = 0; i < array.length; ++i) {
			if (array[i] > max) {
				max = (double)array[i];
			}
		}
		return max;
	}
	
	public static double getMin(double[] array) {
		assert array.length > 0;
		double min = Double.MAX_VALUE;
		for (int i = 0; i < array.length; ++i) {
			if (array[i] < min) {
				min = (double)array[i];
			}
		}
		return min;
	}
	
	public static String removeCharFromString(String str, int index) {
		/* removes a single character from a string. should catch bad indexes */
		try {String str2 = new String();
		str2 = str.substring(0, index) + str.substring(index + 1, str.length());
		return str2;} catch(StringIndexOutOfBoundsException e) {
			LogWin.messagePrint("Index of string out of bounds. Will return original string");
			return str;
		}
		//assert (index > 0 && index < str.length());
		
		
	}
	
	
	public static double getMean(double[] array) {
		assert array.length > 0;
		double mean = 0;
		for (int i = 0; i < array.length; ++i) {
			mean += array[i];
		}
		mean /= array.length;
		return mean;
	}
	
	public static double getSum(double[] array) {
		assert array.length > 0;
		double sum = 0;
		for (int i = 0; i < array.length; ++i) {
			sum += array[i];
		}
		return sum;
	}
	
	public static void main(String[] args) {
		LogWin.messagePrint(removeCharFromString("Hello to you", 12));
	}
	
	
	
}
