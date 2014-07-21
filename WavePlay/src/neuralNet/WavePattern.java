package neuralNet;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import filemanager.CSVString;
import riff.Wave;

public class WavePattern extends Pattern{
	
	public Wave wave;

	public WavePattern(Wave wave,ArrayList<Double> DoubleArray,
			ArrayList<Double> targetArray, Integer id) {
		super(DoubleArray, targetArray, id);
		this.wave = wave;
	}

	public Wave getWave() {
		return wave;
	}
	
	

	public void setWave(Wave wave) {
		this.wave = wave;
	}

	public WavePattern(int id) {
		super(id);
	}
	

}
