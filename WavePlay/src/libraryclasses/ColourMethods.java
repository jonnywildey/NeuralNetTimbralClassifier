package libraryclasses;

import java.awt.Color;

public class ColourMethods {
	
	public static Color randomColor(int alpha) {
		/*returns a random color with alpha choice */
		return new Color((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255), alpha);
	}
	
	public static Color randomColor() {
		return new Color((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255), 255);
	}
	
	public static Color[] ranColorArray(int a, int alpha) {
		/*returns an array of random colors */
		Color[] rcol = new Color[a];
		for (int i = 0; i < a; ++i) {
			rcol[i] = randomColor(alpha);
		}
		return rcol;
	}
	

}
