package com.neuralNet;

import java.io.Serializable;


public class InputShell extends NeuralComponent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 668455893860894139L;
	private double value;
	
	public void setValue(double value) {
		this.value = value;
	}

	public InputShell(double value) {
		this.value = value;
	}
	
	@Override
	public Double getValue() {
		return value;
	}
	
	

}
