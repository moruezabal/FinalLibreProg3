package main;

import java.util.ArrayList;

public class Vuelo {
	
	private ArrayList<String> aerolineas;
	private ArrayList<String> aeropuertos;
	private int cantEscalas;
	private double kilometros;
	
	public Vuelo(){
		this.aerolineas = new ArrayList<String>();
		this.aeropuertos = new ArrayList<String>();
		this.cantEscalas = 0;
		this.kilometros = 0;
	}
	
	public Vuelo(Aeropuerto origen) {
		this.aerolineas = new ArrayList<String>();
		this.aeropuertos = new ArrayList<String>();
		this.cantEscalas = 0;
		this.kilometros = 0;
		this.aeropuertos.add(origen.getNombre());
	}
	
	public Vuelo(Vuelo v) {
		this.aerolineas = new ArrayList<String>(v.getAerolineas());
		this.aeropuertos = new ArrayList<String>(v.getAeropuertos());
		this.cantEscalas = v.getCantEscalas();
		this.kilometros = v.getKilometros();
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
	
	public void agregarKilometros(double kilometros) {
		this.kilometros += kilometros;
	}
	
	public void restarKilometros(double kilometros) {
		this.kilometros -= kilometros;
	}

	public ArrayList<String> getAerolineas() {
		return aerolineas;
	}

	public ArrayList<String> getAeropuertos() {
		return aeropuertos;
	}

	public void addAerolinea(String a) {
		aerolineas.add(a);
	}
	
	public void addAeropuerto(String a) {
		aeropuertos.add(a);
	}
	
	public void removeAeropuerto(String s) {
		aeropuertos.remove(s);
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
