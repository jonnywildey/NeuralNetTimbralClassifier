package com.neuralNet.pattern;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import javax.naming.InvalidNameException;

import com.DSP.waveAnalysis.FFTBox;
import com.DSP.waveAnalysis.FrameFFT;
import com.DSP.waveProcess.FFTChain;
import com.DSP.waveProcess.SignalChain;
import com.neuralNet.layers.InputShell;
import com.riff.Chunk;
import com.riff.InfoChunk;
import com.riff.Signal;
import com.riff.Wave;
import com.util.ArrayMethods;
import com.util.Log;
import com.util.fileReading.CSVString;

/**
 * Extension of the pattern class with some Wave specific utilities
 * 
 */
public class WavePattern extends Pattern implements Serializable {

	private static final long serialVersionUID = -1749577429354984802L;
	/** Adds meta data to a Wave file **/
	public static Wave addMetaData(Signal signal, FFTBox fftData, String type,
			String input) {
		Chunk fftChunk = getFFTData(fftData);
		Chunk metaChunk = new Chunk();
		Chunk artist = new Chunk();
		InfoChunk infoChunk = new InfoChunk();
		Wave wav = new Wave();
		wav.setSignal(signal);
		try {
			artist.setName("IART");
			artist.setData("Jonny Wildey");
			metaChunk.setName(type);
			metaChunk.setData(input);
			infoChunk.addChunk(artist);
			infoChunk.addChunk(metaChunk);
			infoChunk.addChunk(fftChunk);
			wav.addChunk(infoChunk);
			// Log.d(infoChunk.toStringRecursive());
		} catch (InvalidNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return wav;
	}

	/** Convert arraylist of wave patterns to array of wave patterns **/
	public static WavePattern[] arrayListToArray(
			ArrayList<WavePattern> wavePatterns) {
		WavePattern[] wp = new WavePattern[wavePatterns.size()];
		for (int i = 0; i < wp.length; ++i) {
			wp[i] = wavePatterns.get(i);
		}
		return wp;
	}

	/** Get data chunk from Wave **/
	public static String getDataChunk(Wave wave) {
		String str = "";
		try {
			// get inputs
			str = new String(wave.getSubChunk("LIST").getSubChunk("IAS7")
					.getData(), "UTF-8");
		} catch (Exception e) {
			Log.d(e);
		}
		return str;
	}

	/** Attaches fft analysis to data chunk **/
	public static Chunk getFFTData(FFTBox fftData) {
		// 4096 works well
		try {
			String str = ArrayMethods.toString(fftData.getTable());
			Chunk chunk = new Chunk();
			chunk.setName("IAS7");
			chunk.setData(str);
			return chunk;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/** Get inputs from CSV row **/
	public static ArrayList<InputShell> getInputs(String str) {
		CSVString s = new CSVString(str);
		s.readFile();
		return Pattern.doubleToInputShell(s.makeDoubleArray()[1]);
	}

	/**
	 * Get the output from a Wave
	 * 
	 * @throws Exception
	 **/
	public static String getInstrumentalOutputs(Wave wave) throws Exception {
		try {
			return new String(wave.getSubChunk("LIST").getSubChunk("IAS8")
					.getData(), "UTF-8");
		} catch (Exception e) {
			// Log.d(e);
			throw new Exception();
		}
	}



	public String instrument; // The instrument the Wave represents

	/** Default constructor class. Try and avoid **/
	public WavePattern() {
		super();
	}

	/** Full parameter constructor **/
	public WavePattern(File waveFile, ArrayList<Double> DoubleArray,
			ArrayList<Double> targetArray, Integer id) {
		super(DoubleArray, targetArray, id);
		this.filePath = waveFile;
	}

	/** Id constructor **/
	public WavePattern(int id) {
		super(id);
	}

	/** id and Wave constructor **/
	public WavePattern(int id, Wave wave) {
		this(id);
		this.filePath = wave.getFilePath();
	}

	/** Full parameter constructor **/
	public WavePattern(Wave wave, ArrayList<Double> DoubleArray,
			ArrayList<Double> targetArray, Integer id) {
		super(DoubleArray, targetArray, id);
		this.filePath = wave.filepath;
	}

	/**
	 * Adds instrumental data from fftbox. Assumes fft box is correctly
	 * processed
	 */
	public void addInputData(FFTBox fftBox) {
		this.setInputArray(WavePattern.doubleToInputShell(fftBox.getValues()[0]));
	}

	/** Get instrument **/
	public String getInstrument() {
		return instrument;
	}

	/** Creates a wave object based on the filepath **/
	public Wave getWave() {
		Wave wave = new Wave(this.filePath);
		wave.readFile();
		return wave;
	}

	/** Set instrument **/
	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	/** Set the Wave **/
	public void setWave(File filePath) {
		this.filePath = filePath;
		;
	}

	/** Set the Wave **/
	public void setWave(Wave wave) {
		this.filePath = wave.filepath;
	}
	
	/** Generate a Wave Pattern from the Wave using the original representation **/
	public static WavePattern getOriginalMonoRepresentation(Signal signal, int id) {
		WavePattern wavePattern = new WavePattern(id);
		FFTBox fftBox = FFTChain.monoFFTChain(signal);
		wavePattern.addInputData(fftBox);
		return wavePattern;
	}
	/** Generate a Wave Pattern from the Wave using the temporal representation **/
	public static WavePattern getTempRepresentation(Signal signal, int id) {
		WavePattern wavePattern = new WavePattern(id);
		FFTBox fftBox = FFTChain.polyFFTChainSplit(signal);
		wavePattern.addInputData(fftBox);
		return wavePattern;
	}
	
	/** Generate a Wave Pattern from the Wave using the original representation **/
	public static WavePattern getOriginalPolyRepresentation(Signal signal, int id) {
		WavePattern wavePattern = new WavePattern(id);
		FFTBox fftBox = FFTChain.polyFFTChain(signal);
		wavePattern.addInputData(fftBox);
		return wavePattern;
	}

}
