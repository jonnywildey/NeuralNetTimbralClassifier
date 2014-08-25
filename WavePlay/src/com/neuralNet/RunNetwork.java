package com.neuralNet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.List;

import com.DSP.waveProcess.filters.MultiLayerNet;
import com.neuralNet.layers.LayerStructure;
import com.neuralNet.pattern.Combine;
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
		long seed = 4564564536l;
		File pretrain = new File("/Users/Jonny/Documents/Timbre/JSON/WavePatterns/Poly/Combined.json");
		File posttrain = new File("/Users/Jonny/Documents/Timbre/JSON/WavePatterns/Poly/Separate");
		File com = new File("/Users/Jonny/Documents/Timbre/JSON/Committee/PretrainGradual.json");
		WavePatterns lp = Serialize.getFromGson(pretrain, WavePatterns.class);
		WavePatterns wp = Combine.combineFromJSONs(posttrain);
		TestPatterns pre = new TestPatterns(lp.patterns, seed);
		TestPatterns post = new TestPatterns(wp.patterns, seed);
		MultiLayerNet[] mns = MultiNNUtilities.runPreTrainGradual(pre, post, 4, verbose);
		Committee committee = new Committee();
		committee.removePatterns();
		//test pre
		MultiNNUtilities.testCommittee(pre, committee);
		Serialize.serializeGson(committee, com);
		
		Log.d("time spent: " + ((System.currentTimeMillis() - start) / 1000d) + " seconds");
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
	
	/**Get Wave Patterns from a serialised file **/
	public static TestPatterns getWavePatternsJSON(long seed,
			File json, boolean verbose) {
		WavePatterns wavePatterns = Serialize.getFromGson(json, WavePatterns.class);
		//wavePatterns.reduceScale(2); //added
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
		nn.setMaxEpoch(100);
		nn.initialiseRandomWeights(seed2);
		nn.setShuffleTrainingPatterns(true, seed3);
		return nn;
	}
	
}
