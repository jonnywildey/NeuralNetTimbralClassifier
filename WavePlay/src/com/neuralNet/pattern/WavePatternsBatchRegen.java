package com.neuralNet.pattern;

import java.io.File;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.riff.Signal;
import com.riff.Wave;
import com.util.Serialize;
import com.waveProcess.SignalChain;

public class WavePatternsBatchRegen extends WavePatterns{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 202416054125154835L;
	public int regenCount;
	
	@Override
	public WavePatterns call() throws Exception {
		this.wavReFFTBatchPattern(regenCount, this.files);
		return this;
	}
	
	public WavePatternsBatchRegen() {
		super();
	}

	public int getRegenCount() {
		return regenCount;
	}

	public void setRegenCount(int regenCount) {
		this.regenCount = regenCount;
	}

	public WavePatternsBatchRegen(File filePath) {
		super(filePath);
	}

	/** Find waves, analyse, generate variations, analyse. **/
	public void wavReFFTBatchPattern(int count, File[] files) {
		//randoms
		Random pitchRand = new Random();
		Random noiseRand = new Random();
		Random hpRand = new Random();
		Random lpRand = new Random();
		//patterns
		this.patterns = new WavePattern[files.length * count];
		String[] instrs = new String[files.length * count];
		Wave wave = null;
		Signal signal = null;
		for (int i = 0; i < files.length; ++i) {
			//read
			wave = new Wave(files[i]);
			signal = wave.getSignals();
			for (int j = 0; j < count; ++j) {
				patterns[i * count + j] = new WavePattern(i, wave); //make pattern
				patterns[i * count + j].setInputArray(reFFT(
						SignalChain.processSignalChain(pitchRand, noiseRand, hpRand, lpRand, signal)));
				//get instrumental outputs
				instrs[i * count + j] = WavePattern.getInstrumentalOutputs(wave);
				patterns[i * count + j].instrument = instrs[i * count + j];
			}
		}
		this.instrs = instrs;
		getOutputs(instrs);
	}
	
	/** Threadable version of analysis. Uses call method for analysis **/
	public static WavePatterns genWaves(WavePatterns wavePatterns, int threadCount, int regenCount) {
		Class<? extends WavePatterns> type = wavePatterns.getClass();
		wavePatterns.files = Serialize.getActualFiles(wavePatterns.filePath);
		File[][] splits = splitFiles(wavePatterns.files, threadCount);
		//create Wave Patterns
		WavePatterns[] newWP = initWavePatterns(wavePatterns, threadCount,
				type, splits, regenCount);
		//job
		runJobs(threadCount, newWP);
		WavePatterns comb = WavePatterns.combinePatterns(newWP);
		return comb;
	}
	
	
	/**Initialise wave patterns for multithread jobs **/
	protected static WavePatterns[] initWavePatterns(WavePatterns wavePatterns,
			int threadCount, Class<? extends WavePatterns> type, File[][] splits, int regenCount) {
		WavePatternsBatchRegen[] newWP = new WavePatternsBatchRegen[threadCount];
		// Create jobs 
		for (int i = 0; i < threadCount; ++i) {
			try {
				newWP[i] = (WavePatternsBatchRegen) type.newInstance();
				newWP[i].filePath = wavePatterns.filePath;
				newWP[i].files = splits[i];
				newWP[i].regenCount = regenCount;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return newWP;
	}
}
