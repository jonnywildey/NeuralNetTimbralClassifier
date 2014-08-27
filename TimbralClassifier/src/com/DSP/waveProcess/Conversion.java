package com.DSP.waveProcess;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

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
		Log.setFilePath(new File("/Users/Jonny/Documents/Timbre/Logs/WaveCreate.log"));
		Long start = System.currentTimeMillis();
		File comb5 = new File("/Users/Jonny/Documents/Timbre/JSON/WavePatterns/Poly/");
		File out = new File("/Users/Jonny/Documents/Timbre/JSON/WavePatterns/Poly/SplitComb");
		File c1 = new File("/Volumes/KINGSTON/Timbre/Samples/Combine/Comb1");
		File c2 = new File("/Volumes/KINGSTON/Timbre/Samples/Combine/Comb2");
		File files[] = Serialize.getDirectories(c1);
		//File c2 = new File("/Volumes/KINGSTON/Timbre/Samples/Combine/Comb6");

		//WavePatterns wp = GenerateWavePatterns.regeneratePatternsSplit(c1,
		//		new File(out.getAbsolutePath() + 4 + ".json"));
		//WavePatterns wp2 = GenerateWavePatterns.regeneratePatternsSplit(c2,
		//		new File(out.getAbsolutePath() + 5 + ".json"));
		
		//WavePatterns wp = Combine.combineFromJSONs(comb5);
		//Log.d("Combined");
		
		//Serialize.writeJSON(wp, comb6);

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
