package com.neuralNet.layers;

import java.io.Serializable;
import java.util.ArrayList;

import com.neuralNet.NeuralComponent;

public class Neuron extends NeuralComponent implements Serializable {
	
	private static final long serialVersionUID = -1422063420400907090L;
	private ArrayList<Double> weightList; //0 to 1
	protected double randomRange; //range where weights can be initially set.
								 // normally 0.2 (-0.1 - 0.1)
	protected Double output; 
	private Integer id; //Mainly for iteration
	protected Double activation; //non-sigmoided. for sigmoid derivation
	protected boolean learning; //am I learning? not necessary any more but
								//good as a precaution
	protected Integer layer; //Not necessary but useful for analysis
							 //perhaps...
	protected Double target; //Won't necessarily have one (I think...)
	protected double trainingRate;
	protected double delta; //necessary for easy back-prop
	
	public Neuron(Integer id, int inputCount, int layer) {
		this.randomRange = 0.2;
		this.setWeightList(new ArrayList<Double>(inputCount + 1));
		this.setId(id);
		this.learning = true;	
		this.layer = layer;
	}
	
	/* Copy constructor */
	public Neuron(Neuron n) {
		this.setId(n.getId());
		this.randomRange = n.randomRange;
		this.learning = n.learning;
		this.layer = n.layer;
		this.setWeightList(new ArrayList<Double>(n.getWeightList().size()));
		for (Double d : n.getWeightList()) {
			this.getWeightList().add(new Double(d));
		}
		this.activation = n.activation;
		this.delta = n.delta;
		this.output = n.output;
		this.trainingRate = n.trainingRate;
		this.target = n.target;
	}

	public void turnOffLearning(){
		this.learning = false;
	}
	
	public void turnOnLearning() {
		this.learning = true;
	}
	
	@Override
	public Double getValue() {
		return this.output;
	}
		
	/**Only useful if calculating error **/
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
		String str = "\nNeuron id: " + this.getId() + " \tLayer: " + this.layer +  "\tWeights: \n";
		for (Double f: this.getWeightList()) {
			str = str.concat(f + "\t");
		}
		str = str.concat("\nactivation: " + this.activation);
		str = str.concat("\noutput: " + this.output);
		str = str.concat("\n");
		return str;
	}

	public ArrayList<Double> getWeightList() {
		return weightList;
	}

	public void setWeightList(ArrayList<Double> weightList) {
		this.weightList = weightList;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
}
