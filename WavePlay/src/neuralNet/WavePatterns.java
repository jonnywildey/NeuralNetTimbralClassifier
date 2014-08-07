package neuralNet;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import riff.Signal;
import riff.Wave;
import waveAnalysis.FFTBox;
import waveAnalysis.FrameFFT;
import waveProcess.Samples;
import filemanager.ArrayMethods;
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
		for (WavePattern p : patterns) {
			for (InputShell is : p.inputArray) {
				is.value /=  val;
			}
			Log.d(p.toString());
			Log.d(p.getInstrument());
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
	
	/** turns a set of waves with metadata to a pattern. This also redoes the FFT (input analysis) **/
	public void wavReFFTPattern() {
		//patterns
		File[] files = getFilesFromDirectory();
		this.patterns = new WavePattern[files.length];
		String[] instrs = new String[files.length];
		Wave wave = null;
		for (int i = 0; i < files.length; ++i) {
			wave = new Wave(files[i]);
			patterns[i] = new WavePattern(i, wave); //make pattern
			patterns[i].inputArray = reFFT(wave.getSignals());
			//get instrumental outputs
			instrs[i] = getInstrumentalOutputs(wave);
			patterns[i].instrument = instrs[i];
		}
		getOutputs(instrs, files);
	}
	
	/** turns a set of waves with metadata to a pattern. This also redoes the FFT (input analysis) **/
	public void wavReFFTBatchPattern(int count) {
		//randoms
		Random pitchRand = new Random();
		Random noiseRand = new Random();
		Random hpRand = new Random();
		Random lpRand = new Random();
		//patterns
		File[] files = getFilesFromDirectory();
		this.patterns = new WavePattern[files.length * count];
		String[] instrs = new String[files.length * count];
		Wave wave = null;
		Signal signal = null;
		for (int i = 0; i < files.length; ++i) {
			//read
			wave = new Wave(files[i]);
			signal = wave.getSignals();
			for (int j = 0; j < count; ++j) {
				patterns[i * count + j] = new WavePattern(i, wave); //make pattern
				patterns[i * count + j].inputArray = reFFT(
						Samples.processSignalChain(pitchRand, noiseRand, hpRand, lpRand, signal));
				//get instrumental outputs
				instrs[i * count + j] = getInstrumentalOutputs(wave);
				patterns[i * count + j].instrument = instrs[i];
			}
		}
		getOutputs(instrs, files);
	}



	private ArrayList<InputShell> reFFT(Signal signals) {
		FrameFFT fft = new FrameFFT(signals, 4096);
		FFTBox dd = fft.analyse(20, 20000);
		//fft.makeGraph();
		//dd = FrameFFT.getExponentTable(dd, 0.78); //rate
		dd = FFTBox.getSumTable(dd, 10);
		
		dd = FFTBox.getHiResBarkedSubset(dd, 0.1);
		
		//Log.d((dd.toString()));
		dd = FFTBox.normaliseTable(dd, 10);
		return Pattern.doubleToInputShell(dd.getValues()[0]);
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
					patterns[i].targetArray = ArrayMethods.doubleToArrayList(bits[j]);
					
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
