package libraryclasses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.text.*;

import libraryclasses.WordNumbers;
import libraryclasses.LogWin;
public class Time {
	
	
	private static char[] dateSeparators = {'/','-',',','.', ' ', ':'};

	
	public static Date UKDateConverter(String dateString) {
		/* Converts a UK date into a date object */
		if (dateString.isEmpty() == true) {LogWin.messagePrintNoCancel("Sent empty string"); return null;}
		DateFormat ukDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		ukDateFormat.setLenient(true);
		//Date Parsing
		try {
			Date newDate = ukDateFormat.parse(dateString);
			return newDate;
					} catch(ParseException e) {
			LogWin.messagePrintNoCancel("Error! Couldn't read date");
			return null;
		}
		
	}
	public static Date vagueDateConverter(String dateString, boolean printDateFormat) {
		Calendar cr = vagueDateConverterCal(dateString, printDateFormat);
		Date dt = cr.getTime();
		return dt;
	}
	 
	
	public static Calendar vagueDateConverterCal(String dateString, boolean printDateFormat) {
		/* Splits up a vague date string into its components, 
		 * returns a date format of the string, or says it can't.
		 * If it ambiguous assumes UK date system. If printDateFormat is true,
		 * console prints dateFormat.
		 */
		
		boolean flags = true;
		
		ArrayList<String> dateComponents = new ArrayList<String>();
		
		if (dateString.isEmpty() == true) {LogWin.messagePrintNoCancel("Sent empty string"); return null;}
		//Since we do NOT want this to be case specific convert the string to lowercase;
		dateString = dateString.toLowerCase();
		dateString = dateString.trim();
				
		//looks for first separator. Due to null int not existing and no date ever starting with a separator
		//assumes 0 means no separators.
		
		//Lets assume there will be a date separator.
		//int countstr = 0;
		int a = findDateSeparator(dateString, 0, flags);
			do {				
				dateComponents.add(dateString.substring(0, a).trim());
				dateString = dateString.substring(a + 1, dateString.length()).trim();
				//a + 1 as we don't want the separator
				a = findDateSeparator(dateString, 0, flags);
				System.out.println("a = " + a);
				System.out.println(dateString);
			} while (a != 0);
			dateComponents.add(dateString);
			
			System.out.println(Arrays.toString(dateComponents.toArray()));
			
			for (int i = 0; i < dateSeparators.length; ++i) {
				
			}	
			
			int[] dateOrder = dateComponentOrder();
			
			System.out.println(Arrays.toString(dateOrder));
			
			//Years. haven't done a year word converter so this catches.
			int years = 0;
			int months = 0;
			int days = 0;
			int minutes = 0;
			int seconds = 0;
			int milliseconds = 0;
			
			try {years = yearExtender(Integer.parseInt(dateComponents.get(dateOrder[0])));} catch (Exception e) {LogWin.messagePrintNoCancel("Can't understand year");}
			System.out.println(years);
			
			
			//Months 
			if (isLetterOrNumber(dateComponents.get(dateOrder[1])) == 1) {months = WordNumbers.getMonthValue(dateComponents.get(dateOrder[1]), flags);} else {
			months = Integer.parseInt(dateComponents.get(dateOrder[1]));	
			}
			--months; 
			System.out.println(months);
			//days
			if (isLetterOrNumber(dateComponents.get(dateOrder[2])) == 1) {days = WordNumbers.getDateNumberValue(dateComponents.get(dateOrder[2]), flags);} else {
				days = Integer.parseInt(dateComponents.get(dateOrder[2]));	
				}
			
			//asumption that 4 and 5 must be minutes and seconds etc. no word conversion yet so shady catch exceptions
			if (dateComponents.size() > 3) { 
				try {minutes = Integer.parseInt(dateComponents.get(3));} catch(Exception e) {LogWin.messagePrintNoCancel("Couldn't understand minutes");}
			}
			if (dateComponents.size() > 4) { 
				try {seconds = Integer.parseInt(dateComponents.get(4));} catch(Exception e) {LogWin.messagePrintNoCancel("Couldn't understand seconds");}
			}
			
			if (dateComponents.size() > 5) { 
				try {milliseconds = Integer.parseInt(dateComponents.get(5));} catch(Exception e) {LogWin.messagePrintNoCancel("Couldn't understand milliseconds");}
			}	
			
			Calendar cr = Calendar.getInstance();
			cr.set(years,  months, days, minutes, seconds, milliseconds);
			return cr;

		
		}
		
	
	private static int isMonth(String str) {
		/*determines whether a string is definitely a month (1), could be a month (2)
		 * or is definitely not a month (0)
		 */
		if (str.isEmpty() == true) {LogWin.messagePrintNoCancel("Sent empty string"); return 0;}
		int a = isLetterOrNumber(str);
		if (a == 1) {
			int x = WordNumbers.getMonthValue(str, true);	
			if (x > 0) {return 1;} else {return 0;}
		} else {
			
			try {int x = Integer.parseInt(str);
				if (x > 0 && x < 13) {return 2;} else {return 0;}
			} catch (Exception e) {LogWin.messagePrintNoCancel("Couldn't understand the \"month\" you gave"); return 0;}
		}
	}
	
