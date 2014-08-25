package com.neuralNet.layers;

import java.io.Serializable;
import java.util.Random;

import com.neuralNet.pattern.Pattern;


public class LayerList implements Serializable {

	private static final long serialVersionUID = 907430324885673987L;
	private NeuralLayer firstLayer;
	private int layerCount;
	private int epoch;
	

	public LayerList() {
		this.layerCount = 0;
	}
	

	public LayerList(LayerStructure layerStructure, int inputCount) {
		this();
		this.initialiseList(layerStructure, inputCount);
	}
	
	/**Copy constructor **/
	public LayerList(LayerList layerList) {
		this.layerCount = layerList.layerCount;
		this.firstLayer = new NeuralLayer(layerList.getFirstLayer());
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
	
	public void initialiseList(LayerStructure ls, int inputCount) {
		for (int i = 0; i < ls.getTotalLayers(); ++i) {
			NeuralLayer l = new NeuralLayer();
			l.neuronCount = ls.getLayerCount(i);
			l.layerNumber = i;
			this.addLayer(l);
			l.initialiseLayer(inputCount);	
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
			sb.append(l.toStringVerbose() + "\n");
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
	
	public long setInitialWeights(Pattern p, long seed) {
		Random r = new Random(seed);
		NeuralLayer l = this.firstLayer;
		double factor = 0.2;
		while (l != null) {
			for (Neuron n : l.neurons) {
				if (l.isFirst()) {
					for (int i = 0; i < p.getInputArray().size() + 1; ++i) { //+ 1 for bias
						n.getWeightList().add((r.nextDouble() * factor) - factor / 2);
					}
				} else {
					for (int i = 0; i < l.inputLayer.neuronCount + 1; ++i) {
						n.getWeightList().add((r.nextDouble() * factor) - factor / 2);
					}
				}

				//System.out.println(n.toString());
			}
			l = l.outputLayer;
		}
		return seed;
	}
	
	public long setInitialWeights(Pattern p) {
		return setInitialWeights(p, System.currentTimeMillis());
	}


	public void setEpoch(int epoch) {
		this.epoch = epoch;
	}
	
	public int getEpoch() {
		return this.epoch;
	}

	
}
