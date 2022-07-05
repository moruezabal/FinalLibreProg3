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
		//				+ "\nReservas: " + sa.cantReservas());
		
//		Aeropuerto origen  = sa.getAeropuertoByName("Pucon");
//		Aeropuerto destino = sa.getAeropuertoByName("Ministro Pistarini");
//		String aerolinea = "United Airlines";
		
		System.out.println(sa.iniciarMenu());
	}

}
