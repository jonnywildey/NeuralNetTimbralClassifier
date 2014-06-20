package filemanager;

import java.io.IOException;
import java.util.Arrays;

public class WavReader extends ByteReader{

	public WavReader(String file) {
		super(file);
	}
	private byte[] bytes;
	private byte[] data;
	public int bit;
	private long dataSize;
	private int channel;
	
	
	//converts byte array to hex array//
	public static int[] convertByteArray(byte[] bytes) {
		int[] sep = new int[bytes.length * 2];
		int c = 0;
		//convert bytes into standard hex int array
		for (byte b: bytes) {
			if (b < 0) {
				sep[c] = (b + 256) / 16;
				sep[c + 1] = (b + 256) % 16;
			} else {
				sep[c] = b / 16;
				sep[c + 1] = b % 16;
			}
			c += 2;
		}
		return sep;
	}
	
	//converts byte array to hex array//
		public static int[] convertByteArrayLE(byte[] bytes) {
			int[] sep = new int[bytes.length * 2];
			int c = 0;
			//convert bytes into standard hex int array
			for (byte b: bytes) {
				if (b < 0) {
					sep[sep.length - c - 2] = (b + 256) / 16;
					sep[sep.length - c - 1] = (b + 256) % 16;
				} else {
					sep[sep.length - c - 2] = b / 16;
					sep[sep.length - c - 1] = b % 16;
				}
				c += 2;
			}
			return sep;
		}
		
	public static long hexArrayToInt(int[] bytes) {
		long tally = 0;
		for (int i = 0; i < bytes.length; ++i) {
			tally += (Math.pow(16, i) * bytes[bytes.length - i - 1]);
			//System.out.print("tally " +tally);
		}
		return tally;
	}
	
	
	/**hexadecimal conversion. Unsigned **/
	public static long hexToDecimalUnsigned(byte[] bytes) {
		int[] sep = convertByteArray(bytes);
		return hexArrayToInt(sep);
	}
	
	/**hexadecimal conversion. Unsigned **/
	public static long hexToDecimalUnsignedLE(byte[] bytes) {
		int[] sep = convertByteArrayLE(bytes);
		//convert to decimal
		//System.out.println("sep " + Arrays.toString(sep));
		return hexArrayToInt(sep);
	}
	
	/**hexadecimal conversion. Signed **/
	public static long hexToDecimalSigned(byte[] bytes) {
		//System.out.println(Arrays.toString(bytes));
		long unsigned = hexToDecimalUnsignedLE(bytes);
		//System.out.println("uns " + unsigned );
		long max = (long) Math.pow(2, (bytes.length * 8) - 1);
		//System.out.println("max " + max );
		if (unsigned > max) { //BIT FLIP
			//System.out.println("BitFlip!!! " + (int) (((max * 2) - unsigned + 1) * -1));
			return  (((max * 2) - unsigned + 1) * -1);
		} else {
			return unsigned;
		}
	}
	
	public static double hexToFloat(byte[] bytes) {
		int[] sep = convertByteArrayLE(bytes); //we now have an array of the correct numbers
		int n = (int)hexArrayToInt(sep); 
		//System.out.println(Float.intBitsToFloat(n) * 65536);
		return Float.intBitsToFloat(n) * 65536;
	}
	
	
	
	
	 
	public static int hexToDecimal(byte b) {
		StringBuffer buf = new StringBuffer(1);
			buf.append(b);
		return Integer.parseInt(buf.toString(), 16);
	}
	
	

	
	public static String byteToHexString(byte[] bytes) {
			StringBuilder buf = new StringBuilder(bytes.length * 5);
			int c4 = 1;
			int c16 = 1;
			for (byte bt: bytes) {
				//System.out.println((int)bt);
				String s = String.format("%02x", bt);
				s += " ";
				if (c4 % 4 == 0) {s += "\t";}
				if (c16 % 16 == 0) {s += "\n";}
				
				buf.append(s);
				c4++;
				c16++;
				
			}
			return buf.toString();
		
	}
	
	public String getHeader() {
		this.bytes = this.readFile();
		this.bit = this.getBitSize();
		this.channel = this.getNumberOfChannels();
		this.dataSize = this.getDataSize();
		return byteToHexString(getSubset(0, 200));	
	}
	
