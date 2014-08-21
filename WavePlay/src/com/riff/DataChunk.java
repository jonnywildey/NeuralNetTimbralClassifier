package com.riff;

import com.util.ArrayMethods;
import com.util.HexByte;
import com.util.Log;


public class DataChunk extends Chunk{

	public DataChunk(byte[] bytes) {
		super(bytes);
	}
	
	
	public DataChunk(Signal signal) {
		signalToByte(signal);
		
	}
	
	public void signalToByte(Signal signal) {
		int bj = signal.getBit() / 8;
		int channels = signal.getChannels();
		int jump = bj * channels;
		//int cj = bj / channels;
		double[][] s = signal.getSignal();
		byte[] nb = new byte[s[0].length * channels * bj];
		for (int i = 0; i < s[0].length; ++i) {
			for (int j = 0; j < channels; ++j) {
				//Log.d(i + " " + s[j][i]);
				nb = ArrayMethods.addBytes(nb, 
						HexByte.doubleToLittleEndianBytes( //need to make this work for floats
								s[j][i], bj), (i*jump) + (j*bj));
			}
			
		}
		doHeader(nb);
	}
	
	private void doHeader(byte[] data) {
		this.bytes = new byte[data.length + 8];
		this.bytes = ArrayMethods.addBytes(bytes, HexByte.stringToBytes("data", 4), 0);
		this.bytes = ArrayMethods.addBytes(bytes, HexByte.longToLittleEndianBytes(data.length, 4), 4);
		this.bytes = ArrayMethods.addBytes(bytes, data, 8);	
		this.name = new String(HexByte.getSubset(bytes, 0, 3));
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
					signal[i] =  HexByte.hexToFloat16(newBit);	 //FLOAT
				} else {
					signal[i] =  (HexByte.hexToDecimalSigned(newBit)); //FIXED
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
