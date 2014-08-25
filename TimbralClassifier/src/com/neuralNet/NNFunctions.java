package com.neuralNet;

import com.util.ArrayMethods;

/**
 * Math functions for neural networks *.
 *
 * @author Jonny Wildey
 * @version 1.0
 */
public class NNFunctions {

	/**
	 * Sigmoid.
	 *
	 * @param f the f
	 * @return the double
	 */
	public static Double sigmoid(Double f) {
		double x = (double)f;
		x = 1 / (1 + (Math.pow(Math.E, x * -1)));
		return (Double)x;
	}

	/**
	 * Sigmoid derivative.
	 *
	 * @param f the f
	 * @return the double
	 */
	public static Double sigmoidDerivative(Double f) {
		f = sigmoid(f) * (1 - sigmoid(f));
		return f;
	}

	/**
	 * Step function.
	 *
	 * @param input the input
	 * @return the integer
	 */
	public static Integer stepFunction(Double input) {
		if (input > 0f) {
				return 1;
		} else {
			return 0;
		}
	}
	
	
	
	/**
	 * Average.
	 *
	 * @param values the values
	 * @return the double
	 */
	public static double average(double[] values) {
		double total = 0;
		for (double d : values) {
			total += d;
		}
		if (total == 0) {
			return 0;
		} else {
			return total / values.length;
		}
		
	}
	
	/**
	 * Average.
	 *
	 * @param values the values
	 * @return the double
	 */
	public static double average(double[][] values) {
		return average(ArrayMethods.tableToLongRow(values));
	}
	
	/**
	 * Geo mean.
	 *
	 * @param values the values
	 * @return the double
	 */
	public static double geoMean(double[] values) {
		double l = values.length;
		double total = 0;
		for (double d : values) {
			total *= d;
		}
		return Math.pow(total, 1 / l);
	}
	
	/**
	 * Geo mean.
	 *
	 * @param values the values
	 * @return the double
	 */
	public static double geoMean(double[][] values) {
		return geoMean(ArrayMethods.tableToLongRow(values));
	}
}
