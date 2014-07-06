package neuralNet;

import java.util.ArrayList;

import com.matrix.ConfusionMatrix;

public class Epoch {
	protected ArrayList<Pattern> trainingPatterns;
	protected ArrayList<Pattern> testingPatterns;
	protected LayerList neurons;
	protected ArrayList<Double> errorAmount;
	protected ArrayList<Double> errorList;
	protected ArrayList<Double>  rmsError;
	protected Double trainingRate;
	protected ConfusionMatrix confusionMatrix;
	protected boolean debug;
	protected boolean verbose;
	public Double meanError;
	
	public Epoch() {
		this.trainingPatterns = new ArrayList<Pattern>();
	}
	
	public Epoch(ArrayList<Pattern> trainingPatterns,
			ArrayList<Pattern> testingPatterns, LayerList neurons,
			Double trainingRate) {
		this.neurons = neurons;
		this.trainingPatterns = trainingPatterns;
		this.testingPatterns = testingPatterns;
		this.trainingRate = trainingRate;
		this.errorAmount = new ArrayList<Double>();
		this.errorList = new ArrayList<Double>();
		this.rmsError = new ArrayList<Double>();
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


	public ConfusionMatrix getConfusionMatrix() {
		return confusionMatrix;
	}

	public void shuffleTrainingPatterns() {
		this.trainingPatterns = NNUtilities.knuthShuffle(this.trainingPatterns);
	}

	public void addTrainingPattern(Pattern trainingPattern) {
		trainingPatterns.add(trainingPattern);
	}
	
	public void setTrainingRate(Double trainingRate) {
		this.trainingRate = trainingRate;
	}
	
	public ConfusionMatrix runValidationEpoch() {
		confusionMatrix = new ConfusionMatrix(neurons.getOutputCount());
		neurons.setLearning(false);
		for (Pattern p: this.testingPatterns) {
			//input pattern inputs
			//forward pass
			for (int i = 0; i < neurons.getLayerCount(); ++i) {
				NeuralLayer l = neurons.getLayer(i);
				l.setTrainingRate(trainingRate);
				l.process(p);
			}
			//System.out.println(p.toString());
			for (Neuron n : neurons.getLastLayer().neurons) {
				int roundedOut = (int)Math.rint(n.output);
				if (roundedOut == 1) {
					//System.out.println("***\n" + n.id + "\n" + n.output + "\n" + p.getTargetNumber());
					confusionMatrix.addToCell(n.id, p.getTargetNumber());
				}
			}
		}
		
		if (verbose) {
			System.out.println(confusionMatrix.toString());
		}
		return confusionMatrix;	
	}
	
	
	
	public void runEpoch() {
		
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
			patternErrors += neurons.getLastLayer().getErrors();
		}
		double error = Math.pow((this.trainingPatterns.size() * this.neurons.getOutputCount()), -1) *
				patternErrors;
		this.meanError =  Math.pow(error, 0.5);
		
	}
	
	
	
	public String toString() {
		return this.neurons.toString();
 
	}
	
	
}
