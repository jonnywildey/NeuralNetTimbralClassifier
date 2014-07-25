package neuralNet;

import java.util.ArrayList;
import java.util.Random;

import com.matrix.ConfusionMatrix;

import filemanager.Log;

public class Epoch {
	protected ArrayList<Pattern> trainingPatterns;
	protected ArrayList<Pattern> testingPatterns;
	protected LayerList neurons;
	protected ArrayList<Double> errorAmount;
	protected ArrayList<Double> errorList;
	protected ArrayList<Double>  rmsError;
	protected Double trainingRate;
	protected ConfusionMatrix testConfusionMatrix;
	public ConfusionMatrix trainingConfusionMatrix;
	protected boolean debug;
	protected boolean verbose;
	protected Random shuffleRandom;
	private boolean addMaxOutputNeuron;
	public Double meanError;
	
	public Epoch() {
		this.trainingPatterns = new ArrayList<Pattern>();
		this.testingPatterns = new ArrayList<Pattern>();
		this.errorAmount = new ArrayList<Double>();
		this.errorList = new ArrayList<Double>();
		this.rmsError = new ArrayList<Double>();
		this.shuffleRandom = new Random();
	}
	
	
	public Epoch(ArrayList<Pattern> trainingPatterns,
			ArrayList<Pattern> testingPatterns, LayerList neurons,
			Double trainingRate, boolean verbose, boolean debug) {
		this();
		this.neurons = neurons;
		this.trainingPatterns = trainingPatterns;
		this.testingPatterns = testingPatterns;
		this.trainingRate = trainingRate;
		this.verbose = verbose;
		this.debug = debug;
		this.addMaxOutputNeuron = true;
		
		//set up lists. only useful for error based epochs
		for (int i = 0; i < neurons.getOutputCount(); ++i) { //is only last correct?
			this.errorAmount.add(0d);
			this.errorList.add(0d);
			this.rmsError.add(0d);
		}
	}
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}
	
	public void setShuffleSeed(long seed) {
		shuffleRandom.setSeed(seed);
	}

	public ConfusionMatrix getConfusionMatrix() {
		return testConfusionMatrix;
	}

	public void shuffleTrainingPatterns() {
		Pattern holder = null;//ugh
		int j = 0;
		for (int i = trainingPatterns.size() - 1; i > 0; --i) {
			j = (int) Math.rint(shuffleRandom.nextDouble() * i); //random number
			holder = (trainingPatterns.get(j));
			trainingPatterns.set(j, trainingPatterns.get(i));
			trainingPatterns.set(i, holder);
		}
		
	}
	

	public void addTrainingPattern(Pattern trainingPattern) {
		trainingPatterns.add(trainingPattern);
	}
	
	public void setTrainingRate(Double trainingRate) {
		this.trainingRate = trainingRate;
	}
	
	public ConfusionMatrix runValidationEpoch() {
		testConfusionMatrix = new ConfusionMatrix(neurons.getOutputCount());
		neurons.setLearning(false);
		for (Pattern p: this.testingPatterns) {
			//input pattern inputs
			//forward pass
			for (int i = 0; i < neurons.getLayerCount(); ++i) {
				NeuralLayer l = neurons.getLayer(i);
				l.setTrainingRate(trainingRate);
				l.process(p);
			}
			if (addMaxOutputNeuron) {
				addMax(p, testConfusionMatrix);
			} else {
				addRounded(p, testConfusionMatrix);
			}
		}
		if (verbose) {
			Log.d(testConfusionMatrix.toString());
		}
		return testConfusionMatrix;	
	}
	
	/** add neuron outputs > 0.5 **/
	public void addRounded(Pattern p, ConfusionMatrix cm) {
		for (Neuron n : neurons.getLastLayer().neurons) {
			int roundedOut = (int)Math.rint(n.output);
			if (roundedOut == 1) {
				cm.addToCell(n.id, p.getTargetNumber()); 
				//Neuron Row, Class Column
			}
		}
	}
	
	public void addMax(Pattern p, ConfusionMatrix cm) {
		double max = 0;
		int nid = 0;
		for (Neuron n : neurons.getLastLayer().neurons) {
			if (n.output > max) {
					max = n.output;
					nid = n.id;
			}
		}
		cm.addToCell(nid, p.getTargetNumber());
		//Neuron Row, Class Column
	}
	
	
	
	
	
	public void runEpoch() {
		trainingConfusionMatrix = new ConfusionMatrix(neurons.getOutputCount());
		Double patternErrors = 0d;
		//Load Pattern
		neurons.setLearning(true);
		for (Pattern p: this.trainingPatterns) {
			//forward pass
			for (int i = 0; i < neurons.getLayerCount(); ++i) {
				NeuralLayer l = neurons.getLayer(i);
				l.setTrainingRate(trainingRate);
				l.process(p);
			}
			//backward pass
			for (int i = neurons.getLayerCount() - 1; i >= 0; --i) {
				NeuralLayer l = neurons.getLayer(i);
				l.calculateDelta(p);
				l.setWeights(p);
			}
			if (addMaxOutputNeuron) {
				addMax(p, trainingConfusionMatrix);
			} else {
				addRounded(p, trainingConfusionMatrix);
			}
			patternErrors += neurons.getLastLayer().getErrors();
		}
		if (verbose) {
			Log.d(trainingConfusionMatrix.toString());
		}
		double error = Math.pow((this.trainingPatterns.size() * this.neurons.getOutputCount()), -1) *
				patternErrors;
		this.meanError =  Math.pow(error, 0.5);
		
	}
	
	
	
	public String toString() {
		return this.neurons.toString();
 
	}
	
	
}
