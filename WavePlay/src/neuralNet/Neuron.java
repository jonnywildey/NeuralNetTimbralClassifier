package neuralNet;

import java.io.Serializable;
import java.util.ArrayList;

public class Neuron extends NeuralComponent implements Serializable {
	
	private static final long serialVersionUID = -1422063420400907090L;
	protected ArrayList<Double> weightList; //0 to 1
	protected double randomRange; //range where weights can be initially set.
								 // normally 0.2 (-0.1 - 0.1)
	protected Double output; 
	protected Integer id; //Mainly for iteration
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
		this.weightList = new ArrayList<Double>(inputCount + 1);
		this.id = id;
		this.learning = true;	
		this.layer = layer;
	}
	
	/* Copy constructor */
	public Neuron(Neuron n) {
		this.id = n.id;
		this.randomRange = n.randomRange;
		this.learning = n.learning;
		this.layer = n.layer;
		this.weightList = new ArrayList<Double>(n.weightList.size());
		for (Double d : n.weightList) {
			this.weightList.add(new Double(d));
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
