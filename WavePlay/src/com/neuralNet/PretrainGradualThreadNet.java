package com.neuralNet;

import java.io.File;
import java.util.ArrayList;

import com.DSP.waveProcess.filters.MultiLayerNet;
import com.neuralNet.pattern.TestPatterns;
import com.util.Log;

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
	
	
	public PretrainGradualThreadNet(File name,
			TestPatterns testPatterns, int hiddenLayer, int maxEpoch, int id,
			boolean verbose) {
		super(name,  testPatterns, hiddenLayer, maxEpoch, id, verbose);
		this.defaultTransistions();
	}

	/** Create Thread Net. filename,  pretrainPatterns, 
	 * posttrain patterns, hidden layer
	 * max epoch, postepoch, id, verbose. transitions are default. 
	 * Combine must be true (for obvious reasons)
	 */
	public PretrainGradualThreadNet(File name,
			TestPatterns preTrainPatterns, TestPatterns postTrainPatterns,
			int hiddenLayer, int maxEpoch, int postEpoch, int id,
			 boolean verbose) {
		super(name, preTrainPatterns, postTrainPatterns, hiddenLayer,
				maxEpoch, postEpoch, id, false, verbose);
		this.defaultTransistions();
	}
	
	/** Create Thread Net. filename, pretrainPatterns, 
	 * posttrain patterns, hidden layer
	 * max epoch, postepoch, transition epochs. transition phases, id, verbose.
	 */
	public PretrainGradualThreadNet(File name,
			TestPatterns preTrainPatterns, TestPatterns postTrainPatterns,
			int hiddenLayer, int maxEpoch, int postEpoch, int transitionEpochs, int transitionPhases, int id,
			 boolean verbose) {
		super(name, preTrainPatterns, postTrainPatterns, hiddenLayer,
				maxEpoch, postEpoch, id, true, verbose);
		this.transitionEpochs = transitionEpochs;
		this.transitionPhases = transitionPhases;
	}
	
	
	
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
				Log.d("Pattern count " + this.transitionPatterns.get(i).getPatternCount());
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
		this.transitionEpochs = 200;
	}

}
