package neuralNet;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import filemanager.CSVReader;
import filemanager.Log;
import filemanager.Serialize;

/**Run time part of NN **/
public class RunNetwork {

		
	public static void main(String[] args) {
		Log.setFilePath(new File("/Users/Jonny/Documents/Timbre/Logs/RunNN.Log"));
		//Make Iris data
		boolean verbose = true;
		long seed = System.currentTimeMillis();
		//TestPatterns testPatterns = getTestPatterns("/Users/Jonny/Documents/Timbre/NN/iris.float.txt", verbose, seed);
		//TestPatterns testPatterns = getTestPatterns("/Users/Jonny/Documents/Timbre/NN/2BitXOR.txt", verbose, seed);
		String serialPatterns = "/Users/Jonny/Documents/Timbre/WaveCombPatterns.ser";
		TestPatterns testPatterns = getWavePatternsSerial(seed, serialPatterns);
		int runCount = 1;
		
		ManyNets evenNet = new ManyNets();
		evenNet.name = new File("/Users/Jonny/Documents/Timbre/Logs/comp20evens.csv");
		evenNet.even = true;
		evenNet.runCount = runCount;
		evenNet.testPatterns = testPatterns;
		evenNet.verbose = true;
		
		ManyNets oddNet = new ManyNets();
		oddNet.name = new File("/Users/Jonny/Documents/Timbre/Logs/comp20odds.csv");
		oddNet.runCount = runCount;
		oddNet.even = false;
		oddNet.testPatterns = testPatterns;
		oddNet.verbose = true;
		
		serialPatterns = "/Users/Jonny/Documents/Timbre/WaveCombExtraBarkPatterns.ser";
		TestPatterns test40Patterns = getWavePatternsSerial(seed, serialPatterns);
		
		ManyNets even40Net = new ManyNets();
		even40Net.name = new File("/Users/Jonny/Documents/Timbre/Logs/comp40evens.csv");
		even40Net.runCount = runCount;
		even40Net.even = true;
		even40Net.testPatterns = test40Patterns;
		even40Net.verbose = true;
		
		ManyNets odd40Net = new ManyNets();
		odd40Net.name = new File("/Users/Jonny/Documents/Timbre/Logs/comp40odds.csv");
		odd40Net.runCount = runCount;
		odd40Net.even = false;
		odd40Net.testPatterns = test40Patterns;
		odd40Net.verbose = true;
		
		ManyNets[] mn = new ManyNets[]{evenNet, oddNet, even40Net, odd40Net};

		
		ExecutorService threadPool = Executors.newFixedThreadPool(4);
		// define your jobs somehow
		for (ManyNets job : mn) {
		    // under the covers this creates a FutureTask instance
		    Future future = threadPool.submit(job);
		    // save the future if necessary in a collection or something
		}
		// once we have submitted all jobs to the thread pool, it should be shutdown
		threadPool.shutdown();
		
		
	}

	/**Get Wave Patterns from a serialised file **/
	public static TestPatterns getWavePatternsSerial(long seed,
			String serialPatterns) {
		WavePatterns wavePatterns = (WavePatterns) Serialize.getFromSerial(
				serialPatterns);
		wavePatterns.reduceScale(2); //added
		TestPatterns testPatterns = new TestPatterns(wavePatterns.patterns, seed);
		return testPatterns;
	}
	
	/**Config settings for MLN **/
	public static MultiLayerNet config(MultiLayerNet nn, TestPatterns testPatterns, 
			Integer neuronCount, boolean verbose, long seed2, long seed3) {
		LayerStructure ls = new LayerStructure(testPatterns);
		if (neuronCount != null) {
			ls.addHiddenLayer(neuronCount);
		}
		
		nn.setTrainingRate(0.1d);
		nn.setLayerStructure(ls);
		nn.setTestPatterns(testPatterns);
		nn.setDebug(false);
		nn.initialiseNeurons();
		nn.setVerbose(verbose);
		nn.setAcceptableErrorRate(0.1d);
		if (neuronCount > 100) {
			nn.setMaxEpoch(100);
		} else {
			nn.setMaxEpoch(200);
		}
		
		nn.initialiseRandomWeights(seed2);
		nn.setShuffleTrainingPatterns(true, seed3);
		return nn;
	}
	
	
	/** Get test Patterns from a file **/
	public static TestPatterns getTestPatterns(String file, boolean verbose, long seed) {
		CSVReader sr = new CSVReader(file);
		sr.readFile();
		double[][] arr = sr.makeDoubleArray();
		arr = NNUtilities.removeNulls(arr, verbose); //remove null rows
		//System.out.println(Arrays.deepToString(arr));
		NNUtilities.createTargetConversionTable(arr, verbose); //see how bit array relates to original
		//separate pattern into training, testing and validation into 3 1 1 ratio
		ArrayList<Pattern> patterns = NNUtilities.createPatterns(arr, false);
		TestPatterns testPatterns = new TestPatterns(patterns, seed);
		System.out.println("TP" + testPatterns.toString());
		return testPatterns;
	}
	
	
	
	
	

}
