package neuralNet;

import java.util.ArrayList;

public abstract class NeuralComponent {

	public NeuralComponent() {
	}
	protected ArrayList<NeuralComponent> inputNeurons;
	protected ArrayList<NeuralComponent> outputNeurons;
	public Double getValue() {
		return null;
	}
	public Neuron getNeuron() {
		return null;
	}
}
