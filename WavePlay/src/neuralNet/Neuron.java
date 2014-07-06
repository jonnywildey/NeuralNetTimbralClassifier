package neuralNet;

import java.io.Serializable;
import java.util.ArrayList;

public class Neuron extends NeuralComponent implements Serializable {
	
	private static final long serialVersionUID = -1422063420400907090L;
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
	
	
	@Override
	public Double getValue() {
		return this.output;
	}
		
	
	public Double getError() {
		return (Double) Math.pow((this.target - this.output), 2); //I think...
	}
	
	
	public void setTarget(Double target) {
		this.target = target;
	}
	
	public void setTargetRate(Double targetRate) {
		this.trainingRate = targetRate;
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
