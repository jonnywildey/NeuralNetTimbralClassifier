package filemanager;

import java.io.UnsupportedEncodingException;
import java.text.Format;
import java.util.ArrayList;

public class InfoChunkType {
	protected String name;
	protected byte[] byteName;
	protected long dataLength;
	protected byte[] data;
	
	public InfoChunkType(String name, byte[] byteName, long dataLength,
			byte[] data) {
		this.name = name;
		this.byteName = byteName;
		this.dataLength = dataLength;
		this.data = data;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n" + name);
		sb.append("\tLength: " + dataLength);
		//sb.append("\nData:\n" + HexByte.byteToLetterString(data));
		try {
			sb.append("\tData:\t" + new String(data, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	/** return byte array that can be inserted 
	 * into a RIFF file.
	 */
	public Byte[] toByteChunk() {
		ArrayList<Byte> bytes = new ArrayList<Byte>((int) (this.dataLength + 8));
		bytes = ArrayStuff.addBytes(bytes, this.name.getBytes());
		bytes = ArrayStuff.addBytes(bytes, HexByte.longToLittleEndianBytes(this.dataLength, 4));
		bytes = ArrayStuff.addBytes(bytes, this.data);
		Byte[] nb = new Byte[bytes.size()];
		return bytes.toArray(nb);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getByteName() {
		return byteName;
	}

	public void setByteName(byte[] byteName) {
		this.byteName = byteName;
	}

	public long getDataLength() {
		return dataLength;
	}

	public void setDataLength(long dataLength) {
		this.dataLength = dataLength;
	}

	public byte[] getData() {
		return data;
	}
	
	
	/** this should be a multiple of 4. **/
	public void setData(byte[] data) {
		this.data = data;
	}
	
	public void setData(String str) {
		this.data = HexByte.stringToBytes(str, 4);
		this.dataLength = this.data.length;
	}
	
	
	
	
}
