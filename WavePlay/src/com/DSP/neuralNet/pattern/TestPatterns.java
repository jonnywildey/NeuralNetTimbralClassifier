package neuralNet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import com.util.Log;

/** A group of patterns. Useful methods for separation into training, validation
 * and testing sets
 * @author Jonny Wildey
 *
 */
public class TestPatterns implements Serializable {
	

	private static final long serialVersionUID = -4219496637757583180L;
	private ArrayList<Pattern> trainingPatterns;
	private ArrayList<Pattern> validationPatterns;
	private ArrayList<Pattern> testingPatterns;
	private long seed;
	private double trainingPortion; //how much training patterns
	private double testingPortion; // how much testing patterns
	private double validationPortion; //how much validation patterns
	
	
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
		//must convert because Knuth Shuffle uses ArrayLists
		this(convertPatterns(patterns), seed);
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

	/** Default constructor. Try not to use this **/
	public TestPatterns() {
		this(null);
	}
	
	/** Convert array of patterns to arraylist of patterns **/
	public static ArrayList<Pattern> convertPatterns(Pattern[] wavePatterns) {
		ArrayList<Pattern> nwp = new ArrayList<Pattern>(wavePatterns.length);
		for (Pattern wp : wavePatterns) {
			nwp.add(wp);
		}
		return nwp;
	}
	
