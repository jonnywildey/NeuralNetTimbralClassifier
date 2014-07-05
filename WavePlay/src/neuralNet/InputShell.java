package neuralNet;

import java.util.ArrayList;

public class InputShell  extends NeuralComponent {

	protected double value;
	
	public InputShell(double value) {
		this.inputNeurons = null;
		this.value = value;
	}
	
	@Override
	public Double getValue() {
		return value;
	}
	
	public void setOutputArray(ArrayList<NeuralComponent> outputArray) {
		this.outputNeurons = outputArray;
	}
	

}
