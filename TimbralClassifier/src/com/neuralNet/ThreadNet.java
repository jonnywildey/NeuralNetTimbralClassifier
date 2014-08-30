package com.neuralNet;

import java.io.File;
import java.util.concurrent.Callable;

import com.neuralNet.layers.LayerStructure;
import com.neuralNet.pattern.TestPatterns;

/**
 * Threadable MultiLayerNet *.
 * 
 * @author Jonny Wildey
 * @version 1.0
 */
public class ThreadNet implements Callable<MultiLayerNet> {

	/**
	 * Makes a graph of your nets' Matthews Coefficient *.
	 * 
	 * @param nets
	 *            the nets
	 */
	public static void graphNets(MultiLayerNet[] nets) {
		CoefficientLogger[] mBox = CoefficientLogger
		.getErrorsFromMultiLayer(nets);
		CoefficientLogger.makeGraph(mBox);
	}
	/**
	 * Makes a graph of your nets' Matthews Coefficient *.
	 * 
	 * @param nets
	 *            the nets
	 */
	public static void graphNets(MultiLayerNet[][] nets) {
		CoefficientLogger[][] mBox = CoefficientLogger
		.getErrorsFromMultiLayer(nets);
		for (int i = 0; i < mBox.length; ++i) {
			CoefficientLogger.makeGraph(mBox[i]);
		}

	}
	/**
	 * Pick best net.
	 * 
	 * @param nns
	 *            the nns
	 * @return the multi layer net
	 */
	public static MultiLayerNet pickBestNet(MultiLayerNet[] nns) {
		MultiLayerNet nn = null;
		double er = 0;
		for (MultiLayerNet mln : nns) {
			double val = Math.abs(mln.getErrorRate()); // +
			// mln.getValidationErrorRate());
			if (val > er) {
				er = mln.getErrorRate(); // + mln.getValidationErrorRate();
				nn = mln;
			}
			if (er == 0) {
				break;
			}
		}
		return nn;
	}
	public TestPatterns testPatterns;
	public boolean verbose;
	public File name;

	public int id;

	public int maxEpoch;

	public int hiddenLayer;

	/**
	 * Instantiates a new thread net.
	 */
	public ThreadNet() {
		super();
	}

	/**
	 * Create Thread Net. filename, testPatterns, hidden layer max epoch, id,
	 * verbose
	 * 
	 * @param name
	 *            the name
	 * @param testPatterns
	 *            the test patterns
	 * @param hiddenLayer
	 *            the hidden layer
	 * @param maxEpoch
	 *            the max epoch
	 * @param id
	 *            the id
	 * @param verbose
	 *            the verbose
	 */
	public ThreadNet(File name, TestPatterns testPatterns, int hiddenLayer,
			int maxEpoch, int id, boolean verbose) {
		super();
		this.name = name;
		this.testPatterns = testPatterns;
		this.hiddenLayer = hiddenLayer;
		this.maxEpoch = maxEpoch;
		this.id = id;
		this.verbose = verbose;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.Callable#call()
	 */
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

	/**
	 * Config settings for MLN *.
	 * 
	 * @param nn
	 *            the nn
	 */
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

	/**
	 * Checks for test patterns.
	 * 
	 * @return true, if successful
	 */
	public boolean hasTestPatterns() {
		return (this.testPatterns != null);
	}

}
