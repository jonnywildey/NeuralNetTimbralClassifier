package neuralNet;


/** Traversing Neurons became too complicated in multilayers */
public class NeuronShell extends NeuralComponent {

	public NeuronShell() {
		// TODO Auto-generated constructor stub
	}
	
	public NeuronShell(Neuron n) {
		this.neuron = n;
	}

	protected Neuron neuron;
	
	public Double getValue() {
		return neuron.getOutput();
	}
	public Neuron getNeuron() {
		return neuron;
	}
	
	public String toString() {
		return neuron.toString();
	}

}
