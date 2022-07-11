package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import lib.CSVReaderAeropuertos;
import lib.CSVReaderReservas;
import lib.CSVReaderRutas;

public class Administracion {
	
	private SistemaAereo sistema;
	
	public Administracion() {
		this.sistema = new SistemaAereo();
	}

	public SistemaAereo getSistema() {
		return sistema;
	}
	
	public void iniciarSistema(){
		cargarDatos();
		System.out.println(iniciarMenu());
	};
	
	public void cargarDatos() {

		ArrayList<String[]> aeropuertosImportados = CSVReaderAeropuertos.read("./src/resources/Aeropuertos.csv");
		ArrayList<String[]> rutasImportadas = CSVReaderRutas.read("./src/resources/Rutas.csv");
		ArrayList<String[]> reservasImportadas = CSVReaderReservas.read("./src/resources/Reservas.csv");
		
		this.cargarAeropuertosCSV(aeropuertosImportados);
		this.cargarRutasCSV(rutasImportadas);
		this.cargarReservasCSV(reservasImportadas);
	};
	
	public void cargarAeropuertosCSV(ArrayList<String[]> aeropuertos) {
		
		for(String[] data : aeropuertos) {
			Aeropuerto nuevo = new Aeropuerto(data[0],data[1],data[2]);
			this.getSistema().addAeropuerto(nuevo);
		}
	}
	
	public void cargarRutasCSV(ArrayList<String[]> rutas) {
		String origen, destino;
		double distancia;
		boolean esCabotaje;
		
		for (String[] data : rutas) {
			HashMap <String, Integer >capacidad = new HashMap<>();
			origen = data[0];
			destino = data[1];
			distancia = Double.parseDouble(data[2]);
			esCabotaje = data[3].equals("1"); //No hay control sobre valores != 0
			
			for( int i = 4; i< data.length - 1 ; i++) {
				String[] vuelo = data[i].split("-");
				capacidad.put(vuelo[0], Integer.parseInt(vuelo[1]));
			}
			
			Aeropuerto aOrigen = this.getSistema().getAeropuertoByName(origen);
			Aeropuerto aDestino = this.getSistema().getAeropuertoByName(destino);
			
			//Las funciones de abajo deberían resumirse en una para reutilización
			aOrigen.addRuta(new Ruta(aDestino,distancia,esCabotaje,capacidad)); //Agrega ruta de ida
			aDestino.addRuta(new Ruta(aOrigen,distancia,esCabotaje, new HashMap <String, Integer >(capacidad))); //Agrega ruta de vuelta
		}
	}
		
