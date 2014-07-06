package neuralNet;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import exceptions.*;


public class MultiLayerNet  implements Serializable{

	private static final long serialVersionUID = -5601412683842445747L;
	private double acceptableErrorRate; //if we achieve this error rate stop
	private Double bias; // bias weight. Normally around -0.5
	private boolean debug; //prints even more stuff when running
	private Integer inputCount; //number of input vectors
	private LayerStructure layerStructure; //constructor for the LayerList
	private Integer maxEpoch; // Maximum number of epochs to run before giving up
	private LayerList neuronLayers; //where all the neurons are
	private Integer outputCount; //number of outputs
	private boolean shuffleTrainingPatterns; //do you want to shuffle between epochs?
	private TestPatterns testPatterns;
	private Double trainingRate; // how much to push neurons by. Typically around 0.1
	private boolean verbose; //prints a lot of stuff when running
	
	public MultiLayerNet() {
		setDefaultParameters();
	}
	
	/** set up a neural network with default parameters and one hidden neuron per input vector **/
	public MultiLayerNet(TestPatterns testPatterns, LayerStructure ls) {
		this.setTestPatterns(testPatterns);
		this.setLayerStructure(ls);
		this.setDefaultParameters();
	}


	public void initialiseNeurons() {
		this.neuronLayers = new LayerList(this.layerStructure, bias, this.inputCount);
		if (debug) {
			System.out.println(this.neuronLayers.toString());
		}
	}

	public long initialiseRandomWeights() {
		return neuronLayers.setInitialWeights(testPatterns.getTrainingPatterns().get(0), bias);
	}
	
	public long initialiseRandomWeights(long seed) {
		return neuronLayers.setInitialWeights(testPatterns.getTrainingPatterns().get(0), bias, seed);
	}

	public  void runEpoch() throws Exception {
		ready();
		/* make epoch */
		Epoch e = new Epoch(testPatterns.getTrainingPatterns(), testPatterns.getTestingPatterns(), 
								neuronLayers, trainingRate, verbose, debug);
		for (int i = 0; i < maxEpoch; ++i) {
			if (this.shuffleTrainingPatterns) {e.shuffleTrainingPatterns();}
			if (verbose){System.out.println("\n******** EPOCH " + (i + 1) + " ********\n");}
			e.runEpoch();
			if (debug) {System.out.println(e.toString());}
			e.runValidationEpoch();
			double er = getErrorRate(this.testPatterns.getTestingPatterns(), e);
			if (er == 0) { //perfect
				if (verbose) {System.out.println("Perfect Test Score!");}
				break;
			}
		}
	}
	
	public double getErrorRate(ArrayList<Pattern> patterns, Epoch e) {
		int size = patterns.size();
		double er = e.getConfusionMatrix().getErrorRate(size);
		if (verbose) {
			System.out.println("Error: " + er +" Total: " + 
								e.getConfusionMatrix().getTotal() + " of " + size);
		}
		return er;
	}

	
	
	public void validate() throws Exception {
		ready();
		if (verbose) {System.out.println("\nValidating Network with validation patterns\n");}
		Epoch e = new Epoch(null, testPatterns.getValidationPatterns(), neuronLayers, trainingRate, verbose, debug);
		e.setDebug(debug); e.setVerbose(verbose);
		e.runValidationEpoch();
		double er = getErrorRate(this.testPatterns.getTestingPatterns(), e);
		if (er == 0) { //perfect
			if (verbose) {System.out.println("Perfect Validation Score!");}
		}
	}
	
	public String toString() {
		return this.layerStructure.toString() + this.neuronLayers.toString();
	}
	
	public double getAcceptableErrorRate() {
		return acceptableErrorRate;
	}

	public  Double getBias() {
		return bias;
	}

	public  Integer getInputCount() {
		return inputCount;
	}

	public LayerStructure getLayerStructure() {
		return layerStructure;
	}

	public  Integer getMaxEpoch() {
		return maxEpoch;
	}

	public  Integer getOutputCount() {
		return outputCount;
	}

	public  Double getTrainingRate() {
		return trainingRate;
	}

	public void setAcceptableErrorRate(double acceptableErrorRate) {
		this.acceptableErrorRate = acceptableErrorRate;
	}

	public  void setBias(Double bias) {
		this.bias = bias;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
		if (debug) {verbose = true;}
	}
	
	public void setDefaultParameters() {
		bias = -0.5d;
		maxEpoch = 1000;
		trainingRate = 0.1d;
		acceptableErrorRate = 0.1d;
		shuffleTrainingPatterns = false;
		this.verbose = false;
		this.debug = false;
	}
	
	public  void setInputCount(Integer inputCount) {
		this.inputCount = inputCount;
	}
	
	public void setLayerStructure(LayerStructure layerStructure) {
		this.layerStructure = layerStructure;
		this.outputCount = layerStructure.outputCount;
	}
	
	public  void setMaxEpoch(Integer maxEpoch) {
		this.maxEpoch = maxEpoch;
	}
	
	public  void setOutputCount(Integer outputCount) {
		this.outputCount = outputCount;
	}

	public void setShuffleTrainingPatterns(boolean shuffleTrainingPatterns) {
		this.shuffleTrainingPatterns = shuffleTrainingPatterns;
	}
	
	/** sets and initialises training patterns **/
	public  void setTestPatterns(TestPatterns testPatterns) {
		this.testPatterns = testPatterns;
		this.inputCount = this.testPatterns.getTrainingPatterns().get(0).getInputCount();
	}

	public void setTrainingRate(Double trainingRate) {
		this.trainingRate = trainingRate;
	}
	
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}
	
	public boolean isDebug() {
		return debug;
	}
	
	public boolean isShuffleTrainingPatterns() {
		return shuffleTrainingPatterns;
	}


	public boolean isVerbose() {
		return verbose;
	}

	/** checks whether NN is ready to begin running **/
	public void ready() throws NoNeuronsException, NoPatternsException,
			UnitialisedWeightsException {
		if (!this.areThereNeurons()) {
			throw new NoNeuronsException();
		}
		
		if (!this.areTherePatterns()) {
			throw new NoPatternsException();
		}
		
		if (!this.areWeightsInitialised()) {
			throw new UnitialisedWeightsException();
		}
	}
	
	public boolean areThereNeurons() {
		return this.neuronLayers.getFirstLayer().neuronCount > 0;
	}
	
	public boolean areTherePatterns() {
		return (testPatterns.getTrainingPatterns().size() > 0 & 
				testPatterns.getTestingPatterns().size() > 0);
	}

	public boolean areWeightsInitialised() {
		return (neuronLayers.getFirstLayer().neurons.get(0).weightList.size() > 0);
	}
	
	
	public  void runEpochWithErrorRate() {
		/* make epoch */
		for (int i = 0; i < maxEpoch; ++i) {
			if (verbose){System.out.println("\n******** EPOCH " + (i + 1) + " ********\n");}
			Epoch e = new Epoch(testPatterns.getTrainingPatterns(), 
								null, neuronLayers, trainingRate, verbose, debug);
			e.runEpoch();
			if (debug) {System.out.println(e.toString());}
			//check errors
			double errors = e.meanError;
			if (verbose) {System.out.println("mean error: " + errors);}
			
			if (errors <= acceptableErrorRate) {
				if (verbose) {
					System.out.println("Learnt!");
					System.out.println(e.toString());
					System.out.println("System has learnt after " + (i + 1) + " epochs");
				}
				break; 
			}	

		}
	}

}
