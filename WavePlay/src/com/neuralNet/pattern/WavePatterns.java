package com.neuralNet.pattern;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.neuralNet.NNUtilities;
import com.neuralNet.layers.InputShell;
import com.riff.Signal;
import com.riff.Wave;
import com.util.ArrayMethods;
import com.util.Log;
import com.util.Serialize;
import com.waveAnalysis.FFTBox;
import com.waveAnalysis.FrameFFT;
import com.waveProcess.FFTChain;
import com.waveProcess.SignalChain;

/**Container object for multiple Wave Patterns. Allows for multithreaded generation
 * of patterns from a folder of Waves, regeneration of patterns from metadata in waves,
 * batch processing and some other useful methods. See sub classes for other multi
 * threaded operations **/
public class WavePatterns implements Serializable, Callable<WavePatterns> {
	
	private static final long serialVersionUID = 8265438349914627431L;
	public File filePath; //Where to find Waves
	public WavePattern[] patterns; //Group of patterns
	protected String[] instruments; // all instruments of WavePatterns
	public File[] files; // Files of Waves
	public String[] instrs; // For output analysis
	
	/** Default constructor **/
	public WavePatterns() {
		super();
	}
	
	/** FilePath Constructor **/
	public WavePatterns(File filePath) {
		this.filePath = filePath;
	}
	
	/** Return Patterns **/
	public WavePattern[] getPatterns() {
		return patterns;
	}
	
	/**Set Patterns **/
	public void setPatterns(WavePattern[] patterns) {
		this.patterns = patterns;
	}
	
	/**Set patterns. Converts arraylist to array, so be careful with
	 * large arraylists 
	 */
	public void setPatterns(ArrayList<WavePattern> wavePatterns) {
		this.patterns = WavePattern.arrayListToArray(wavePatterns);
	}
	
	/** Return the name of the instrument based on the target number **/
	public String getInstrumentFromTargetNumber(int num) {
		String ans = "";
		//Log.d(ArrayMethods.toString(this.instruments));
		for (int i = 0; i < this.instruments.length; ++i) {
			if (num == i) {
				ans = this.instruments[i];
				break;
			}
		}
		return ans;
	}
	
	/** Reduces the size of all patterns input by 2^x. 
	 * Useful if amplitudes were not normalised **/
	public void reduceScale(double twoToThePower) {
		double val = Math.pow(2, twoToThePower);
		for (WavePattern p : patterns) {
			for (InputShell is : p.getInputArray()) {
				is.setValue(is.getValue() / val);
			}
			//Log.d(p.getInstrument());
		}
	}
	
	/** Get the files from the directory **/
	protected File[] getFilesFromDirectory() {
		return Serialize.getActualFiles(this.filePath);
	}
	
	
	/** turns a set of waves with metadata to a pattern **/
	public void wavMetaToPattern(File[] files) {
		//patterns
		this.files = getFilesFromDirectory();
		this.patterns = new WavePattern[files.length];
		String[] instrs = new String[files.length];
		String str = "";
		Wave wave = null;
		for (int i = 0; i < files.length; ++i) {
			wave = new Wave(files[i]);
			this.patterns[i] = new WavePattern(i, wave); //make pattern
			str = WavePattern.getDataChunk(wave);
			this.patterns[i].setInputArray(WavePattern.getInputs(str));
			//get instrumental outputs
			instrs[i] = WavePattern.getInstrumentalOutputs(wave);
			this.patterns[i].instrument = instrs[i];
		}
		this.instrs = instrs;
		getOutputs(instrs);
	}
	
	

	/**FFT analysis sequence **/
	protected ArrayList<InputShell> reFFT(Signal signals) {
		FFTBox dd = FFTChain.polyFFTChain(signals);
		return Pattern.doubleToInputShell(dd.getValues()[0]);
	}
	
	/**FFT analysis sequence, returns FFT box **/
	protected FFTBox reFFTBox(Signal signals) {
		FFTBox dd = FFTChain.polyFFTChain(signals);
		return dd;
	}
	
