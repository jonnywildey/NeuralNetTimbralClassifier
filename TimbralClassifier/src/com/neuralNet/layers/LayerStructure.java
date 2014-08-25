package com.neuralNet.layers;

import java.io.Serializable;
import java.util.ArrayList;

import com.neuralNet.pattern.Pattern;
import com.neuralNet.pattern.TestPatterns;

/**
 * for representing the somewhat complex structure of layers in Neural Networks.  *
 *
 * @author Jonny Wildey
 * @version 1.0
 */
public class LayerStructure implements Serializable {

	private static final long serialVersionUID = 6014061235694376364L;
	public int outputCount;
	private ArrayList<Integer> hiddenLayer;
	
	/**
	 * Instantiates a new layer structure.
	 */
	public LayerStructure() {
		hiddenLayer = new ArrayList<Integer>();
	}
	
	/**
	 * Instantiates a new layer structure.
	 *
	 * @param p the p
	 */
	public LayerStructure(Pattern p) {
		this();
		this.setOutput(p);
	}
	
	/**
	 * Instantiates a new layer structure.
	 *
	 * @param tp the tp
	 */
	public LayerStructure(TestPatterns tp) {
		this(tp.getTestingPatterns().get(0));
	}
	
	/**
	 * infers inputCount and outputCount from pattern *.
	 *
	 * @param p the new output
	 */
	public void setOutput(Pattern p) {
		this.outputCount = p.getOutputCount();
	}
	
	/**
	 * Adds a hidden layer to the end of current hidden layers *.
	 *
	 * @param neuronCount the neuron count
	 */
	public void addHiddenLayer(int neuronCount) {
		hiddenLayer.add(neuronCount);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Layer Structure:\n");
		for (Integer i : hiddenLayer) {
			sb.append("(" + i + ") - ");
		}
		sb.append("(" + this.outputCount + ")");
		return sb.toString();
	}

	/**
	 * Gets the total layers.
	 *
	 * @return the total layers
	 */
	public int getTotalLayers() {
		int c = 0;
		if (outputCount != 0) {c++;}
		c += hiddenLayer.size();
		return c;
	}

	/**
	 * Gets the layer count.
	 *
	 * @param i the i
	 * @return the layer count
	 */
	public int getLayerCount(int i) {
		if (i == (hiddenLayer.size())) {
			return outputCount;
		} else {
			return hiddenLayer.get(i) ;
		}
	}
}
