package neuralNet;

import java.util.ArrayList;
import java.util.Arrays;

import filemanager.CSVReader;


public class IrisData  {

	private Integer inputCount; //number of input vectors
	private Integer outputCount; //number of outputs
	private Integer hiddenCount; //number of hidden neurons
	private boolean shuffleTrainingPatterns; 
	public ArrayList<Pattern> getTestingPatterns() {
		return testingPatterns;
	}

	public void setTestingPatterns(ArrayList<Pattern> testingPatterns) {
		this.testingPatterns = testingPatterns;
	}

	private ArrayList<Pattern> trainingPatterns;
	private ArrayList<Pattern> testingPatterns;
	private ArrayList<NeuronShell> hiddenNeuronList;
	private ArrayList<NeuronShell> lastNeuronList; 
	private Double bias; // bias weight. Normally around -0.5
	private Integer maxEpoch; // Maximum number of epochs to run before giving up
	private Double trainingRate; // how much to push neurons by. Typically around 0.1
	private boolean verbose; //prints a lot of stuff when running
	private boolean debug; //prints even more stuff when running
	private double acceptableErrorRate; //if we achieve this error rate stop
	
	public IrisData() {
		setDefaultParameters();
	}
	
	/** set up a neural network with default parameters and one hidden neuron per input vector **/
	public IrisData(ArrayList<Pattern> trainingPatterns) {
		this.setTrainingPatterns(trainingPatterns);
		this.setDefaultParameters();
		this.hiddenCount = trainingPatterns.get(0).getInputCount(); 
		this.initialiseNeurons();
	}
	
	
	public void setDefaultParameters() {
		bias = -0.5d;
		maxEpoch = 20000;
		trainingRate = 0.1d;
		acceptableErrorRate = 0.1d;
		shuffleTrainingPatterns = false;
		this.verbose = false;
		this.debug = false;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
		if (debug) {verbose = true;}
	}

	public double getAcceptableErrorRate() {
		return acceptableErrorRate;
	}

	public void setAcceptableErrorRate(double acceptableErrorRate) {
		this.acceptableErrorRate = acceptableErrorRate;
	}

	public  Integer getInputCount() {
		return inputCount;
	}

	public  void setInputCount(Integer inputCount) {
		this.inputCount = inputCount;
	}

	public  Integer getOutputCount() {
		return outputCount;
	}

	public  void setOutputCount(Integer outputCount) {
		this.outputCount = outputCount;
	}

	public  Integer getHiddenCount() {
		return hiddenCount;
	}

	public  void setHiddenCount(Integer hiddenCount) {
		this.hiddenCount = hiddenCount;
	}


	public  ArrayList<Pattern> getTrainingPatterns() {
		return trainingPatterns;
	}
	
	/** sets and initialises training patterns **/
	public  void setTrainingPatterns(ArrayList<Pattern> trainingPatterns) {
		this.trainingPatterns = trainingPatterns;
		this.inputCount = this.trainingPatterns.get(0).getInputCount();
	}


	public  Double getBias() {
		return bias;
	}

	public  void setBias(Double bias) {
		this.bias = bias;
	}

	public  Integer getMaxEpoch() {
		return maxEpoch;
	}

	public  void setMaxEpoch(Integer maxEpoch) {
		this.maxEpoch = maxEpoch;
	}

	public  Double getTrainingRate() {
		return trainingRate;
	}

	public void setTrainingRate(Double trainingRate) {
		this.trainingRate = trainingRate;
	}

	public void setShuffleTrainingPatterns(boolean shuffleTrainingPatterns) {
		this.shuffleTrainingPatterns = shuffleTrainingPatterns;
	}

	public  void runEpochWithErrorRate() {
		/* make epoch */
		for (int i = 0; i < maxEpoch; ++i) {
			if (verbose){System.out.println("\n******** EPOCH " + (i + 1) + " ********\n");}
			Epoch e = new Epoch(trainingPatterns, null, hiddenNeuronList, lastNeuronList, trainingRate);
			e.setDebug(debug);
			e.setVerbose(verbose);
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
	
	public  void runEpoch() {
		/* make epoch */
		Epoch e = new Epoch(trainingPatterns, testingPatterns, hiddenNeuronList, lastNeuronList, trainingRate);
		e.setDebug(debug);
		e.setVerbose(verbose);
		for (int i = 0; i < maxEpoch; ++i) {
			if (this.shuffleTrainingPatterns) {
				e.shuffleTrainingPatterns();
			}
			if (verbose){System.out.println("\n******** EPOCH " + (i + 1) + " ********\n");}
			e.runEpoch();
			if (debug) {System.out.println(e.toString());}
			//run validation
			e.runValidationEpoch();
			if (verbose) {
				System.out.println(e.getConfusionMatrix().getErrorRate(this.testingPatterns.size()));
			}
			if (e.getConfusionMatrix().getErrorRate(this.testingPatterns.size()) == 1) { //perfect
				if (verbose) {System.out.println("Perfect Test Score!");}
				break;
			}
		}
	}
	
	public void validate() {
		/* make epoch */
		if (verbose) {
			System.out.println("\nValidating Network with validation patterns\n");
		}
		Epoch e = new Epoch(trainingPatterns, testingPatterns, hiddenNeuronList, lastNeuronList, trainingRate);
		e.setDebug(debug); e.setVerbose(verbose);
		e.runValidationEpoch();
		if (verbose){
			System.out.println("Correct Pattern Rate: " + e.getConfusionMatrix().getErrorRate(this.testingPatterns.size()));
		}
		if (e.getConfusionMatrix().getErrorRate(this.testingPatterns.size()) == 1) { //perfect
			if (verbose) {System.out.println("Perfect Validation Score!");}
		}
	}
	
	/** debug function that checks the sigMoid function works correctly **/
	public  void checkSigmoid() {
		/* check sigmoid */
		System.out.println("checking sigmoid function");
		for (int i = -4;i < 4; ++i) {
			System.out.println(NNFunctions.sigmoid((double)i / 2) + " gradient: " + 
					NNFunctions.sigmoidDerivative((double)i / 2));
		}
	}

	public void initialiseNeurons() {
		/* make neurons */
		//Hidden
		this.hiddenNeuronList = new ArrayList<NeuronShell>();
		this.lastNeuronList = new ArrayList<NeuronShell>();
		for (int i = 0; i < hiddenCount; ++i) {
			hiddenNeuronList.add(new NeuronShell(new Neuron(bias, i, inputCount, 1)));
		}
		if (debug) {System.out.println("Hidden Layer Neurons: " + hiddenNeuronList.toString());}
		//Last
		for (int i = 0; i < outputCount; ++i) {
			lastNeuronList.add(new NeuronShell(new Neuron(bias, i, hiddenCount, 2)));
		}
		if (debug) {System.out.println("Last Layer Neurons: " + lastNeuronList.toString());}
	}

}