	/** get the outputs from the patterns **/
	protected void getOutputs(String[] instrs) {
		// convert to bitarray
		String[][] targets = NNUtilities.getCount(instrs, false);
		this.instruments = ArrayMethods.reverse(ArrayMethods.flip(targets)[0]);
		double[][] bits = NNUtilities.createUniqueBits(targets.length);
		//convert back
		for (int i = 0; i < this.patterns.length; ++i) {
			for (int j = 0; j < targets.length; ++j) {
				if (instrs[i].equals(targets[j][0])) {
					patterns[i].setTargetArray(ArrayMethods.doubleToArrayList(bits[j]));
					//Log.d(patterns[i].toString());
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
			for (WavePattern wp : this.patterns) {
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
	
	public static WavePatterns combinePatterns(WavePatterns... patterns ) {
		//get size
		int length = 0;
		for (int i = 0; i < patterns.length; ++i) {
			length += patterns[i].patterns.length;
		}
		//combine
		int c = 0;
		WavePattern[] np = new WavePattern[length];
		String[] instruments = new String[length];
		for (int i = 0; i < patterns.length;++i) {
			for (int j = 0; j < patterns[i].patterns.length;++j) {
				np[c] = patterns[i].patterns[j];
				np[c].setId(c);
				instruments[c] = patterns[i].instrs[j];
				c++;
			}
		}
		WavePatterns combined = new WavePatterns(patterns[0].filePath);
		combined.patterns = np;
		combined.instrs = instruments;
		
		combined.getOutputs(instruments);
		
		return combined;
	}
	
	/** Separate a WavePattern array into separate arrays **/
	public static WavePattern[][] splitPatterns(WavePattern[] patterns, int count) {
		WavePattern[][] np = new WavePattern[count][];
		//determine lengths
		int div = patterns.length / count;
		int mod = patterns.length % count;
		for (int i = 0; i < count; ++i) {
			if (mod != 0) {
				np[i] = new WavePattern[div + 1];
				mod--;
			} else {
				np[i] = new WavePattern[div];
			}
		}
		//write values
		int c = 0;
		for (int i = 0; i < np.length; ++i) {
			for (int j = 0; j < np[i].length; ++j) {
				np[i][j] = patterns[c];
				c++;
			}
		}
		return np;
	}
	
	/** Separate a File array into separate arrays **/
	public static File[][] splitFiles(File[] files, int count) {
		File[][] np = new File[count][];
		//determine lengths
		int div = files.length / count;
		int mod = files.length % count;
		for (int i = 0; i < count; ++i) {
			if (mod != 0) {
				np[i] = new File[div + 1];
				mod--;
			} else {
				np[i] = new File[div];
			}
		}
		//write values
		int c = 0;
		for (int i = 0; i < np.length; ++i) {
			for (int j = 0; j < np[i].length; ++j) {
				np[i][j] = files[c];
				c++;
				
			}
		}
		return np;
	}
	

	@Override
	public WavePatterns call() throws Exception {
		this.wavMetaToPattern(this.files);
		return this;
	}
	
	/** Threadable version of analysis. Uses call method for analysis **/
	public static WavePatterns genWaves(WavePatterns wavePatterns, int threadCount) {
		Class<? extends WavePatterns> type = wavePatterns.getClass();
		wavePatterns.files = Serialize.getActualFiles(wavePatterns.filePath);
		File[][] splits = splitFiles(wavePatterns.files, threadCount);
		//create Wave Patterns
		WavePatterns[] newWP = initWavePatterns(wavePatterns, threadCount,
				type, splits);
		//job
		runJobs(threadCount, newWP);
		WavePatterns comb = WavePatterns.combinePatterns(newWP);
		return comb;
	}
	
	/** Run multi threaded jobs **/
	protected static void runJobs(int threadCount, WavePatterns[] newWP) {
		ExecutorService threadPool = Executors.newFixedThreadPool(threadCount);
		Set<Future<WavePatterns>> futures = new HashSet<Future<WavePatterns>>(threadCount);
		for (int i = 0; i < threadCount; ++i) {
		    Future<WavePatterns> f = threadPool.submit(newWP[i]);
		    futures.add(f);
		}
		threadPool.shutdown();
		try {
			threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**Initialise wave patterns for multithread jobs **/
	protected static WavePatterns[] initWavePatterns(WavePatterns wavePatterns,
			int threadCount, Class<? extends WavePatterns> type, File[][] splits) {
		WavePatterns[] newWP = new WavePatterns[threadCount];
		// Create jobs 
		for (int i = 0; i < threadCount; ++i) {
			try {
				newWP[i] = (WavePatterns) type.newInstance();
				newWP[i].filePath = wavePatterns.filePath;
				newWP[i].files = splits[i];
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return newWP;
	}

	
	



}
