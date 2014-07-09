package filemanager;

import java.io.File;

import plotting.WavController;



public class Wave {
	
	private byte[] bytes;
	private byte[] data;
	public long[][] signals;
	private int bit;
	private long dataSize;
	private long sampleRate;
	private int channel;
	public File filepath;
	
	public Wave(File f) {
		this.filepath = f;
	}

	public Wave(String file) {
		this.filepath = new File(file);
	}
	
	
	/** Basic checks as to whether file is wav **/
	public boolean isWav() {
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
	
	public void readFile() {
		ByteReader br = new ByteReader(this.filepath.toString());
		this.bytes = br.readFile();
	}
	
	/** get byteArray **/
	public byte[] getBytes() {
		return this.bytes;
	}
	
	public boolean isEmpty() {
		if (this.signals == null) {
			return true;
		} else {
			return false;
		}
	}
	
	/** Makes a waveform graph **/
	public void makeGraph() {
		if (!isEmpty()) {
			WavController c = new WavController(this.signals);
			c.makeChart();
		}
	}
	/** Makes a waveform graph of size specified **/
	public void makeGraph(int width, int height) {
		if (!isEmpty()) {
			WavController c = new WavController(this.signals, width, height);
			c.makeChart();
		}
	}
	
	/** Make a graph using one channel of wav **/
	public void makeGraphMono(int channel) {
		long[][] sig = new long[1][];
		sig[0] = this.signals[0];
		if (!isEmpty() & channel < this.channel) {
			WavController c = new WavController(sig);
			c.makeChart();
		}
	}
	
	public byte[] getListChunk() {
		//assume overall chunk size (0x4) is accurate?
		System.out.println(this.bytes.length);
		System.out.println(this.dataSize);
		System.out.println(this.bytes.length - this.dataSize);
		byte[] listChunk = (HexByte.getOffsetSubset(this.bytes, 44 + (int)this.dataSize, 
							(long)(this.bytes.length - this.dataSize - 44)));
		return listChunk;
	}
	
	public String toString() {
		if (!isEmpty()) {
			StringBuffer sb = new StringBuffer(100);
			sb.append("Filename: " + this.filepath + "\n");
			sb.append("Bits: " + this.bit + "\n");
			sb.append("Sample Rate: " + this.sampleRate + "\n");
			sb.append("Channels: " + this.channel + "\n");
			sb.append("Data Size: " + this.dataSize + "\n");
			return sb.toString();
		}
		return "Either not a wav or not initialised!";
		
	}
	
	
	
	/**reads and initialises file. returns true if successful **/
	public boolean init() {
		this.readFile();
		if (this.isWav()) {
			this.getBitSize();
			this.getNumberOfChannels();
			this.getDataSize();
			this.getData();
			this.getSampleRate();
			this.getSignals();
			return true;
		} else {
			return false;
		}
	}
	
	public void changeFile(String str) {
		this.filepath = new File(str);
	}
	public void changeFile(File f) {
		this.filepath = f;
	}
	
	
	/** Return a semireadable header **/
	public String getHexHeader() {
		return HexByte.byteToHexString(HexByte.getSubset(this.bytes, 0, 44));
	}
	
	/** Return the first x of the file in a semireadable format **/
	public String getHex(int x) {
		return HexByte.byteToHexString(HexByte.getSubset(this.bytes, 0, x));
	}
	
	
	/** Get overall size of wav file in chunks **/
	public long getChunkSize() {
		byte[] chunk = HexByte.getLittleEndianSubset(this.bytes, 4, 7);
		return HexByte.hexToDecimalUnsigned(chunk);
	}
	
	public int getBitSize() {
		byte[] bit = HexByte.getEndianSubset(this.bytes, 34, 2);
		this.bit = (int)HexByte.hexToDecimalUnsigned(bit); 
		return this.bit;
	}
	
	public long getSampleRate() {
		byte[] sampleRate = HexByte.getEndianSubset(this.bytes, 24,4);
		this.sampleRate = HexByte.hexToDecimalUnsigned(sampleRate);
		return this.sampleRate;
	}
	
	public int getNumberOfChannels() {
		byte[] channel = HexByte.getEndianSubset(this.bytes, 22, 2);
		this.channel = (int)HexByte.hexToDecimalUnsigned(channel);
		return this.channel;
	}
	
	public long getDataSize() {
		byte[] dataChunk = HexByte.getEndianSubset(this.bytes, 40, 4);
		this.dataSize = HexByte.hexToDecimalUnsigned(dataChunk);
		return this.dataSize;
	}
	
	
	public byte[] getData() {
		byte[] data = HexByte.getOffsetSubset(this.bytes, 44, this.getDataSize());
		this.data = data;
		//System.out.println("data" + byteToHexString(getSubset(this.data, 0, 20) ));
		return data;
	}
	
	/** return int array of each channel **/
	public long[][] getSignals() {
		
		int bitJump = this.bit / 8; //how many bytes per sample
		int loopLength = (int) (this.dataSize / this.channel / bitJump) ; //how many samples per signal
		long[][] signals = new long[channel][];
		int offset = 0; //offset for each channel
		byte[] newBit = new byte[bitJump];
		
		for (int k = 0; k < channel; ++k) {
			long[] signal = new long[loopLength];
			for (int i = 0; i < loopLength; i++) { 
				for (int l = 0; l < bitJump; ++l) {
					//get byte array
					newBit[l] = this.data[(i * channel * bitJump) + l + offset];
				}
				//turn bytearray to int
				if (this.bit >= 32) {
					signal[i] = (long) HexByte.hexToFloat(newBit);	 //FLOAT
				} else {
					signal[i] =  (HexByte.hexToDecimalSigned(newBit)); //NORMAL
				}
			}
			signals[k] = signal;
			offset += bitJump;
		}
		this.signals = signals;
		return signals;
	}
	
	public Wave() {
		super();
	}
	
	
}