	/** Convert arraylist of patterns to array **/
	public static Pattern[] convertPatterns(ArrayList<Pattern> patterns) {
		Pattern[] pat = new Pattern[patterns.size()];
		for (int i = 0; i < pat.length; ++i) {
			pat[i] = patterns.get(i);
		}
		return pat;
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
	
	/** Return the training portion **/
	public double getTrainingPortion() {
		return trainingPortion;
	}
	
	/** Set training portion **/
	public void setTrainingPortion(double trainingPortion) {
		this.trainingPortion = trainingPortion;
	}
	
	/**Set portions **/
	public void setPortions(double trainingPortion, double validationPortion, 
			double testingPortion) {
		this.trainingPortion = trainingPortion;
		this.validationPortion = validationPortion;
		this.testingPortion = testingPortion;
	}
	
	/** Return testing portion **/
	public double getTestingPortion() {
		return testingPortion;
	}

	/** Set the testing portion **/
	public void setTestingPortion(double testingPortion) {
		this.testingPortion = testingPortion;
	}
	
	/** Return validation portion **/
	public double getValidationPortion() {
		return validationPortion;
	}
	
	/** Set validation portion **/
	public void setValidationPortion(double validationPortion) {
		this.validationPortion = validationPortion;
	}

	/** Return training patterns **/
	public ArrayList<Pattern> getTrainingPatterns() {
		return trainingPatterns;
	}
	
	/** Set training patterns **/
	public void setTrainingPatterns(ArrayList<Pattern> trainingPatterns) {
		this.trainingPatterns = trainingPatterns;
	}
	
	/** Get shuffle random seed **/
	public long getSeed() {
		return seed;
	}
	
	/** Set shuffle random seed **/
	public void setSeed(long seed) {
		this.seed = seed;
	}
	
	/** Set the training, validation and testing sets to a 3:1:1 ratio **/
	public void setDefaultPortions() {
		this.trainingPortion = 0.6;
		this.testingPortion = 0.2;
		this.validationPortion = 0.2;
	}
	
	/**Return validation patterns**/
	public ArrayList<Pattern> getValidationPatterns() {
		return validationPatterns;
	}
	
	/**Set validation patterns**/
	public void setValidationPatterns(ArrayList<Pattern> validationPatterns) {
		this.validationPatterns = validationPatterns;
	}
	/**Get test patterns**/
	public ArrayList<Pattern> getTestingPatterns() {
		return testingPatterns;
	}
	/**Set test patterns **/
	public void setTestingPatterns(ArrayList<Pattern> testingPatterns) {
		this.testingPatterns = testingPatterns;
	}
	
	/** Do a set of training, validation and test patterns exist? **/
	public boolean hasSeparatedPatterns() {
		return (this.testingPatterns != null & 
				this.validationPatterns != null & 
				this.testingPatterns != null);
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (hasSeparatedPatterns()) {
		sb.append("\nTraining Patterns Count: " + this.trainingPatterns.size() + "\n");
		sb.append("\nTesting Patterns Count: " + this.validationPatterns.size() + "\t" + "\n");
		sb.append("\nValidation Patterns Count: " + this.testingPatterns.size() + "\t" +"\n");
		} else {
			sb.append("Patterns not separated");
		}
		return sb.toString();
	}
	
	public String toStringVerbose() {
		StringBuffer sb = new StringBuffer();
		if (hasSeparatedPatterns()) {
		sb.append("\nTraining Patterns Count: " + this.trainingPatterns.size() + "\t" +
				Arrays.deepToString(this.getPatternOuts(this.trainingPatterns)) + "\n");
		sb.append("\nTesting Patterns Count: " + this.validationPatterns.size() + "\t" +
				Arrays.deepToString(this.getPatternOuts(this.validationPatterns)) + "\n");
		sb.append("\nValidation Patterns Count: " + this.testingPatterns.size() + "\t" +
				Arrays.deepToString(this.getPatternOuts(this.testingPatterns)) + "\n");
		} else {
			sb.append("Patterns not separated");
		}
		return sb.toString();
	}
	
	public void setPatterns(ArrayList<Pattern> trainingPatterns,
			ArrayList<Pattern> validationPatterns, ArrayList<Pattern> testingPatterns) {
		this.trainingPatterns = trainingPatterns;
		this.validationPatterns = validationPatterns;
		this.testingPatterns = testingPatterns;
	}
	
	/** Split a group of test patterns into equal groups **/
	public static ArrayList<TestPatterns> split(TestPatterns testPatterns, int count) {
		ArrayList<ArrayList<Pattern>> trainingPatterns = split(testPatterns.getTrainingPatterns(), count);
		ArrayList<ArrayList<Pattern>> validationPatterns = split(testPatterns.getValidationPatterns(), count);
		ArrayList<ArrayList<Pattern>> testingPatterns = split(testPatterns.getTestingPatterns(), count);
		ArrayList<TestPatterns> np = new ArrayList<TestPatterns>(count);
		Log.d("size: " + np.size());
		//set to new test Patterns
		for (int i = 0; i < count; ++i) {
			np.set(i, new TestPatterns(
					trainingPatterns.get(i), 
					validationPatterns.get(i),
					testingPatterns.get(i)));
		}
		return np;
	}
	
	/** Split a group of test patterns into equal groups. each group contains all
	 * of the previous group and more. i.e the last group will be the original testPatterns **/
	@SuppressWarnings("unchecked")
	public static ArrayList<TestPatterns> splitAndAdd(TestPatterns testPatterns, int count) {
		ArrayList<ArrayList<Pattern>> trainingPatterns = split(testPatterns.getTrainingPatterns(), count);
		ArrayList<ArrayList<Pattern>> validationPatterns = split(testPatterns.getValidationPatterns(), count);
		ArrayList<ArrayList<Pattern>> testingPatterns = split(testPatterns.getTestingPatterns(), count);
		//set to new test Patterns
		ArrayList<Pattern> trP = new ArrayList<Pattern>(0);
		ArrayList<Pattern> vaP = new ArrayList<Pattern>(0);
		ArrayList<Pattern> teP = new ArrayList<Pattern>(0);
		ArrayList<TestPatterns> np = addToTestPatternsArrayList(count,
				trainingPatterns, validationPatterns, testingPatterns, trP,
				vaP, teP);
		return np;
	}
	
	/** Split a group of test patterns into equal groups and add them to initialTestPatterns. 
	 * each group contains all
	 * of the previous group and more. i.e the last group will be the original testPatterns **/
	@SuppressWarnings("unchecked")
	public static ArrayList<TestPatterns> splitAndAddToInital(TestPatterns initial, TestPatterns additional, int count) {
		ArrayList<ArrayList<Pattern>> trainingPatterns = split(additional.getTrainingPatterns(), count);
		ArrayList<ArrayList<Pattern>> validationPatterns = split(additional.getValidationPatterns(), count);
		ArrayList<ArrayList<Pattern>> testingPatterns = split(additional.getTestingPatterns(), count);
		//set to new test Patterns
		ArrayList<Pattern> trP = (ArrayList<Pattern>) initial.getTrainingPatterns().clone();
		ArrayList<Pattern> vaP = (ArrayList<Pattern>) initial.getValidationPatterns().clone();
		ArrayList<Pattern> teP = (ArrayList<Pattern>) initial.getTestingPatterns().clone();
		ArrayList<TestPatterns> np = addToTestPatternsArrayList(count,
				trainingPatterns, validationPatterns, testingPatterns, trP,
				vaP, teP);
		return np;
	}

	@SuppressWarnings("unchecked")
	protected static ArrayList<TestPatterns> addToTestPatternsArrayList(
			int count, ArrayList<ArrayList<Pattern>> trainingPatterns,
			ArrayList<ArrayList<Pattern>> validationPatterns,
			ArrayList<ArrayList<Pattern>> testingPatterns,
			ArrayList<Pattern> trP, ArrayList<Pattern> vaP,
			ArrayList<Pattern> teP) {
		ArrayList<TestPatterns> np = new ArrayList<TestPatterns>(count);
		Log.d("size: " + np.size());
		for (int i = 0; i < count; ++i) {
			trP = (ArrayList<Pattern>) trP.clone();
			trP.addAll(trainingPatterns.get(i));
			vaP = (ArrayList<Pattern>) vaP.clone();
			vaP.addAll(validationPatterns.get(i));
			teP = (ArrayList<Pattern>) teP.clone();
			teP.addAll(testingPatterns.get(i));
			np.set(i, new TestPatterns(trP,vaP,teP));
		}
		return np;
	}
	
	/**Split up an arraylist by the count. **/
	protected static <T> ArrayList<ArrayList<T>> split(ArrayList<T> patterns, int count) {
		ArrayList<ArrayList<T>> np = new ArrayList<ArrayList<T>>(count);
		//determine lengths
		int div = patterns.size() / count;
		int mod = patterns.size() % count;
		for (int i = 0; i < count; ++i) {
			if (mod != 0) {
				np.set(i,  new ArrayList<T>(div + 1));
				mod--;
			} else {
				np.set(i,  new ArrayList<T>(div));
			}
		}
		//write values
		int c = 0;
		for (int i = 0; i < np.size(); ++i) {
			for (int j = 0; j < np.get(i).size(); ++j) {
				np.get(i).set(j, patterns.get(c));
				c++;
			}
		}
		return np;
	}
	

	

}
