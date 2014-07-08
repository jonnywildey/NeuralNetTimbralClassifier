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
	
	/**Converts a 32 bit hex array to a 32bit float. 
	 * Also multiplies by 2^16 to get some useful amp values
	 */
	public static double hexToFloat(byte[] bytes) {
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
	
	public static String byteToLetterString(byte[] bytes) {
		StringBuilder buf = new StringBuilder(bytes.length * 5);
		int c4 = 1;
		int c16 = 1;
		for (byte bt: bytes) {
			String s = String.valueOf((char)bt);
			s += " ";
			//if (c4 % 1 == 0) {s += "\n";}
			//if (c16 % 4 == 0) {s += "\n";}
			
			buf.append(s);
			c4++;
			c16++;
			
		}
		return buf.toString();
	}
	

	public static byte[] getSubset(byte[] bytes, int start, int end) {
		byte[] newBytes = new byte[(end + 1) - start];
		for (int i = 0; i <= (end - start); ++i) {
			newBytes[i] = bytes[start + i];
		}
		return newBytes;
	}

	/** returns Little Endian subset of the wav bytes **/
	static byte[] getLittleEndianSubset(byte[] bytes, int start, int end) {
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
		return getSubset(bytes, offset, (int) (offset + count - 1));
	}

	public static byte[] getSubset(byte[] bytes, long offset, long length) {
		return getSubset(bytes, (int)offset, (int)length);
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
	
	/**bits must be divisible by 8 **/
	public static String hexStringToLittleEndianHexString(String str, int bit) {
		int bc = bit / 8;
		//flip bits first
		char[] ca = str.toCharArray();
		String sa = "";
		for (int i = 0; i < ca.length; i += bc) {
			char[] nc = new char[bc];
			str.getChars(i, i + bc, nc, 0);
			nc = reverseChars(nc);
			sa = sa.concat(new String(nc));
		}
		return sa;
	}
	
	private static char[] reverseChars(char[] ca) {
		char ch = '0';
		int lim = (int) Math.rint((double)ca.length / 2); //only need to travel halfway
		for (int i = 0; i < lim; ++i) {
			//reverse
			ch = ca[i];
			ca[i] = ca[ca.length - 1 - i];
			ca[ca.length - 1 - i] = ch;
		}
		return ca;
	}
	
	

}
