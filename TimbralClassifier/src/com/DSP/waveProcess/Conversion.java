package com.DSP.waveProcess;

import java.io.File;

import com.DSP.waveAnalysis.FFT;
import com.DSP.waveAnalysis.FrameFFT;
import com.riff.Signal;
import com.riff.Wave;

/**
 * Class for transforming samples and preparing them for neural net *.
 * 
 * @author Jonny Wildey
 * @version 1.0
 */
public class Conversion {

	public static void main(String[] args) {
		Signal s = Gen.tapeHiss(44100, 0, 44100, 24);
		FFT fft = new FFT(s);
		fft.analyse();
		fft.makeGraph(4, 20000, 1280, 467);
		Wave w = new Wave(s);
		w.writeFile(new File("white.wav"));
	
	}

}
