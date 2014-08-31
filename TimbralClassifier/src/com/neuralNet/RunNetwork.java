package com.neuralNet;

import java.io.File;
import com.neuralNet.layers.LayerStructure;
import com.neuralNet.pattern.Combine;
import com.neuralNet.pattern.TestPatterns;
import com.neuralNet.pattern.WavePatterns;
import com.util.Log;
import com.util.Serialize;



/**
 * Run time part of NN *.
 * 
 * @author Jonny Wildey
 * @version 1.0
 */
public class RunNetwork {
	
	public static void main(String[] args) {
		double start = System.currentTimeMillis();
		Log.setFilePath(new File(
		"../assets/log/RunNN.Log"));
		// Make Iris data
		boolean verbose = true;
		long seed = 4564564536l;
		File pretrain = new File("/Users/Jonny/Documents/Timbre/JSON/WavePatterns/Poly/Separate");
		File sc4 = new File("/Users/Jonny/Documents/Timbre/JSON/WavePatterns/Poly/SplitCombFour");
		File posttrain = new File(
		"/Users/Jonny/Documents/Timbre/JSON/WavePatterns/Poly/Split");
		File split150com = new File(
		"/Users/Jonny/Documents/Timbre/Ser/Committee/Poly/SplitBatch150.ser");
		File split100com = new File(
		"/Users/Jonny/Documents/Timbre/Ser/Committee/Poly/SplitBatch.ser");
		File splitCombFour = new File(
				"/Users/Jonny/Documents/Timbre/Ser/Committee/Poly/SplitCombFour.ser");
		File combinedVanilla = new File(
				"/Users/Jonny/Documents/Timbre/Ser/Committee/Poly/CombinedVanilla.ser");
		File combinedPat = new File("/Users/Jonny/Documents/Timbre/JSON/WavePatterns/Poly/Combined.json");
		File combinedLarge = new File("/Users/Jonny/Documents/Timbre/Ser/Committee/Poly/CombinedMega.ser");

		WavePatterns lp = Combine.combineFromJSONs(pretrain);
		//WavePatterns lp = Serialize.getFromJSON(splitCombFour, WavePatterns.class);
		TestPatterns pre = new TestPatterns(lp, 12356l);
		Log.d(pre.getPatternCount());
		Committee committee = MultiNNUtilities.createCommittee(pre, 4, 100, 30, verbose);
		//Committee committee = Serialize.getFromSerial(split100com, Committee.class);
		//committee.trainEpochs(pre, 60);
		MultiNNUtilities.testCommittee(pre, committee);
		CoefficientLogger.makeGraph(committee.getNets());
		committee.removePatterns();
		
		//MultiNNUtilities.testCommittee(pre, committee);
			 
		// MultiNNUtilities.testCommittee(pre, com);
		// MultiLayerNet[] nets = com.getNets();
		// MultiNNUtilities.testPatternConfusionMatrix(pre, nets);
		Serialize.serialize(committee, combinedLarge);

		Log.d("time spent: " + ((System.currentTimeMillis() - start) / 1000d)
				+ " seconds");
	}

	/**
	 * Config settings for MLN *.
	 * 
	 * @param nn
	 *            the nn
	 * @param testPatterns
	 *            the test patterns
	 * @param neuronCount
	 *            the neuron count
	 * @param verbose
	 *            the verbose
	 * @param seed2
	 *            the seed2
	 * @param seed3
	 *            the seed3
	 * @return the multi layer net
	 */
	public static MultiLayerNet config(MultiLayerNet nn,
			TestPatterns testPatterns, Integer neuronCount, boolean verbose,
			long seed2, long seed3) {
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

	/**
	 * Get Wave Patterns from a serialised file *.
	 * 
	 * @param seed
	 *            the seed
	 * @param json
	 *            the json
	 * @param verbose
	 *            the verbose
	 * @return the wave patterns json
	 */
	public static TestPatterns getWavePatternsJSON(long seed, File json,
			boolean verbose) {
		WavePatterns wavePatterns = Serialize.getFromJSON(json,
				WavePatterns.class);
		// wavePatterns.reduceScale(2); //added
		if (verbose) {
			Log.d("Pattern size: " + wavePatterns.patterns.length);
		}
		TestPatterns testPatterns = new TestPatterns(wavePatterns.patterns,
				seed);
		return testPatterns;
	}

	/**
	 * Get Wave Patterns from a serialised file *.
	 * 
	 * @param seed
	 *            the seed
	 * @param serialPatterns
	 *            the serial patterns
	 * @param verbose
	 *            the verbose
	 * @return the wave patterns serial
	 */
	public static TestPatterns getWavePatternsSerial(long seed,
			String serialPatterns, boolean verbose) {
		WavePatterns wavePatterns = (WavePatterns) Serialize
		.getFromSerial(serialPatterns);
		// wavePatterns.reduceScale(2); //added
		if (verbose) {
			Log.d("Pattern size: " + wavePatterns.patterns.length);
		}
		TestPatterns testPatterns = new TestPatterns(wavePatterns.patterns,
				seed);
		return testPatterns;
	}



}
