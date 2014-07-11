package riff;

import filemanager.ArrayStuff;
import filemanager.HexByte;

/** Essentially the header chunk for the Wave file
 * Contains lots of format stuff **/
public class FMTChunk extends Chunk{

	public FMTChunk(byte[] bytes) {
		super(bytes);
	}
	
	public int getBitSize() {
		byte[] bit = HexByte.getEndianSubset(this.bytes, 22, 2);
		return (int)HexByte.hexToDecimalUnsigned(bit); 
	}
	
	public long getSampleRate() {
		byte[] sampleRate = HexByte.getEndianSubset(this.bytes, 12,4);
		return HexByte.hexToDecimalUnsigned(sampleRate);
	}
	
	public int getChannels() {
		byte[] channel = HexByte.getEndianSubset(this.bytes, 10, 2);
		return (int)HexByte.hexToDecimalUnsigned(channel);
	}
	
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
