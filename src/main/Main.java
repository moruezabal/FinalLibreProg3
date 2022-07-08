package main;

import java.util.ArrayList;

import lib.CSVReaderAeropuertos;
import lib.CSVReaderReservas;
import lib.CSVReaderRutas;

public class Main {

	public static void main(String[] args) {
		
		SistemaAereo sa = new SistemaAereo();

		ArrayList<String[]> aeropuertosImportados = CSVReaderAeropuertos.read("./src/resources/Aeropuertos.csv");
		ArrayList<String[]> rutasImportadas = CSVReaderRutas.read("./src/resources/Rutas.csv");
		ArrayList<String[]> reservasImportadas = CSVReaderReservas.read("./src/resources/Reservas.csv");
		
		sa.cargarAeropuertosCSV(aeropuertosImportados);
		sa.cargarRutasCSV(rutasImportadas);
		sa.cargarReservasCSV(reservasImportadas);
		
		//System.out.println(sa.mostrarReservas());
		//System.out.println(sa.mostrarRutas());
		//System.out.println("Aeropuertos: " + sa.cantAeropuertos() 
		//				+ "\nReservas: " + sa.cantReservas());
		
		//Aeropuerto origen  = sa.getAeropuertoByName("Tancredo Neves");
		
		
//		Aeropuerto destino = sa.getAeropuertoByName("Ministro Pistarini");
//		String aerolinea = "United Airlines";
		
		
		//sa.recorridoMasCortoGreedy(origen);
		
		//System.out.println(sa.mostrarRutas());
		
		//System.out.println(sa.vuelosDirectosSegunPaises("USA", "ARG"));
		
		System.out.println(sa.iniciarMenu());
	}

}
