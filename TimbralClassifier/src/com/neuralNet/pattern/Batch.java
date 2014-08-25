package com.neuralNet.pattern;

import java.io.File;
import java.util.Random;

import com.DSP.waveAnalysis.FFTBox;
import com.DSP.waveProcess.FFTChain;
import com.DSP.waveProcess.SignalChain;
import com.riff.Signal;
import com.riff.Wave;
import com.util.Serialize;

/**
 * Principally useful utility classes for batch wav processing *.
 *
 * @author Jonny Wildey
 * @version 1.0
 */
public class Batch {

	/**
	 * Instantiates a new batch.
	 */
	public Batch() {
	}

	/**
	 * Get waves from a directory *.
	 *
	 * @param dir the dir
	 * @return the wavs
	 */
	public static Wave[] getWavs(File dir) {
		File[] files = Serialize.getActualFiles(dir);
		Wave[] waves = new Wave[files.length];
		for (int i = 0; i < files.length; ++i) {
			waves[i] = new Wave(files[i]);
		}
		return waves;
	}
	
	/**
	 * Performs batch processing of all the audio files in a directory.
	 * For monotimbral processing *
	 *
	 * @param sampleDir the sample dir
	 * @param newDir the new dir
	 */
	protected static void batchWavProcessMono(File sampleDir, File newDir) {
		batchWavProcess(sampleDir, newDir, true);
	}
	
	/**
	 * Performs batch processing of all the audio files in a directory.
	 * For polytimbral processing *
	 *
	 * @param sampleDir the sample dir
	 * @param newDir the new dir
	 */
	protected static void batchWavProcessPoly(File sampleDir, File newDir) {
		batchWavProcess(sampleDir, newDir, false);
	}
	
	/**
	 * Performs batch processing of all the audio files in multiple directories.
	 * For monotimbral processing *
	 *
	 * @param batch the batch
	 * @param dirs the dirs
	 */
	public static void batchWavProcessMono(File batch, File...dirs) {
		for (File f : dirs) {
			Batch.batchWavProcess(f, batch, true);
		}
	}
	
	/**
	 * Performs batch processing of all the audio files in multiple directories.
	 * For polytimbral processing *
	 *
	 * @param batch the batch
	 * @param dirs the dirs
	 */
	public static void batchWavProcessPoly(File batch, File...dirs) {
		for (File f : dirs) {
			Batch.batchWavProcess(f, batch, false);
		}
	}
	
	/**
	 * Performs batch processing of all the audio files in a directory *.
	 *
	 * @param sampleDir the sample dir
	 * @param newDir the new dir
	 * @param mono the mono
	 */
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
