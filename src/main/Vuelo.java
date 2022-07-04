package main;

import java.util.ArrayList;

public class Vuelo {
	
	private ArrayList<String> aerolineas;
	private ArrayList<String> aeropuertos;
	private int escalas;
	private int kilometros;
	
	public Vuelo(){
		aerolineas = new ArrayList<String>();
		aeropuertos = new ArrayList<String>();
		escalas = 0;
		kilometros = 0;	
	}

	public int getEscalas() {
		return escalas;
	}

	public void setEscalas(int escalas) {
		this.escalas = escalas;
	}

	public int getKilometros() {
		return kilometros;
	}

	public void setKilometros(int kilometros) {
		this.kilometros = kilometros;
	}

	public ArrayList<String> getAerolineas() {
		return aerolineas;
	}

	public ArrayList<String> getAeropuertos() {
		return aeropuertos;
	}
	
	public void addAerolinea (String a) {
		aerolineas.add(a);
	}
	
	public void addAeropuertos (String a) {
		aeropuertos.add(a);
	}
	
	public void registrarEscala(String aeropuerto, String aerolinea) {
		this.addAeropuertos(aerolinea);
		this.addAerolinea(aerolinea);
		this.aumentarCantEscala();
	}

	private void aumentarCantEscala() {
		this.escalas++;
	}

	@Override
	public String toString() {
		return "Vuelo [aeropuertos: " + aeropuertos
					+ ", aerolineas: " + aerolineas
					+ ", escalas: " + escalas
					+ ", kilometros: " + kilometros + "]";
	}
}
