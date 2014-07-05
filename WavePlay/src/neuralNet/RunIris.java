package neuralNet;

import java.util.ArrayList;
import java.util.Arrays;

import filemanager.CSVReader;

public class RunIris {

	public RunIris() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
		//generate random seed
		long seed = System.currentTimeMillis();
		
		//check shuffle
		int n = 100;
		ArrayList<Integer> ints = new ArrayList<Integer>(100);
		for (int i = 0; i < n; ++i) {
			ints.add(i + 1);
		}
		ints = NNUtilities.knuthShuffle(ints);
		System.out.println(ints.toString());
		
		
		//Make Iris data
		boolean verbose = true;
		String file = "/Users/Jonny/Documents/Timbre/NN/iris.float.txt";
		CSVReader sr = new CSVReader(file);
		sr.readFile();
		double[][] arr = sr.makeDoubleArray();
		arr = NNUtilities.removeNulls(arr, verbose); //remove null rows
		//System.out.println(Arrays.deepToString(arr));
		NNUtilities.createTargetConversionTable(arr, verbose); //see how bit array relates to original
		//separate pattern into training, testing and validation into 3 1 1 ratio
		ArrayList<Pattern> patterns = NNUtilities.createPatterns(arr, verbose);
		TestPatterns testPatterns = new TestPatterns(patterns, seed);
		System.out.println("TP" + testPatterns.toString());
		IrisData id = new IrisData(); //Make a net;
		id.setHiddenCount(10); //hidden layer neurons
		id.setTrainingPatterns(testPatterns.getTrainingPatterns()); //add training patterns
		id.setTestingPatterns(testPatterns.getTestingPatterns()); //and testing
		id.setOutputCount(3);
		id.initialiseNeurons();
		id.setDebug(false);
		id.setVerbose(verbose);
		id.setShuffleTrainingPatterns(true);
		id.setAcceptableErrorRate(0.1d);
		id.setMaxEpoch(1000);
		id.checkSigmoid();	
		id.runEpoch();
		//run validation
		id.setTestingPatterns(testPatterns.getValidationPatterns());
		id.validate();
		
		

			
			
			
		
	}

}
