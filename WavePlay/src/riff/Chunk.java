package riff;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.naming.InvalidNameException;

import filemanager.ArrayMethods;
import filemanager.HexByte;
import filemanager.Log;


/** Basic RIFF type chunk object. In order to stop the whole thing being horribly confusing
 * most methods just read/write straight from the chunk byte array. However this can't really be done
 * for sub-chunks, so be aware that every time you make a sub chunk you are essentially copying the data.
 * As this will be mainly used for meta data I don't think this will be much of a problem **/

public class Chunk implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 780237399568607900L;
	protected String name;
	protected byte[] bytes; //the byte array of the entire chunk including
							//name and length
	protected ArrayList<Chunk> chunks; //may or may not have sub-chunks
	protected String[] acceptableSubChunks;
	protected static int dataOffset = 8; //amount of bytes before actual data. Normally 8
	
	public Chunk() {
		this.chunks = new ArrayList<Chunk>();
	}
	
	public Chunk(byte[] bytes) {
		this();
		this.bytes = bytes;
		this.name = new String(HexByte.getSubset(bytes, 0, 3));
		this.initTypes();
		this.initSubChunks();
	}
	
	/*generates a new byte chunk based upon data and name */
	private byte[] generateByteChunk() {
		if (this.hasSubChunks()) {
			byte[][] tb = new byte[this.chunks.size()][];
			for (int i = 0; i < this.chunks.size(); ++i) {
				tb[i] = this.chunks.get(i).generateByteChunk();
			}
			this.setData(ArrayMethods.tableToLongRow(tb));
		} 
		return bytes;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (hasBytes()) {
			sb.append("\nName: " + name);
			sb.append("\tData Length: " + (this.bytes.length - dataOffset));
			sb.append("\tData: " + new String(ArrayMethods.getSubset(bytes, dataOffset)));
		}
		return sb.toString();
	}
	

	public String toStringRecursive() {
		if (hasSubChunks()) {
			StringBuilder sb = new StringBuilder();
			sb.append("\nName: " + name);
			sb.append("\tData Length: " + (this.bytes.length - dataOffset));
			for (Chunk c: this.chunks) {
				sb.append(c.toStringRecursive());
			}
			return sb.toString();
		} else {
			return this.toString();
		}
	}
	
	protected String toStringNoData() {
		StringBuilder sb = new StringBuilder();
		if (hasBytes()) {
			sb.append("\nName: " + name);
			sb.append("\tData Length: " + (this.bytes.length - dataOffset));
		}
		return sb.toString();
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
				//System.out.println(lc + " " + ic);
				if (ic != null) { //chunk exists
					long cs = getSubChunkSize(ic);
					chunks.add(makeChunk(lc, ic, cs));
				}
			}
		}
	}
	
	/** kind of lame method that determines which class
	 * should be made. length input is PRE offset**/
	private Chunk makeChunk(String name, int start, long Datalength) { 
		Chunk chunk;
		byte[] tBytes;
		switch (name) {
		case "LIST":
			tBytes = getSubChunkBytes(start, Datalength + InfoChunk.dataOffset);
			chunk = new InfoChunk(tBytes);
			break;
		case "fmt ":
			tBytes = getSubChunkBytes(start, Datalength + FMTChunk.dataOffset);
			chunk = new FMTChunk(tBytes);
			break;
		case "data":
			tBytes = getSubChunkBytes(start, Datalength + DataChunk.dataOffset);
			chunk = new DataChunk(tBytes);
			break;
		default:
			tBytes = getSubChunkBytes(start, Datalength + Chunk.dataOffset);
			chunk = new Chunk(tBytes);
			break;
		}
		return chunk;
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
		if (hasSubChunks()) {
			for (Chunk ict : chunks) {
				if (ict.name.equals(chunkType)) {
					return ict;
				}
			} 
		}
		return null;
	}
	

	
	/** returns true if this Chunk has any sub-chunks **/
	public boolean hasSubChunks() {
		if (this.chunks != null) {
			return this.chunks.size() > 0;
		} else {
			return false;
		}
	}
	
	/** Returns true if sub-chunk exists **/
	public boolean hasSubChunk(byte[] chunkType) {
		if (this.hasSubChunks()) {
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
		if (this.hasSubChunks()) {
			for (Chunk ict : chunks) {
				if (ict.name.equals(chunkType)) {
					return true;
				}
			} 
		}
		return false;
	}
	
	/** adds a chunk to the chunkList **/
	public void addChunk(Chunk chunk) {
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
	
	public byte[] getData() {
		return ArrayMethods.getSubset(bytes, 8);
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
			this.bytes = ArrayMethods.addBytes(this.bytes, byteName, 0);
		}
	}
	
	/** Writes the bytes to a file **/
	public void writeFile(File f) {
		try {
			FileOutputStream fileOut = new FileOutputStream(f.getAbsoluteFile());
			fileOut.write(this.bytes);
			fileOut.close();
			Log.d("File " + f.getName() + "written successfully");
		} catch (Exception e) {
			Log.bad("Couldn't write file.");
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
	
	/**Does this object actually have any information
	 * or is it an empty shell?
	 */
	protected boolean hasBytes() {
		return (this.bytes != null);
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
		bytes = ArrayMethods.addBytes(bytes, HexByte.stringToBytes(this.name, 4), 0);
		bytes = ArrayMethods.addBytes(bytes, HexByte.longToLittleEndianBytes(data.length, 4), 4);
		bytes = ArrayMethods.addBytes(bytes, data, 8);	
	}
	
	public void setData(String str) {
		setData(HexByte.stringToBytes(str, 4));
	}

	protected void initTypes() {
		//For overriding
	}
	
}
