package neuralNet;


import java.io.Serializable;
import java.util.ArrayList;


import exceptions.*;


public class MultiLayerNet  implements Serializable{

	private static final long serialVersionUID = -5601412683842445747L;
	private double acceptableErrorRate; //if we achieve this error rate stop
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
	private double matthewsCo;
	private long shuffleSeed;
	private boolean matthews; //whether to use the matthews coefficient for testing
	private CoefficientLogger errorBox; //where you put errors;
	

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
		this.neuronLayers = new LayerList(this.layerStructure, this.inputCount);
		if (debug) {
			System.out.println(this.neuronLayers.toString());
		}
	}

	public long initialiseRandomWeights() {
		return neuronLayers.setInitialWeights(testPatterns.getTrainingPatterns().get(0));
	}
	
	public long initialiseRandomWeights(long seed) {
		return neuronLayers.setInitialWeights(testPatterns.getTrainingPatterns().get(0), seed);
	}

	public  void runEpoch() throws Exception {
		ready();
		Epoch e = new Epoch(testPatterns.getTrainingPatterns(), testPatterns.getTestingPatterns(), 
								neuronLayers, trainingRate, verbose, debug);
		e.setShuffleSeed(shuffleSeed);
		//run epochs
		double er = 0;
		LayerList tally = null;
		double currentMaxMC = 0;
		for (int i = 0; i < maxEpoch; ++i) {
			if (this.shuffleTrainingPatterns) {
				e.shuffleTrainingPatterns();
				}
			if (verbose){System.out.println("\n******** EPOCH " + (i + 1) + " ********\n");}
			e.runEpoch();
			if (debug) {System.out.println(e.toString());}
			e.runValidationEpoch();
			er = calculateMatthews(this.testPatterns.getTestingPatterns(), e);
			this.errorBox.add(er);
			if(er > currentMaxMC) { //Should maybe be >
				System.out.println(er + "  " + currentMaxMC);
				System.out.println("Tally up: e: " + (i + 1));
				currentMaxMC = er;
				tally = new LayerList(this.neuronLayers);
				tally.setEpoch(i + 1);
			}
			this.matthewsCo = er;
		}
		if (verbose) {
			System.out.println("Best Result at Epoch: " + tally.getEpoch());
			}		
		this.neuronLayers = tally;
	}
	
	/** Kind of a home-brewed error rate **/
	public double calculateErrorRate(ArrayList<Pattern> patterns, Epoch e) {
		int size = patterns.size();
		double er = e.getConfusionMatrix().getErrorRate(size);
		if (verbose) {
			System.out.println("Error: " + er +" Total: " + 
								e.getConfusionMatrix().getTotal() + " of " + size);
		}
		return er;
	}
	
	/** get Matthews Coefficient **/
	public double calculateMatthews(ArrayList<Pattern> patterns, Epoch e) {
		double er = e.getConfusionMatrix().matthewsCoefficient();
		if (verbose) {
			System.out.println("Matthews: " + er);
		}
		return er;
	}

	
	
	public void runTestPatterns() throws Exception {
		ready();
		if (verbose) {System.out.println("\n****RUNNING TEST PATTERNS****\n"
				+ "Testing Network with test patterns\n");}
		Epoch e = new Epoch(null, testPatterns.getValidationPatterns(), neuronLayers, trainingRate, verbose, debug);
		e.runValidationEpoch();
		double er = 1;
		if (matthews) {
			er = calculateMatthews(this.testPatterns.getTestingPatterns(), e);
		} else {
			er = calculateErrorRate(this.testPatterns.getTestingPatterns(), e);
		}
		
		if ((matthews & er == 1) | (!matthews & er == 0)) { //perfect
			if (verbose) {System.out.println("Perfect Validation Score!");}
		}
		this.matthewsCo = er;
	}
	
	public String toString() {
		return this.layerStructure.toString() + "\n" + 
				this.neuronLayers.toString() + "\nMatthews Coefficient: " +
				this.matthewsCo;
	}
	
	public double getAcceptableErrorRate() {
		return acceptableErrorRate;
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
		if (debug) {verbose = true;}
	}
	
	public void setDefaultParameters() {
		maxEpoch = 1000;
		trainingRate = 0.1d;
		acceptableErrorRate = 0.1d;
		shuffleTrainingPatterns = false;
		this.verbose = false;
		this.debug = false;
		this.matthews = true;
		this.errorBox = new CoefficientLogger(maxEpoch);
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
	
	public boolean isMatthews() {
		return matthews;
	}

	public void setMatthews(boolean matthews) {
		this.matthews = matthews;
	}

	public  void setOutputCount(Integer outputCount) {
		this.outputCount = outputCount;
	}

	public long setShuffleTrainingPatterns(boolean shuffleTrainingPatterns) {
		long seed = System.currentTimeMillis();
		this.shuffleTrainingPatterns = shuffleTrainingPatterns;
		this.shuffleSeed = seed;
		return seed;
	}
	
	public void setShuffleTrainingPatterns(boolean shuffleTrainingPatterns, long seed) {
		this.shuffleTrainingPatterns = shuffleTrainingPatterns;
		this.shuffleSeed = seed;
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
	
	

}
