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
import riff.Pitch;
import riff.Signal;
import riff.WaveChunk;
import filemanager.ArrayStuff;
import filemanager.ByteReader;
import filemanager.HexByte;
import filemanager.Log;
import filemanager.Wave;

public class WaveReader {

	public WaveReader() {
		
	}
	
	public static void main(String[] args) {
	
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
		String f2 = "/Users/Jonny/Documents/Timbre/PracticeWav/PracticePan.wav";
		//String f2 = "/Users/Jonny/Documents/Timbre/PracticeANorm.wav";
		WaveChunk wr2 = new WaveChunk(f2);
		Log.d(wr2.toStringRecursive());
		Signal sigs = wr2.getSignals();
		//Signal bigSig = Gain.volume(sigs, 12);
		//Signal smallSig = Gain.volume(sigs, -6.02);
		//Log.d(ArrayStuff.getMax(sigs.getSignal()));
		//Signal pSig = Pitch.pitchShift(sigs, 0.5);
		sigs = Pitch.pitchShift(sigs, 2.4);
		WaveChunk pw = new WaveChunk(sigs);
		pw.writeFile(new File("/Users/Jonny/Documents/PracticeWav/Timbre/PA.wav"));
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
