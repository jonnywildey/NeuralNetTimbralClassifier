package com.riff;

import javax.naming.InvalidNameException;

import com.util.ArrayMethods;
import com.util.HexByte;


/**
 * A class for determining what information there is in an INFO chunk
 * Written more for readability than efficiency*.
 *
 * @author Jonny Wildey
 * @version 1.0
 */
public class InfoChunk extends Chunk{
	
	protected static int dataOffset = 12;

	/**
	 * Instantiates a new info chunk.
	 *
	 * @param chunk the chunk
	 */
	public InfoChunk(byte[] chunk) {
		super(chunk);
	}
	
	/**
	 * Instantiates a new info chunk.
	 */
	public InfoChunk() {
		super();
		try {
			this.setName("LIST");
		} catch (InvalidNameException e) {
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.riff.Chunk#initTypes()
	 */
	@Override
	protected void initTypes() {
		/* All the official chunk types you can get in INFO */
		String[] s = {"AGES",	"CMNT",	
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
		this.acceptableSubChunks = s;
	}
	
	/* (non-Javadoc)
	 * @see com.riff.Chunk#setData(byte[])
	 */
	@Override
	//List has a weird Header...
	public void setData(byte[] data) {
		data =  HexByte.roundUpBytes(data, 4);
		this.bytes = new byte[data.length + 12];
		//Should say list
		bytes = ArrayMethods.addBytes(bytes, HexByte.stringToBytes(this.name, 4), 0);
		bytes = ArrayMethods.addBytes(bytes, HexByte.longToLittleEndianBytes(data.length, 4), 4);
		bytes = ArrayMethods.addBytes(bytes, HexByte.stringToBytes("INFO", 4), 8);
		bytes = ArrayMethods.addBytes(bytes, data, 12);	
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
