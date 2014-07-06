package neuralNet;


public class InputShell extends NeuralComponent {

	protected double value;
	
	public InputShell(double value) {
		this.value = value;
	}
	
	@Override
	public Double getValue() {
		return value;
	}
	
	

}
