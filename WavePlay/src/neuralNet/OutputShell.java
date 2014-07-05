package neuralNet;

import java.util.ArrayList;

public class OutputShell extends NeuralComponent {

protected Double value;
	
	public OutputShell(Double value) {
		this.outputNeurons = null;
		this.value = value;
	}
	
	@Override
	public Double getValue() {
		return value;
	}
	
	public void setInputArray(ArrayList<NeuralComponent> inputArray) {
		this.inputNeurons = inputArray;
	}

}
