package filemanager;


/** kind of a weird shell thing for the StringWriter and StringReader classes*/
public class StringManager {
	
	public static String readFile(String file) {
		return new StringReader(file).readFile();
	}
	
	public static String readFile() {
		return new StringReader().readFile();
	}
	
	public static void writeFile(String file, String stringToPut) {
		StringWriter x = new StringWriter(file, stringToPut);
		x.writeTheString();
		x.close();
		System.out.print("String Written");
	}
	
	public static void appendFile(String file, String stringToPut) {
		StringAppender x = new StringAppender(file, stringToPut);
		x.appendTheString();
		x.close();
		System.out.print("String Written");
	}

}
