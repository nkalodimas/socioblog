package gr.uoa.di.scan.thesis.exception;

public class FailedLoginException  extends RuntimeException{
	
	public FailedLoginException(String msg) {
		super(msg);
	}

	private static final long serialVersionUID = 1L;
}
