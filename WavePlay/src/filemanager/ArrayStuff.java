package filemanager;

import java.util.ArrayList;

public class ArrayStuff {

	public static ArrayList<Byte> addBytes(ArrayList<Byte> al, byte[] bytes) {
		for (byte b : bytes) {
			al.add(b);
		}
		return al;
	}
	
	public static byte[] bigByteTobyte(Byte[] bytes) {
		byte[] nb = new byte[bytes.length];
		for (int i = 0; i < bytes.length; ++i) {
			nb[i] = bytes[i];
		}
		return nb;
	}

}
