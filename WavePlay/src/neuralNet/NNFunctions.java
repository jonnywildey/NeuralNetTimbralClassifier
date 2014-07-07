package neuralNet;

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
		return total / values.length;
	}
	
	public static double average(double[][] values) {
		return average(tableToLongRow(values));
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
		return geoMean(tableToLongRow(values));
	}
	
	public static double[] tableToLongRow(double[][] table) {
		//get length
		int l = 0;
		for (double[] row : table) {
			l += row.length;
		}
		double[] lr = new double[l];
		int c = 0;
		for (int i = 0; i < table.length; ++i) {
			for (int j = 0; j < table[i].length; ++j) {
				lr[c] = table[i][j];
				c++;
			}
		}
		return lr;
	}
}
