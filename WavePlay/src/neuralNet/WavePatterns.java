package neuralNet;

import java.io.File;
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

	public Pattern[] getPatterns() {
		return patterns;
	}

	public void setPatterns(Pattern[] patterns) {
		this.patterns = patterns;
	}
	
	public void setPatterns(ArrayList<WavePattern> wavePatterns) {
		this.patterns = arrayListToArray(wavePatterns);
	}
	
	/**Convert arraylist of wave patterns to array of wave patterns **/
	public static Pattern[] arrayListToArray
	(ArrayList<WavePattern> wavePatterns) {
		Pattern[] wp = new Pattern[wavePatterns.size()];
		for (int i = 0; i < wp.length; ++i) {
			wp[i] = wavePatterns.get(i);
		}
		return wp;
	}

	public Wave[] waves;
	public Pattern[] patterns;
	
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
		for (Pattern p : patterns) {
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
			patterns[i] = new WavePattern(i, waves[i]); //make pattern
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
	
	/** Get all the file names in a set a wave patterns **/
	public static File[] getFileNames(Pattern[] patterns) {
		File[] files = new File[patterns.length];
		for (int i = 0; i < files.length; ++i) {
			files[i] = patterns[i].getFilePath();
			Log.d(files[i].getName());
		}
		return files;
	}
	
	public File[] getFileNames() {
		return getFileNames(this.patterns);
	}
	
	public String toString() {
		if (hasPatterns()) {
			StringBuilder sb = new StringBuilder(this.patterns.length * 200);
			for (Pattern wp : this.patterns) {
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
		for (Pattern p : this.patterns) {
			WavePattern wp = (WavePattern)p;
			wp.wave = null;
		}
	}

}
