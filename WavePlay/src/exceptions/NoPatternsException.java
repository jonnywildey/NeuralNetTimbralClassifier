package exceptions;

public class NoPatternsException extends Exception{
	public NoPatternsException() {
		super();
		System.out.println("You haven't loaded any patterns!");
	}
}
