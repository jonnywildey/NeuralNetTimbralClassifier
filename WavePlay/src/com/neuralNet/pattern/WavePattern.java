package com.neuralNet.pattern;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.naming.InvalidNameException;

import com.neuralNet.layers.InputShell;
import com.riff.Chunk;
import com.riff.InfoChunk;
import com.riff.Signal;
import com.riff.Wave;
import com.util.ArrayMethods;
import com.util.Log;
import com.util.fileReading.CSVString;
import com.waveAnalysis.FFTBox;
import com.waveProcess.Conversion;

/** Extension of the pattern class with some Wave specific utilities
 *
 */
public class WavePattern extends Pattern implements Serializable{
	
	private static final long serialVersionUID = -1749577429354984802L;
	public String instrument; //The instrument the Wave represents
	
	
	/**Default constructor class. Try and avoid **/
	public WavePattern() {
		super();
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
	
	/**Full parameter constructor **/
	public WavePattern(Wave wave,ArrayList<Double> DoubleArray,
			ArrayList<Double> targetArray, Integer id) {
		super(DoubleArray, targetArray, id);
		this.filePath = wave.filepath;
	}
	
	/**Full parameter constructor **/
	public WavePattern(File waveFile,ArrayList<Double> DoubleArray,
			ArrayList<Double> targetArray, Integer id) {
		super(DoubleArray, targetArray, id);
		this.filePath = waveFile;
	}
	
	/** Creates a wave object based on the filepath **/
	public Wave getWave() {
		Wave wave = new Wave(this.filePath);
		wave.readFile();
		return wave;
	}
	
	/** Set the Wave **/
	public void setWave(Wave wave) {
		this.filePath = wave.filepath;
	}
	
	/** Set the Wave **/
	public void setWave(File filePath) {
		this.filePath = filePath;;
	}

	/** Get instrument **/
	public String getInstrument() {
		return instrument;
	}
	
	/**Set instrument **/
	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	/** Attaches fft analysis to data chunk **/
	public static Chunk getFFTData(FFTBox fftData) {
		//4096 works well
		try {
			String str = ArrayMethods.toString(fftData.getTable());
			Chunk chunk = new Chunk();
			chunk.setName("IAS7");
			chunk.setData(str);
			return chunk;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/** Adds meta data to a Wave file **/
	public static Wave addMetaData(Signal signal, FFTBox fftData, String type, String input) {
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
			//Log.d(infoChunk.toStringRecursive());
		} catch (InvalidNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return wav;
	}

	/** Get data chunk from Wave **/
	protected static String getDataChunk(Wave wave) {
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

	/** Get the output from a Wave **/
	protected static String getInstrumentalOutputs(Wave wave) {
		try {
		return new String(wave.getSubChunk("LIST").
				getSubChunk("IAS8").getData(), "UTF-8");
		} catch (Exception e) {
			Log.d(e);
			return null;
		}
	}

	/** Get inputs from CSV row **/
	protected static ArrayList<InputShell> getInputs(String str) {
		CSVString s = new CSVString(str);
		s.readFile();
		return Pattern.doubleToInputShell(
				s.makeDoubleArray()[1]);
	}

	/**Convert arraylist of wave patterns to array of wave patterns **/
	public static WavePattern[] arrayListToArray(ArrayList<WavePattern> wavePatterns) {
		WavePattern[] wp = new WavePattern[wavePatterns.size()];
		for (int i = 0; i < wp.length; ++i) {
			wp[i] = wavePatterns.get(i);
		}
		return wp;
	}

	

	

}
