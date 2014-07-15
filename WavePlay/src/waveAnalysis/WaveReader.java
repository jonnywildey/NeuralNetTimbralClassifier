package waveAnalysis;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import javax.naming.InvalidNameException;

import plotting.WavController;
import riff.Chunk;
import riff.InfoChunk;
import riff.Signal;
import riff.WaveChunk;
import waveProcess.EQFilter;
import waveProcess.Gain;
import waveProcess.Gen;
import waveProcess.Noise;
import waveProcess.Pitch;
import filemanager.ArrayStuff;
import filemanager.ByteReader;
import filemanager.CSVWriter;
import filemanager.HexByte;
import filemanager.Log;
import filemanager.Wave;

public class WaveReader {

	public WaveReader() {
		
	}
	
	public static void main(String[] args) throws Exception{
	
		//String f = "/Users/Jonny/Documents/Timbre/PracticeBass.wav";
		//Wave wr = new Wave(f);
		//System.out.println(wr.init());
		//wr.isThereStuffAfterData();
		//System.out.println(wr.toString());
		//System.out.println(wr.isWav());
		//System.out.println(wr.getHex(100));
		//System.out.println(wr.getHexHeader());
		//wr.makeGraph();
		//File logPath = new File("/Users/Jonny/Documents/Timbre/Logs/log.txt");
		
		
		Log.setFilePath(new File("/Users/Jonny/Documents/Timbre/Logs/log.txt"));
		String f1 = "/Users/Jonny/Documents/Timbre/PracticeWav/PracticeA.wav";
		String f2 = "/Users/Jonny/Documents/Timbre/PracticeWav/PracticeSil.wav";
		//String f2 = "/Users/Jonny/Documents/Timbre/PracticeANorm.wav";
		WaveChunk wr1 = new WaveChunk(f1);
		//WaveChunk wr2 = new WaveChunk(f2);
		Log.d(wr1.toStringRecursive());
		//Log.d(wr2.toStringRecursive());
		//Signal s1 = Gen.sineSweep(200, 500, 100000, -3, 44100, 16);
		//s1 = Gen.pinkNoise(100000, -3, 44100, 16);
		//s1 = Gain.changeGain(s1, 0);
		Signal s1 = wr1.getSignals();
		WaveChunk wr2 = new WaveChunk(s1);
		Signal s3 = wr1.getSignals();
		//WaveChunk wr3 = new WaveChunk(Gain.volume(s3, -2));

		//s1 = Noise.whiteNoise(s1, -6);
		//s1.makeGraph(800, 600);

		
		//s1 = Noise.whiteNoise(s1, -18);
		//s1 = EQFilter.highPassFilter(s1, 50, 1);
		//s1 = Gain.volume(s1, -12);
		//s1 = EQFilter.notch(s1, 600, 1);
		//s2 = Gain.volume(s2, -9);
		//Signal bigSig = Gain.volume(sigs, 12);
		//Signal smallSig = Gain.volume(sigs, -6.02);
		//Log.d(ArrayStuff.getMax(sigs.getSignal()));
		//Signal pSig = Pitch.pitchShift(sigs, 0.5);
		//Signal sum = Gain.sum(s1, s2);
		//sigs = Pitch.reverse(sigs);
		FFT fft = new FFT(s1);
		double[][] ddd = fft.analyse(20000);
		fft.filter(60, 700);
		fft.makeChart();
		CSVWriter cd = new CSVWriter("/Users/Jonny/Documents/Timbre/Logs/fft.csv");
		cd.writeArraytoFile(ddd);
		//Log.d(fft.toString());
		WaveChunk pw = new WaveChunk(s1);
		Log.d(pw.toStringRecursive());
		pw.writeFile(new File("/Users/Jonny/Documents/Timbre/PracticeWav/PA.wav"));
		//for (int i = 200; i < 300; ++i) {
			//Log.d(sigs.getSignal()[0][i] + "  " + s2.getSignal()[0][i]);
		//}
		
		
		
		//wr2.makeGraph();
		//wr2.makeGraph();
		//System.out.println(bds.toString());
		//System.out.println(HexByte.byteToLetterString(bds));
		//InfoChunk lc = new InfoChunk(bds);
		//System.out.println(lc.toStringRecursive());
		
		//System.out.println(HexByte.byteToLetterString(lc.getChunk()));
		/**
		lc.initChunkTypes();
		System.out.println(lc.hasSubChunk("IART"));
		TagChunk ic = (TagChunk) lc.getSubChunk("IART");
		System.out.println(ic.toString());
		System.out.println(HexByte.byteToHexString((ic.generateByteChunk())));
		//just need to make a method that rounds up to nearest 4 bytes.
		System.out.println(HexByte.byteToHexString((ic.generateByteChunk())));
		*/
		
		
		
		/** WaveFund wf = new WaveFund(wr);
		wf.verbose(true);
		double fund = wf.zeroCross();
		System.out.println(Music.hzToMidi(fund));
		System.out.println(Music.hzToPitch(fund));
		//System.out.println(WavReader.hexToDecimal((byte)52)); **/
		
		



		//System.out.println(Arrays.deepToString(signals));
		//Controller c = new Controller();
		//c.makeChart(signals[0]);
		//c.makeChart(signals[1]);
	}

}
