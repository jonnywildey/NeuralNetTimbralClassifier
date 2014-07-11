package riff;

import java.io.File;
import filemanager.ArrayStuff;
import filemanager.ByteReader;
import filemanager.HexByte;
import filemanager.Log;
import plotting.WavController;

public class WaveChunk extends Chunk{
	
	protected static int dataOffset = 12;
	public File filepath;
	
	public WaveChunk() {
		super();
	}
	
	public WaveChunk(File f) {
		super();
		this.filepath = f;
		this.readFile();
		this.name = new String(HexByte.getSubset(bytes, 0, 3));
		this.initTypes();
		this.initSubChunks();
	}

	public WaveChunk(String file) {
		this(new File(file));
	}
	
	public WaveChunk(byte[] bytes) {
		this.bytes = bytes;
	}
	
	
	/** Basic checks as to whether file is wav **/
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
	
	/** gets byte array from filePath file **/
	public void readFile() {
		try{
			ByteReader br = new ByteReader(this.filepath.toString());
			this.bytes = br.readFile();
			Log.d("Wave " + this.filepath.getName() + " read successfully");
		} catch (Exception e) {
			Log.d("Wave " + this.filepath.getName() + " could not be read");
		}
	}
	
	/** gets byte array from file **/
	public void readFile(File f) {
		this.filepath = f;
		readFile();
	}
	
	
	/** Writes the bytes to a file **/
	public void writeFile(File f) {
		super.writeFile(f);
		this.filepath = f;
	}
	
	/** get byteArray **/
	public byte[] getBytes() {
		return this.bytes;
	}

	
	/** Makes a waveform graph **/
	public void makeGraph() {
		if (hasBytes()) {
			WavController c = new WavController(this.getSignalsLong());
			c.makeChart();
		}
	}
	/** Makes a waveform graph of size specified **/
	public void makeGraph(int width, int height) {
		if (hasBytes()) {
			WavController c = new WavController(this.getSignalsLong(), width, height);
			c.makeChart();
		}
	}
	
	/** Make a graph using one channel of wav **/
	public void makeGraphMono(int channel) {
		long[][] sig = new long[1][];
		sig[0] = this.getSignalsLong()[0];
		if (hasBytes() & channel < this.getChannels()) {
			WavController c = new WavController(sig);
			c.makeChart();
		}
	}
	
	
	/** Return a semireadable header **/
	public String getHexHeader() {
		return HexByte.byteToHexString(ArrayStuff.getSubset(this.bytes, 0, 44));
	}
	
	/** Return the first x of the file in a semireadable format **/
	public String getHex(int x) {
		return HexByte.byteToHexString(ArrayStuff.getSubset(this.bytes, 0, x));
	}
	
	
	/**Returns bit rate of Wave **/
	public int getBitRate() {
		FMTChunk fc =  (FMTChunk) this.getSubChunk("fmt ");
		return fc.getBitSize();
	}
	
	/**Returns sample rate of Wave **/
	public long getSampleRate() {
		FMTChunk fc =  (FMTChunk) this.getSubChunk("fmt ");
		return fc.getSampleRate();
	}
	
	/** Returns channel number of Wave **/
	public int getChannels() {
		FMTChunk fc =  (FMTChunk) this.getSubChunk("fmt ");
		return fc.getChannels();
	}
	
	/** Returns the length of the signal data. A bit 
	 * different than other chunk's getDataLength
	 * @return
	 */
	@Override
	public long getDataLength() {
		DataChunk dc = (DataChunk) this.getSubChunk("data");
		return dc.getDataLength();
	}
	
	
	/**Different than other chunks, this sends out the signal data (in bytes)**/
	@Override
	public byte[] getData() {
		return HexByte.getOffsetSubset(this.bytes, 44, this.getDataLength());
	}
	

	@Override
	protected void initTypes() {
		/* All the official chunk types you can get in INFO */
		String[] s = {"fmt ", "data", "LIST"};
		this.acceptableSubChunks = s;
	}

	/** return the signals in a fixed point format **/
	public long[][] getSignalsLong() {
		DataChunk dc = (DataChunk) this.getSubChunk("data");
		long[][] signals = dc.getSignalsLong(this.getBitRate(), this.getChannels());
		return signals;
	}
	
	/** return the signal(s) in a floating point format **/
	public double[][] getSignalsDouble() {
		DataChunk dc = (DataChunk) this.getSubChunk("data");
		double[][]	signals = dc.getSignalsDouble(this.getBitRate(), this.getChannels());
		return signals;
	}
	
	
}
