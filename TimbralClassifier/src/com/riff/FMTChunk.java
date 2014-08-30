package com.riff;

import com.util.ArrayMethods;
import com.util.HexByte;

/**
 * Header chunk for the Wave file Contains lots of format stuff *.
 * 
 * @author Jonny Wildey
 * @version 1.0
 */
public class FMTChunk extends Chunk {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new fMT chunk.
	 * 
	 * @param bytes
	 *            the bytes
	 */
	public FMTChunk(byte[] bytes) {
		super(bytes);
	}

	/**
	 * Constructor From Nothing *.
	 * 
	 * @param bit
	 *            the bit
	 * @param sampleRate
	 *            the sample rate
	 * @param channels
	 *            the channels
	 */
	public FMTChunk(int bit, int sampleRate, int channels) {
		int byteRate = sampleRate * channels * (bit / 8);
		int blockAlign = channels * (bit / 8);
		byte[] bytes = new byte[24];
		bytes = ArrayMethods.addBytes(bytes, HexByte.stringToBytes("fmt ", 4),
				0); // name
		bytes = ArrayMethods.addBytes(bytes,
				HexByte.longToLittleEndianBytes(16, 4), 4); // length
		bytes = ArrayMethods.addBytes(bytes,
				HexByte.longToLittleEndianBytes(1, 2), 8); // compression
		bytes = ArrayMethods.addBytes(bytes,
				HexByte.longToLittleEndianBytes(channels, 2), 10); // channels
		bytes = ArrayMethods.addBytes(bytes,
				HexByte.longToLittleEndianBytes(sampleRate, 4), 12); // samplerate
		bytes = ArrayMethods.addBytes(bytes,
				HexByte.longToLittleEndianBytes(byteRate, 4), 16); // byterate
		bytes = ArrayMethods.addBytes(bytes,
				HexByte.longToLittleEndianBytes(blockAlign, 2), 20); // blockalign
		bytes = ArrayMethods.addBytes(bytes,
				HexByte.longToLittleEndianBytes(bit, 2), 22); // bit
		this.bytes = bytes;
		this.name = new String(HexByte.getSubset(bytes, 0, 3));
	}

	/**
	 * Gets the bit size.
	 * 
	 * @return the bit size
	 */
	public int getBitSize() {
		byte[] bit = HexByte.getEndianSubset(this.bytes, 22, 2);
		return (int) HexByte.hexToDecimalUnsigned(bit);
	}

	/**
	 * Gets the channels.
	 * 
	 * @return the channels
	 */
	public int getChannels() {
		byte[] channel = HexByte.getEndianSubset(this.bytes, 10, 2);
		return (int) HexByte.hexToDecimalUnsigned(channel);
	}

	/**
	 * Gets the sample rate.
	 * 
	 * @return the sample rate
	 */
	public int getSampleRate() {
		byte[] sampleRate = HexByte.getEndianSubset(this.bytes, 12, 4);
		return (int) HexByte.hexToDecimalUnsigned(sampleRate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.riff.Chunk#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(100);
		if (hasBytes()) {
			sb.append(super.toStringNoData());
			sb.append("\nBits: " + this.getBitSize() + "\n");
			sb.append("Sample Rate: " + this.getSampleRate() + "\n");
			sb.append("Channels: " + this.getChannels());
		}
		return sb.toString();
	}

}