	private  byte[] getSubset(int start, int end) {
		byte[] newBytes = new byte[(end + 1) - start];
		for (int i = 0; i <= (end - start); ++i) {
			newBytes[i] = this.bytes[start + i];
		}
		return newBytes;
	}
	
	private  byte[] getSubset(byte[] bytes, int start, int end) {
		byte[] newBytes = new byte[(end + 1) - start];
		for (int i = 0; i <= (end - start); ++i) {
			newBytes[i] = bytes[start + i];
		}
		return newBytes;
	}
	private  byte[] getLittleEndianSubset(int start, int end) {
		byte[] newBytes = new byte[(end + 1) - start];
		for (int i = 0; i <= (end - start); ++i) {
			newBytes[newBytes.length - i - 1] = this.bytes[start + i];
			//System.out.println(newBytes[newBytes.length - i - 1]);
		}
		
		return newBytes;
	}
	
	private byte[] getEndianSubset(int offset, long count) {
		return getLittleEndianSubset(offset, (int) (offset + count - 1));
	}
	
	private byte[] getOffsetSubset(int offset, long count) {
		return getSubset(offset, (int) (offset + count - 1));
	}
	
	public long getChunkSize() {
		byte[] chunk = this.getLittleEndianSubset(4, 7);
		return hexToDecimalUnsigned(chunk);
	}
	
	public int getBitSize() {
		byte[] bit = this.getEndianSubset(34, 2);
		//System.out.println(Arrays.toString(bit));
		return (int)hexToDecimalUnsigned(bit);
	}
	
	public long getSampleRate() {
		byte[] sampleRate = this.getEndianSubset(24,4);
		return hexToDecimalUnsigned(sampleRate);
	}
	
	public int getNumberOfChannels() {
		byte[] channel = this.getEndianSubset(22, 2);
		return (int)hexToDecimalUnsigned(channel);
	}
	
	public long getDataSize() {
		byte[] dataChunk = this.getEndianSubset(40, 4);
		return hexToDecimalUnsigned(dataChunk);
	}
	
	public byte[] getData() {
		byte[] data = this.getOffsetSubset(44, this.getDataSize());
		this.data = data;
		//System.out.println("data" + byteToHexString(getSubset(this.data, 0, 20) ));
		return data;
	}
	
	/** return int array of each channel **/
	public long[][] getSignals() {
		
		int bitJump = this.bit / 8; //how many bytes per sample
		System.out.println("bitjump " + bitJump);
		int loopLength = (int) (this.dataSize / this.channel / bitJump) ; //how many samples per signal
		//int loopLength = 10;
		System.out.println("loop length " + loopLength);
		long[][] signals = new long[channel][];
		int offset = 0; //offset for each channel
		byte[] newBit = new byte[bitJump];
		
		for (int k = 0; k < channel; ++k) {
			long[] signal = new long[loopLength];
			for (int i = 0; i < loopLength; i++) { 
				//System.out.println("i is " + i);
				for (int l = 0; l < bitJump; ++l) {
					//get byte array
					newBit[l] = this.data[(i * channel * bitJump) + l + offset];
					//System.out.println("looking at " + ((i * channel * bitJump) + l + offset));
				}
				//turn bytearray to int
				//System.out.println("signal val " + ((hexToDecimalSigned(newBit)) - 939524096));
				if (this.bit >= 32) {
					signal[i] = (long) hexToFloat(newBit);	 //FLOAT
				} else {
					signal[i] =  (hexToDecimalSigned(newBit));
				}
				
			}
			signals[k] = signal;
			//System.out.println(Arrays.toString(signals[k]));
			offset += bitJump;
		}
		return signals;
	}
	
	static String stringToNumbers(String str) {
		StringBuilder buf = new StringBuilder(str.length() * 5); //16 bit number has max value 65536, 5 chars long
		for (char ch: str.toCharArray()) {
			buf.append(String.valueOf((int)ch) + " ");
		}
		return buf.toString();
	}

	
	public WavReader() {
		super();
	}
	
	
}