		private static int isYear(String str) {
			/*determines whether a string is definitely a day (1), could be a day (2)
			 * or is definitely not a year (0). No getYearNumber yet. No BC. 
			 * Works best when cascading with isMonth and isDay
			 */	
			if (str.isEmpty() == true) {LogWin.messagePrintNoCancel("Sent empty string"); return 0;}
			int a = isLetterOrNumber(str);
			if (a == 1) {
				int x = WordNumbers.getYearValue(str, true);	
				if (x > 0) {return 1;} else {return 0;}
			} else {
				
				try {int x = Integer.parseInt(str);
					if (x > 0) {return 2;} else {return 0;}
				} catch (Exception e) {LogWin.messagePrintNoCancel("Couldn't understand the \"day\" you gave"); return 0;}
			}
			
		}
	
	
		private static int isDay(String str) {
			/*determines whether a string is definitely a day (1), could be a day (2)
			 * or is definitely not a day (0)
			 */
			if (str.isEmpty() == true) {LogWin.messagePrintNoCancel("Sent empty string"); return 0;}
			int a = isLetterOrNumber(str);
			if (a == 1) {
				int x = WordNumbers.getDateNumberValue(str, true);	
				if (x > 0) {return 1;} else {return 0;}
			} else {
				
				try {int x = Integer.parseInt(str);
					if (x > 0 && x < 32) {return 2;} else {return 0;}
				} catch (Exception e) {LogWin.messagePrintNoCancel("Couldn't understand the \"day\" you gave"); return 0;}
			}
		
		
		
		
	}
	
	
	
	private static int[] dateComponentOrder() {
		/* returns the year, month, day order of a date format in an int */
		int[] x = {2,1,0};
		
		//Gets locale date format, assumes this is the correct date format
		//SimpleDateFormat localDate = new SimpleDateFormat();
		//DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		
		/*int month = df.MONTH_FIELD;
		int year = df.YEAR_FIELD;
		int day = df.DAY_OF_WEEK_IN_MONTH_FIELD;
		
		if (year < month && year < day) {x[0] = 0;} else if (year < month || year < day) {x[0] = 1;} else {x[0] = 2;} 
		if (month < year && month < day) {x[1] = 0;} else if (month < day || month < year) {x[1] = 1;} else {x[1] = 2;} 
		if (day < year && day < month) {x[2] = 0;} else if (day < month || day < year) {x[2] = 1;} else {x[2] = 2;} */
		
		return x;
		
	}
	
	
	private static int yearExtender(int year) {
		/* give it a fragment of a year, it outputs the 
		 * whole year. i.e. 12 -> 2012
		 */
		
		if (year > 99) {
			return year;
		}
		
		Date now = new Date();
		DateFormat years = new SimpleDateFormat("yy");
		int centurySeparator = Integer.parseInt(years.format(now));
		 if (year < centurySeparator) {
			 return (year + 2000);
		 } else {
			return (year + 1900);
		 }

	}
	
	private static int findDateSeparator(String str, int startInt, boolean printSymbol) {
		/* searches through a string looking for the first instance of a 
		 * date separator symbol. returns index pos of string where
		 * it occurs.
		 */
		if (str.isEmpty() == true) {LogWin.messagePrintNoCancel("Sent empty string"); return 0;}
		if (printSymbol == true) {
		System.out.println("start int is " + startInt + "\t stringlength is " + str.length() );	
		if (startInt > str.length()) {System.out.println("startInt greater than string length"); return 0;}
		}
		//Runs through string index, then through charArray, making sure the first date separator 
		//is found
		for (int i = startInt; i < str.length(); ++i) {
			for (int j = 0; j < dateSeparators.length; ++j) {
				
				if (str.charAt(i) == dateSeparators[j]) {

					if (printSymbol == true) {System.out.println("separated symbol is " + dateSeparators[j]);}
					return i;
				}
			}
		}
			
		
		return 0;
		
	}
	
	private static int isLetterOrNumber(String x) {
		/* Returns 1 if string contains any nonnumber chars */
		if (x.isEmpty() == true) {LogWin.messagePrintNoCancel("Sent empty string"); return 0;}
		char c;
		for (int i = 0; i < x.length(); ++i) {
			c = x.charAt(i);
			if (isLetterOrNumber(c) == 1) {return 1;}
		}
		return 2;
		
	}
	
	private static int isLetterOrNumber(char x) {
		/* Returns 1 if character is letter, 2 if character is number
		 * 0 if neither
		 */
		
		
		int a = (int)x;
		//System.out.println("a is" + a);
		
		if ((a > 64 && a < 92) || (a > 96 && a < 123)) {
			//System.out.println(1);
			return 1;
		}
		if (a > 47 || a < 48) {
			//System.out.println(2);
			return 2;
		}
		//System.out.println(0);
		return 0;
		
	}
	
	
	public static void main(String[] args) throws ParseException {
		//DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		
		System.out.print(vagueDateConverter("twentyfirst November 1856 15:32", true));
		//Date date = new Date();
		//Date a = UKDateConverter("10/10/2034");
		//System.out.println(a);
		//DateFormat b = new SimpleDateFormat("MM/dd/yyyy");
		//Date newDate = b.parse("11/09/2045");
		//System.out.print(newDate);
		
	}
}
