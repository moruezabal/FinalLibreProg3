package main;

import java.util.ArrayList;

public class Aeropuerto {
	
	private String nombre;
	private String pais;
	private String ciudad;
	private ArrayList<Ruta> salidas;
	
	public Aeropuerto(String nombre, String ciudad, String pais) {
		this.nombre = nombre;
		this.pais = pais;
		this.ciudad = ciudad;
		this.salidas = new ArrayList<Ruta>();
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public ArrayList<Ruta> getSalidas() {
		return salidas;
	}
	
	private String mostrarSalidas() {
		String salidas = "Sin destinos\n";
		if(this.getSalidas().size() != 0) {
			salidas = "Destinos directos a: ";
			for (Ruta salida : this.getSalidas()) {
				salidas += salida.getDestino().getFullName() + ", ";
			}
			salidas = salidas.substring(0, salidas.length() - 2) + "\n";
		}
		return salidas;
	}
	
	public String getFullName() {
		return this.nombre + " (" + this.ciudad + ", " + this.pais + ")";
	}
	
	public void addRuta(Ruta r) {
		salidas.add(r);
	}
	
	public Ruta getVueloDirecto(String destino) {
		Ruta ruta = null;
		for (Ruta r : this.getSalidas()) {
			if(r.getDestino().getNombre().equals(destino)) {
				ruta = r;
				break;
			}
		}
		return ruta;
	}
	
	public Ruta getVueloDirecto(String destino, String aerolínea) {
		Ruta ruta = null;
		for (Ruta r : this.getSalidas()) {
			if(r.getDestino().getNombre().equals(destino)) {
				ruta = r;
				break;
			}
		}
		return ruta;
	}
	
	@Override
	public String toString() {
		return this.nombre + " (" + this.ciudad + ", " + this.pais + ").\n" + this.mostrarSalidas();
	}
	
	public ArrayList<Aeropuerto> getDestinosDirectos(){
		ArrayList<Aeropuerto> destinos = new ArrayList<>();
			for (Ruta r: this.getSalidas()){
				destinos.add(r.getDestino());
			}
		return destinos;
	}
	
	public ArrayList<Aeropuerto> getDestinosDirectosPosibles(){
		ArrayList<Aeropuerto> destinos = new ArrayList<>();
			for (Ruta r: this.getSalidas()){
				if(r.hayPasaje()){
					destinos.add(r.getDestino());					
				}
			}
		return destinos;
	}
	
	public ArrayList<Aeropuerto> getDestinosDirectosPosibles(String excluida){
		ArrayList<Aeropuerto> destinos = new ArrayList<>();
			for (Ruta r: this.getSalidas()){
				if(r.hayPasajeSinAerolinea(excluida)){
					destinos.add(r.getDestino());					
				}
			}
		return destinos;
	}
	
}
