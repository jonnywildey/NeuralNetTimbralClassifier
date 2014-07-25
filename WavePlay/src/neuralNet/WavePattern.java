package neuralNet;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import filemanager.CSVString;
import riff.Wave;

public class WavePattern extends Pattern{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1749577429354984802L;
	public Wave wave;
	public File filePath;

	public WavePattern(Wave wave,ArrayList<Double> DoubleArray,
			ArrayList<Double> targetArray, Integer id) {
		super(DoubleArray, targetArray, id);
		this.wave = wave;
		this.filePath = wave.filepath;
	}

	public Wave getWave() {
		return wave;
	}
	
	

	public void setWave(Wave wave) {
		this.wave = wave;
		this.filePath = wave.filepath;
	}

	public File getFilePath() {
		return filePath;
	}

	public void setFilePath(File filePath) {
		this.filePath = filePath;
	}

	public WavePattern(int id) {
		super(id);
	}
	

}
