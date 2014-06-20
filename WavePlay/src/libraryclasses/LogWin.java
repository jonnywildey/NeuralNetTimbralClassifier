/**
 * <dl>
 * 	<dd> Useful Library things
 * 
 * 	<dt> Description
 * 	<dd> Some useful debug & message commands
 * 	</dl>
 * 
 * 	@author Jonny Wildey
 * 	@version $Date: 2013/10/9 $
 */

package libraryclasses;
import javax.swing.*;


public class LogWin {

	/** Get the current line number.
	 * @return int - Current line number.
	 * Got from ANGSUMAN CHAKRABORT.
	 */
	private static int getLineNumber() {
	    return Thread.currentThread().getStackTrace()[3].getLineNumber();
	}
	
	public static String makeInputBox() {
		String s = (String)JOptionPane.showInputDialog(
                null,
                "Write a thing plz",
                ""
                );

			return s;
	}
	
	public static String makeInputBox(String message) {
		String s = (String)JOptionPane.showInputDialog(
                null,
                message,
                ""
                );

			return s;
	}
	
	
	public static <T> void messagePrint(T message) {
		/**sends a system out and window of a message */
		JOptionPane.showMessageDialog(null, message);
		System.out.println(message);
	}
	
	public static <T> void messagePrintNoCancel(T message) {
		/**sends a system out and window of a message */
		JOptionPane.showMessageDialog(null, message, "OK!", JOptionPane.OK_OPTION);
		System.out.println(message);
	}
	
	public static void linePrint() {
		/**sends a system out and Window of current line number */
		messagePrint("@ Line #" + Integer.toString(getLineNumber()));
	}
	
	/*Code test */
	//public static void main(String[] args) {
		//linePrint();
		//messagePrint(678);
	//}
	
	
	
}
