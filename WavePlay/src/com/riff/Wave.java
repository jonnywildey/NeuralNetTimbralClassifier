package com.riff;

import java.io.File;
import java.util.ArrayList;

import com.plotting.WavController;
import com.util.ArrayMethods;
import com.util.HexByte;
import com.util.Log;
import com.util.fileReading.ByteReader;

/**
 * Wave chunk. Includes encoding and decoding methods,
 * read/write, and graphing
 *
 * @author Jonny Wildey
 * @version 1.0
 */
public class Wave extends Chunk{
	
	protected static int dataOffset = 12;
	public File filepath;
	
	/**
	 * Instantiates a new wave.
	 */
	public Wave() {
		super();
	}
	
	/**
	 * Instantiates a new wave.
	 *
	 * @param file the f
	 */
	public Wave(File file) {
		super();
		this.filepath = file;
		this.readFile();
		this.name = new String(HexByte.getSubset(bytes, 0, 3));
		this.initTypes();
		this.initSubChunks();
	}

	/**
	 * Instantiates a new wave.
	 *
	 * @param file the file
	 */
	public Wave(String file) {
		this(new File(file));
	}
	
	/**
	 * Instantiates a new wave.
	 *
	 * @param bytes the bytes
	 */
	public Wave(byte[] bytes) {
		this.bytes = bytes;
	}
	
	/**
	 * Constructs a Wave from a signal. Make sure you've set
	 * your bit rate and sample rate
	 *
	 * @param signal the signal
	 */
	public Wave(Signal signal) {
		this.name = "RIFF";
		setSignal(signal);
	}
	
	
	
	
	
	/**
	 * gets byte array from filePath file *.
	 */
	public void readFile() {
		try{
			ByteReader br = new ByteReader(this.filepath.toString());
			this.bytes = br.readFile();
			br.close();
			Log.d("Wave " + this.filepath.getName() + " read successfully");
		} catch (Exception e) {
			Log.d("Wave " + this.filepath.getName() + " could not be read");
		}
	}
	
	
	/**
	 * gets byte array from file *.
	 *
	 * @param f the f
	 */
	public void readFile(File f) {
		this.filepath = f;
		readFile();
	}
	
	
	/**
	 * Writes the bytes to a file *.
	 *
	 * @param f the f
	 */
	public void writeFile(File f) {
		super.writeFile(f);
		this.filepath = f;
	}
	
	/**
	 * get byteArray *.
	 *
	 * @return the bytes
	 */
	public byte[] getBytes() {
		return this.bytes;
	}
	
	/**
	 * This essentially completely rewrites the data of the
	 * wav, reencoding the header, fmt and data chunks.
	 *
	 * @param s the new signal
	 */
	public void setSignal(Signal s) {
		this.chunks = new ArrayList<Chunk>();
		FMTChunk fmt = new FMTChunk(s.getBit(), s.getSampleRate(), s.getChannels());
		DataChunk dc = new DataChunk(s);
		this.chunks.add(fmt);
		this.chunks.add(dc);
		int fmtl = (int) fmt.getBytesLength();
		int dcl = (int) dc.getBytesLength();
		this.bytes = new byte[(int) (12 + fmtl + dcl)];
		this.bytes = ArrayMethods.addBytes(bytes, HexByte.stringToBytes("RIFF", 4), 0);
		this.bytes = ArrayMethods.addBytes(bytes, HexByte.longToLittleEndianBytes(fmtl + dcl, 4), 4);
		this.bytes = ArrayMethods.addBytes(bytes, HexByte.stringToBytes("WAVE", 4), 8);
		this.bytes = ArrayMethods.addBytes(bytes, fmt.getBytes(), 12);
		this.bytes =  ArrayMethods.addBytes(bytes, dc.getBytes(), 12 + fmtl);

	}
	
	/**
	 * Clears the wav of any information *.
	 */
	public void clear() {
		this.bytes = null;
		this.chunks = new ArrayList<Chunk>();
	}
	
	/* (non-Javadoc)
	 * @see com.riff.Chunk#addChunk(com.riff.Chunk)
	 */
	@Override
	/** Adds an infoChunk **/
	public void addChunk(Chunk chunk) {
		this.bytes = ArrayMethods.concat(this.bytes, chunk.bytes);
		//this.bytes = ArrayStuff.addBytes(bytes, HexByte.longToLittleEndianBytes(
		//		this.bytes.length - 8, 4), 4);
		
	}
	
	
	/**
	 * return the signals in a fixed point format *.
	 *
	 * @return the signals long
	 */
	public long[][] getSignalsLong() {
		DataChunk dc = (DataChunk) this.getSubChunk("data");
		long[][] signals = dc.getSignalsLong(this.getBitRate(), this.getChannels());
		return signals;
	}
	
	/**
	 * return the signal(s) in a floating point format *.
	 *
	 * @return the signals double
	 */
	public double[][] getSignalsDouble() {
		DataChunk dc = (DataChunk) this.getSubChunk("data");
		double[][]	signals = dc.getSignalsDouble(this.getBitRate(), this.getChannels());
		return signals;
	}
	
