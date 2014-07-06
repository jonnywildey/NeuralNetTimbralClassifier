package neuralNet;

import java.util.ArrayList;

public class Layer {
	
	public Layer inputLayer;
	public Layer outputLayer;
	public ArrayList<Neuron> neurons;
	public int neuronCount;
	public int layerNumber;

	public Layer() {
		neurons = new ArrayList<Neuron>();
		neuronCount = 0;
		layerNumber = 0;
	}
	
	
	/** returns true if this layer is last of network **/
	public boolean isLast() {
		if (this.outputLayer == null) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isFirst() {
		if (this.inputLayer == null) {
			return true;
		} else {
			return false;
		}
	}
	
	public void initialiseLayer(Layer prevLayer, Layer nextLayer, double bias, int inputCount) {
		int ic = (this.isFirst()) ? inputCount : this.inputLayer.neuronCount;
		for (int i = 0; i < neuronCount; ++i) {
			Neuron n = new Neuron(bias, i, ic, layerNumber);
			
			if (prevLayer != null) {
				n.setInputArray(prevLayer.neurons);
			} 
			if (nextLayer != null) {
				n.setOutputArray(nextLayer.neurons);
			}
			//System.out.println(ic);

			neurons.add(n);
		}
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder(); 
		sb.append("Layer number: " + this.layerNumber);
		sb.append(" Count " + this.neuronCount);
		for (Neuron n : this.neurons) {
			sb.append(n.toString());
		}
		return sb.toString();
	}
	
	public void processLayer() {
		
	}
	
	/**correctly sets all the input arrays for the layer. Pattern only necessary 
	 * for first layer, can be set to null otherwise **/
	public void setInputArray(Pattern p) {
		if (this.isFirst()) {
			insertPattern(p);
		} else {
			for (Neuron n : this.neurons) {
				n.setInputArray(this.inputLayer.neurons);
			}
				
		}
	}
	
	private void insertPattern(Pattern p) {
		for (Neuron n : this.neurons) {
			n.setInputArray(p.inputArray);
		}
	}
	
	public void setOutputArray(Pattern p) {
		if (this.isLast()) {
			for (Neuron n : this.neurons) {
				n.setTarget(p.targetArray.get(n.id)); //set target
			}
		} else {
			for (Neuron n : this.neurons) {
				n.setOutputArray(this.outputLayer.neurons);
			}
		}
	}
	
	/** calculates delta values for entire layer and sets weights. returns pattern error **/
	public void calculateDelta(Pattern p) {
		
		if (this.isLast()) {	
			for (Neuron n : this.neurons) {
				n.setTarget(p.targetArray.get(n.id)); //set target
				if (n.learning) {
					n.delta = (n.target - n.output) * NNFunctions.sigmoidDerivative(n.activation);
				}	
			}
		} else {
			for (Neuron n: this.neurons) {
				if (n.learning) {
					double sum = 0;
					for (Neuron nn : this.outputLayer.neurons) {
						sum += nn.delta * nn.weightList.get(n.id);
						//System.out.println(nn.weightList.get(n.id) + "\t" + nn.delta);
					}
					n.delta = sum * NNFunctions.sigmoidDerivative(n.activation);
				}
			}
		}
	}
	
	/** Processes entire layer. Pattern p necessary for first and 
	 * last layers
	 * @param p
	 */
	public void process(Pattern p) {
		if (this.isFirst()) {
			for (Neuron n : this.neurons) {
				double sum = 0;
				for (int i = 0; i < p.inputArray.size(); ++i) {
					sum += (p.inputArray.get(i).getValue() * n.weightList.get(i)); 
					//System.out.println("input: " + p.inputArray.get(i).getValue() +
					//" current weight: " + n.weightList.get(i) + " sum: " + sum);
				} 
				//and bias
				sum += n.weightList.get(n.weightList.size() - 1);
				//System.out.println("bias: " + n.weightList.get(n.weightList.size() - 1));
				n.activation = sum;
				n.output = NNFunctions.sigmoid(sum);
				//System.out.println("output: " + n.output + "\tactivation: " + n.activation + "\t" + n.id);
			}
		} else {
			for (Neuron n : this.neurons) {
				double sum = 0;
				for (int i = 0; i < this.inputLayer.neurons.size(); ++i) {
					sum += (this.inputLayer.neurons.get(i).getValue() * n.weightList.get(i)); 
					//System.out.println("input: " + this.inputArray.get(i) +
					//" current weight: " + this.weightList.get(i) + " sum: " + sum);
				}
				//and bias
				sum += n.weightList.get(n.weightList.size() - 1);
				n.activation = sum;
				n.output = NNFunctions.sigmoid(sum);
			}
		}
	}
	
	public void setTrainingRate(double trainingRate) {
		for (Neuron n : this.neurons) {
			n.trainingRate = trainingRate;
		}
	}


	public Double getErrors() {
		double sum = 0;
		for (Neuron n : this.neurons) {
			sum += n.getError();
		}
		return sum;
	}
	
	public void setWeights(Pattern p) {
		for (Neuron n : this.neurons) {
			if (n.learning) {
				for (int i = 0; i < n.weightList.size(); ++i) {
					Double weightChange = 1 * n.trainingRate * n.delta; 
					if (i < n.weightList.size() - 1) { //bias will always have a signal
						if (this.isFirst()) {
							weightChange *= p.inputArray.get(i).getValue(); //first layer pattern input
						} else {
							weightChange *= this.inputLayer.neurons.get(i).getValue(); //only change if it had an input signal
						}
					}
					n.weightList.set(i, n.weightList.get(i)  + weightChange);// current weight NOT RIGHT
					}
			}
			//System.out.println(n.toString());
		}	
	}

}
