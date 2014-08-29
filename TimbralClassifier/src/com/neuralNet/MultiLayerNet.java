package com.neuralNet;

import java.io.Serializable;
import java.util.ArrayList;

import com.DSP.waveAnalysis.Statistics;
import com.exceptions.*;
import com.neuralNet.layers.LayerList;
import com.neuralNet.layers.LayerStructure;
import com.neuralNet.matrix.ConfusionMatrix;
import com.neuralNet.pattern.Pattern;
import com.neuralNet.pattern.TestPatterns;
import com.neuralNet.pattern.WavePattern;
import com.util.Log;

/**
 * Multiple layer feedforward neural network Set testPatterns, modify parameters
 * (such as adding layers) and then run epoch
 **/
public class MultiLayerNet implements Serializable {

	private static final long serialVersionUID = -5601412683842445747L;
	private double acceptableErrorRate; // if we achieve this error rate stop
	private boolean debug; // prints even more stuff when running
	private Integer inputCount; // number of input vectors
	private LayerStructure layerStructure; // constructor for the LayerList
	private Integer maxEpoch; // Maximum number of epochs to run before giving
								// up
	private LayerList neuronLayers; // where all the neurons are
	private Integer outputCount; // number of outputs
	private boolean shuffleTrainingPatterns; // do you want to shuffle between
												// epochs?
	private TestPatterns testPatterns;
	private Double trainingRate; // how much to push neurons by. Typically
									// around 0.1
	private boolean verbose; // prints a lot of stuff when running
	private double matthewsCo;
	private long shuffleSeed;
	private boolean matthews; // whether to use the matthews coefficient for
								// testing
	private CoefficientLogger errorBox; // where you put errors;

	/** Default constructor **/
	public MultiLayerNet() {
		setDefaultParameters();
	}

	/**
	 * set up a neural network with default parameters and one hidden neuron per
	 * input vector
	 **/
	public MultiLayerNet(TestPatterns testPatterns, LayerStructure ls) {
		this.setTestPatterns(testPatterns);
		this.setLayerStructure(ls);
		this.setDefaultParameters();
	}

	/** Set up neuron layers **/
	public void initialiseNeurons() {
		this.neuronLayers = new LayerList(this.layerStructure, this.inputCount);

		debug(this.neuronLayers.toString());

	}

	/**
	 * Set random weights. Requires testPatterns to be set to determine input
	 * size
	 * 
	 * @return
	 */
	public long initialiseRandomWeights() {
		return neuronLayers.setInitialWeights(testPatterns
				.getTrainingPatterns().get(0));
	}

	/**
	 * Set random weights with seed. Requires testPatterns to be set to
	 * determine input size
	 * 
	 * @return
	 */
	public long initialiseRandomWeights(long seed) {
		return neuronLayers.setInitialWeights(testPatterns
				.getTrainingPatterns().get(0), seed);
	}

	/**
	 * Runs a single pattern returning the output. Does not know whether correct
	 **/
	public int runSinglePatternUnsupervised(Pattern p) {
		Epoch e = new Epoch(null, null, neuronLayers, trainingRate, null,
				verbose, debug);
		e.runPattern(p);
		return e.getMaxId();
	}

	/** Runs patterns returning the output. Does not know whether correct **/
	public int[] runPatternsUnsupervised(Pattern[] patterns) {
		Epoch e = new Epoch(null, null, neuronLayers, trainingRate, null,
				verbose, debug);
		return e.runPatternsGetMax(patterns);
	}

	/** Run an epoch using set training patterns and configuration **/
	public void runEpoch() throws Exception {
		ready();
		Epoch e = new Epoch(testPatterns.getTrainingPatterns(),
				testPatterns.getTestingPatterns(), neuronLayers, trainingRate,
				this.testPatterns.getTargets(), verbose, debug);
		e.setShuffleSeed(shuffleSeed);
		// run epochs
		double er = 0;
		LayerList tally = null;
		double currentMaxMC = 0;
		for (int i = 0; i < maxEpoch; ++i) {
			if (this.shuffleTrainingPatterns) {
				e.shuffleTrainingPatterns();
			}
			log("\n******** EPOCH " + (i + 1) + " Hidden Neurons: "
					+ this.layerStructure.getLayerCount(0) + " inputs: "
					+ this.inputCount + "********\n");
			e.runEpoch();
			debug(e.toString());
			e.runValidationEpoch();
			er = calculateMatthews(this.testPatterns.getTestingPatterns(), e);
			this.errorBox.add(er);
			if (er > currentMaxMC) { // Should maybe be >
				log("Previous highest validation MC: "
						+ Statistics.round(currentMaxMC, 4));
				log("Increase in MC!");
				currentMaxMC = er;
				tally = new LayerList(this.neuronLayers);
				tally.setEpoch(i + 1);
			}
			this.matthewsCo = er;
		}
		log("Best Result at Epoch: " + tally.getEpoch() + " with MC: "
				+ Statistics.round(currentMaxMC, 4));
		this.neuronLayers = tally;
	}

	/** Calculate simple error rate **/
	public double calculateErrorRate(ArrayList<Pattern> patterns, Epoch e) {
		int size = patterns.size();
		double er = e.getConfusionMatrix().getErrorRate(size);
		log("Error: " + Statistics.round(er, 4) + " Total: "
				+ e.getConfusionMatrix().getTotal() + " of " + size);
		return er;
	}

	/** get Matthews Coefficient **/
	public double calculateMatthews(ArrayList<Pattern> patterns, Epoch e) {
		double er = e.getConfusionMatrix().matthewsCoefficient();
		log("Average MC: " + Statistics.round(er, 4));
		return er;
	}

