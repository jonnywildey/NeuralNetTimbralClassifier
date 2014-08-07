package neuralNet;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import filemanager.CSVReader;
import filemanager.Log;
import filemanager.Serialize;

/**Run time part of NN **/
public class RunNetwork {

		
	public static void main(String[] args) {
		double start = System.currentTimeMillis();
		Log.setFilePath(new File("/Users/Jonny/Documents/Timbre/Logs/RunNN.Log"));
		//Make Iris data
		boolean verbose = false;
		long seed = System.currentTimeMillis();
		//TestPatterns testPatterns = getTestPatterns("/Users/Jonny/Documents/Timbre/NN/iris.float.txt", verbose, seed);
		//TestPatterns testPatterns = getTestPatterns("/Users/Jonny/Documents/Timbre/NN/2BitXOR.txt", verbose, seed);
		//String serialPatterns = "/Users/Jonny/Documents/Timbre/WaveCombPatterns.ser";
		//TestPatterns testPatterns = getWavePatternsSerial(seed, serialPatterns);
		String serialPatterns = "/Users/Jonny/Documents/Timbre/WaveComb40ExpPatterns.ser";
		File outputSerial = new File("/Users/Jonny/Documents/Timbre/Serial/Committee/CommO4R16N100E400.ser");
		TestPatterns testPatterns = getWavePatternsSerial(seed, serialPatterns, verbose);
		int runCount = 16;
		Committee committee = createCommittee(testPatterns, runCount, 100, 400, verbose);
		
		committee.testPatterns(testPatterns.getTrainingPatterns());
		committee.testPatterns(testPatterns.getTestingPatterns());
		committee.testPatterns(testPatterns.getValidationPatterns());
		Log.d("time spent: " + ((System.currentTimeMillis() - start) / 1000d) + " seconds");
		Serialize.serialize(committee, outputSerial);
		
	}
	
	/** MultiThreaded committee generation. generates a thread for every single run count, 
	 * so be careful with very large committees **/
	protected static Committee createCommittee(TestPatterns testPatterns, 
			int runCount, int hiddenLayer, int epoch,
			boolean verbose) {
		Committee committee = new Committee();
		ThreadNet[] threadNets = generateThreadNets(runCount, testPatterns, hiddenLayer, epoch, verbose);
		ExecutorService threadPool = Executors.newFixedThreadPool(runCount);
		Set<Future<MultiLayerNet>> futures = new HashSet<Future<MultiLayerNet>>(runCount);
		for (int i = 0; i < runCount; ++i) {
		    Future<MultiLayerNet> f = threadPool.submit(threadNets[i]);
		    // save the future if necessary in a collection or something
		    futures.add(f);
		}
		threadPool.shutdown();
		MultiLayerNet[] nets = new MultiLayerNet[threadNets.length];
		int i = 0;
		for (Future<MultiLayerNet> f : futures) {
			try {
				nets[i] = f.get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
		}
		
		committee.setNets(nets);
		return committee;
	}

	protected static ManyNets[] generateManyNets(TestPatterns testPatterns,
			int runCount, boolean verbose, File writePath) {
		ManyNets[] manyNets = new ManyNets[runCount];
		File name = null;
		for (int i = 0; i < runCount; ++i) {
			name = new File(writePath.getAbsolutePath() + i + ".csv");
			manyNets[i] = new ManyNets(name, runCount, i, testPatterns, verbose);
		}
		return manyNets;
	}
	
	protected static ThreadNet[] generateThreadNets(int runCount, TestPatterns testPatterns,
			int hiddenLayer, int epoch, boolean verbose) {
		ThreadNet[] manyNets = new ThreadNet[runCount];
		for (int i = 0; i < runCount; ++i) {
			manyNets[i] = new ThreadNet(null, runCount, testPatterns, 
					hiddenLayer, epoch, i, verbose);
		}
		return manyNets;
	}
	
	protected static Set<ManyNets> generateManyNetsSet(TestPatterns testPatterns,
			int runCount, boolean verbose, File writePath) {
		Set<ManyNets> manyNets = new HashSet<ManyNets>(runCount);
		File name = null;
		for (int i = 0; i < runCount; ++i) {
			name = new File(writePath.getAbsolutePath() + i + ".csv");
			manyNets.add( new ManyNets(name, runCount, i, testPatterns, verbose));
		}
		return manyNets;
	}
	
	
	

	protected static void runThreadedNets(TestPatterns test40Patterns,
			TestPatterns testPatterns, int runCount) {
		ManyNets evenNet = new ManyNets();
		evenNet.name = new File("/Users/Jonny/Documents/Timbre/Logs/comp20extraevens.csv");
		evenNet.even = true;
		evenNet.runCount = runCount;
		evenNet.testPatterns = testPatterns;
		evenNet.verbose = true;
		
		ManyNets oddNet = new ManyNets();
		oddNet.name = new File("/Users/Jonny/Documents/Timbre/Logs/comp20extraodds.csv");
		oddNet.runCount = runCount;
		oddNet.even = false;
		oddNet.testPatterns = testPatterns;
		oddNet.verbose = true;
		
		
		
		ManyNets even40Net = new ManyNets();
		even40Net.name = new File("/Users/Jonny/Documents/Timbre/Logs/comp40bigevens.csv");
		even40Net.runCount = runCount;
		even40Net.even = true;
		even40Net.testPatterns = test40Patterns;
		even40Net.verbose = true;
		
		ManyNets odd40Net = new ManyNets();
		odd40Net.name = new File("/Users/Jonny/Documents/Timbre/Logs/comp40bigodds.csv");
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
			String serialPatterns, boolean verbose) {
		WavePatterns wavePatterns = (WavePatterns) Serialize.getFromSerial(
				serialPatterns);
		wavePatterns.reduceScale(2); //added
		if (verbose) {
			Log.d("Pattern size: " + wavePatterns.patterns.length);
		}
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
		nn.setMaxEpoch(500);
		
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
