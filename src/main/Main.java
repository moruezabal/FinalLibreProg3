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
		
		System.out.println(sa.iniciarMenu());
	}

}
