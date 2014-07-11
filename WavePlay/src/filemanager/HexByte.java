package filemanager;


/** a bunch of utility methods for hex and byte conversion **/
public class HexByte {

	public HexByte() {
	}

	/** converts byte array to hex array **/
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

	/**converts byte array to hex array. little Endian**/
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
	
	/**Converts an array of byte values to an integer. Unsigned **/
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

	/**hexadecimal conversion. Unsigned with Little Endian **/
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
	
	/**Converts hex to double. 
	 * 
	 */
	public static double hexToFloat(byte[] bytes) {
		int[] sep = convertByteArrayLE(bytes); //we now have an array of the correct numbers
		int n = (int)hexArrayToInt(sep); 
		//System.out.println(Float.intBitsToFloat(n) * 65536);
		return Float.intBitsToFloat(n);
	}
	
	/**Converts hex to double approximating a 16 bit fixed point value 
	 * 
	 */
	public static double hexToFloat16(byte[] bytes) {
		int[] sep = convertByteArrayLE(bytes); //we now have an array of the correct numbers
		int n = (int)hexArrayToInt(sep); 
		//System.out.println(Float.intBitsToFloat(n) * 65536);
		return Float.intBitsToFloat(n) * 65536;
	}
	
	
	/**Converts single byte to decimal **/
	public static int hexToDecimal(byte b) {
		StringBuffer buf = new StringBuffer(1);
			buf.append(b);
		return Integer.parseInt(buf.toString(), 16);
	}

	/** converts array of bytes into somewhat readable hex format **/
	public static String byteToHexString(byte[] bytes) {
			StringBuilder buf = new StringBuilder(bytes.length * 5);
			int c4 = 1;
			int c16 = 1;
			for (byte bt: bytes) {
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
	
	
	/** Converts bytes to fairly unreadable char string **/
	public static String byteToLetterString(byte[] bytes) {
		StringBuilder buf = new StringBuilder(bytes.length * 5);
		for (byte bt: bytes) {
			String s = String.valueOf((char)bt);
			s += " ";
			buf.append(s);
			
		}
		return buf.toString();
	}
	
	
	public static byte[] getSubset(byte[] bytes, int start, int end) {
		return ArrayStuff.getSubset(bytes, start, end);
	}
	
	/** returns Little Endian (reversed) subset of bytes **/
	public static byte[] getLittleEndianSubset(byte[] bytes, int start, int end) {
		byte[] newBytes = new byte[(end + 1) - start];
		for (int i = 0; i <= (end - start); ++i) {
			newBytes[newBytes.length - i - 1] = bytes[start + i];
			//System.out.println(newBytes[newBytes.length - i - 1]);
		}
		return newBytes;
	}

	public static byte[] getEndianSubset(byte[] bytes, int offset, long count) {
		return getLittleEndianSubset(bytes, offset, (int) (offset + count - 1));
	}

	public static byte[] getOffsetSubset(byte[] bytes, int offset, long count) {
		return ArrayStuff.getSubset(bytes, offset, (int) (offset + count - 1));
	}

	public static byte[] getSubset(byte[] bytes, long offset, long length) {
		return ArrayStuff.getSubset(bytes, (int)offset, (int)length);
	}
	
	/** returns the position of bytes if within, null if not found **/
	public static Integer findBytes(byte[] toFind, byte[] within) {
		for (int i = 0; i < within.length; ++i) {
			//System.out.println("i is: " + i);
			if (findBytesIsEqual(toFind, within, 0, i)) {
				return i;
			}
		}
		return null;
	}
	
	private static boolean findBytesIsEqual(byte[] toFind, byte[] within, int tf, int wi) {
		//System.out.println("Checking: " + toFind[tf] + " + " + within[wi]);
		if (tf >= toFind.length) {
			return true; //correct!
		} else if (wi >= within.length) {
			return false; //gotten to end of within, so no match
		}	else {
			if (toFind[tf] == within[wi]) { //next step
				return findBytesIsEqual(toFind, within, tf + 1, wi + 1); 
			} else {
				return false; //also no match
			}
		}
	}
	
	public static long byteSum(byte[] bytes) {
		long c = 0;
		for (byte b : bytes) {
			c += b;
		}
		return c;
	}
	
	
	
	/**cheap and cheerful number to byte converter. If length is too
	 * small things will go wrong.
	 */
	public static byte[] longToLittleEndianBytes(long val, int length) {
		byte[] bytes = new byte[length];
		long mod = 0;
		int q = 256;
		for (int i = 0; i < bytes.length; ++i) {
			mod = val % q;
			val /= q;
			bytes[i] = (byte) mod;
		}
		return bytes;
	}
	
	public static byte[] stringToBytes(String str, int byteRound) {
		byte[] bytes = str.getBytes(); //check charset is UTF-16
		return roundUpBytes(bytes,byteRound);
		
	}
	
	
	/** Adds empty bytes to a byte array to allow it to be a multiple 
	 * e.g. roundUpBytes([1, 5], 4) => [1,5,0,0]**/
	public static byte[] roundUpBytes(byte[] bytes, int byteRound) {
		if (bytes.length % byteRound == 0) {
			return bytes;
		} else {
			byte[] nBytes = new byte[bytes.length + (bytes.length % byteRound)];
			for (int i = 0; i < nBytes.length; ++i) {
				if (i < bytes.length) {
					nBytes[i] = bytes[i]; //copy
				} else {
					nBytes[i] = 0; //add 0
				}
			}
			return nBytes;
		}
	}
	

}
