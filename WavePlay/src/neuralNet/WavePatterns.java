package neuralNet;

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
public class WavePatterns implements Serializable, Callable<WavePatterns> {
	
	private static final long serialVersionUID = 8265438349914627431L;
	public File filePath;
	public WavePattern[] patterns;
	private String[] instruments;
	//
	public File[] files;
	public String[] instrs;
	
	
	
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
			//Log.d(p.getInstrument());
		}
	}
	
	protected File[] getFilesFromDirectory() {
		return Serialize.getActualFiles(this.filePath);
	}
	
	
	/** turns a set of waves with metadata to a pattern **/
	public void wavMetaToPattern() {
		//patterns
		this.files = getFilesFromDirectory();
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
		getOutputs(instrs);
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
		getOutputs(instrs);
	}
	
	/** Find waves, analyse, do more **/
	public void wavReFFTBatchPattern(int count, File[] files) {
		//randoms
		Random pitchRand = new Random();
		Random noiseRand = new Random();
		Random hpRand = new Random();
		Random lpRand = new Random();
		//patterns
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
				patterns[i * count + j].instrument = instrs[i * count + j];
			}
		}
		this.instrs = instrs;
		getOutputs(instrs);
	}



	private ArrayList<InputShell> reFFT(Signal signals) {
		FrameFFT fft = new FrameFFT(signals, 4096);
		FFTBox dd = fft.analyse(20, 20000);
		//fft.makeGraph();
		//dd = FrameFFT.getExponentTable(dd, 0.78); //rate
		dd = FFTBox.getSumTable(dd, 7);
		dd = FFTBox.getHiResBarkedSubset(dd, 0.5);
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
	private void getOutputs(String[] instrs) {
		// convert to bitarray
		String[][] targets = NNUtilities.getCount(instrs, true);
		this.instruments = targets[0];
		double[][] bits = NNUtilities.createUniqueBits(targets.length);
		//convert back
		for (int i = 0; i < this.patterns.length; ++i) {
			for (int j = 0; j < targets.length; ++j) {
				if (instrs[i].equals(targets[j][0])) {
					
					patterns[i].targetArray = ArrayMethods.doubleToArrayList(bits[j]);
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
				np[c].id = c;
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
		this.wavReFFTBatchPattern(15, this.files);
		return this;
	}
	
	/** Threadable version of analysis. Uses call method for analysis **/
	public static WavePatterns genWaves(WavePatterns wavePatterns, int threadCount) {
		wavePatterns.files = Serialize.getActualFiles(wavePatterns.filePath);
		File[][] splits = splitFiles(wavePatterns.files, threadCount);
		WavePatterns[] newWP = new WavePatterns[threadCount];
		for (int i = 0; i < threadCount; ++i) {
			newWP[i] = new WavePatterns(wavePatterns.filePath);
			newWP[i].files = splits[i];
		}
		//job
		ExecutorService threadPool = Executors.newFixedThreadPool(threadCount);
		Set<Future<WavePatterns>> futures = new HashSet<Future<WavePatterns>>(threadCount);
		for (int i = 0; i < threadCount; ++i) {
		    Future<WavePatterns> f = threadPool.submit(newWP[i]);
		    // save the future if necessary in a collection or something
		    futures.add(f);
		}
		threadPool.shutdown();
		try {
			threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WavePatterns comb = WavePatterns.combinePatterns(newWP);
		return comb;
	}

	
	



}
