package waveAnalysis;

import java.io.File;
import java.util.Arrays;

import wavePlot.Controller;
import filemanager.ArrayStuff;
import filemanager.ByteReader;
import filemanager.HexByte;
import filemanager.InfoChunkType;
import filemanager.ListChunk;
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
		String f2 = "/Users/Jonny/Documents/Timbre/PracticeBassMeta.wav";
		Wave wr2 = new Wave(f2);
		wr2.init();
		wr2.makeGraph();
		ListChunk lc = new ListChunk(wr2.getListChunk());
		System.out.println(HexByte.byteToHexString(lc.getChunk()));
		//System.out.println(HexByte.byteToLetterString(lc.getChunk()));
		lc.initChunkTypes();
		System.out.println(lc.hasLittleChunk("IART"));
		InfoChunkType ic = lc.getLittleChunk("IART");
		System.out.println(ic.toString());
		System.out.println(HexByte.byteToHexString(ArrayStuff.bigByteTobyte(ic.toByteChunk())));
		//just need to make a method that rounds up to nearest 4 bytes.
		System.out.println(HexByte.byteToHexString(ArrayStuff.bigByteTobyte(ic.toByteChunk())));
		
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
