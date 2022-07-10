package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SistemaAereo {

	private ArrayList<Aeropuerto> aeropuertos;
	private ArrayList<Reserva> reservas;
	
	public SistemaAereo() {
		this.aeropuertos = new ArrayList<Aeropuerto>();
		this.reservas = new ArrayList<Reserva>();
	}
	
	public ArrayList<Reserva> getReservas() {
		return reservas;
	}
	
	public ArrayList<Aeropuerto> getAeropuertos() {
		return aeropuertos;
	}
	
	public void addAeropuerto(Aeropuerto a) {
		this.aeropuertos.add(a);
	}
	
	public void addReserva(Reserva r) {
		this.reservas.add(r);
	}
	
	public int cantAeropuertos() {
		return this.aeropuertos.size();
	}
	
	public int cantReservas() {
		return this.reservas.size();
	}
	
	public Aeropuerto getAeropuertoByName(String name) {
		Aeropuerto encontrado = null;
		for (Aeropuerto a : this.getAeropuertos()) {
			if( a.getNombre().equals(name)) {
				encontrado = a;
				break;
			}
		}
		return encontrado;
	}

	public String mostrarAeropuertos(){
		String informe = "";
		for (Aeropuerto a: this.aeropuertos) {
			informe += a + "\n";
		}
		return informe;
	}
	
	public String mostrarReservas(){
		String informe = "";
		for (Reserva r: this.reservas) {
			informe += r + "\n";
		}
		return informe;
	}
	
	public String mostrarRutas() {
		String rutas ="";
		for (Aeropuerto a: this.getAeropuertos()) {
			rutas += "\nDesde " + a.getFullName() + ":\n";
			for (Ruta r: a.getSalidas())
				rutas += r;
		}
		return rutas;
	}

	public void cargarAeropuertosCSV(ArrayList<String[]> aeropuertos) {
		
		for(String[] data : aeropuertos) {
			Aeropuerto nuevo = new Aeropuerto(data[0],data[1],data[2]);
			this.addAeropuerto(nuevo);
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
			
			Aeropuerto aOrigen = this.getAeropuertoByName(origen);
			Aeropuerto aDestino = this.getAeropuertoByName(destino);
			
			//Las funciones de abajo deberían resumirse en una para reutilización
			aOrigen.addRuta(new Ruta(aDestino,distancia,esCabotaje,capacidad)); //Agrega ruta de ida
			aDestino.addRuta(new Ruta(aOrigen,distancia,esCabotaje, new HashMap <String, Integer >(capacidad))); //Agrega ruta de vuelta
		}
	}
	
	public void cargarReservasCSV(ArrayList<String[]> reservas) {
		for(String[] data : reservas) {
			Aeropuerto aOrigen = this.getAeropuertoByName(data[0]);
			Aeropuerto aDestino = this.getAeropuertoByName(data[1]);
			String aerolinea = data[2];
			int asientos = Integer.parseInt(data[3]);
			
			
			try {
				Ruta ruta = this.validarDestino(aOrigen,aDestino);
				if(ruta.validarReserva(aerolinea, asientos)) {
					ruta.realizarReserva(aerolinea,asientos);
					this.addReserva(new Reserva(aOrigen,aDestino,data[2],Integer.parseInt(data[3])));					
				}
				
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	private Ruta validarDestino(Aeropuerto origen, Aeropuerto destino) throws AeropuertoNoExistenteException, RutaNoExistenteException {
		
		if(origen != null && destino != null) {
			Ruta rutaValida = origen.getVueloDirecto(destino.getNombre());
			if(rutaValida != null)
				return rutaValida;
			else
				throw new RutaNoExistenteException("No hay ruta entre los aeropuertos " + origen.getFullName() + " y " + destino.getFullName()); 
		}
		else 
			throw new AeropuertoNoExistenteException("El aeropuerto origen o destino no existe");
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
				return this.mostrarAeropuertos();
			case "2":
				return this.mostrarReservas();
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
		Vuelo recorrido = this.recorridoMasCortoBacktracking(origen);
		return recorrido != null ?
				"Recorrido: " + recorrido :
				"No se pudo recorrer todos los aeropuertos de un solo viaje";
	}

	private String mostrarGreedy() {
		
		Aeropuerto origen = this.conseguirAeropuertoSolicitado("Ingresar aeropuerto de origen:");
		ArrayList<Aeropuerto> recorrido = this.recorridoMasCortoGreedy(origen);
		return recorrido.size() > this.cantAeropuertos() ? 
				"Todos los aeropuertos recorridos" :
				"Aeropuertos recorridos: " + recorrido.size() + "/" + this.cantAeropuertos() + "\n";
	}

	private String mostrarVuelosDirectosEntrePaises() {

		String paisOrigen = solicitarTexto("Ingresar pais de partida");
		String paisDestino = solicitarTexto("Ingresar pais de destino");
		
		return vuelosDirectosSegunPaises(paisOrigen, paisDestino);
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
		
		ArrayList<Vuelo> vuelos = encontrarVuelosSinAerolinea(origen, destino, aerolinea);
		
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
		
		Aeropuerto a = this.getAeropuertoByName(entrada);
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
	
	public Ruta getRuta(Aeropuerto origen, Aeropuerto destino) {
		Ruta salida = null;
		
		for (Ruta ruta : origen.getSalidas()) {
			if (ruta.getDestino().equals(destino))
				salida = ruta;
		}
		return salida;
	}
	  
	  private boolean hayRutaDisponible(HashMap<String, Boolean> visitado, Aeropuerto origen, Aeropuerto destino) { //chequeandoPasajes
		  boolean encontrado = false;
		  visitado.put(origen.getNombre(), true); // podría ser un arreglo de nombres?
		  if (origen.getNombre().equals(destino.getNombre()))
			  return true;
		  else
			  for (Aeropuerto siguiente : origen.getDestinosDirectosPosibles()) {		  
				  if(!encontrado && visitado.get(siguiente.getNombre()) == null ) {
					  encontrado = hayRutaDisponible(visitado, siguiente, destino);					  
				  }
			  }
		  return encontrado;
	  }
	  
	  public boolean hayRutaDisponible(Aeropuerto origen, Aeropuerto destino) {
		  return hayRutaDisponible(new HashMap<String, Boolean>(), origen, destino);
	  }
	  
	  private ArrayList<Aeropuerto> encontrarRutaDisponible(HashMap<String, Boolean> visitado, Aeropuerto origen, Aeropuerto destino) { //chequeandoPasajes
		  ArrayList<Aeropuerto> escalas = new ArrayList<Aeropuerto>();
		  
		  visitado.put(origen.getNombre(), true); // podría ser un arreglo de nombres?
		  
		  if (origen.getNombre().equals(destino.getNombre())) {
			  escalas.add(origen);
			  return escalas;			  
		  }
		  else
			  for (Aeropuerto siguiente : origen.getDestinosDirectosPosibles()) {		  
				  if(visitado.get(siguiente.getNombre()) == null ) {
					  ArrayList<Aeropuerto> camino = encontrarRutaDisponible(visitado, siguiente, destino);	
					  if (!camino.isEmpty()) {
						  escalas.add(origen);
						  escalas.addAll(camino);
					  }
				  }
			  }
		  return escalas;
	  }
	  
	  public ArrayList<Aeropuerto> encontrarRutaDisponible(Aeropuerto origen, Aeropuerto destino){
		  return encontrarRutaDisponible(new HashMap<String, Boolean>(), origen, destino);
	  }
	  
	  public void imprimirEscalas(ArrayList<Aeropuerto> aeropuertos) { 
		  for(Aeropuerto a: aeropuertos)
			  System.out.println(a.getFullName());
	  }
	  
	  private ArrayList<Vuelo> encontrarVuelosSinAerolinea(HashMap<String, Boolean> visitado, Aeropuerto origen, Aeropuerto destino, String aerolineaExcluida) { //chequeandoPasajes
		  ArrayList<Vuelo> vuelos = new ArrayList<Vuelo>();
		  
		  visitado.put(origen.getNombre(), true); // podría ser un arreglo de nombres?
		  
		  if (origen.getNombre().equals(destino.getNombre())) {
			  Vuelo vueloUnico = new Vuelo();
			  vueloUnico.addAeropuerto(origen.getNombre());
			  vueloUnico.setCantEscalas(1);
			  vuelos.add(vueloUnico);
			  visitado.remove(origen.getNombre());
			  return vuelos;
		  }
		  else {
			  HashMap<Aeropuerto,String> destinosPosibles = origen.getDestinosDirectosPosiblesConAerolinea(aerolineaExcluida);
			  for (Map.Entry<Aeropuerto, String> siguiente : destinosPosibles.entrySet()) {		  
				  if(visitado.get(siguiente.getKey().getNombre()) == null ) {
					  ArrayList<Vuelo> caminos = encontrarVuelosSinAerolinea(visitado, siguiente.getKey(), destino, aerolineaExcluida);	
					  for(Vuelo camino : caminos) {
						  if (!camino.getAeropuertos().isEmpty()) {
							  Vuelo caminoCompleto = new Vuelo();
							  // Actualizar listado de aeropuertos
							  caminoCompleto.addAeropuerto(origen.getNombre());
							  caminoCompleto.getAeropuertos().addAll(camino.getAeropuertos());
							  // Actualizar listado de aerolíneas
							  caminoCompleto.addAerolinea(siguiente.getValue());
							  caminoCompleto.getAerolineas().addAll(camino.getAerolineas());
							  // Actualizar cantidad de escalas
							  caminoCompleto.setCantEscalas(camino.getCantEscalas()+1);
							  // Actualizar kilometraje
							  Ruta rutaActual = origen.getVueloDirecto(siguiente.getKey().getNombre());
							  caminoCompleto.setKilometros(camino.getKilometros()+ rutaActual.getDistancia());
							  // Agregar vuelo al listado
							  vuelos.add(caminoCompleto);
						  }  
					  }  
				  }
			  } 
		  }
			  
	  	visitado.remove(origen.getNombre());
	  	return vuelos;
	  }
	  
	  public ArrayList<Vuelo> encontrarVuelosSinAerolinea(Aeropuerto origen, Aeropuerto destino, String aerolineaExcluida){
		  return encontrarVuelosSinAerolinea(new HashMap<String, Boolean>(), origen, destino, aerolineaExcluida);
	  }
	  
	  public String vuelosDirectosSegunPaises(String paisOrigen, String paisDestino){
		 String informeVuelos = "";
		
		  for (Aeropuerto a : this.getAeropuertos()) {
			  if (a.getPais().equals(paisOrigen)){
				  for (Ruta r: a.getSalidas()) {
					  if(r.getDestino().getPais().equals(paisDestino)){
							informeVuelos += this.mostrarRuta(a,r);
						}  
				  }
			  }  
		  }
		  return informeVuelos.length() > 0 ? informeVuelos : "No hay vuelos disponibles entre los dos paises";
	  }

	private String mostrarRuta(Aeropuerto a, Ruta r) {
		return a.getFullName() + " a " + r.getDestino().getFullName() + ":\n" +
			"Por " + r.imprimirAerolineasDisponibles() + ".\nDistancia: " + r.getDistancia() + " Kms.\n\n";
	}
	
	public ArrayList<Aeropuerto> recorridoMasCortoGreedy(Aeropuerto origen){
		
		ArrayList<Ruta> candidatos = origen.getSalidas(); //Listado de candidatos -> Rutas posibles para el nuevo destino
		
		ArrayList<Aeropuerto> visitados = new ArrayList<>();
		
		visitados.add(origen);
		
		System.out.println(origen.getFullName());
		
		Aeropuerto candSeleccionado = null; //Aeropuerto destino de la ruta elegida
		
		while(candidatos.size() > 0 && visitados.size() <= this.cantAeropuertos()) {
			
			candSeleccionado = seleccionarMasCercano(candidatos, visitados); 
			System.out.println(candSeleccionado.getFullName());
			
			visitados.add(candSeleccionado);
			candidatos = candSeleccionado.getRutasPosibles(visitados);
			
		}
		
		if(visitados.size() == this.cantAeropuertos()) {
			Ruta regresoOrigen = candSeleccionado.getVueloDirecto(origen.getNombre());
			if(regresoOrigen != null) {
				System.out.println("Recorrido completado y regreso a origen");
				System.out.println(origen.getFullName());
				
				visitados.add(origen);
				return visitados;
			}
			else{
				System.out.println("Recorrido completado, pero no ha llegado a origen");
				return visitados;
			}
		}
		else {
			System.out.println("No ha recorrido todos los aeropuertos");
			return visitados;
		}
	}
	
	private Vuelo recorridoMasCortoBacktracking(Estado e) {
		
		e.aumentarCantEstados();
		  
		if(e.cantVisitados() == this.cantAeropuertos()) {
			e.aumentarCantSoluciones();
			
			Ruta regresoOrigen = e.getActual().getVueloDirecto(e.getOrigen().getNombre());
			if(regresoOrigen != null) {
				e.getSolucion().agregarKilometros(regresoOrigen.getDistancia());
				e.getSolucion().addAeropuerto(e.getOrigen().getNombre());
				
				if(e.getMejorSolucion() == null || e.hayMejorSolucion()) {
					Vuelo mejorSolucion = new Vuelo(e.getSolucion());
					e.setMejorSolucion(mejorSolucion);
				}
				
				e.getSolucion().restarKilometros(regresoOrigen.getDistancia());
				e.getSolucion().removeLastAeropuerto();
			}
			
		}
		else {
			Aeropuerto actual = e.getActual();
			ArrayList<Ruta> rutasPosibles = e.getActual().getRutasPosibles(e.getVisitados());
			for( Ruta r: rutasPosibles) {
				Aeropuerto siguiente = r.getDestino(); //Guardo referencia para restarla de las visitas luego. Se podría haber suprimido por índice para evitar esto.
				
				e.addVisita(siguiente);
				e.setActual(siguiente);
				e.getSolucion().addAeropuerto(siguiente.getNombre());
				e.getSolucion().agregarKilometros(r.getDistancia());
				
				if(!e.podaPorKilometrajeExcedido()) {
					this.recorridoMasCortoBacktracking(e);					
				}
				
				e.removeVisita(siguiente);
				e.setActual(actual);
				e.getSolucion().removeLastAeropuerto();
				e.getSolucion().restarKilometros(r.getDistancia());
			}
		  }
		
	  	return e.getMejorSolucion();
	  }
	
	public Vuelo recorridoMasCortoBacktracking(Aeropuerto origen) {
		
		Estado e = new Estado(origen);
		
		Vuelo masCorto = recorridoMasCortoBacktracking(e);
		
		System.out.println("Cantidad de Estados: " + e.getCantEstados());
		System.out.println("Cantidad de Soluciones: " + e.getCantSoluciones());
		
		return masCorto;
	}

	private Aeropuerto seleccionarMasCercano(ArrayList<Ruta> destinos, ArrayList<Aeropuerto> visitados) {
		Aeropuerto elegido = null;
		
		double distanciaMenor = Double.MAX_VALUE;
		
		for( Ruta destino : destinos) {
			if(!visitados.contains(destino.getDestino()) && destino.getDistancia() < distanciaMenor) {
				distanciaMenor = destino.getDistancia();
				elegido = destino.getDestino();
			}
		}
		
		return elegido;
	}
		
}
