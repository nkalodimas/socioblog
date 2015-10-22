package gr.uoa.di.scan.thesis.exception;

public class FailedSignUpException extends RuntimeException{
	
	public FailedSignUpException(String msg) {
		super(msg);
	}

	private static final long serialVersionUID = 1L;
}
