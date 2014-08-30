package com.neuralNet.layers;

import java.io.Serializable;

import com.neuralNet.NeuralComponent;

/**
 * Very small class useful for allowing first layer neurons to treat patterns as
 * other neurons.
 * 
 * @author Jonny Wildey
 * @version 1.0
 */
public class InputShell extends NeuralComponent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 668455893860894139L;
	private double value;

	/**
	 * Instantiates a new input shell.
	 * 
	 * @param value
	 *            the value
	 */
	public InputShell(double value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.neuralNet.NeuralComponent#getValue()
	 */
	@Override
	public Double getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 * 
	 * @param value
	 *            the new value
	 */
	public void setValue(double value) {
		this.value = value;
	}

}
