package main;

import java.util.ArrayList;

import lib.CSVReaderAeropuertos;
import lib.CSVReaderReservas;
import lib.CSVReaderRutas;

public class Main {

	public static void main(String[] args) {
		
		SistemaAereo sa = new SistemaAereo();

		ArrayList<String[]> aeropuertosImportados = CSVReaderAeropuertos.read();
		ArrayList<String[]> rutasImportadas = CSVReaderRutas.read();
		ArrayList<String[]> reservasImportadas = CSVReaderReservas.read();
		
		sa.cargarAeropuertosCSV(aeropuertosImportados);
		sa.cargarRutasCSV(rutasImportadas);
		sa.cargarReservasCSV(reservasImportadas);
		
		//System.out.println(sa.mostrarReservas());
		//System.out.println(sa.mostrarRutas());
		//System.out.println("Aeropuertos: " + sa.cantAeropuertos() 
		//s				+ "\nReservas: " + sa.cantReservas());
		
		Aeropuerto origen  = sa.getAeropuertoByName("Pucon");
		Aeropuerto destino = sa.getAeropuertoByName("Ministro Pistarini");
		String aerolinea = "United Airlines";
		
		//ArrayList<Aeropuerto> vuelo = sa.encontrarRutaDisponible(origen, destino);
		
		//sa.imprimirEscalas(vuelo);
		
		ArrayList<ArrayList<Aeropuerto>> vuelos = sa.encontrarCaminosSinAerolinea(origen, destino, aerolinea);
		System.out.println("Cantidad de Vuelos: "+vuelos.size());
		
		for (ArrayList<Aeropuerto> vuelo : vuelos) {
				sa.imprimirEscalas(vuelo);
				System.out.println("\n--------------\n");	
		}
		
		//Ruta r = sa.getRuta(origen, destino);
		//System.out.println(r.hayPasaje("Delta"));
		//System.out.println(r.hayPasaje());
		
		//System.out.println(sa.hayRutaDisponible(origen, destino));
		
		
		System.out.println(sa.iniciarMenu());
	}

}
