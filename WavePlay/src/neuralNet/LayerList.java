package neuralNet;

import java.util.Random;


public class LayerList {
	private NeuralLayer firstLayer;
	private int layerCount;
	

	public LayerList() {
		this.layerCount = 0;
	}
	

	public LayerList(LayerStructure layerStructure, Double bias, int inputCount) {
		this();
		this.initialiseList(layerStructure, bias, inputCount);
	}


	public void addLayer(NeuralLayer layer) {
		if (this.firstLayer == null) {
			this.firstLayer = layer;
		} else {
			layer.inputLayer = this.getLastLayer();
			this.getLastLayer().outputLayer = layer;
		}
		layerCount++;
	}
	
	public NeuralLayer getLayer(int layerNo) {
		NeuralLayer holdLayer = firstLayer;
		for (int i = 0; i < layerNo; ++i) {
			holdLayer = holdLayer.outputLayer;
		}
		return holdLayer;
	}
	
	public NeuralLayer getLastLayer() {
		NeuralLayer l = this.firstLayer;
		if (l == null) {return null;}
		while (l.outputLayer !=  null) {
			l = l.outputLayer;
		}
		return l;
	}
	
	public NeuralLayer getFirstLayer() {
		return this.firstLayer;
	}
	
	public void initialiseList(LayerStructure ls, double bias, int inputCount) {
		for (int i = 0; i < ls.getTotalLayers(); ++i) {
			NeuralLayer l = new NeuralLayer();
			l.neuronCount = ls.getLayerCount(i);
			l.layerNumber = i;
			this.addLayer(l);
			l.initialiseLayer(bias, inputCount);	
		}
	}
	
	
	
	public int getLayerCount() {
		return layerCount;
	}


	public int getOutputCount() {
		return this.getLastLayer().neuronCount;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Layer List \n");
		NeuralLayer l = this.firstLayer;
		while (l != null) {
			sb.append(l.toString() + "\n");
			l = l.outputLayer;
		}
		return sb.toString();
	}
	
	/** sets learning to on/off for entire List **/
	public void setLearning(boolean learn) {
		NeuralLayer l = this.firstLayer;
		if (l != null) {
			while (l.outputLayer !=  null) {
				for (Neuron n : l.neurons) {
					n.learning = learn;
				}
				l = l.outputLayer;
			}
		}
		
	}
	
	public long setInitialWeights(Pattern p, double bias, long seed) {
		Random r = new Random(seed);
		NeuralLayer l = this.firstLayer;
		double factor = 0.1;
		while (l != null) {
			for (Neuron n : l.neurons) {
				if (l.isFirst()) {
					for (int i = 0; i < p.inputArray.size(); ++i) {
						n.weightList.add(r.nextDouble() * factor);
					}
				} else {
					for (int i = 0; i < l.inputLayer.neuronCount; ++i) {
						n.weightList.add(r.nextDouble() * factor);
					}
				}
				n.weightList.add(bias);
				//System.out.println(n.toString());
			}
			l = l.outputLayer;
		}
		return seed;
	}
	
	public long setInitialWeights(Pattern p, double bias) {
		return setInitialWeights(p, bias, System.currentTimeMillis());
	}

	
}
