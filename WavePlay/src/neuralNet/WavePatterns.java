package neuralNet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import riff.Wave;
import filemanager.ArrayStuff;
import filemanager.CSVString;
import filemanager.Log;

/**Container object for multiple Wave Patterns **/
public class WavePatterns extends TestPatterns implements Serializable {
	
	private static final long serialVersionUID = 8265438349914627431L;
	
	public Wave[] getWaves() {
		return waves;
	}

	public void setWaves(Wave[] waves) {
		this.waves = waves;
	}

	public WavePattern[] getPatterns() {
		return patterns;
	}

	public void setPatterns(WavePattern[] patterns) {
		this.patterns = patterns;
	}

	public Wave[] waves;
	public WavePattern[] patterns;
	
	/**Converts double values to input shell objects **/
	public static ArrayList<InputShell> doubleToInputShell(double[] values) {
		ArrayList<InputShell> al = new ArrayList<InputShell>(values.length);
		for (double value : values) {
			al.add(new InputShell(value));
		}
		return al;
	}
	
	/** Reduces the size of all patterns input by 2^x **/
	public void reduceScale(double twoToThePower) {
		double val = Math.pow(2, twoToThePower);
		for (WavePattern p : patterns) {
			for (InputShell is : p.inputArray) {
				is.value /=  val;
			}
			Log.d(p.toString());
		}
	}
	
	/** turns a wav with metadata to a pattern **/
	public void wavToPattern() {
		//patterns
		this.patterns = new WavePattern[waves.length];
		String[] instrs = new String[waves.length];
		for (int i = 0; i < waves.length; ++i) {
			patterns[i] = new WavePattern(i); //make pattern
			String str;
			try {
				//get inputs
				str = new String(
						waves[i].getSubChunk("LIST").getSubChunk("IAS7").getData(), "UTF-8");
				CSVString s = new CSVString(str);
				s.readFile();
				patterns[i].inputArray = doubleToInputShell(
						s.makeDoubleArray()[1]);
				//get instrumental outputs
				instrs[i] = new String(waves[i].getSubChunk("LIST").
						getSubChunk("IAS8").getData(), "UTF-8");
			} catch (Exception e) {
				
			}	
		}
		getOutputs(instrs);
	}
	
	
	private void getOutputs(String[] instrs) {
		// convert to bitarray
		String[][] targets = NNUtilities.getCount(instrs, true);
		double[][] bits = NNUtilities.createUniqueBits(targets.length);
		//convert back
		for (int i = 0; i < waves.length; ++i) {
			for (int j = 0; j < targets.length; ++j) {
				if (instrs[i].equals(targets[j][0])) {
					patterns[i].targetArray = ArrayStuff.doubleToArrayList(bits[j]);
					
				}
			}
		}
	}
	
	public String toString() {
		if (hasPatterns()) {
			StringBuilder sb = new StringBuilder(this.patterns.length * 200);
			for (WavePattern wp : this.patterns) {
				sb.append(wp.toString());
			}
			return sb.toString();
		} else {
			return null;
		}
		
	}

	private boolean hasPatterns() {
		return (this.patterns != null);
	}



	public void removeWaves() {
		this.waves = null;
	}

}
