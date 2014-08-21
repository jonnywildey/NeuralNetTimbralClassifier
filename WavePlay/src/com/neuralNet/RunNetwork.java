package com.neuralNet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.matrix.ConfusionMatrix;
import com.neuralNet.layers.LayerStructure;
import com.neuralNet.pattern.Pattern;
import com.neuralNet.pattern.TestPatterns;
import com.neuralNet.pattern.WavePatterns;
import com.util.Log;
import com.util.Serialize;
import com.util.fileReading.CSVReader;
import com.util.fileReading.HTML;

/**Run time part of NN **/
public class RunNetwork {

		
	public static void main(String[] args) {
		double start = System.currentTimeMillis();
		Log.setFilePath(new File("/Users/Jonny/Documents/Timbre/Logs/RunNN.Log"));
		//Make Iris data
		boolean verbose = true;
		long seed = System.currentTimeMillis();
		//TestPatterns testPatterns = getTestPatterns("/Users/Jonny/Documents/Timbre/NN/iris.float.txt", verbose, seed);
		//TestPatterns testPatterns = getTestPatterns("/Users/Jonny/Documents/Timbre/NN/2BitXOR.txt", verbose, seed);
		//String serialPatterns = "/Users/Jonny/Documents/Timbre/WaveCombPatterns.ser";
		//TestPatterns testPatterns = getWavePatternsSerial(seed, serialPatterns);
		String serialPatterns = "/Users/Jonny/Documents/Timbre/WaveCombExtraBarkPatterns.ser";
		File outputSerial = new File("/Users/Jonny/Documents/Timbre/Serial/MultipleNeurons/10Multiple.ser");
		WavePatterns wavePatterns = getWavePatterns();
		TestPatterns testPatterns = new TestPatterns(wavePatterns.patterns, seed);
		//TestPatterns testPatterns = getWavePatternsSerial(System.currentTimeMillis(), serialPatterns,verbose);
		//wavePatterns = null;
		int runCount = 1;
		MultiLayerNet mn = config(new MultiLayerNet(), testPatterns, 400, verbose, 1256l, 65488l);
		try {
			mn.runEpoch();
			mn.runTestPatterns();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d("time spent: " + ((System.currentTimeMillis() - start) / 1000d) + " seconds");
	}

	protected static void testCommittee(TestPatterns testPatterns,
			Committee committee) {
		ConfusionMatrix cm = committee.testPatterns(testPatterns.getTrainingPatterns());
		cm.makeGraph();
		cm = committee.testPatterns(testPatterns.getValidationPatterns());
		cm.makeGraph();
		cm = committee.testPatterns(testPatterns.getTestingPatterns());
		cm.makeGraph();
	}

	protected static void runMultipleNeuron(TestPatterns testPatterns,
			int runCount, boolean verbose) {
		ManyNets mN1 = new ManyNets(null, runCount, 1, testPatterns, verbose);
		mN1.even = true;
		ManyNets mN2 = new ManyNets(null, runCount, 2, testPatterns, verbose);
		mN2.even = false;
		ManyNets[] mn = new ManyNets[]{mN1, mN2};
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
		//wavePatterns.reduceScale(2); //added
		if (verbose) {
			Log.d("Pattern size: " + wavePatterns.patterns.length);
		}
		TestPatterns testPatterns = new TestPatterns(wavePatterns.patterns, seed);
		return testPatterns;
	}
	
	public static WavePatterns getWavePatterns() {
		WavePatterns wp1 = (WavePatterns) Serialize.getFromSerial("/Users/Jonny/Documents/Timbre/WaveLOADSLOADS1.ser");
		Log.d("serialised");
		Log.d(wp1.patterns.length + " " + wp1.patterns[0].getInputCount());
		WavePatterns wp2 = (WavePatterns) Serialize.getFromSerial("/Users/Jonny/Documents/Timbre/WaveLOADSLOADS2.ser");
		Log.d("serialised");
		Log.d(wp2.patterns.length + " " + wp2.patterns[0].getInputCount());
		WavePatterns wp3 = (WavePatterns) Serialize.getFromSerial("/Users/Jonny/Documents/Timbre/WaveLOADSLOADS3.ser");
		Log.d("serialised");
		Log.d(wp3.patterns.length + " " + wp3.patterns[0].getInputCount());
		WavePatterns wp4 = (WavePatterns) Serialize.getFromSerial("/Users/Jonny/Documents/Timbre/WaveLOADSLOADS4.ser");
		Log.d("serialised");
		Log.d(wp4.patterns.length + " " + wp4.patterns[0].getInputCount());
		
		WavePatterns wp = WavePatterns.combinePatterns(wp1,wp2,wp3,wp4);
		return wp;
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
		nn.setMaxEpoch(100);
		
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
