package com.neuralNet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.List;

import com.neuralNet.layers.LayerStructure;
import com.neuralNet.pattern.Combine;
import com.neuralNet.pattern.Pattern;
import com.neuralNet.pattern.TestPatterns;
import com.neuralNet.pattern.WavePatterns;
import com.util.Log;
import com.util.Serialize;
import com.util.fileReading.CSVReader;
import com.util.fileReading.HTML;

/**
 * Run time part of NN *.
 *
 * @author Jonny Wildey
 * @version 1.0
 */
public class RunNetwork {

	public static void main(String[] args) {
		double start = System.currentTimeMillis();
		Log.setFilePath(new File("/Users/Jonny/Documents/Timbre/Logs/RunNN.Log"));
		//Make Iris data
		boolean verbose = true;
		long seed = 4564564536l;
		File pretrain = new File("/Users/Jonny/Documents/Timbre/JSON/WavePatterns/Poly/Combined.json");
		File posttrain = new File("/Users/Jonny/Documents/Timbre/JSON/WavePatterns/Poly/Separate");
		File out = new File("/Users/Jonny/Documents/Timbre/Ser/Committee/Poly/SplitBatch.ser");
		File in = new File("/Users/Jonny/Documents/Timbre/JSON/WavePatterns/Poly/SplitComb.json");
		WavePatterns lp = Serialize.getFromJSON(in, WavePatterns.class);
		//WavePatterns wp = Combine.combineFromJSONs(posttrain);
		TestPatterns pre = new TestPatterns(lp.patterns, seed);
		//TestPatterns post = new TestPatterns(wp.patterns, seed);

		Committee committee = MultiNNUtilities.createCommittee(pre, 4, 100, 200, verbose);
		committee.removePatterns();
		//test pre
		MultiNNUtilities.testCommittee(pre, committee);
		Serialize.serialize(committee, out);
		
		Log.d("time spent: " + ((System.currentTimeMillis() - start) / 1000d) + " seconds");
	}

	/**
	 * Get Wave Patterns from a serialised file *.
	 *
	 * @param seed the seed
	 * @param serialPatterns the serial patterns
	 * @param verbose the verbose
	 * @return the wave patterns serial
	 */
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
	
	/**
	 * Get Wave Patterns from a serialised file *.
	 *
	 * @param seed the seed
	 * @param json the json
	 * @param verbose the verbose
	 * @return the wave patterns json
	 */
	public static TestPatterns getWavePatternsJSON(long seed,
			File json, boolean verbose) {
		WavePatterns wavePatterns = Serialize.getFromJSON(json, WavePatterns.class);
		//wavePatterns.reduceScale(2); //added
		if (verbose) {
			Log.d("Pattern size: " + wavePatterns.patterns.length);
		}
		TestPatterns testPatterns = new TestPatterns(wavePatterns.patterns, seed);
		return testPatterns;
	}
	
	/**
	 * Config settings for MLN *.
	 *
	 * @param nn the nn
	 * @param testPatterns the test patterns
	 * @param neuronCount the neuron count
	 * @param verbose the verbose
	 * @param seed2 the seed2
	 * @param seed3 the seed3
	 * @return the multi layer net
	 */
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
	
}
