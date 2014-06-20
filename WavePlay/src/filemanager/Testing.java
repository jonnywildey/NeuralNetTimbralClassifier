package filemanager;

import java.util.ArrayList;
import java.util.Arrays;


public class Testing {
	
	public static void main(String[] args) {
			
			/*int[][] testArray = {{312312,123,54,6567,123},{324,4564,12,76,867}};
			//char[] testArray = {23,65,23,35,675,334,74};
			CSVWriter z = new CSVWriter();
			z.writeArraytoFile(testArray);
			
			CSVReader x = new CSVReader();
			x.readFile();
			String[][] c = x.makeStringArray();
			System.out.print(Arrays.deepToString(c));
			x.close(); */
		
		byte[] z = (new ByteReader().readFile());
		//byte[] y = {3,6,12,12, 98, 45, 34, 26, 8, 78, 76,  };
		ByteWriter x = new ByteWriter("/Users/Jonny/Documents/numbers.txt", z);
		x.writeByteArray();
		
		
	/*String x = "aaaaaaaabbbbbbbccccccdddddeeeeeffff";
	String pathname = "/Users/Jonny/Documents/numbers.txt";
	StringWriter sw = new StringWriter(pathname, x);
	sw.writeTheString();
	sw.appendTheString();
	sw.appendTheString();
	sw.close(); */
		
		//String s = "63562382 \n 876876876";
		//StringManager.writeFile("/Users/Jonny/Documents/numbers.txt", s);
	}

	
}
