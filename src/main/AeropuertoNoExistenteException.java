package main;

public class AeropuertoNoExistenteException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public AeropuertoNoExistenteException() {};
	
	public AeropuertoNoExistenteException(String error) {
		super(error);
	};

}
