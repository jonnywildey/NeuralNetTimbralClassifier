package libraryclasses;

public class Random {

	public static String stringToStars(String str) {
		/*replaces a string with asterisks */
		assert str.length() > 0;
		char[] sArr = str.toCharArray();
		for (int i = 0; i < str.length(); ++i) {
			sArr[i] = '*';
		}
		return String.valueOf(sArr);
	}
	
	public static boolean isPrime(int x) {
		/* laaaaame way of determining if number is prime. Boolean true for 1 */
		assert x != 0;
		if (x == 1) {return false;}
		
		for (int i = 2; i <= (x / 2)  ; ++i) {
			if (x % i == 0) 
			{return false;}
		}
		return true;
	}
	
	public int randBetween(int x, int y) {
		double a = Math.random();
		int z = (int)(a * (y - x) + x);
		return z;
	}
	
	public static boolean isEven(int x) {
		int y = x % 2;
		if (y == 0) {return true;} 
		return false;
	}
	
	
	//public static void main(String[] args) {
	//LogWin.messagePrint(isEven(400));
	//}
	
}
