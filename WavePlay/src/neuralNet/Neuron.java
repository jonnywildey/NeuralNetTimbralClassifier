package neuralNet;

import java.util.ArrayList;

// Couldn't work out an easy way of doing generics
public class Neuron extends NeuralComponent{
	
	protected ArrayList<NeuralComponent> outputNeurons;
	protected ArrayList<NeuralComponent> inputNeurons;
	protected ArrayList<Double> inputArray; //array of inputs where bias input is added
	protected Double bias; //typically -0.5
	protected ArrayList<Double> weightList; //0 to 1
	protected Double output; 
	protected Integer id; //Mainly for iteration
	protected Double activation; //non-sigmoided. for sigmoid derivation
	protected boolean learning; //am I learning?
	protected Integer layer;
	protected Double target; //Won't necessarily have one (I think...)
	protected Double trainingRate;
	protected double delta;
	
	public Neuron( Double bias, Integer id, int inputCount, int layer) {
		this.bias = bias;
		this.weightList = new ArrayList<Double>(inputCount + 1);
		this.randomWeight(inputCount);
		this.id = id;
		this.learning = true;	
		this.layer = layer;
	}
	
	public void turnOffLearning(){
		this.learning = false;
	}
	
	public void turnOnLearning() {
		this.learning = true;
	}
	
	public void randomWeight(int inputCount) {
		for (int i = 0; i < inputCount; ++i) {
			weightList.add( (Double) (Math.random() * 0.1));		
		}
		//add bias
		weightList.add( this.bias);
		
	}
	
	
	@SuppressWarnings("unchecked")
	public void setInputArray(ArrayList<? extends NeuralComponent> inputNeurons) {
		this.inputNeurons = (ArrayList<NeuralComponent>) inputNeurons;
		this.inputArray = new ArrayList<Double>(); //new array
		for (NeuralComponent nc: inputNeurons) {
			this.inputArray.add(nc.getValue());
			
		}
		this.inputArray.add(1d); //add bias firing
	}
	
	@Override
	public Double getValue() {
		return this.output;
	}
	
	@SuppressWarnings("unchecked")
	public void setOutputArray(ArrayList<? extends NeuralComponent> outputNeurons) {
		this.outputNeurons = (ArrayList<NeuralComponent>) outputNeurons;
		
	}
	
	
	
	/* Make sure target and targetrate have been set */
	public boolean learnLastLayer() {
		//System.out.println("target: " + this.target);
		//System.out.println("output: " + this.output);
		
		//System.out.println(this.activation);
		//printCurrentWeights();

		/*learning */
		if (this.learning == true) {
			delta = (this.target - this.output) * NNFunctions.sigmoidDerivative(this.activation);
			//System.out.println("last delta" + delta);
			setWeights();
			//System.out.println("new weights");
			//printCurrentWeights();
			return true;
		} else {
			return false;
		}
	}
	
	// not acceptable for last
	public boolean learnNotLastLayer() {
		if (this.learning == true) {
			//construct sum error
			Double sum = 0d;
			for (NeuralComponent nc: this.outputNeurons) {
				Neuron n = nc.getNeuron();
				
				Double nextDelta = (n.target - n.output) * NNFunctions.sigmoidDerivative(n.activation); //last layer output. 
																					//this would change if we wanted > 2 layers
				//check this isn't stupid
				//System.out.println("nd: " + nextDelta);
				
				//check weightlist number
				//System.out.println("wl" + n.weightList.get(this.id));
				
				sum += (nextDelta * n.weightList.get(this.id)); //this could be wrong
				
				//sum += nextDelta;  //this could be wrong
			}
			//System.out.println("sum: " + sum);
			delta = sum * NNFunctions.sigmoidDerivative(activation); //sum is now current delta
			
			
			setWeights();
			return true;
		} else {
			return false;
		}
		
	}
	
	public Double getError() {
		return (Double) Math.pow((this.target - this.output), 2); //I think...
	}
	
	
	/* Works for all layers. sets weight according to delta */
	public void setWeights() {
		for (int i = 0; i < this.weightList.size(); ++i) {
			
			Double weightChange = 1 * this.trainingRate 
					* this.inputArray.get(i) //only change if it had an input signal
					* delta; 
			//System.out.println("weight change: " + weightChange);
			this.weightList.set(i, this.weightList.get(i)  + weightChange);// current weight NOT RIGHT
			//System.out.println("new weight: " + this.weightList.get(i));		
		}
		
	}
	
	private void printCurrentWeights() {
		System.out.println("printing current weights");
		for (int i = 0; i < this.weightList.size(); ++i) {
			System.out.println("current weight for: " + i + " : \t" + this.weightList.get(i));
		}
	}
	
	public void setTarget(Double target) {
		this.target = target;
	}
	
	public void setTargetRate(Double targetRate) {
		this.trainingRate = targetRate;
	}
	
	public void process(Double targetRate) {
		Double sum = 0d;	
		setTargetRate(targetRate);
		System.out.println(this.inputArray.toString());
		for (int i = 0; i < this.inputArray.size(); ++i) {
			sum += (this.inputArray.get(i) * this.weightList.get(i)); 
			//System.out.println("input: " + this.inputArray.get(i) + " current weight: " + this.weightList.get(i) + " sum: " + sum);
		}
		//System.out.println("sum: " + sum);
		this.activation = sum;
		this.output = NNFunctions.sigmoid(sum);
		//System.out.println(this.toString());
		//System.out.println("output: " + this.activation);

	}
	
	public Double getOutput() {
		return this.output;
	}
	

	
	public String toString() {
		String str = "\nNeuron id: " + this.id + " \tLayer: " + this.layer +  "\tWeights: \n";
		for (Double f: this.weightList) {
			str = str.concat(f + "\t");
		}
		
		str = str.concat("\nactivation: " + this.activation);
		str = str.concat("\noutput: " + this.output);
		str = str.concat("\n");
		return str;
	}
	
}
