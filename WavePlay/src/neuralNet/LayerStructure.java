package neuralNet;

import java.io.Serializable;
import java.util.ArrayList;

/** for representing the somewhat complex structure of layers in Neural Networks.  **/
public class LayerStructure implements Serializable {

	private static final long serialVersionUID = 6014061235694376364L;
	public int outputCount;
	private ArrayList<Integer> hiddenLayer;
	
	public LayerStructure() {
		hiddenLayer = new ArrayList<Integer>();
	}
	
	public LayerStructure(Pattern p) {
		this();
		this.setOutput(p);
	}
	
	public LayerStructure(TestPatterns tp) {
		this(tp.getTestingPatterns().get(0));
	}
	
	/** infers inputCount and outputCount from pattern **/
	public void setOutput(Pattern p) {
		this.outputCount = p.getOutputCount();
	}
	
	public void addHiddenLayer(int neuronCount) {
		hiddenLayer.add(neuronCount);
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Layer Structure:\n");
		for (Integer i : hiddenLayer) {
			sb.append("(" + i + ") - ");
		}
		sb.append("(" + this.outputCount + ")");
		return sb.toString();
	}

	public int getTotalLayers() {
		int c = 0;
		if (outputCount != 0) {c++;}
		c += hiddenLayer.size();
		return c;
	}

	public int getLayerCount(int i) {
		if (i == (hiddenLayer.size())) {
			return outputCount;
		} else {
			return hiddenLayer.get(i) ;
		}
	}
}
