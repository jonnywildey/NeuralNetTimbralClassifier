package libraryclasses;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WordNumbers {
	/*dictionaries for mapping words to numbers */
	
	private static Map<String, Integer> numberNames = new HashMap<String, Integer>(){{
		put("first", 1);
		put("second",2);
		put("third", 3);
		put("fourth", 4);
		put("fifth", 5);
		put("sixth", 6);
		put("seventh", 7);
		put("eighth", 8);
		put("ninth", 9);
		put("tenth", 10);
		put("eleventh", 11);
		put("twelfth", 12);
		put("thirteenth", 13);
		put("fourteenth", 14);
		put("fifteenth", 15);
		put("sixteenth", 16);
		put("seventeenth", 17);
		put("eighteenth", 18);
		put("nineteenth", 19);
		put("twentieth", 20);
		put("twenty", 20);
		put("thirtieth", 30);
		put("thirty", 30);
		put("fourty", 40);
		put("fourtieth", 40);
		put("fifty", 50);
		put("fiftieth", 50);
		put("sixty", 60);
		put("sixtieth", 60);
		put("seventy", 70);
		put("seventieth", 70);
		put("eighty", 80);
		put("eightieth", 80);
		put("ninety", 90);
		put("ninetieth", 90);
		put("hundred", 100);
	}};
	
	private static Map<String, Integer> monthNames = new HashMap<String, Integer>() {{
		put("jan",1);
		put("feb", 2);
		put("mar", 3);
		put("apr",4);
		put("may",5);
		put("jun",6);
		put("jul",7);
		put("aug",8);
		put("sep",9);
		put("oct",10);
		put("nov",11);
		put("dec",12);
	}};
	
	
	public static int getYearValue(String year, boolean flag) {
		return 0;
	}
	
	public static int getMonthValue(String month, boolean flag) {
		/*give it a string with a month, sends back the int value of
		 * that month. if flag is true method is verbose. will not deal with
		 * strings that contain multiple months well.
		 */
		
		month = month.trim().toLowerCase();
		
		int x = 0;
		//This is the way you iterate through hashmaps...
		Iterator<String> keyIterator = monthNames.keySet().iterator();
		while(keyIterator.hasNext()){
			  String key = keyIterator.next();
			  if (month.contains(key) == true) {
				  x = monthNames.get(key);
				  break;
			  }
			  
			}
		//if flag is true, print stuff
		if(x == 0) {
			if (flag == true) {
				System.out.println("no month found");
			}
		}
		
		return x;
	}
	
	public static int getDateNumberValue(String dateValue, boolean flag) {
		/*give it a string with a dateValue, sends back the int value of
		 * that dateValue. if flag is true method is verbose. 
		 * Works with values up to 199 (I think)
		 */
		
		dateValue = dateValue.trim().toLowerCase();
		
		int x = 0;
		//This is the way you iterate through hashmaps...
		Iterator<String> keyIterator = numberNames.keySet().iterator();
		while(keyIterator.hasNext()){
			  String key = keyIterator.next();
			  if (dateValue.contains(key) == true) {
				  x += numberNames.get(key);
			  }
			  
			}
		//if flag is true, print stuff
		if(x == 0) {
			if (flag == true) {
				System.out.println("no numbers found");
			}
		}
		
		return x;
	}
	
	
	public static void main(String[] args) {
		System.out.print(getDateNumberValue("eighty fourth", true));
	}

}
