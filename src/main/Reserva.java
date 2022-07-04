package main;

public class Reserva {
	
	private Aeropuerto origen;
	private Aeropuerto destino;
	private String aerolínea;
	private int asientos;
	
	public Reserva(Aeropuerto origen, Aeropuerto destino, String aerolínea, int asientos) { //Objeto Aeropuerto o solo el nombre?
		this.origen = origen;
		this.destino = destino;
		this.aerolínea = aerolínea;
		this.asientos = asientos;
	}

	public Aeropuerto getOrigen() {
		return origen;
	}
	
	public void setOrigen(Aeropuerto origen) {
		this.origen = origen;
	}
	
	public Aeropuerto getDestino() {
		return destino;
	}
	
	public void setDestino(Aeropuerto destino) {
		this.destino = destino;
	}
	
	public String getAerolínea() {
		return aerolínea;
	}
	
	public void setAerolínea(String aerolínea) {
		this.aerolínea = aerolínea;
	}
	
	public int getAsientos() {
		return asientos;
	}
	
	public void setAsientos(int asientos) {
		this.asientos = asientos;
	}
	
	@Override
	public String toString() {
		return "Reserva [origen: " + origen.getFullName() + ", destino: " + destino.getFullName() + ", aerolinea: " + aerolínea + ", asientos: "
				+ asientos + "]";
	}
}

