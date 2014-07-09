package neuralNet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class TestPatterns implements Serializable {
	

	private static final long serialVersionUID = -4219496637757583180L;
	private ArrayList<Pattern> trainingPatterns;
	private ArrayList<Pattern> validationPatterns;
	private ArrayList<Pattern> testingPatterns;
	private long seed;
	private double trainingPortion;
	private double testingPortion;
	private double validationPortion;
	
	

	public TestPatterns(ArrayList<Pattern> patterns, long seed) {
		trainingPatterns = new ArrayList<Pattern>();
		validationPatterns = new ArrayList<Pattern>();
		testingPatterns = new ArrayList<Pattern>();
		this.setDefaultPortions();
		this.seed = seed;
		if (patterns != null) {
			this.separatePatterns(patterns);
		}
	}
	
	
	public TestPatterns(ArrayList<Pattern> patterns) {
		this(patterns, System.currentTimeMillis());
	}
	
	public TestPatterns() {
		this(null);
	}
	
	/** separates training pattern sets, testing pattern sets and validation pattern sets **/
	public void separatePatterns(ArrayList<Pattern> patterns) {
		//shuffle
		patterns = NNUtilities.knuthShuffle(patterns, seed);
		int i = 0;
		for (Pattern p: patterns) {
			if (i < (trainingPortion * patterns.size())) {
				this.trainingPatterns.add(p); //training
			} else if(i < (trainingPortion + testingPortion) * patterns.size()) {
				this.validationPatterns.add(p); //validation
			} else {
				this.testingPatterns.add(p); //validation
			}
			i++;
		}
		//check to make sure validation has at least one of each
		if (getPatternOutputCount(this.testingPatterns) < getPatternOutputCount(patterns)) {
			separatePatterns(patterns);
		}

	}
	
	public int getPatternOutputCount(ArrayList<Pattern> patterns) {
		double[] vals = new double[patterns.size()];
		for (int i = 0; i < patterns.size(); ++i) {
			vals[i] = (double) patterns.get(i).getTargetNumber();
		}
		Double[][] counts = NNUtilities.getCount(vals);
		return counts.length;
		
	}
	
	public Double[][] getPatternOuts(ArrayList<Pattern> patterns) {
		double[] vals = new double[patterns.size()];
		for (int i = 0; i < patterns.size(); ++i) {
			vals[i] = (double) patterns.get(i).getTargetNumber();
		}
		Double[][] counts = NNUtilities.getCount(vals);
		return counts;
		
	}
	
	
	public double getTrainingPortion() {
		return trainingPortion;
	}

	public void setTrainingPortion(double trainingPortion) {
		this.trainingPortion = trainingPortion;
	}

	public double getTestingPortion() {
		return testingPortion;
	}

	public void setTestingPortion(double testingPortion) {
		this.testingPortion = testingPortion;
	}

	public double getValidationPortion() {
		return validationPortion;
	}

	public void setValidationPortion(double validationPortion) {
		this.validationPortion = validationPortion;
	}

	
	public ArrayList<Pattern> getTrainingPatterns() {
		return trainingPatterns;
	}

	public void setTrainingPatterns(ArrayList<Pattern> trainingPatterns) {
		this.trainingPatterns = trainingPatterns;
	}
	
	public long getSeed() {
		return seed;
	}

	public void setSeed(long seed) {
		this.seed = seed;
	}
	
	public void setDefaultPortions() {
		this.trainingPortion = 0.6;
		this.testingPortion = 0.2;
		this.validationPortion = 0.2;
	}
	
	public ArrayList<Pattern> getValidationPatterns() {
		return validationPatterns;
	}

	public void setValidationPatterns(ArrayList<Pattern> validationPatterns) {
		this.validationPatterns = validationPatterns;
	}

	public ArrayList<Pattern> getTestingPatterns() {
		return testingPatterns;
	}

	public void setTestingPatterns(ArrayList<Pattern> testingPatterns) {
		this.testingPatterns = testingPatterns;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\nTraining Patterns Count: " + this.trainingPatterns.size() + "\t" +
				Arrays.deepToString(this.getPatternOuts(this.trainingPatterns)) + "\n");
		sb.append("\nTesting Patterns Count: " + this.validationPatterns.size() + "\t" +
				Arrays.deepToString(this.getPatternOuts(this.validationPatterns)) + "\n");
		sb.append("\nValidation Patterns Count: " + this.testingPatterns.size() + "\t" +
				Arrays.deepToString(this.getPatternOuts(this.testingPatterns)) + "\n");
		return sb.toString();
	}
	

	

}
