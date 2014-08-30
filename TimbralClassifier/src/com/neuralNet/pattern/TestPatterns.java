package com.neuralNet.pattern;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import com.neuralNet.NNUtilities;
import com.util.Log;

/**
 * A group of patterns. Useful methods for separation into training, validation
 * and testing sets
 * 
 * @author Jonny Wildey
 * 
 */
public class TestPatterns implements Serializable {

	private static final long serialVersionUID = -4219496637757583180L;
	@SuppressWarnings("unchecked")
	protected static ArrayList<TestPatterns> addToTestPatternsArrayList(
			int count, ArrayList<ArrayList<Pattern>> trainingPatterns,
			ArrayList<ArrayList<Pattern>> validationPatterns,
			ArrayList<ArrayList<Pattern>> testingPatterns,
			ArrayList<Pattern> trP, ArrayList<Pattern> vaP,
			ArrayList<Pattern> teP) {
		ArrayList<TestPatterns> np = new ArrayList<TestPatterns>(count);
		// Log.d("size: " + np.size());
		for (int i = 0; i < count; ++i) {
			trP = (ArrayList<Pattern>) trP.clone();
			trP.addAll(trainingPatterns.get(i));
			vaP = (ArrayList<Pattern>) vaP.clone();
			vaP.addAll(validationPatterns.get(i));
			teP = (ArrayList<Pattern>) teP.clone();
			teP.addAll(testingPatterns.get(i));
			np.add(i, new TestPatterns(trP, vaP, teP));
		}
		return np;
	}
	/** Convert arraylist of patterns to array **/
	public static Pattern[] convertPatterns(ArrayList<Pattern> patterns) {
		Pattern[] pat = new Pattern[patterns.size()];
		for (int i = 0; i < pat.length; ++i) {
			pat[i] = patterns.get(i);
		}
		return pat;
	}
	/** Convert array of patterns to arraylist of patterns **/
	public static ArrayList<Pattern> convertPatterns(Pattern[] wavePatterns) {
		ArrayList<Pattern> nwp = new ArrayList<Pattern>(wavePatterns.length);
		for (Pattern wp : wavePatterns) {
			nwp.add(wp);
		}
		return nwp;
	}
	/** Split up an arraylist by the count. **/
	protected static <T> ArrayList<ArrayList<T>> split(ArrayList<T> patterns,
			int count) {
		ArrayList<ArrayList<T>> np = new ArrayList<ArrayList<T>>(count);
		int[] sizes = new int[count]; // size of array list
		// determine lengths
		int div = patterns.size() / count;
		int mod = patterns.size() % count;
		for (int i = 0; i < count; ++i) {
			if (mod != 0) {
				np.add(i, new ArrayList<T>(div + 1));
				sizes[i] = div + 1;
				mod--;
			} else {
				np.add(i, new ArrayList<T>(div));
				sizes[i] = div;
			}
		}
		// write values
		int c = 0;
		for (int i = 0; i < np.size(); ++i) {
			for (int j = 0; j < sizes[i]; ++j) {
				np.get(i).add(j, patterns.get(c));
				c++;
			}
		}
		return np;
	}
	/** Split a group of test patterns into equal groups **/
	public static ArrayList<TestPatterns> split(TestPatterns testPatterns,
			int count) {
		ArrayList<ArrayList<Pattern>> trainingPatterns = split(
				testPatterns.getTrainingPatterns(), count);
		ArrayList<ArrayList<Pattern>> validationPatterns = split(
				testPatterns.getValidationPatterns(), count);
		ArrayList<ArrayList<Pattern>> testingPatterns = split(
				testPatterns.getTestingPatterns(), count);
		ArrayList<TestPatterns> np = new ArrayList<TestPatterns>(count);
		Log.d("size: " + np.size());
		// set to new test Patterns
		for (int i = 0; i < count; ++i) {
			np.set(i, new TestPatterns(trainingPatterns.get(i),
					validationPatterns.get(i), testingPatterns.get(i)));
		}
		return np;
	}
	/**
	 * Split a group of test patterns into equal groups. each group contains all
	 * of the previous group and more. i.e the last group will be the original
	 * testPatterns
	 **/
	@SuppressWarnings("unchecked")
	public static ArrayList<TestPatterns> splitAndAdd(
			TestPatterns testPatterns, int count) {
		ArrayList<ArrayList<Pattern>> trainingPatterns = split(
				testPatterns.getTrainingPatterns(), count);
		ArrayList<ArrayList<Pattern>> validationPatterns = split(
				testPatterns.getValidationPatterns(), count);
		ArrayList<ArrayList<Pattern>> testingPatterns = split(
				testPatterns.getTestingPatterns(), count);
		// set to new test Patterns
		ArrayList<Pattern> trP = new ArrayList<Pattern>(0);
		ArrayList<Pattern> vaP = new ArrayList<Pattern>(0);
		ArrayList<Pattern> teP = new ArrayList<Pattern>(0);
		ArrayList<TestPatterns> np = addToTestPatternsArrayList(count,
				trainingPatterns, validationPatterns, testingPatterns, trP,
				vaP, teP);
		return np;
	}
	/**
	 * Split a group of test patterns into equal groups and add them to
	 * initialTestPatterns. each group contains all of the previous group and
	 * more. i.e the last group will be the original testPatterns
	 **/
	@SuppressWarnings("unchecked")
	public static ArrayList<TestPatterns> splitAndAddToInital(
			TestPatterns initial, TestPatterns additional, int count) {
		ArrayList<ArrayList<Pattern>> trainingPatterns = split(
				additional.getTrainingPatterns(), count);
		ArrayList<ArrayList<Pattern>> validationPatterns = split(
				additional.getValidationPatterns(), count);
		ArrayList<ArrayList<Pattern>> testingPatterns = split(
				additional.getTestingPatterns(), count);
		// set to new test Patterns
		ArrayList<Pattern> trP = (ArrayList<Pattern>) initial
		.getTrainingPatterns().clone();
		ArrayList<Pattern> vaP = (ArrayList<Pattern>) initial
		.getValidationPatterns().clone();
		ArrayList<Pattern> teP = (ArrayList<Pattern>) initial
		.getTestingPatterns().clone();
		ArrayList<TestPatterns> np = addToTestPatternsArrayList(count,
				trainingPatterns, validationPatterns, testingPatterns, trP,
				vaP, teP);
		return np;
	}
	private ArrayList<Pattern> trainingPatterns;