	public void cargarReservasCSV(ArrayList<String[]> reservas) {
		for(String[] data : reservas) {
			Aeropuerto aOrigen = this.getSistema().getAeropuertoByName(data[0]);
			Aeropuerto aDestino = this.getSistema().getAeropuertoByName(data[1]);
			String aerolinea = data[2];
			int asientos = Integer.parseInt(data[3]);
			
			try {
				Ruta ruta = this.getSistema().validarDestino(aOrigen,aDestino);
				if(ruta.validarReserva(aerolinea, asientos)) {
					ruta.realizarReserva(aerolinea,asientos);
					this.getSistema().addReserva(new Reserva(aOrigen,aDestino,data[2],Integer.parseInt(data[3])));					
				}
				
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}
	
	public String iniciarMenu() {
		  String opcion ="";
		  BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		  System.out.println("Elegir una opcion:\n"
		  		+ "1- Listar todos los aeropuertos\n"
		  		+ "2- Listar todas las reservas realizadas\n"
		  		+ "3- Verificar vuelo directo\n"
		  		+ "4- Obtener vuelos sin una aerolinea determinada\n"
		  		+ "5- Vuelos disponibles entre paises\n"
		  		+ "6- Recorrer Aeropuertos en menor distancia - Greedy\n"
		  		+ "7- Recorrer Aeropuertos en menor distancia - Backtracing\n");
		  
		  try {
			opcion = br.readLine();
		} catch (IOException e) {
			return e.getMessage();
		}
		 
		return this.procesarConsulta(opcion);   
	}

	private String procesarConsulta(String opcion) {
		
		switch (opcion) {
			case "1": 
				return this.getSistema().mostrarAeropuertos();
			case "2":
				return this.getSistema().mostrarReservas();
			case "3":
				return this.mostrarVueloDirecto();
			case "4":
				return this.mostrarVuelosSinAerolinea();
			case "5":
				return this.mostrarVuelosDirectosEntrePaises();
			case "6":
				return this.mostrarGreedy();
			case "7":
				return this.mostrarBacktracking();
			default:
				return "Opcion incorrecta: " + opcion;
			}
	}
	
	private String mostrarBacktracking() {
		Aeropuerto origen = this.conseguirAeropuertoSolicitado("Ingresar aeropuerto de origen:");
		Vuelo recorrido = this.getSistema().recorridoMasCortoBacktracking(origen);
		return recorrido != null ?
				"Recorrido: " + recorrido :
				"No se pudo recorrer todos los aeropuertos de un solo viaje";
	}

	private String mostrarGreedy() {
		
		Aeropuerto origen = this.conseguirAeropuertoSolicitado("Ingresar aeropuerto de origen:");
		ArrayList<Aeropuerto> recorrido = this.getSistema().recorridoMasCortoGreedy(origen);
		return recorrido.size() > this.getSistema().cantAeropuertos() ? 
				"Todos los aeropuertos recorridos" :
				"Aeropuertos recorridos: " + recorrido.size() + "/" + this.getSistema().cantAeropuertos() + "\n";
	}

	private String mostrarVuelosDirectosEntrePaises() {

		String paisOrigen = solicitarTexto("Ingresar pais de partida");
		String paisDestino = solicitarTexto("Ingresar pais de destino");
		
		return this.getSistema().vuelosDirectosSegunPaises(paisOrigen, paisDestino);
	}

	private String mostrarVueloDirecto() {
		
		Aeropuerto origen = this.conseguirAeropuertoSolicitado("Ingresar aeropuerto de origen:");
		Aeropuerto destino = this.conseguirAeropuertoSolicitado("Ingresar Aeropuerto de destino:");
		String aerolinea = "";
			
			Ruta r = origen.getVueloDirecto(destino.getNombre());
			if (r != null) {
				aerolinea = solicitarTexto("Ingresar aerolinea");
				
				if(r.existeAerolinea(aerolinea)) { // Valida si la Aerolínea opera en la ruta, no valida si existe la aerolínea
					if(r.hayPasaje(aerolinea)) {
						return "El vuelo entre los aeropuertos " + origen.getFullName() + " y " + destino.getFullName() +
								" tiene una distancia de " + r.getDistancia() + " kilometros y " + r.getAsientosDisponibles(aerolinea) + 
								" asientos disponibles en la Aerolinea " + aerolinea;						
					}
					else
						return "No quedan pasajes para el vuelo con la aerolinea indicada";
				}
				else
					return "La aerolinea indicada no cubre el vuelo directo entre esos dos aeropuertos";
			}
			else
				return "No existe vuelo directo entre los aeropuertos mencionados";
	}
	
	private String mostrarVuelosSinAerolinea() {
		
		String informe = "";
		
		Aeropuerto origen = this.conseguirAeropuertoSolicitado("Ingresar aeropuerto de origen:");
		Aeropuerto destino = this.conseguirAeropuertoSolicitado("Ingresar Aeropuerto de destino:");
		String aerolinea = solicitarTexto("Ingresar aerolinea");
		
		ArrayList<Vuelo> vuelos = this.getSistema().encontrarVuelosSinAerolinea(origen, destino, aerolinea);
		
		informe += "Cantidad de vuelos: " + vuelos.size() + "\n";
		for (Vuelo vuelo : vuelos) {
			informe += vuelo +"\n--------------\n";	
		}
		return informe;
	}

	private Aeropuerto conseguirAeropuertoSolicitado(String mensaje) {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String entrada = ""; 
		System.out.println(mensaje);
		
		try {
			entrada = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Aeropuerto a = this.getSistema().getAeropuertoByName(entrada);
		while( a == null)  {
			System.out.println("Aeropuerto inexistente, intente nuevamente.");
			a = this.conseguirAeropuertoSolicitado(mensaje);
		}
		return a;
	}
	
	private String solicitarTexto(String mensaje) {
		String aerolinea = "";
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println(mensaje);
		
		try {
			aerolinea = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return aerolinea;
	}
}
