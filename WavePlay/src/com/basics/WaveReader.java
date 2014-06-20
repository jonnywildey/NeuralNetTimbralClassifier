package com.basics;

import java.io.File;
import java.util.Arrays;

import wavePlot.Controller;
import filemanager.ByteReader;
import filemanager.WavReader;

public class WaveReader {

	public WaveReader() {
		
	}
	
	public static void main(String[] args) {
	
		String f = "/Users/Jonny/Documents/Timbre/PracticeSil.wav";
		WavReader wr = new WavReader(f);
		System.out.println(wr.getHeader());
		//System.out.println(WavReader.hexToDecimal((byte)52));
		System.out.println(wr.getChunkSize());
		System.out.println("Bits " + wr.getBitSize());
		System.out.println(wr.getSampleRate());
		System.out.println(wr.getNumberOfChannels());
		System.out.println(wr.getDataSize());
		
		wr.getData();

		long[][] signals = wr.getSignals();
		//System.out.println(Arrays.deepToString(signals));
		Controller c = new Controller();
		c.makeChart(signals[0]);
		//c.makeChart(signals[1]);
	}

}
