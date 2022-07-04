package main;

import java.util.HashMap;
import java.util.Map;

public class Ruta {
	
	private Aeropuerto destino;
	private double distancia;
	private boolean esCabotaje;
	private HashMap<String,Integer> capacidad;
	
	public Ruta(Aeropuerto destino, double distancia, boolean esCabotaje, HashMap<String, Integer> capacidad) {
		this.destino = destino;
		this.distancia = distancia;
		this.esCabotaje = esCabotaje;
		this.capacidad = capacidad;
	}

	public Aeropuerto getDestino() {
		return destino;
	}
	
	public boolean esCabotaje() {
		return esCabotaje;
	}

	public void setEsCabotaje(boolean esCabotaje) {
		this.esCabotaje = esCabotaje;
	}

	public void setDestino(Aeropuerto destino) {
		this.destino = destino;
	}

	public double getDistancia() {
		return distancia;
	}

	public void setDistancia(double distancia) {
		this.distancia = distancia;
	}

	public HashMap<String, Integer> getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(HashMap<String, Integer> capacidad) {
		this.capacidad = capacidad;
	}
	
	public boolean validarReserva(String aerolinea, int pasajes) {
		return this.capacidad.containsKey(aerolinea) && pasajes <= this.capacidad.get(aerolinea);
	}

	public void realizarReserva(String aerolinea, int pasajes) {
		this.capacidad.replace(aerolinea, this.capacidad.get(aerolinea) - pasajes );
	}
	
	public boolean existeAerolinea(String aerolinea) {
		return this.capacidad.containsKey(aerolinea);
	}
	
	public int getAsientosDisponibles(String aerolínea) {
		return this.capacidad.get(aerolínea);
	}
	
	public boolean hayPasaje() {
		return capacidad.values().stream().anyMatch(x -> x > 0);
	}
	
	public boolean hayPasaje(String aerolinea) {
		return capacidad.get(aerolinea) > 0;
	}
	
	public boolean hayPasajeSinAerolinea(String aerolinea) {
		boolean hayPasajes = false;
		for( Map.Entry<String, Integer> entry : capacidad.entrySet()) {
			if(!entry.getKey().equals(aerolinea) && entry.getValue() > 0) {
				hayPasajes = true;
			} 
		}
		return hayPasajes;
	}
	
	@Override
	public String toString() {
		return "Ruta [destino=" + destino.getFullName() + ", distancia=" + distancia + ", esCabotaje=" + esCabotaje + ", capacidad="
				+ capacidad + "]\n";
	}
}
