package com.waveProcess;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import com.neuralNet.RunNetwork;
import com.neuralNet.pattern.Batch;
import com.neuralNet.pattern.Combine;
import com.neuralNet.pattern.GenerateWavePatterns;
import com.neuralNet.pattern.Pattern;
import com.neuralNet.pattern.WavePatterns;
import com.riff.*;
import com.util.ArrayMethods;
import com.util.Log;
import com.waveAnalysis.FFTBox;
import com.waveAnalysis.FrameFFT;

/** Class for transforming samples and preparing them for neural net **/
public class Conversion {
	
	public static void main(String[] args) {
		/*Log.setFilePath(new File("/Users/Jonny/Documents/Timbre/Logs/WaveCreate.log"));
		Long start = System.currentTimeMillis();
		File[] batchDirs = {new File("/Users/Jonny/Documents/Timbre/Samples/Cello"),
				new File("/Users/Jonny/Documents/Timbre/Samples/Harp"),
				new File("/Users/Jonny/Documents/Timbre/Samples/Marimba"),
				new File("/Users/Jonny/Documents/Timbre/Samples/Trombone")};
		File batchFolder = new File("/Users/Jonny/Documents/Timbre/Samples/Batch");
		File combineDir = new File("/Users/Jonny/Documents/Timbre/Samples/Comb1");
		//batchFromFolders(batchFolder, batchFiles); 
		//batchCombine(combineDir, batchDirs);
		File waveSerial = new File("/Users/Jonny/Documents/Timbre/WaveLOADSLOADS5.ser");
		WavePatterns wp = new WavePatterns(new File("/Volumes/KINGSTON/Timbre/Samples/Combine/Comb5"));
		WavePatterns wavePatterns = regenerateAndBatchPatterns(wp, waveSerial);
		Log.d("time spent: " + ((System.currentTimeMillis() - start) / 1000d) + " seconds"); */
		File combineDir = new File("/Users/Jonny/Documents/Timbre/Serial/WavePatterns/PolyTimbre/Separate");
		File waveSerial = new File("/Users/Jonny/Documents/Timbre/Serial/WavePatterns/PolyTimbre/GeneratedCombine.ser");
		Combine.combineAndSerialize(waveSerial, combineDir);
		/*
		WavePatterns wp5 = (WavePatterns) Serialize.getFromSerial(
				"/Users/Jonny/Documents/Timbre/Serial/WavePatterns/PolyTimbre/Batch40Input7FrameNorm10.ser");
		
		Log.d("serialised");
		Log.d(+ wp5.getPatterns().length);
		WavePatterns wp1 = (WavePatterns) Serialize.getFromSerial(
				"/Users/Jonny/Documents/Timbre/Serial/WavePatterns/PolyTimbre/Separate/WaveSep1.ser");
		Log.d("serialised" + + wp1.getPatterns().length);
		WavePatterns wp2 = (WavePatterns) Serialize.getFromSerial(
				"/Users/Jonny/Documents/Timbre/Serial/WavePatterns/PolyTimbre/Separate/WaveSep2.ser");
		Log.d("serialised" + + wp2.getPatterns().length);
		WavePatterns wp3 = (WavePatterns) Serialize.getFromSerial(
				"/Users/Jonny/Documents/Timbre/Serial/WavePatterns/PolyTimbre/Separate/WaveSep3.ser");
		Log.d("serialised" + wp3.getPatterns().length);
		WavePatterns wp4 = (WavePatterns) Serialize.getFromSerial(
				"/Users/Jonny/Documents/Timbre/Serial/WavePatterns/PolyTimbre/Separate/WaveSep4.ser");
		Log.d("serialised" + wp4.getPatterns().length);
		
		WavePatterns wp = WavePatterns.combinePatterns(wp1,wp2,wp3,wp4,wp5); */
		

		


		// ...
		
		// ...
		
		/*Log.setFilePath(new File("/Users/Jonny/Documents/Timbre/Logs/WaveCreate.log"));
		Long start = System.currentTimeMillis();
		File[] batchDirs = {new File("/Users/Jonny/Documents/Timbre/Samples/Cello"),
				new File("/Users/Jonny/Documents/Timbre/Samples/Harp"),
				new File("/Users/Jonny/Documents/Timbre/Samples/Marimba"),
				new File("/Users/Jonny/Documents/Timbre/Samples/Trombone")};
		File batchFolder = new File("/Volumes/KINGSTON/Timbre/Samples/Batch");
		File combineDir = new File("/Volumes/Rickay/Timbre/Combine");
		File waveSerial = new File("/Users/Jonny/Documents/Timbre/Serial/WavePatterns/PolyTimbre/Batch40Input7FrameNorm10.ser");
		GenerateWavePatterns.regeneratePatterns(combineDir, waveSerial);
		
		GenerateWavePatterns.regenerateAndBatchPatterns(
				new File("/Volumes/KINGSTON/Timbre/Samples/Combine/Comb6"), 
				new File("/Users/Jonny/Documents/Timbre/WaveLOADSLOADS6.ser"), 15);
		GenerateWavePatterns.regenerateAndBatchPatterns(
				new File("/Volumes/KINGSTON/Timbre/Samples/Combine/Comb5"), 
				new File("/Users/Jonny/Documents/Timbre/WaveLOADSLOADS5.ser"), 15);

		Log.d("time spent: " + ((System.currentTimeMillis() - start) / 1000d) + " seconds"); */
		
		
	}
	
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
