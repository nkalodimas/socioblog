package gr.uoa.di.scan.thesis.exception;

public class IsNotOwner extends RuntimeException{
	
	public IsNotOwner(String msg) {
		super(msg);
	}

	private static final long serialVersionUID = 1L;
}
