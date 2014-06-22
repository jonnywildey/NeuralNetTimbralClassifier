package analysis;

import java.io.File;
import java.util.Arrays;

import wavePlot.Controller;
import filemanager.ByteReader;
import filemanager.Wave;

public class WaveReader {

	public WaveReader() {
		
	}
	
	public static void main(String[] args) {
	
		String f = "/Users/Jonny/Documents/Timbre/PracticeBass.wav";
		Wave wr = new Wave(f);
		System.out.println(wr.init());
		System.out.println(wr.toString());
		System.out.println(wr.isWav());
		//System.out.println(wr.getHex(2000));
		System.out.println(wr.getHexHeader());
		wr.makeGraph();
		WaveFund wf = new WaveFund(wr);
		wf.verbose(true);
		double fund = wf.zeroCross();
		System.out.println(Music.hzToMidi(fund));
		System.out.println(Music.hzToPitch(fund));
		//System.out.println(WavReader.hexToDecimal((byte)52));
		
		



		//System.out.println(Arrays.deepToString(signals));
		//Controller c = new Controller();
		//c.makeChart(signals[0]);
		//c.makeChart(signals[1]);
	}

}
