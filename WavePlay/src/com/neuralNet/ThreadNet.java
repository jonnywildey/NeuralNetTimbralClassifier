package com.neuralNet;

import java.io.File;
import java.util.concurrent.Callable;

import com.neuralNet.layers.LayerStructure;
import com.neuralNet.pattern.TestPatterns;
import com.util.Log;

/**Threadable MultiLayerNet **/
public class ThreadNet implements Callable<MultiLayerNet> {
	
	public int runCount;
	public TestPatterns testPatterns;
	public boolean verbose;
	public File name;
	public int id;
	
	public ThreadNet() {
		super();
	}

	public int maxEpoch;
	public int hiddenLayer;
	
	/** Create Thread Net. filename, runCount, testPatterns, hidden layer
	 * max epoch, id, verbose
	 */
	public ThreadNet(File name, int runCount, TestPatterns testPatterns,
			int hiddenLayer, int maxEpoch, int id, boolean verbose) {
		super();
		this.name = name;
		this.runCount = runCount;
		this.testPatterns = testPatterns;
		this.hiddenLayer = hiddenLayer;
		this.maxEpoch = maxEpoch;
		this.id = id;
		this.verbose = verbose;
	}
	
	@Override
	public MultiLayerNet call() throws Exception {
		MultiLayerNet nn = new MultiLayerNet();
		config(nn);
		try {
			nn.runEpoch();
			nn.runTestPatterns();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nn;
	}
	
	public boolean hasTestPatterns() {
		return (this.testPatterns != null);
	}
	
	/**Config settings for MLN **/
	public void config(MultiLayerNet nn) {
		LayerStructure ls = new LayerStructure(testPatterns);
		if (hiddenLayer != 0) {
			ls.addHiddenLayer(hiddenLayer);
		}		
		nn.setTrainingRate(0.1d);
		nn.setLayerStructure(ls);
		nn.setTestPatterns(testPatterns);
		nn.setDebug(false);
		nn.initialiseNeurons();
		nn.setVerbose(verbose);
		nn.setAcceptableErrorRate(0.1d);
		nn.setMaxEpoch(maxEpoch);
		nn.initialiseRandomWeights(System.currentTimeMillis());
		nn.setShuffleTrainingPatterns(true, System.currentTimeMillis());
	}

}
