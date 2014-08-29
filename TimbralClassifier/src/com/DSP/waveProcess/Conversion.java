package com.DSP.waveProcess;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Random;

import com.DSP.waveAnalysis.FFTBox;
import com.DSP.waveAnalysis.FrameFFT;
import com.neuralNet.RunNetwork;
import com.neuralNet.layers.InputShell;
import com.neuralNet.pattern.Batch;
import com.neuralNet.pattern.Combine;
import com.neuralNet.pattern.GenerateWavePatterns;
import com.neuralNet.pattern.Pattern;
import com.neuralNet.pattern.WavePatterns;
import com.riff.*;
import com.util.ArrayMethods;
import com.util.Log;
import com.util.Serialize;

/**
 * Class for transforming samples and preparing them for neural net *.
 *
 * @author Jonny Wildey
 * @version 1.0
 */
public class Conversion {
	
	public static void main(String[] args) {
		Log.setFilePath(new File("/Users/jonnywildey/git/NeuralNetTimbralClassifier/assets/log/WaveCreate.log"));
		Long start = System.currentTimeMillis();
		File comb5 = new File("/Users/Jonny/Documents/Timbre/JSON/WavePatterns/Poly/");
		File out = new File("/Users/Jonny/Documents/Timbre/JSON/WavePatterns/Poly/SplitComb");
		File c1 = new File("/Volumes/KINGSTON/Timbre/Samples/Batch");
		File c4 = new File("/Volumes/KINGSTON/Timbre/Samples/BatchNoise");
		File c2 = new File("/Users/jonnywildey/git/NeuralNetTimbralClassifier/assets/json/BatchNoise");
		File c3 = new File("/Users/jonnywildey/git/NeuralNetTimbralClassifier/assets/json/BatchNoiseComb.json");
		File files[] = Serialize.getDirectories(c4);
		//File c2 = new File("/Volumes/KINGSTON/Timbre/Samples/Combine/Comb6");
		WavePatterns[] patterns = new WavePatterns[files.length];
		for (int i = 0; i < files.length; ++i) {
			Log.d("files " + files[i].getName());
		patterns[i] = GenerateWavePatterns.regeneratePatternsMono(files[i],
				new File(c2.getAbsolutePath() + i + ".json"));
		}
		WavePatterns wp = WavePatterns.combinePatterns(patterns);
		Serialize.writeJSON(wp, c3);
		//WavePatterns wp = GenerateWavePatterns.regeneratePatterns(c1, c3);
		Random r = new Random();
//		for (File f : Serialize.getActualFiles(c1)) {
//			Wave wav = new Wave(f);
//			Signal s = wav.getSignals();
//			s = SignalChain.addNoise(s, r, -50);
//			Wave nw = new Wave(s);
//			nw.writeFile(new File(c4.getAbsolutePath() + "/" + f.getName()));
//		}
		//Log.d("Combined");
		
		//Serialize.writeJSON(wp, c3);

	}
	
	/**
	 * Signal to pattern mono.
	 *
	 * @param s the s
	 * @return the pattern
	 */
	public static Pattern signalToPatternMono(Signal s) {
		FrameFFT fft = new FrameFFT(s, 4096);
		FFTBox dd = fft.analyse(20, 20000);
		dd = FFTBox.getSumTable(dd, 10);
		dd = FFTBox.getBarkedSubset(dd);
		dd = FFTBox.normaliseTable(dd, 10);
		double[] arr = {0,0,0,1};
		Pattern p = new Pattern(ArrayMethods.doubleToArrayList(dd.getValues()[0]), ArrayMethods.doubleToArrayList(arr), 1);
		return p;
	}
	
	
	
}
