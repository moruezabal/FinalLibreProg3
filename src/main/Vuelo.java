package main;

import java.util.ArrayList;

public class Vuelo {
	
	private ArrayList<String> aerolineas;
	private ArrayList<String> aeropuertos;
	private int cantEscalas;
	private double kilometros;
	
	//Solución
	
	
	public Vuelo(){
		this.aerolineas = new ArrayList<String>();
		this.aeropuertos = new ArrayList<String>();
		this.cantEscalas = 0;
		this.kilometros = 0;
	}

	public int getCantEscalas() {
		return cantEscalas;
	}

	public void setCantEscalas(int escalas) {
		this.cantEscalas = escalas;
	}

	public double getKilometros() {
		return kilometros;
	}

	public void setKilometros(double kilometros) {
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
	
	public void addAeropuerto (String a) {
		aeropuertos.add(a);
	}

	@Override
	public String toString() {
		return "Vuelo [\n 	Aeropuertos: " + aeropuertos
					+ "\n 	Aerolineas: " + aerolineas
					+ "\n 	Escalas: " + cantEscalas
					+ "\n	Kilometros: " + (int) kilometros 
					+ "\n      ]";
	}
}
