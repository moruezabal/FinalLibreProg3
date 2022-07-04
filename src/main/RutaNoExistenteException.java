package main;

public class RutaNoExistenteException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public RutaNoExistenteException() {};
	
	public RutaNoExistenteException(String error) {
		super(error);
	};

}