	/**
	 * Returns the signals in a process-friendly Signal format *.
	 *
	 * @return the signals
	 */
	public Signal getSignals() {
		DataChunk dc = (DataChunk) this.getSubChunk("data");
		double[][]	signals = dc.getSignalsDouble(this.getBitRate(), this.getChannels());
		return new Signal(signals, this.getBitRate(), this.getSampleRate());
	}
	
	
	/**
	 * Makes a waveform graph *.
	 */
	public void makeGraph() {
		if (hasBytes()) {
			WavController c = new WavController(this.getSignalsLong());
			c.makeChart();
		}
	}
	
	/**
	 * Makes a waveform graph of size specified *.
	 *
	 * @param width the width
	 * @param height the height
	 */
	public void makeGraph(int width, int height) {
		if (hasBytes()) {
			WavController c = new WavController(this.getSignalsLong(), width, height);
			c.makeChart();
		}
	}
	
	/**
	 * Make a graph using one channel of wav *.
	 *
	 * @param channel the channel
	 */
	public void makeGraphMono(int channel) {
		long[][] sig = new long[1][];
		sig[0] = this.getSignalsLong()[0];
		if (hasBytes() & channel < this.getChannels()) {
			WavController c = new WavController(sig);
			c.makeChart();
		}
	}
	
	
	/**
	 * Return a semireadable header *.
	 *
	 * @return the hex header
	 */
	public String getHexHeader() {
		return HexByte.byteToHexString(ArrayMethods.getSubset(this.bytes, 0, 44));
	}
	
	/**
	 * Return the first x of the file in a semireadable format *.
	 *
	 * @param x the x
	 * @return the hex
	 */
	public String getHex(int x) {
		return HexByte.byteToHexString(ArrayMethods.getSubset(this.bytes, 0, x - 1));
	}
	
	/**
	 * Gets the hex.
	 *
	 * @return the hex
	 */
	public String getHex() {
		return getHex(this.bytes.length);
	}
	
	
	/**
	 * Returns bit rate of Wave *.
	 *
	 * @return the bit rate
	 */
	public int getBitRate() {
		FMTChunk fc =  (FMTChunk) this.getSubChunk("fmt ");
		return fc.getBitSize();
	}
	
	/**
	 * Returns sample rate of Wave *.
	 *
	 * @return the sample rate
	 */
	public int getSampleRate() {
		FMTChunk fc =  (FMTChunk) this.getSubChunk("fmt ");
		return fc.getSampleRate();
	}
	
	/**
	 * Returns channel number of Wave *.
	 *
	 * @return the channels
	 */
	public int getChannels() {
		FMTChunk fc =  (FMTChunk) this.getSubChunk("fmt ");
		return fc.getChannels();
	}
	
	/**
	 * get the bitwise hamming distance between the signals
	 * of waves. correlation above 0.9 suggests waves are VERY similar *
	 *
	 * @param wave the wave
	 * @return the double
	 */
	public double compareTo(Wave wave) {
		return ArrayMethods.byteSimilarity(this.getData(), wave.getData());
	}
	
	/**
	 * Returns the length of the signal data. A bit
	 * different than other chunk's getDataLength
	 *
	 * @return the data length
	 */
	@Override
	public long getDataLength() {
		DataChunk dc = (DataChunk) this.getSubChunk("data");
		return dc.getDataLength();
	}
	
	
	/**
	 * Different than other chunks, this sends out the signal data (in bytes)*.
	 *
	 * @return the data
	 */
	@Override
	public byte[] getData() {
		return HexByte.getOffsetSubset(this.bytes, 44, this.getDataLength());
	}
	

	/* (non-Javadoc)
	 * @see com.riff.Chunk#initTypes()
	 */
	@Override
	protected void initTypes() {
		/* All the official chunk types you can get in INFO */
		String[] s = {"fmt ", "data", "LIST"};
		this.acceptableSubChunks = s;
	}
	
	/*@Override
	public void setData(byte[] data) {
		throw new UnsupportedOperationException();
	} */
	

	
	/**
	 * Basic checks as to whether file is wav *.
	 *
	 * @param bytes the bytes
	 * @return true, if is wav
	 */
	public boolean isWav(byte[] bytes) {
		try {
			long riff = HexByte.hexArrayToInt(HexByte.convertByteArray(HexByte.getOffsetSubset(bytes, 0, 4)));
			long wave = HexByte.hexArrayToInt(HexByte.convertByteArray(HexByte.getOffsetSubset(bytes, 8, 4)));
			long data = HexByte.hexArrayToInt(HexByte.convertByteArray(HexByte.getOffsetSubset(bytes, 36, 4)));
			long fmt = HexByte.hexArrayToInt(HexByte.convertByteArray(HexByte.getOffsetSubset(bytes, 12, 4)));
			if (riff == 1380533830 & wave == 1463899717 && data == 1684108385 & fmt == 1718449184) {
				return true;
			} else {
				return false;
			} 
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Gets the file path.
	 *
	 * @return the file path
	 */
	public File getFilePath() {
		return this.filepath;
	}

	
	
	
}