	private ArrayList<Pattern> validationPatterns;

	private ArrayList<Pattern> testingPatterns;

	private long seed;

	private double trainingPortion; // how much training patterns

	private double testingPortion; // how much testing patterns

	private double validationPortion; // how much validation patterns

	protected String[] targets; // labels for targets

	/** Default constructor. Try not to use this **/
	public TestPatterns() {
		super();
	}

	/** Constructor using random seed generation **/
	public TestPatterns(ArrayList<Pattern> patterns) {
		this(patterns, System.currentTimeMillis());
	}

	/** pre separated constructor. Training, validation & testing **/
	public TestPatterns(ArrayList<Pattern> trainingPatterns,
			ArrayList<Pattern> validationPatterns,
			ArrayList<Pattern> testingPatterns) {
		super();
		this.trainingPatterns = trainingPatterns;
		this.validationPatterns = validationPatterns;
		this.testingPatterns = testingPatterns;
	}

	/** full parameter constructor. Uses default portions **/
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

	/** full parameter constructor. Uses default portions **/
	public TestPatterns(Pattern[] patterns, long seed) {
		// must convert because Knuth Shuffle uses ArrayLists
		this(convertPatterns(patterns), seed);
	}

	/** WavePatterns constructor **/
	public TestPatterns(WavePatterns wp) {
		this(wp.patterns, System.currentTimeMillis());
		this.targets = wp.instruments;
	}

	/** WavePatterns constructor **/
	public TestPatterns(WavePatterns wp, long seed) {
		this(wp.patterns, seed);
		this.targets = wp.instruments;
	}

	/** Get over count of patterns **/
	public int getPatternCount() {
		return this.trainingPatterns.size() + this.validationPatterns.size()
		+ this.testingPatterns.size();
	}

	/** how many different kinds of output **/
	public int getPatternOutputCount(ArrayList<Pattern> patterns) {
		double[] vals = new double[patterns.size()];
		for (int i = 0; i < patterns.size(); ++i) {
			vals[i] = (double) patterns.get(i).getTargetNumber();
		}
		Double[][] counts = NNUtilities.getCount(vals);
		return counts.length;

	}

	/** Get all the pattern outputs **/
	public Double[][] getPatternOuts(ArrayList<Pattern> patterns) {
		double[] vals = new double[patterns.size()];
		for (int i = 0; i < patterns.size(); ++i) {
			vals[i] = (double) patterns.get(i).getTargetNumber();
		}
		Double[][] counts = NNUtilities.getCount(vals);
		return counts;

	}

	/** Get shuffle random seed **/
	public long getSeed() {
		return seed;
	}

	public String[] getTargets() {
		return targets;
	}

	/** Get test patterns **/
	public ArrayList<Pattern> getTestingPatterns() {
		return testingPatterns;
	}

	/** Return testing portion **/
	public double getTestingPortion() {
		return testingPortion;
	}

