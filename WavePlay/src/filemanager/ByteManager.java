package filemanager;

public class ByteManager {
	/**readfile that asks for a prompt */
	public static byte[] readFile() {
		byte[] newByte = new ByteReader().readFile();
		return newByte;
	}
	
	public static byte[] readFile(String filePath) {
		byte[] newByte = new ByteReader(filePath).readFile();
		return newByte;
	}
	
	
	public static void writeFile(String filepath, byte[] fileToWrite) {
		ByteWriter bw = new ByteWriter(filepath, fileToWrite);
		bw.writeByteArray();
	}
	
}
