package com.neuralNet.layers;

import java.io.Serializable;
import java.util.Random;

import com.neuralNet.pattern.Pattern;

/**
 * Linked list of neural layers *.
 *
 * @author Jonny Wildey
 * @version 1.0
 */
public class LayerList implements Serializable {

	private static final long serialVersionUID = 907430324885673987L;
	private NeuralLayer firstLayer;
	private int layerCount;
	private int epoch;
	

	/**
	 * Instantiates a new layer list.
	 */
	public LayerList() {
		this.layerCount = 0;
	}
	

	/**
	 * Instantiates a new layer list.
	 *
	 * @param layerStructure the layer structure
	 * @param inputCount the input count
	 */
	public LayerList(LayerStructure layerStructure, int inputCount) {
		this();
		this.initialiseList(layerStructure, inputCount);
	}
	
	/**
	 * Copy constructor *.
	 *
	 * @param layerList the layer list
	 */
	public LayerList(LayerList layerList) {
		this.layerCount = layerList.layerCount;
		this.firstLayer = new NeuralLayer(layerList.getFirstLayer());
	}


	/**
	 * Adds the layer.
	 *
	 * @param layer the layer
	 */
	public void addLayer(NeuralLayer layer) {
		if (this.firstLayer == null) {
			this.firstLayer = layer;
		} else {
			layer.inputLayer = this.getLastLayer();
			this.getLastLayer().outputLayer = layer;
		}
		layerCount++;
	}
	
	/**
	 * Gets the layer.
	 *
	 * @param layerNo the layer no
	 * @return the layer
	 */
	public NeuralLayer getLayer(int layerNo) {
		NeuralLayer holdLayer = firstLayer;
		for (int i = 0; i < layerNo; ++i) {
			holdLayer = holdLayer.outputLayer;
		}
		return holdLayer;
	}
	
	/**
	 * Gets the last layer.
	 *
	 * @return the last layer
	 */
	public NeuralLayer getLastLayer() {
		NeuralLayer l = this.firstLayer;
		if (l == null) {return null;}
		while (l.outputLayer !=  null) {
			l = l.outputLayer;
		}
		return l;
	}
	
	/**
	 * Gets the first layer.
	 *
	 * @return the first layer
	 */
	public NeuralLayer getFirstLayer() {
		return this.firstLayer;
	}
	
	/**
	 * Initialise list.
	 *
	 * @param ls the ls
	 * @param inputCount the input count
	 */
	public void initialiseList(LayerStructure ls, int inputCount) {
		for (int i = 0; i < ls.getTotalLayers(); ++i) {
			NeuralLayer l = new NeuralLayer();
			l.neuronCount = ls.getLayerCount(i);
			l.layerNumber = i;
			this.addLayer(l);
			l.initialiseLayer(inputCount);	
		}
	}
	
	
	
	/**
	 * Gets the layer count.
	 *
	 * @return the layer count
	 */
	public int getLayerCount() {
		return layerCount;
	}


	/**
	 * Gets the output count.
	 *
	 * @return the output count
	 */
	public int getOutputCount() {
		return this.getLastLayer().neuronCount;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
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
	
	/**
	 * sets learning to on/off for entire List *.
	 *
	 * @param learn the new learning
	 */
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
	
	/**
	 * Sets the initial weights.
	 *
	 * @param p the p
	 * @param seed the seed
	 * @return the long
	 */
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
			}
			l = l.outputLayer;
		}
		return seed;
	}
	
	/**
	 * Sets the initial weights.
	 *
	 * @param p the p
	 * @return the long
	 */
	public long setInitialWeights(Pattern p) {
		return setInitialWeights(p, System.currentTimeMillis());
	}


	/**
	 * Sets the epoch.
	 *
	 * @param epoch the new epoch
	 */
	public void setEpoch(int epoch) {
		this.epoch = epoch;
	}
	
	/**
	 * Gets the epoch.
	 *
	 * @return the epoch
	 */
	public int getEpoch() {
		return this.epoch;
	}

	
}