	/** Return training patterns **/
	public ArrayList<Pattern> getTrainingPatterns() {
		return trainingPatterns;
	}

	/** Return the training portion **/
	public double getTrainingPortion() {
		return trainingPortion;
	}

	/** Return validation patterns **/
	public ArrayList<Pattern> getValidationPatterns() {
		return validationPatterns;
	}

	/** Return validation portion **/
	public double getValidationPortion() {
		return validationPortion;
	}

	/** Do a set of training, validation and test patterns exist? **/
	public boolean hasSeparatedPatterns() {
		return (this.testingPatterns != null & this.validationPatterns != null & this.testingPatterns != null);
	}

	/**
	 * separates training pattern sets, testing pattern sets and validation
	 * pattern sets
	 **/
	public void separatePatterns(ArrayList<Pattern> patterns) {
		// shuffle
		patterns = NNUtilities.knuthShuffle(patterns, seed);
		int i = 0;
		for (Pattern p : patterns) {
			if (i < (trainingPortion * patterns.size())) {
				this.trainingPatterns.add(p); // training
			} else if (i < (trainingPortion + testingPortion) * patterns.size()) {
				this.validationPatterns.add(p); // validation
			} else {
				this.testingPatterns.add(p); // validation
			}
			i++;
		}
		// check to make sure validation has at least one of each
		if (getPatternOutputCount(this.testingPatterns) < getPatternOutputCount(patterns)) {
			separatePatterns(patterns);
		}
	}

	/** Set the training, validation and testing sets to a 3:1:1 ratio **/
	public void setDefaultPortions() {
		this.trainingPortion = 0.6;
		this.testingPortion = 0.2;
		this.validationPortion = 0.2;
	}

	public void setPatterns(ArrayList<Pattern> trainingPatterns,
			ArrayList<Pattern> validationPatterns,
			ArrayList<Pattern> testingPatterns) {
		this.trainingPatterns = trainingPatterns;
		this.validationPatterns = validationPatterns;
		this.testingPatterns = testingPatterns;
	}

	/** Set portions **/
	public void setPortions(double trainingPortion, double validationPortion,
			double testingPortion) {
		this.trainingPortion = trainingPortion;
		this.validationPortion = validationPortion;
		this.testingPortion = testingPortion;
	}

	/** Set shuffle random seed **/
	public void setSeed(long seed) {
		this.seed = seed;
	}

	public void setTargets(String[] targets) {
		this.targets = targets;
	}

	/** Set test patterns **/
	public void setTestingPatterns(ArrayList<Pattern> testingPatterns) {
		this.testingPatterns = testingPatterns;
	}

	/** Set the testing portion **/
	public void setTestingPortion(double testingPortion) {
		this.testingPortion = testingPortion;
	}

	/** Set training patterns **/
	public void setTrainingPatterns(ArrayList<Pattern> trainingPatterns) {
		this.trainingPatterns = trainingPatterns;
	}

	/** Set training portion **/
	public void setTrainingPortion(double trainingPortion) {
		this.trainingPortion = trainingPortion;
	}

	/** Set validation patterns **/
	public void setValidationPatterns(ArrayList<Pattern> validationPatterns) {
		this.validationPatterns = validationPatterns;
	}

	/** Set validation portion **/
	public void setValidationPortion(double validationPortion) {
		this.validationPortion = validationPortion;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (hasSeparatedPatterns()) {
			sb.append("\nTraining Patterns Count: "
					+ this.trainingPatterns.size() + "\n");
			sb.append("\nTesting Patterns Count: "
					+ this.validationPatterns.size() + "\t" + "\n");
			sb.append("\nValidation Patterns Count: "
					+ this.testingPatterns.size() + "\t" + "\n");
		} else {
			sb.append("Patterns not separated");
		}
		return sb.toString();
	}

	public String toStringVerbose() {
		StringBuffer sb = new StringBuffer();
		if (hasSeparatedPatterns()) {
			sb.append("\nTraining Patterns Count: "
					+ this.trainingPatterns.size()
					+ "\t"
					+ Arrays.deepToString(this
							.getPatternOuts(this.trainingPatterns)) + "\n");
			sb.append("\nTesting Patterns Count: "
					+ this.validationPatterns.size()
					+ "\t"
					+ Arrays.deepToString(this
							.getPatternOuts(this.validationPatterns)) + "\n");
			sb.append("\nValidation Patterns Count: "
					+ this.testingPatterns.size()
					+ "\t"
					+ Arrays.deepToString(this
							.getPatternOuts(this.testingPatterns)) + "\n");
		} else {
			sb.append("Patterns not separated");
		}
		return sb.toString();
	}

}
