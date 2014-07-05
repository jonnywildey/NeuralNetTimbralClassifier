package neuralNet;

import java.util.ArrayList;

import com.matrix.ConfusionMatrix;

public class Epoch {
	protected ArrayList<Pattern> trainingPatterns;
	protected ArrayList<Pattern> testingPatterns;
	protected ArrayList<NeuronShell> hiddenNeuronList;
	protected ArrayList<NeuronShell> lastNeuronList;
	protected ArrayList<Double> errorAmount;
	protected ArrayList<Double> errorList;
	protected ArrayList<Double>  rmsError;
	protected Double trainingRate;
	protected ConfusionMatrix confusionMatrix;
	protected boolean debug;
	protected boolean verbose;
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


	public Double meanError;
	
	public Epoch(ArrayList<Pattern> trainingPatterns, ArrayList<Pattern> testingPatterns, 
				 ArrayList<NeuronShell> hiddenNeuronList, 
				 ArrayList<NeuronShell> lastNeuronList, Double trainingRate) {
		this.hiddenNeuronList = hiddenNeuronList;
		this.lastNeuronList = lastNeuronList;
		this.trainingPatterns = trainingPatterns;
		this.testingPatterns = testingPatterns;
		this.trainingRate = trainingRate;
		this.errorAmount = new ArrayList<Double>();
		this.errorList = new ArrayList<Double>();
		this.rmsError = new ArrayList<Double>();

		//set up lists
		for (int i = 0; i < this.lastNeuronList.size(); ++i) { //is only last correct?
			this.errorAmount.add(0d);
			this.errorList.add(0d);
			this.rmsError.add(0d);
		}
		
	}
	
	public Epoch() {
		this.trainingPatterns = new ArrayList<Pattern>();
	}
	
	public void addTrainingPattern(Pattern trainingPattern) {
		trainingPatterns.add(trainingPattern);
	}
	
	public void setTrainingRate(Double trainingRate) {
		this.trainingRate = trainingRate;
	}
	
	public ConfusionMatrix runValidationEpoch() {
		confusionMatrix = new ConfusionMatrix(this.lastNeuronList.size());
		//Load Pattern
		//init
		for (Pattern p: this.testingPatterns) {
			for (NeuronShell nc : hiddenNeuronList) {
				nc.getNeuron().turnOffLearning();
				//load patterns into hidden layer
				nc.neuron.setInputArray(p.inputArray);
				//set outputs (should be outside loop)
				nc.neuron.setOutputArray(lastNeuronList);
				//calculate hidden layer node outputs
				nc.neuron.process(this.trainingRate);
			}
			for (NeuronShell nc: lastNeuronList) {
				nc.getNeuron().turnOffLearning();
				//set last layer inputs
				nc.neuron.setInputArray(this.hiddenNeuronList);
				//calculate last layer node outputs
				nc.neuron.process(this.trainingRate);
				
				int roundedOut = (int)Math.rint(nc.getNeuron().output);
				if (roundedOut == 1) {
					confusionMatrix.addToCell(nc.neuron.id, p.getTargetNumber());
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
		for (Pattern p: this.trainingPatterns) {
			double patternError = 0f;
			for (NeuronShell nc : hiddenNeuronList) {
				nc.getNeuron().turnOnLearning();
				//load patterns into hidden layer
				nc.neuron.setInputArray(p.inputArray);
				//set outputs (should be outside loop)
				nc.neuron.setOutputArray(lastNeuronList);
				//calculate hidden layer node outputs
				nc.neuron.process(this.trainingRate);
			}
			for (NeuronShell nc: lastNeuronList) {
				nc.getNeuron().turnOnLearning();
				//set last layer inputs
				nc.neuron.setInputArray(this.hiddenNeuronList);
				//calculate last layer node outputs
				nc.neuron.process(this.trainingRate);
				//adapt last layer weights
				nc.neuron.setTarget(p.targetArray.get(nc.neuron.id));
				nc.neuron.learnLastLayer(); //check the ids sync
				//might as well get errors while we're here
				patternError += nc.neuron.getError();
				
			}
			for (NeuronShell nc : hiddenNeuronList) {
				//adapt hidden layer weights
				nc.neuron.learnNotLastLayer();
			}
			//if(debug){System.out.println("Pattern Error: " + patternError);}
			patternErrors += (patternError);
		}
		
		//finish calculating mean square error
		double error = Math.pow((this.trainingPatterns.size() * this.lastNeuronList.size()), -1) *
						patternErrors;
		this.meanError =  Math.pow(error, 0.5);
		
	}
	
	
	
	public String toString() {
		String str = "\nHidden Neurons:\n";
		for (NeuronShell n: this.hiddenNeuronList) {
			str = str.concat(n.toString());
		}
		str += "\nLast Neurons:\n";
		for (NeuronShell n: this.lastNeuronList) {
			str = str.concat(n.toString());
		}
		
		return str;	 
	}
	
	
}
