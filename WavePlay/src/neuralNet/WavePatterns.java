package neuralNet;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import riff.Wave;
import filemanager.ArrayStuff;
import filemanager.CSVString;
import filemanager.Log;
import filemanager.Serialize;

/**Container object for multiple Wave Patterns **/
public class WavePatterns implements Serializable {
	
	private static final long serialVersionUID = 8265438349914627431L;
	public File filePath;
	public WavePattern[] patterns;
	private String[] instruments;
	
	public WavePatterns(File filePath) {
		this.filePath = filePath;
	}
	


	public WavePattern[] getPatterns() {
		return patterns;
	}

	public void setPatterns(WavePattern[] patterns) {
		this.patterns = patterns;
	}
	
	public void setPatterns(ArrayList<WavePattern> wavePatterns) {
		this.patterns = WavePattern.arrayListToArray(wavePatterns);
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
	
	protected File[] getFilesFromDirectory() {
		return Serialize.getActualFiles(this.filePath);
	}
	
	
	/** turns a set of waves with metadata to a pattern **/
	public void wavMetaToPattern() {
		//patterns
		File[] files = getFilesFromDirectory();
		this.patterns = new WavePattern[files.length];
		String[] instrs = new String[files.length];
		String str = "";
		Wave wave = null;
		for (int i = 0; i < files.length; ++i) {
			wave = new Wave(files[i]);
			patterns[i] = new WavePattern(i, wave); //make pattern
			str = getDataChunk(wave);
			patterns[i].inputArray = getInputs(str);
			//get instrumental outputs
			instrs[i] = getInstrumentalOutputs(wave);
			patterns[i].instrument = instrs[i];
		}
		getOutputs(instrs, files);
	}



	protected ArrayList<InputShell> getInputs(String str) {
		CSVString s = new CSVString(str);
		s.readFile();
		return Pattern.doubleToInputShell(
				s.makeDoubleArray()[1]);
	}



	protected String getInstrumentalOutputs(Wave wave) {
		try {
		return new String(wave.getSubChunk("LIST").
				getSubChunk("IAS8").getData(), "UTF-8");
		} catch (Exception e) {
			Log.d(e);
			return null;
		}
	}



	protected String getDataChunk(Wave wave) {
		String str = "";
		try {
			//get inputs
			str = new String(
					wave.getSubChunk("LIST").getSubChunk("IAS7").getData(), "UTF-8");
		}
		catch(Exception e) {
			Log.d(e);
		}
		return str;
	}
		
	
	/** get the outputs from the patterns **/
	private void getOutputs(String[] instrs, File[] files) {
		// convert to bitarray
		String[][] targets = NNUtilities.getCount(instrs, true);
		this.instruments = targets[0];
		double[][] bits = NNUtilities.createUniqueBits(targets.length);
		//convert back
		for (int i = 0; i < files.length; ++i) {
			for (int j = 0; j < targets.length; ++j) {
				if (instrs[i].equals(targets[j][0])) {
					patterns[i].targetArray = ArrayStuff.doubleToArrayList(bits[j]);
					
				}
			}
		}
	}
	
	public String[] getInstruments() {
		return instruments;
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
	
	/** Return all the filenames mentioned in patterns **/
	public File[] getFileNames() {
		return getFileNames(this.patterns);
	}
	
	public File getFilePath() {
		return filePath;
	}

	public void setFilePath(File filePath) {
		this.filePath = filePath;
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
	/** does this have any patterns? **/
	private boolean hasPatterns() {
		return (this.patterns != null);
	}


}
