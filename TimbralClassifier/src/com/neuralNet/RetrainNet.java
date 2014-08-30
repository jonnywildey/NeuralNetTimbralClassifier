package com.neuralNet;

public class RetrainNet extends ThreadNet {
	
	public MultiLayerNet net;

	public MultiLayerNet getNet() {
		return net;
	}

	public void setNet(MultiLayerNet net) {
		this.net = net;
	}
	
	public MultiLayerNet call() {
		try {
			net.runEpoch();
			net.runTestPatterns();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return net;
	}

}
