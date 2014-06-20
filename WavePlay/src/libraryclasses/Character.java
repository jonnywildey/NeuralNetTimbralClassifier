package libraryclasses;

public class Character {

	public static char charmover(char letter, int places) {
		int d = letter;
		if (d > 64  && d < 123) {
			d = (d + places);
			d = ((d - 64) % 58) + 64;
			char c = (char)d;
			return c;
		} else {
			return letter;
		}
	
}
	
}
