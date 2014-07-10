package waveAnalysis;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import javax.naming.InvalidNameException;

import plotting.WavController;
import filemanager.ArrayStuff;
import filemanager.ByteReader;
import filemanager.Chunk;
import filemanager.HexByte;
import filemanager.InfoChunk;
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
		//wr2.makeGraph();
		InfoChunk bds = wr2.getListChunk();
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
		
		
		try {
			String f = "/Users/Jonny/Documents/Timbre/PracticeRunText.wav";
			Wave wr = new Wave(f);
			wr.init();
			ArrayList<Byte> arg = new ArrayList<Byte>();
			for (byte b: wr.getBytes()) {
				arg.add(b);
			}
			
			Chunk nc = new Chunk();
			nc.setName("IART");
			nc.setData("FARTS");
			Chunk nd = new Chunk();
			nd.setName("ICRD");
			nd.setData("1500");
			InfoChunk icr = new InfoChunk();
			icr.AddChunk(nc);
			icr.AddChunk(nd);
			for (byte b: icr.getBytes()) {
				arg.add(b);
			}
			String output = "/Users/Jonny/Documents/Timbre/PracticeRunText2.wav";
			FileOutputStream fileOut = new FileOutputStream(output);
            
            byte[] bb = ArrayStuff.arrayListToByte(arg);
            wr2 = new Wave(bb);
            wr2.init();
            System.out.println(wr.getHexHeader());
            System.out.println(wr2.getHexHeader());
            
            fileOut.write(bb);
            fileOut.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
