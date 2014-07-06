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

}
