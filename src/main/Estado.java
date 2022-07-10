package main;

import java.util.ArrayList;

public class Estado {
	
	private Aeropuerto origen;
	private Aeropuerto actual;
	private Vuelo solucion;
	private Vuelo mejorSolucion;
	private ArrayList<Aeropuerto> visitados;
	private int cantEstados;
	private int cantSoluciones;
	
	
	public Estado(Aeropuerto origen) {
		this.origen = origen;
		this.actual = origen;
		this.solucion = new Vuelo(origen);
		this.mejorSolucion = null;
		this.visitados = new ArrayList<Aeropuerto>();
		this.visitados.add(origen);
		this.cantEstados = 0;
		this.cantSoluciones = 0;
	}

	public Aeropuerto getOrigen() {
		return origen;
	}

	public Aeropuerto getActual() {
		return actual;
	}

	public void setActual(Aeropuerto actual) {
		this.actual = actual;
	}

	public Vuelo getMejorSolucion() {
		return mejorSolucion;
	}

	public void setMejorSolucion(Vuelo mejorSolucion) {
		this.mejorSolucion = mejorSolucion;
	}

	public ArrayList<Aeropuerto> getVisitado() {
		return visitados;
	}
	
	public void addVisita(Aeropuerto a) {
		this.visitados.add(a);
	}
	
	public void removeVisita(Aeropuerto a) {
		this.visitados.remove(a);
	}
	
	public ArrayList<Aeropuerto> getVisitados() {
		return visitados;
	}

	public void setVisitado(ArrayList<Aeropuerto> visitado) {
		this.visitados = visitado;
	}
	
	public Vuelo getSolucion() {
		return solucion;
	}

	public void setSolucion(Vuelo solucion) {
		this.solucion = solucion;
	}
	
	public int cantVisitados() {
		return this.visitados.size();
	}
	
	public boolean actualNoTieneDestinos() {
		return this.actual.getRutasPosibles(this.visitados).isEmpty();
	}

	public boolean hayMejorSolucion() {
		return this.solucion.getKilometros() < this.mejorSolucion.getKilometros();
	}

	public int getCantEstados() {
		return cantEstados;
	}

	public void aumentarCantEstados() {
		this.cantEstados++;
	}
	
	public int getCantSoluciones() {
		return cantSoluciones;
	}

	public void aumentarCantSoluciones() {
		this.cantSoluciones++;
	}

	public boolean podaPorKilometrajeExcedido() {
		return this.mejorSolucion != null ? this.solucion.getKilometros() > this.mejorSolucion.getKilometros() : false;
	}
}
