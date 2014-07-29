package neuralNet;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import filemanager.CSVString;
import filemanager.Log;
import riff.Wave;

public class WavePattern extends Pattern{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1749577429354984802L;
	public Wave wave;
	public WavePattern(Wave wave,ArrayList<Double> DoubleArray,
			ArrayList<Double> targetArray, Integer id) {
		super(DoubleArray, targetArray, id);
		//this.wave = wave; //removed this for now as it takes ages to serialise
		this.filePath = wave.filepath;
	}

	public Wave getWave() {
		return wave;
	}
	
	

	public void setWave(Wave wave) {
		this.wave = wave;
		this.filePath = wave.filepath;
	}

	public WavePattern(int id) {
		super(id);
	}

	public WavePattern(int id, Wave wave) {
		this(id);
		this.filePath = wave.getFilePath();
		this.wave = wave;
	}
	

}
