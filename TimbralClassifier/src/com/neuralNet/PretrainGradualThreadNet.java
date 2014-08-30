package com.neuralNet;

import java.io.File;
import java.util.ArrayList;

import com.neuralNet.pattern.TestPatterns;
import com.util.Log;

/**
 * PreTrain NN where gradually introduces a post training set into a pretraining
 * set *.
 * 
 * @author Jonny Wildey
 * @version 1.0
 */
public class PretrainGradualThreadNet extends PretrainThreadNet {

	/**
	 * Generate transition patterns.
	 * 
	 * @param pretrain
	 *            the pretrain
	 * @param posttrain
	 *            the posttrain
	 * @param count
	 *            the count
	 * @return the array list
	 */
	public static ArrayList<TestPatterns> generateTransitionPatterns(
			TestPatterns pretrain, TestPatterns posttrain, int count) {
		return TestPatterns.splitAndAddToInital(pretrain, posttrain, count);
	}
	public int transitionPhases; // how many steps to introduce the post set
	public int transitionEpochs; // how many epochs to fully introduce

	// post training into
	protected ArrayList<TestPatterns> transitionPatterns; // all the sets of
	// different
	// patterns

	/**
	 * Instantiates a new pretrain gradual thread net.
	 */
	public PretrainGradualThreadNet() {
		super();
		this.defaultTransistions();
	}

	/**
	 * Instantiates a new pretrain gradual thread net.
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
	public PretrainGradualThreadNet(File name, TestPatterns testPatterns,
			int hiddenLayer, int maxEpoch, int id, boolean verbose) {
		super(name, testPatterns, hiddenLayer, maxEpoch, id, verbose);
		this.defaultTransistions();
	}

	/**
	 * Create Thread Net. filename, pretrainPatterns, posttrain patterns, hidden
	 * layer max epoch, postepoch, id, verbose. transitions are default. Combine
	 * must be true (for obvious reasons)
	 * 
	 * @param name
	 *            the name
	 * @param preTrainPatterns
	 *            the pre train patterns
	 * @param postTrainPatterns
	 *            the post train patterns
	 * @param hiddenLayer
	 *            the hidden layer
	 * @param maxEpoch
	 *            the max epoch
	 * @param postEpoch
	 *            the post epoch
	 * @param id
	 *            the id
	 * @param verbose
	 *            the verbose
	 */
	public PretrainGradualThreadNet(File name, TestPatterns preTrainPatterns,
			TestPatterns postTrainPatterns, int hiddenLayer, int maxEpoch,
			int postEpoch, int id, boolean verbose) {
		super(name, preTrainPatterns, postTrainPatterns, hiddenLayer, maxEpoch,
				postEpoch, id, false, verbose);
		this.defaultTransistions();
	}

	/**
	 * Create Thread Net. filename, pretrainPatterns, posttrain patterns, hidden
	 * layer max epoch, postepoch, transition epochs. transition phases, id,
	 * verbose.
	 * 
	 * @param name
	 *            the name
	 * @param preTrainPatterns
	 *            the pre train patterns
	 * @param postTrainPatterns
	 *            the post train patterns
	 * @param hiddenLayer
	 *            the hidden layer
	 * @param maxEpoch
	 *            the max epoch
	 * @param postEpoch
	 *            the post epoch
	 * @param transitionEpochs
	 *            the transition epochs
	 * @param transitionPhases
	 *            the transition phases
	 * @param id
	 *            the id
	 * @param verbose
	 *            the verbose
	 */
	public PretrainGradualThreadNet(File name, TestPatterns preTrainPatterns,
			TestPatterns postTrainPatterns, int hiddenLayer, int maxEpoch,
			int postEpoch, int transitionEpochs, int transitionPhases, int id,
			boolean verbose) {
		super(name, preTrainPatterns, postTrainPatterns, hiddenLayer, maxEpoch,
				postEpoch, id, true, verbose);
		this.transitionEpochs = transitionEpochs;
		this.transitionPhases = transitionPhases;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.neuralNet.PretrainThreadNet#call()
	 */
	@Override
	public MultiLayerNet call() throws Exception {
		MultiLayerNet nn = new MultiLayerNet();
		config(nn);
		try {
			Log.d("Generating transitions");
			this.generateTransitionPatterns();
			if (verbose) {
				Log.d("Pretraining patterns");
			}
			nn.runEpoch();
			nn.runTestPatterns();
			if (verbose) {
				Log.d("adding post training patterns");
			}
			int epochs = this.transitionEpochs / this.transitionPhases;

			Log.d("Epochs are " + epochs);
			for (int i = 0; i < this.transitionPhases; ++i) {
				Log.d("Pattern count "
						+ this.transitionPatterns.get(i).getPatternCount());
				nn.setMaxEpoch(epochs);
				nn.setTestPatterns(this.transitionPatterns.get(i));
				nn.runEpoch();
				Log.d("Transition " + i + " complete");
			}
			Log.d("All transitions complete");
			postConfig(nn);
			nn.runEpoch();
			nn.runTestPatterns();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return nn;
	}

	/**
	 * Default transistions.
	 */
	public void defaultTransistions() {
		this.transitionPhases = 10;
		this.transitionEpochs = 200;
	}

	/**
	 * Create transition patterns based on config. This could take a while for
	 * large sets patterns.
	 */
	public void generateTransitionPatterns() {
		this.transitionPatterns = generateTransitionPatterns(this.testPatterns,
				this.postTrainPatterns, this.transitionPhases);
	}

	/**
	 * Gets the transition epochs.
	 * 
	 * @return the transition epochs
	 */
	public int getTransitionEpochs() {
		return transitionEpochs;
	}

	/**
	 * Gets the transition phases.
	 * 
	 * @return the transition phases
	 */
	public int getTransitionPhases() {
		return transitionPhases;
	}

	/**
	 * Sets the transition epochs.
	 * 
	 * @param transitionEpochs
	 *            the new transition epochs
	 */
	public void setTransitionEpochs(int transitionEpochs) {
		this.transitionEpochs = transitionEpochs;
	}

	/**
	 * Sets the transition phases.
	 * 
	 * @param transitionPhases
	 *            the new transition phases
	 */
	public void setTransitionPhases(int transitionPhases) {
		this.transitionPhases = transitionPhases;
	}

}
