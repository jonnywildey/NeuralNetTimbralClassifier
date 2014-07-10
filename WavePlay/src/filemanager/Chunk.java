package filemanager;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.naming.InvalidNameException;


/** Basic RIFF type chunk object. In order to stop the whole thing being horribly confusing
 * most methods just read/write straight from the chunk byte array. However this can't really be done
 * for sub-chunks, so be aware that every time you make a sub chunk you are essentially copying the data.
 * As this will be mainly used for meta data I don't think this will be much of a problem **/
public class Chunk {
	protected String name;
	protected byte[] bytes; //the byte array of the entire chunk including
							//name and length
	protected ArrayList<Chunk> chunks; //may or may not have sub-chunks
	protected String[] acceptableSubChunks;
	
	public Chunk() {
		this.chunks = new ArrayList<Chunk>();
	}
	
	public Chunk(byte[] bytes) {
		this();
		this.bytes = bytes;
		this.name = new String(HexByte.getSubset(bytes, 0, 4));
		this.initTypes();
		this.initSubChunks();
	}
	
	/*generates a new byte chunk based upon data and name */
	private byte[] generateByteChunk() {
		if (this.hasChunks()) {
			byte[][] tb = new byte[this.chunks.size()][];
			for (int i = 0; i < this.chunks.size(); ++i) {
				tb[i] = this.chunks.get(i).generateByteChunk();
			}
			this.setData(ArrayStuff.tableToLongRow(tb));
		} 
		return bytes;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nName: " + name);
		sb.append("\tData Length: " + (this.bytes.length - 8));
		//sb.append("\nData:\n" + HexByte.byteToLetterString(data));
		try {
			sb.append("\tData: " + new String(ArrayStuff.getSubset(bytes, 8), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	

	public String toStringRecursive() {
		if (hasChunks()) {
			StringBuilder sb = new StringBuilder();
			sb.append("\nName: " + name);
			sb.append("\tData Length: " + (this.bytes.length - 8));
			for (Chunk c: this.chunks) {
				sb.append(c.toStringRecursive());
			}
			return sb.toString();
		} else {
			return this.toString();
		}
	}
	
	/** Does this chunk contain a chunk with name?
	 * Returns the position of chunk if yes and null if
	 * not **/
	protected Integer isThereSubChunk(String lc) {
		byte[] info = lc.getBytes();
		Integer in = HexByte.findBytes(info, this.bytes);
		return in;
	}
	
	public void initSubChunks() {
		if (acceptableSubChunks != null) {
			for (String lc : acceptableSubChunks) {
				Integer ic = isThereSubChunk(lc);
				if (ic != null) { //chunk exists
					long cs = getSubChunkSize(ic);
					chunks.add(new Chunk(getSubChunkBytes(ic, cs + 8)));
				}
			}
		}
	}
	
	/** Private method for pulling out subchunk data **/
	protected byte[] getSubChunkBytes(Integer ic, long size) {
		return HexByte.getOffsetSubset(bytes, ic, size);
	}
	
	/**get size of sub-chunk **/
	protected long getSubChunkSize(Integer ic) {
		return HexByte.hexToDecimalUnsignedLE(
				HexByte.getOffsetSubset(this.bytes, ic + 4, 4));
	}
	
	/**Returns the sub-chunk of this chunk if it exists **/
	public Chunk getSubChunk(String chunkType) {
		if (hasChunks()) {
			for (Chunk ict : chunks) {
				if (ict.name.equals(chunkType)) {
					return ict;
				}
			} 
		}
		return null;
	}
	

	
	/** returns true if this Chunk has any sub-chunks **/
	protected boolean hasChunks() {
		if (this.chunks != null) {
			return this.chunks.size() > 0;
		} else {
			return false;
		}
	}
	
	/** Returns true if sub-chunk exists **/
	public boolean hasSubChunk(byte[] chunkType) {
		if (this.hasChunks()) {
			for (Chunk ict : chunks) {
				if (ict.getByteName() == chunkType) {
					return true;
				}
			} 
		}
		return false;
	}
	
	/** Returns true if sub-chunk exists **/
	public boolean hasSubChunk(String chunkType) {
		if (this.hasChunks()) {
			for (Chunk ict : chunks) {
				if (ict.name.equals(chunkType)) {
					return true;
				}
			} 
		}
		return false;
	}
	
	/** adds a chunk to the chunkList **/
	public void AddChunk(Chunk chunk) {
		this.chunks.add(chunk);
		this.generateByteChunk();
	}
	
	public void removeChunk(Chunk chunk) {
		if (this.chunks.remove(chunk)) {
			this.generateByteChunk();
		}
	}
	
	public String getName() {
		return name;
	}

	
	
	/** return name of the chunk (in bytes) **/
	public byte[] getByteName() {
		return HexByte.getSubset(bytes, 0, 4);
	}
		
	
	/** sets name of the chunk 
	 * @throws InvalidNameException **/
	public void setName(byte[] byteName) throws InvalidNameException {
		if (byteName.length > 4) {
			throw new InvalidNameException("Name too long!");
		}
		byteName = HexByte.roundUpBytes(byteName, 4);
		this.name = new String(byteName);
		if (this.bytes != null) {
			this.bytes = ArrayStuff.addBytes(this.bytes, byteName, 0);
		}
	}
	
	/** Set name of the chunk 
	 * @throws InvalidNameException **/
	public void setName(String name) throws InvalidNameException {
		this.name = name;
		setName(HexByte.stringToBytes(name, 4));
	}
	
	
	/** return length of data (not including head) **/
	public long getDataLength() {
		return this.bytes.length - 8;
	}
	
	/** return length of bytes (including head) **/
	public long getBytesLength() {
		return this.bytes.length;
	}
		
	/**returns the raw byte data of chunk, including name and length **/
	public byte[] getBytes() {
		return bytes;
	}
	
	/**Returns the sub-chunk List**/
	public ArrayList<Chunk> getSubChunks() {
		return this.chunks;
	}
	
	
	/** set chunk data  using raw byte array. 
	 * Clears sub-chunk list **/
	public void setData(byte[] data) {
		data =  HexByte.roundUpBytes(data, 4);
		this.bytes = new byte[data.length + 8];
		bytes = ArrayStuff.addBytes(bytes, HexByte.stringToBytes(this.name, 4), 0);
		bytes = ArrayStuff.addBytes(bytes, HexByte.longToLittleEndianBytes(data.length, 4), 4);
		bytes = ArrayStuff.addBytes(bytes, data, 8);	
	}
	
	public void setData(String str) {
		setData(HexByte.stringToBytes(str, 4));
	}

	protected void initTypes() {
		//For overriding
	}
	
}
