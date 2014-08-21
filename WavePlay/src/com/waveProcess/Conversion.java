package com.waveProcess;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Random;

import com.neuralNet.RunNetwork;
import com.neuralNet.pattern.GenerateWavePatterns;
import com.neuralNet.pattern.Pattern;
import com.neuralNet.pattern.WavePattern;
import com.riff.*;
import com.util.ArrayMethods;
import com.util.Log;
import com.util.Serialize;
import com.waveAnalysis.FFTBox;
import com.waveAnalysis.FrameFFT;
import com.waveAnalysis.SampleRateException;

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
		
		
		/*WavePatterns wp1 = (WavePatterns) Serialize.getFromSerial("/Users/Jonny/Documents/Timbre/WaveLOADSLOADS1.ser");
		Log.d("serialised");
		WavePatterns wp2 = (WavePatterns) Serialize.getFromSerial("/Users/Jonny/Documents/Timbre/WaveLOADSLOADS2.ser");
		Log.d("serialised");
		WavePatterns wp3 = (WavePatterns) Serialize.getFromSerial("/Users/Jonny/Documents/Timbre/WaveLOADSLOADS3.ser");
		Log.d("serialised");
		WavePatterns wp4 = (WavePatterns) Serialize.getFromSerial("/Users/Jonny/Documents/Timbre/WaveLOADSLOADS4.ser");
		Log.d("serialised");
		WavePatterns wp5 = (WavePatterns) Serialize.getFromSerial("/Users/Jonny/Documents/Timbre/WaveLOADSLOADS5.ser");
		Log.d("serialised");
		WavePatterns wp = WavePatterns.combinePatterns(wp1,wp2,wp3,wp4,wp5); */
		

		


		// ...
		
		// ...
		
		Log.setFilePath(new File("/Users/Jonny/Documents/Timbre/Logs/WaveCreate.log"));
		Long start = System.currentTimeMillis();
		File[] batchDirs = {new File("/Users/Jonny/Documents/Timbre/Samples/Cello"),
				new File("/Users/Jonny/Documents/Timbre/Samples/Harp"),
				new File("/Users/Jonny/Documents/Timbre/Samples/Marimba"),
				new File("/Users/Jonny/Documents/Timbre/Samples/Trombone")};
		File batchFolder = new File("/Volumes/KINGSTON/Timbre/Samples/Batch");
		File combineDir = new File("/Volumes/Rickay/Timbre/Combine");
		File waveSerial = new File("/Users/Jonny/Documents/Timbre/Serial/WavePatterns/PolyTimbre/Batch40Input7FrameNorm10.ser");
		GenerateWavePatterns.regeneratePatterns(combineDir, waveSerial);

		Log.d("time spent: " + ((System.currentTimeMillis() - start) / 1000d) + " seconds");
		
		
	}
	
	/** Performs batch processing of all the audio files in a directory **/
	public static void batchFolder(File sampleDir, File newDir) {
		String instrument = sampleDir.getName();
		File[] files = Serialize.getActualFiles(sampleDir);
		Random pitchRand = new Random();
		Random noiseRand = new Random();
		Random hpRand = new Random();
		Random lpRand = new Random();
		
		for (File nf: files) {
			for (int i = 0; i < 15; ++i) {	
				Wave nw = new Wave(nf);
				Signal s1 = nw.getSignals();
				s1 = SignalChain.processSignalChain(pitchRand, noiseRand, hpRand, lpRand,
						s1);
				FFTBox fftBox = FFTChain.monoFFTChain(s1);
				Wave newWave = WavePattern.addMetaData(s1,fftBox, "IAS8", instrument);			
				newWave.writeFile(new File(newDir.getAbsolutePath() + 
						"/" + instrument + i + nf.getName()));
			}
		}
	}


	/** Get Waves **/
	public static Wave[] getWavs(File dir) {
		File[] files = Serialize.getActualFiles(dir);
		Wave[] waves = new Wave[files.length];
		for (int i = 0; i < files.length; ++i) {
			waves[i] = new Wave(files[i]);
		}
		return waves;
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
	
	
	/**Batch process the given files to the batch folder **/
	public static void batchFromFolders(File batch, File...files) {
		for (File f : files) {
			batchFolder(f, batch);
		}
	}
	
	/** processes and then mixes 2 audio files **/
	public static void batchCombine(File dirToWriteTo, File...dirs) {
		Random pitchRand = new Random();
		Random noiseRand = new Random();
		Random hpRand = new Random();
		Random lpRand = new Random();
		//get patterns from each of the dirs
		String[] instruments = new String[dirs.length];
		for (int i = 0; i < dirs.length; ++i) {
			instruments[i] = dirs[i].getName();
		}
		//start combining
		Wave wWave = null;
		Wave nWave = null;
		for (int i = 0; i < dirs.length; ++i) {
			//get all audio files in directory
			File[] wFiles = Serialize.getActualFiles(dirs[i]); 
			for (int j = 0; j < wFiles.length; ++j) {
				wWave = new Wave(wFiles[j]);
				for (int k = i; k < dirs.length; ++k) {
					File[] nFiles = Serialize.getActualFiles(dirs[k]); 
					for (int l = 0; l < nFiles.length; ++l) {
						nWave = new Wave(nFiles[l]);
						//Modify and combine
						combineWaves(pitchRand, noiseRand, hpRand, lpRand, 
								dirToWriteTo, wWave, nWave);
					}
				}
			}
		}
	}
	

	/**Combines and normalises waves from files **/
	public static void combineWaves(Random pitchRand,
			Random noiseRand, Random hpRand, Random lpRand, File dir, Wave...waves) {
		Signal[] signals = new Signal[waves.length];
		String newName = "";
		String instrument = "";
		String wName = "";
		Wave wave = null;
		for (int i = 0; i < waves.length; ++i) {
			//get signals
			wave = waves[i];
			signals[i] = wave.getSignals();
			signals[i] = SignalChain.processSignalChain(pitchRand,
					noiseRand, hpRand, lpRand, signals[i]);
			//get name of file
			wName = waves[i].getFilePath().getParentFile().getName();
			newName = renameCombine(newName, waves[i]);
			//get instrument name
			if (!wName.equals(instrument)) { //don't want cellocello
				instrument = instrument.concat(wName);
			}
		}
		sumAndWrite(dir, signals, newName, instrument);
	}

	/** Renames a combined file in a logical way **/
	private static String renameCombine(String str, Wave wave) {
		if (str.length() == 0 | str == null) {
			str = wave.getFilePath().getName();
		} else {
			str = str.substring(0, str.length() - 4).concat
					(wave.getFilePath().getName());
		}
		return str;
	}



	/**sum a wave, write metadata and write signals **/
	public static void sumAndWrite(File dir, Signal[] signals, String newName,
			String instrument) {
		Signal comb = null;
		//write the file
		try {
			comb = Gain.sumMono(signals);
			comb = Gain.normalise(comb);
			FFTBox fftBox = FFTChain.polyFFTChain(comb);
			Wave newWave = WavePattern.addMetaData(comb, fftBox, "IAS8", instrument);
			newWave.writeFile(new File(dir.getAbsolutePath() + "/" + newName));
		} catch (SampleRateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}
