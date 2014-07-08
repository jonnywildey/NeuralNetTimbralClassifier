package filemanager;

import java.util.ArrayList;

/**A class for determining what information there is in an INFO chunk 
 * Written more for readability than efficiency**/
public class ListChunk {
	private byte[] chunk;
	public ArrayList<InfoChunkType> chunks;
	/* All the official chunk types you can get in INFO */
	private String[] chunkTypes = {"AGES",	"CMNT",	
			"CODE",	"COMM",	"DIRC",	
			"DISP",	"DTIM",	"GENR",	"IARL",	"IART",	
			"IAS1",	"IAS2",	"IAS3",	"IAS4",	"IAS5",	
			"IAS6",	"IAS7",	"IAS8",	"IAS9",	"IBSU",	
			"ICAS",	"ICDS",	"ICMS",	"ICMT",	"ICNM",	
			"ICNT",	"ICOP",	"ICRD",	"ICRP",	"IDIM",	
			"IDPI",	"IDST",	"IEDT",	"IENC",	"IENG",	
			"IGNR",	"IKEY",	"ILGT",	"ILGU",	"ILIU",	
			"ILNG",	"IMBI",	"IMBU",	"IMED",	"IMIT",	
			"IMIU",	"IMUS",	"INAM",	"IPDS",	"IPLT",	
			"IPRD",	"IPRO",	"IRIP",	"IRTD",	"ISBJ",	
			"ISFT",	"ISGN",	"ISHP",	"ISRC",	"ISRF",	
			"ISTD",	"ISTR",	"ITCH",	"IWMU",	"IWRI",	
			"LANG",	"LOCA",	"PRT1",	"PRT2",	"RATE",	
			"STAR",	"STAT",	"TAPE",	"TCDO",	"TCOD",	
			"TITL",	"TLEN",	"TORG",	"TRCK",	"TURL",	
			"TVER",	"VMAJ",	"VMIN",	"YEAR"};
	
	

	public ListChunk(byte[] chunk) {
		this();
		this.chunk = chunk;
	}
	
	public ListChunk() {
		this.chunks = new ArrayList<InfoChunkType>();
	}
	
	
	
	private Integer isThereLittleChunk(String lc) {
		byte[] info = lc.getBytes();
		//System.out.println(lc);
		Integer in = HexByte.findBytes(info, this.chunk);
		//System.out.println(in);
		return in;
	}
	
	public void initChunkTypes() {
		for (String lc : chunkTypes) {
			Integer ic = isThereLittleChunk(lc);
			if (ic != null) { //chunk exists
				long cs = getLittleChunkSize(ic);
				chunks.add(new InfoChunkType(lc, lc.getBytes(), 
							cs, getLittleChunkData(ic, cs) ));
			}
		}
	}
	
	private long getLittleChunkSize(Integer ic) {
		return HexByte.hexToDecimalUnsignedLE(
				HexByte.getOffsetSubset(chunk, ic + 4, 4));
	}
	
	private byte[] getLittleChunkData(Integer ic, long size) {
		return HexByte.getOffsetSubset(chunk, ic + 8, size);
	}
	
	public byte[] getChunk() {
		return chunk;
	}
	
	public InfoChunkType getLittleChunk(String chunkType) {
		for (InfoChunkType ict : chunks) {
			if (ict.name.equals(chunkType)) {
				return ict;
			}
		} 
		return null;
	}
	
	public boolean hasLittleChunk(String chunkType) {
		for (InfoChunkType ict : chunks) {
			if (ict.name.equals(chunkType)) {
				return true;
			}
		} 
		return false;
	}
	
	public boolean hasLittleChunk(byte[] chunkType) {
		for (InfoChunkType ict : chunks) {
			if (ict.byteName == chunkType) {
				return true;
			}
		} 
		return false;
	}
}
