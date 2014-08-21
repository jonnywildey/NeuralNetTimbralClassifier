package com.neuralNet;

import com.filemanager.ArrayMethods;

public class NNFunctions {

	public static Double sigmoid(Double f) {
		double x = (double)f;
		x = 1 / (1 + (Math.pow(Math.E, x * -1)));
		return (Double)x;
	}

	public static Double sigmoidDerivative(Double f) {
		f = sigmoid(f) * (1 - sigmoid(f));
		return f;
	}

	public static Integer stepFunction(Double input) {
		if (input > 0f) {
				return 1;
		} else {
			return 0;
		}
	}
	
	
	
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
	
	public static double average(double[][] values) {
		return average(ArrayMethods.tableToLongRow(values));
	}
	
	public static double geoMean(double[] values) {
		double l = values.length;
		double total = 0;
		for (double d : values) {
			total *= d;
		}
		return Math.pow(total, 1 / l);
	}
	
	public static double geoMean(double[][] values) {
		return geoMean(ArrayMethods.tableToLongRow(values));
	}
}
