package com.DSP.waveAnalysis;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import com.DSP.waveProcess.EQFilter;
import com.DSP.waveProcess.Pitch;
import com.DSP.waveProcess.SignalChain;
import com.neuralNet.Committee;
import com.neuralNet.MultiLayerNet;
import com.neuralNet.MultiNNUtilities;
import com.neuralNet.layers.LayerStructure;
import com.neuralNet.pattern.Combine;
import com.neuralNet.pattern.TestPatterns;
import com.neuralNet.pattern.WavePattern;
import com.neuralNet.pattern.WavePatterns;
import com.riff.Signal;
import com.riff.Wave;
import com.util.Log;
import com.util.Serialize;
import com.util.fileReading.HTML;

public class LiveDemo {
	
	

	public LiveDemo() {
	}
	
	public static void main(String[] args) {
		File tempMonoNoiseJSON = new File(
				"/Users/Jonny/Documents/Timbre/Ghost/Timbre/JSON/Mono/Temporal/Temporal Noise -30db 4815.json");
		File origMonoNoiseJSON = new File(
				"/Users/Jonny/Documents/Timbre/Ghost/Timbre/JSON/Mono/Original/Original 4815.json");
		File tempPolyNoiseJSON = new File(
				"/Users/Jonny/Documents/Timbre/Ghost/Timbre/JSON/Poly/Temporal/Temporal 65271");
		File origPolyNoiseJSON = new File(
				"/Users/Jonny/Documents/Timbre/Ghost/Timbre/JSON/Poly/Original/Original 65293.json");
		File tempMonoComm = new File(
				"/Users/Jonny/Documents/Timbre/Ghost/Timbre/Ser/Mono/Temporal/Temporal4815Noise-30db.ser");
		File origMonoComm = new File(
				"/Users/Jonny/Documents/Timbre/Ghost/Timbre/Ser/Mono/Original/Original 4815 Noise -30db.ser");
		File tempPolyComm = new File (
				"/Users/Jonny/Documents/Timbre/Ghost/Timbre/Ser/Poly/Temporal/SplitBatch150.ser");
		File origPolyComm = new File (
			"/Users/Jonny/Documents/Timbre/Ghost/Timbre/Ser/Poly/Original/Original 65271.ser");
		//useCommittee(origMonoNoiseJSON, origMonoComm);
		//makeCommittee(origPolyNoiseJSON);
		//fftWave();
		//filter();
	    //signalChain();
		//reverse();
		testWaveFiles(tempMonoNoiseJSON, tempMonoComm);
	}

	protected static void makeCommittee(File wavepatterns) {
		int nets = 1;
		int epochs = 5;
		int neurons = 100;
		TestPatterns tp = new TestPatterns(getTestPatterns(wavepatterns));
		Committee committee = MultiNNUtilities.createCommittee(tp, nets, neurons, epochs, true); 
		MultiNNUtilities.testCommittee(tp, committee);
	}
	
	protected static void useCommittee(File wavepatterns, File comm) {
		TestPatterns tp = new TestPatterns(getTestPatterns(wavepatterns));
		Committee committee = Serialize.getFromSerial(comm, Committee.class);
		MultiNNUtilities.testCommittee(tp, committee);
		
	}

	protected static void testWaveFiles(File tempPolyNoiseJSON,
			File tempPolyComm) {
		Committee committee = Serialize.getFromSerial(tempPolyComm, Committee.class);
		WavePatterns wavePatterns = getTestPatterns(tempPolyNoiseJSON);
		boolean out = true;
		while (out) {
			File file = HTML.fileChooser();
			if (file == null) {
				out = false;
			} else {
				Wave testWave = new Wave(file.toString());
				//playSound(file);
				HTML.winDisplay(committee.runWave(testWave, wavePatterns));
			}
			
		}
	}
	
	public static WavePatterns getTestPatterns(File waveFile) {
		WavePatterns wavePatterns;
		if (waveFile.isDirectory()) {
			wavePatterns = Combine.combineFromJSONs(waveFile);
		} else {
			wavePatterns = Serialize.getFromJSON(waveFile, WavePatterns.class);
		}
		return wavePatterns;
	}
	
	public static void fftWave() {
		File file = HTML.fileChooser();
		//playSound(file);
		Wave wave = new Wave(file);
		wave.makeGraph();
		Signal signal = wave.getSignals();
		Log.d(Pitch.getFundamental(signal));
		FrameFFT frame= new FrameFFT(signal);
		frame.analyse();
		frame.makeGraph(20, 20000, 1280, 400);
	}
	
	public static void pitchChange() {
		File file = HTML.fileChooser();
		//playSound(file);
		Wave wave = new Wave(file);
		Signal signal = wave.getSignals();
		signal = Pitch.pitchShift(signal, 12);
		Log.d("Fundamental: " + Pitch.getFundamental(signal) + " hz");
		Wave newWave = new Wave(signal);
		File newFile = new File("temp.wav");
		newWave.writeFile(newFile);
		//playSound(newFile);
	}
	
	public static void filter() {
		File file = HTML.fileChooser();
		//playSound(file);
		Wave wave = new Wave(file);
		Signal signal = wave.getSignals();
		Log.d("Fundamental: " + Pitch.getFundamental(signal) + " hz");
		signal = EQFilter.bandPassFilter(signal, 400, 4);
		
		Wave newWave = new Wave(signal);
		File newFile = new File("temp.wav");
		newWave.writeFile(newFile);
		////playSound(newFile);
	}
	
	public static void reverse() {
		File file = HTML.fileChooser();
		////playSound(file);
		Wave wave = new Wave(file);
		Signal signal = wave.getSignals();
		signal = Pitch.reverse(signal);
		Log.d("Fundamental: " + Pitch.getFundamental(signal) + " hz");
		Wave newWave = new Wave(signal);
		File newFile = new File("temp.wav");
		newWave.writeFile(newFile);
		////playSound(newFile);
	}
	
	public static void signalChain() {
		File file = HTML.fileChooser();
		////playSound(file);
		Wave wave = new Wave(file);
		Signal signal = wave.getSignals();
		signal = SignalChain.processSignalChainNoise(
				new Random(), new Random(), new Random(), new Random(), signal);
		Log.d("Fundamental: " + Pitch.getFundamental(signal) + " hz");
		Wave newWave = new Wave(signal);
		File newFile = new File("temp.wav");
		newWave.writeFile(newFile);
		////playSound(newFile);
	}
	
	public static MultiLayerNet makeNet(TestPatterns testPatterns) {
		MultiLayerNet nn = new MultiLayerNet();
		LayerStructure ls = new LayerStructure(testPatterns);
		nn.setTrainingRate(0.1d);
		nn.setLayerStructure(ls);
		nn.setTestPatterns(testPatterns);
		nn.setDebug(false);
		nn.initialiseNeurons();
		nn.setVerbose(true);
		nn.setAcceptableErrorRate(0.1d);
		nn.setMaxEpoch(100);
		nn.initialiseRandomWeights(12356l);
		nn.setShuffleTrainingPatterns(true, 26487l);
		return nn;
	}
	


}
