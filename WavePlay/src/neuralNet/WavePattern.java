package neuralNet;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import filemanager.CSVString;
import filemanager.Log;
import riff.Wave;

/** Extension of the pattern class with functions for relating the 
 * pattern to the Wave file it came from
 *
 */
public class WavePattern extends Pattern implements Serializable{
	
	private static final long serialVersionUID = -1749577429354984802L;
	public String instrument;
	
	/**Basic constructor **/
	public WavePattern(Wave wave,ArrayList<Double> DoubleArray,
			ArrayList<Double> targetArray, Integer id) {
		super(DoubleArray, targetArray, id);
		//this.wave = wave; //removed this for now as it takes ages to serialise
		this.filePath = wave.filepath;
	}
	
	/** Creates a wave object based on the filepath **/
	public Wave getWave() {
		Wave wave = new Wave(this.filePath);
		wave.readFile();
		return wave;
	}
	
	

	public void setWave(Wave wave) {
		this.filePath = wave.filepath;
	}
	
	public void setWave(File filePath) {
		this.filePath = filePath;;
	}

	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	/**Convert arraylist of wave patterns to array of wave patterns **/
	public static WavePattern[] arrayListToArray(ArrayList<WavePattern> wavePatterns) {
		WavePattern[] wp = new WavePattern[wavePatterns.size()];
		for (int i = 0; i < wp.length; ++i) {
			wp[i] = wavePatterns.get(i);
		}
		return wp;
	}

	public WavePattern(int id) {
		super(id);
	}

	public WavePattern(int id, Wave wave) {
		this(id);
		this.filePath = wave.getFilePath();
	}
	

}
