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
import riff.Wave;
import waveProcess.EQFilter;
import waveProcess.Edit;
import waveProcess.Gain;
import waveProcess.Gen;
import waveProcess.Noise;
import waveProcess.Pitch;
import filemanager.ArrayStuff;
import filemanager.ByteReader;
import filemanager.CSVWriter;
import filemanager.HexByte;
import filemanager.Log;

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
		String f1 = "/Users/Jonny/Documents/Timbre/PracticeWav/PracticeGuit.wav";
		//String f2 = "/Users/Jonny/Documents/Timbre/PracticeWav/PracticeSil.wav";
		//String f2 = "/Users/Jonny/Documents/Timbre/PracticeANorm.wav";
		Wave wr1 = new Wave(f1);
		//WaveChunk wr2 = new WaveChunk(f2);
		Log.d(wr1.toStringRecursive());
		//Log.d(wr2.toStringRecursive());
		//Signal s1 = Gen.sineSweep(200, 500, 100000, -3, 44100, 16);
		//s1 = Gen.pinkNoise(100000, -3, 44100, 16);
		//s1 = Gain.changeGain(s1, 0);
		//Signal s1 = Gen.silence(200000, 44100, 16);
		Signal s1 = wr1.getSignals();
		Wave wer = new Wave(s1);
		
		//Signal s1 = wr1.getSignals();
		//s1 = EQFilter.highPassFilter(s1, 120, 1);
		//s1 = Gain.normalise(s1);

		
		//4096 works well
		FrameFFT fft = new FrameFFT(s1, 4096);
		double[][] dd = fft.analyse(20, 15000);
		dd = FrameFFT.getBarkedSubset(dd);
		//dd = FrameFFT.getSumTable(dd);
		//wer.writeFile(new File("/Users/Jonny/Documents/Timbre/PracticeWav/PA.wav"));
		dd = FrameFFT.convertTableToDecibels(fft.signal, dd, fft.frameSize);
		//double[][] nd = Statistics.getPeaks(dd);
		//nd = FFT.filter(nd, 40, 2000);
		//nd = ArrayStuff.flip(nd);
		//Log.d("freq: " + Pitch.getFundamental(dd));
		//fft.sumDifference();
		//fft.makeGraph(40, 20000, 800, 600);
		//FFTDifference fd = new FFTDifference(fft);
		//fd.analyse();
		//double[][] dd = FFTDifference.splitAndAverage(fft.table, 80);
		//dd = ArrayStuff.flip(dd);
		//fd.makeGraph();
		CSVWriter cd = new CSVWriter("/Users/Jonny/Documents/Timbre/Logs/fft.csv");
		cd.writeArraytoFile(dd);
		//dd = ArrayStuff.flip(dd);

		//s1 = Gain.normalise(s1);
		//s1 = Noise.addTapeHiss(s1, -6);
		
		/*Signal s1 = wr1.getSignals();
		s1 = Edit.crop(s1, 120000, s1.getLength());
		s1 = EQFilter.highPassFilter(s1, 140, 0.72);
		s1 = Gain.changeGain(s1, -1);
		s1.makeGraph();
		WaveChunk wc = new WaveChunk(s1);
		wc.writeFile(new File("../assets/wav/Tape.wav")); */
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
		//s1 = Gain.volume(s1, -12);
		//FrameFFT fft = new FrameFFT(s7, 4096);
		//double[][] table = fft.analyse(10, 20000);
		//table = ArrayStuff.flip(table);
		//CSVWriter cd = new CSVWriter("/Users/Jonny/Documents/Timbre/Logs/fft.csv");
		//cd.writeArraytoFile(table);
		//Log.d(fft.toString());
		//Log.d(pw.compareTo(wr1));
		//Log.d(pw.toStringRecursive());
		//pw.writeFile(new File("/Users/Jonny/Documents/Timbre/PracticeWav/PA.wav"));
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
