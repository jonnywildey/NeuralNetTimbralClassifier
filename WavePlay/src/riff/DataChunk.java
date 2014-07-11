package riff;

import filemanager.HexByte;
import filemanager.Log;


public class DataChunk extends Chunk{

	public DataChunk(byte[] bytes) {
		super(bytes);
	}
	
	
	
	
	/** return array of each channel's amplitude values. For bitRates less than 24 
	 * @throws BitRateException **/
	public long[][] getSignalsLong(int bit, int channel) {
		if (bit > 24) {
			Log.d("Converting floating wav to fixed point");
		}
		int bitJump = bit / 8; //how many bytes per sample
		int loopLength = (int) (this.getDataLength() / channel / bitJump) ; //how many samples per signal
		long[][] signals = new long[channel][];
		int offset = 0; //offset for each channel
		byte[] newBit = new byte[bitJump];
		byte[] data = this.getData();
		
		for (int k = 0; k < channel; ++k) {
			long[] signal = new long[loopLength];
			for (int i = 0; i < loopLength; i++) { 
				for (int l = 0; l < bitJump; ++l) {
					//get byte array
					newBit[l] = data[(i * channel * bitJump) + l + offset];
				}
				//turn bytearray to int				
				if (bit >= 32) {
					signal[i] = (long) HexByte.hexToFloat16(newBit);	 //FLOAT
				} else {
					signal[i] =  (HexByte.hexToDecimalSigned(newBit)); //NORMAL
				}
			}
			signals[k] = signal;
			offset += bitJump;
		}
		return signals;
	}
	
	/** return array of each channel's amplitude values. For bitRates 32 and above 
	 * @throws BitRateException **/
	public double[][] getSignalsDouble(int bit, int channel) {
		if (bit < 32) {
			Log.d("Converting fixed point wav to floating point");
		}
		int bitJump = bit / 8; //how many bytes per sample
		int loopLength = (int) (this.getDataLength() / channel / bitJump) ; //how many samples per signal
		double[][] signals = new double[channel][];
		int offset = 0; //offset for each channel
		byte[] newBit = new byte[bitJump];
		byte[] data = this.getData();
		
		for (int k = 0; k < channel; ++k) {
			double[] signal = new double[loopLength];
			for (int i = 0; i < loopLength; i++) { 
				for (int l = 0; l < bitJump; ++l) {
					//get byte array
					newBit[l] = data[(i * channel * bitJump) + l + offset];
				}
				//turn bytearray to int
				if (bit >= 32) {
					signal[i] = (long) HexByte.hexToFloat16(newBit);	 //FLOAT
				} else {
					signal[i] =  (HexByte.hexToDecimalSigned(newBit)); //NORMAL
				}
			}
			signals[k] = signal;
			offset += bitJump;
		}
		return signals;
	}
	
	
	@Override
	public String toString() {
		return toStringNoData();
	}
	
	@Override
	public String toStringRecursive() {
		return toStringNoData();
	}

}
