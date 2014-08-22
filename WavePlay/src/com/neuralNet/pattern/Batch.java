package com.neuralNet.pattern;

import java.io.File;
import java.util.Random;
import com.riff.Signal;
import com.riff.Wave;
import com.util.Serialize;
import com.waveAnalysis.FFTBox;
import com.waveProcess.FFTChain;
import com.waveProcess.SignalChain;

/** Principally useful utility classes for batch wav processing **/
public class Batch {

	public Batch() {
	}

	/** Get waves from a directory **/
	public static Wave[] getWavs(File dir) {
		File[] files = Serialize.getActualFiles(dir);
		Wave[] waves = new Wave[files.length];
		for (int i = 0; i < files.length; ++i) {
			waves[i] = new Wave(files[i]);
		}
		return waves;
	}
	
	/** Performs batch processing of all the audio files in a directory.
	 * For monotimbral processing **/
	protected static void batchWavProcessMono(File sampleDir, File newDir) {
		batchWavProcess(sampleDir, newDir, true);
	}
	
	/** Performs batch processing of all the audio files in a directory.
	 * For polytimbral processing **/
	protected static void batchWavProcessPoly(File sampleDir, File newDir) {
		batchWavProcess(sampleDir, newDir, false);
	}
	
	/** Performs batch processing of all the audio files in multiple directories.
	 * For monotimbral processing **/
	public static void batchWavProcessMono(File batch, File...dirs) {
		for (File f : dirs) {
			Batch.batchWavProcess(f, batch, true);
		}
	}
	
	/** Performs batch processing of all the audio files in multiple directories.
	 * For polytimbral processing **/
	public static void batchWavProcessPoly(File batch, File...dirs) {
		for (File f : dirs) {
			Batch.batchWavProcess(f, batch, false);
		}
	}
	
	/** Performs batch processing of all the audio files in a directory **/
	protected static void batchWavProcess(File sampleDir, File newDir, boolean mono) {
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
				FFTBox fftBox = null;
				if (mono) {
					fftBox = FFTChain.monoFFTChain(s1);
				} else {
					fftBox = FFTChain.polyFFTChain(s1);
				}
				Wave newWave = WavePattern.addMetaData(s1,fftBox, "IAS8", instrument);			
				newWave.writeFile(new File(newDir.getAbsolutePath() + 
						"/" + instrument + i + nf.getName()));
			}
		}
	}

}