	/** Run network against test patterns **/
	public ConfusionMatrix runTestPatterns() throws Exception {
		ready();
		log("\n****RUNNING TEST PATTERNS****\n"
				+ "Testing Network with test patterns\n");
		Epoch e = new Epoch(null, testPatterns.getValidationPatterns(),
				neuronLayers, trainingRate, this.testPatterns.getTargets(),
				verbose, debug);
		ConfusionMatrix cm = e.runValidationEpoch();
		double er = 1;
		if (matthews) {
			er = calculateMatthews(this.testPatterns.getTestingPatterns(), e);
		} else {
			er = calculateErrorRate(this.testPatterns.getTestingPatterns(), e);
		}
		if ((matthews & er == 1) | (!matthews & er == 0)) { // perfect
			log("Perfect Validation Score!");
		}
		this.matthewsCo = er;
		return cm;
	}

	/** Receive all the incorrect patterns for a given set of patterns **/
	public ArrayList<WavePattern> getProblemPatterns(ArrayList<Pattern> patterns) {
		log("\n****RUNNING TEST PATTERNS****\n"
				+ "Testing Network with test patterns\n");
		Epoch e = new Epoch(null, patterns, neuronLayers, trainingRate, null,
				verbose, debug);
		return e.getProblemPatterns();
	}

	@Override
	public String toString() {
		return this.layerStructure.toString() + "\n"
				+ this.neuronLayers.toString() + "\nMatthews Coefficient: "
				+ this.matthewsCo;
	}

	/** If using error rate thresholds, return the threshold **/
	public double getAcceptableErrorRate() {
		return acceptableErrorRate;
	}

	/** Return input count **/
	public Integer getInputCount() {
		return inputCount;
	}

	/** Return layer structure **/
	public LayerStructure getLayerStructure() {
		return layerStructure;
	}

	/** Return the max epoch **/
	public Integer getMaxEpoch() {
		return maxEpoch;
	}

	/** Return the output count **/
	public Integer getOutputCount() {
		return outputCount;
	}

	public Double getTrainingRate() {
		return trainingRate;
	}

	public LayerList getNeuronLayers() {
		return neuronLayers;
	}

	public TestPatterns getTestPatterns() {
		return testPatterns;
	}

	public double getErrorRate() {
		return matthewsCo;
	}

	public void setAcceptableErrorRate(double acceptableErrorRate) {
		this.acceptableErrorRate = acceptableErrorRate;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
		if (debug) {
			verbose = true;
		}
	}

	/** Default parameters for a network **/
	public void setDefaultParameters() {
		maxEpoch = 100;
		trainingRate = 0.1d;
		acceptableErrorRate = 0.1d;
		shuffleTrainingPatterns = false;
		this.verbose = false;
		this.debug = false;
		this.matthews = true;
		this.errorBox = new CoefficientLogger(maxEpoch);
	}

	/**
	 * Set input count. This is typically done by the setting of testPatterns or
	 * layer structure to the network
	 * 
	 * @param inputCount
	 */
	public void setInputCount(Integer inputCount) {
		this.inputCount = inputCount;
	}

	/** sets the layer structure **/
	public void setLayerStructure(LayerStructure layerStructure) {
		this.layerStructure = layerStructure;
		this.outputCount = layerStructure.outputCount;
	}

	public void setMaxEpoch(Integer maxEpoch) {
		this.maxEpoch = maxEpoch;
		this.errorBox = new CoefficientLogger(maxEpoch);
	}

	public boolean isMatthews() {
		return matthews;
	}

	public void setMatthews(boolean matthews) {
		this.matthews = matthews;
	}

	public void setOutputCount(Integer outputCount) {
		this.outputCount = outputCount;
	}

	/** Should training patterns be shuffled between epochs? **/
	public long setShuffleTrainingPatterns(boolean shuffleTrainingPatterns) {
		long seed = System.currentTimeMillis();
		this.shuffleTrainingPatterns = shuffleTrainingPatterns;
		this.shuffleSeed = seed;
		return seed;
	}

	/** Should training patterns be shuffled between epochs? **/
	public void setShuffleTrainingPatterns(boolean shuffleTrainingPatterns,
			long seed) {
		this.shuffleTrainingPatterns = shuffleTrainingPatterns;
		this.shuffleSeed = seed;
	}

	/** sets and initialises training patterns **/
	public void setTestPatterns(TestPatterns testPatterns) {
		this.testPatterns = testPatterns;
		this.inputCount = this.testPatterns.getTrainingPatterns().get(0)
				.getInputCount();
	}

	public void setTrainingRate(Double trainingRate) {
		this.trainingRate = trainingRate;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public long getShuffleSeed() {
		return shuffleSeed;
	}

	public CoefficientLogger getErrorBox() {
		return errorBox;
	}

	public void setShuffleSeed(long shuffleSeed) {
		this.shuffleSeed = shuffleSeed;
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

	/** Have neurons been initialised? **/
	public boolean areThereNeurons() {
		return this.neuronLayers.getFirstLayer().neuronCount > 0;
	}

	/** Does the network have any patterns? **/
	public boolean areTherePatterns() {
		return (testPatterns.getTrainingPatterns().size() > 0 & testPatterns
				.getTestingPatterns().size() > 0);
	}

	/** Have neuron weights been initialised? **/
	public boolean areWeightsInitialised() {
		return (neuronLayers.getFirstLayer().neurons.get(0).getWeightList()
				.size() > 0);
	}

	/** Removes test patterns **/
	public void removeTestPatterns() {
		this.testPatterns = null;
	}

	/** Logs according to verbose **/
	protected void log(String str) {
		if (this.verbose) {
			Log.d(str);
		}
	}

	/** Logs according to debug **/
	protected void debug(String str) {
		if (this.debug) {
			Log.d(str);
		}
	}

}
