package com.neuralNet;

import java.io.File;
import java.util.ArrayList;

import com.neuralNet.pattern.TestPatterns;

/** PreTrain NN where 
 * gradually introduces a post training set into a pretraining set **/
public class PretrainGradualThreadNet extends PretrainThreadNet{
	
	public int transitionPhases; // how many steps to introduce the post set
	public int transitionEpochs; // how many epochs to fully introduce 
								 // post training into
	protected ArrayList<TestPatterns> transitionPatterns; //all the sets of different patterns

	public PretrainGradualThreadNet() {
		super();
		this.defaultTransistions();
	}


	public PretrainGradualThreadNet(File name, int runCount,
			TestPatterns testPatterns, int hiddenLayer, int maxEpoch, int id,
			boolean verbose) {
		super(name, runCount, testPatterns, hiddenLayer, maxEpoch, id, verbose);
		this.defaultTransistions();
	}

	/** Create Thread Net. filename, runCount, pretrainPatterns, 
	 * posttrain patterns, hidden layer
	 * max epoch, postepoch, id, verbose. transitions are default. 
	 * Combine must be true (for obvious reasons)
	 */
	public PretrainGradualThreadNet(File name, int runCount,
			TestPatterns preTrainPatterns, TestPatterns postTrainPatterns,
			int hiddenLayer, int maxEpoch, int postEpoch, int id,
			 boolean verbose) {
		super(name, runCount, preTrainPatterns, postTrainPatterns, hiddenLayer,
				maxEpoch, postEpoch, id, true, verbose);
		this.defaultTransistions();
	}
	
	/** Create Thread Net. filename, runCount, pretrainPatterns, 
	 * posttrain patterns, hidden layer
	 * max epoch, postepoch, transition epochs. transition phases, id, verbose.
	 */
	public PretrainGradualThreadNet(File name, int runCount,
			TestPatterns preTrainPatterns, TestPatterns postTrainPatterns,
			int hiddenLayer, int maxEpoch, int postEpoch, int transitionEpochs, int transitionPhases, int id,
			 boolean verbose) {
		super(name, runCount, preTrainPatterns, postTrainPatterns, hiddenLayer,
				maxEpoch, postEpoch, id, true, verbose);
		this.transitionEpochs = transitionEpochs;
		this.transitionPhases = transitionPhases;
	}
	
	public static ArrayList<TestPatterns> generateTransitionPatterns(
			TestPatterns pretrain, TestPatterns posttrain, int count) {
		return TestPatterns.splitAndAddToInital(pretrain, posttrain, count);
	}
	
	/** Create transition patterns based on config. This 
	 * could take a while for large sets patterns.
	 */
	public void generateTransitionPatterns() {
		this.transitionPatterns = generateTransitionPatterns(
				this.testPatterns,this.postTrainPatterns, 
				this.transitionPhases);
	}


	public int getTransitionPhases() {
		return transitionPhases;
	}


	public void setTransitionPhases(int transitionPhases) {
		this.transitionPhases = transitionPhases;
	}


	public int getTransitionEpochs() {
		return transitionEpochs;
	}


	public void setTransitionEpochs(int transitionEpochs) {
		this.transitionEpochs = transitionEpochs;
	}


	public void defaultTransistions() {
		this.transitionPhases = 10;
		this.transitionEpochs = 100;
	}

}
