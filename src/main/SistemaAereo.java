package main;

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

	public Ruta validarDestino(Aeropuerto origen, Aeropuerto destino) throws AeropuertoNoExistenteException, RutaNoExistenteException {
		
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
	
	public Ruta getRuta(Aeropuerto origen, Aeropuerto destino) {
		Ruta salida = null;
		
		for (Ruta ruta : origen.getSalidas()) {
			if (ruta.getDestino().equals(destino))
				salida = ruta;
		}
		return salida;
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
			 r.imprimirAerolineasDisponibles() + ".\nDistancia: " + r.getDistancia() + " Kms.\n\n";
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
				
				if(!e.podaPorKilometrajeExcedido())
					this.recorridoMasCortoBacktracking(e);					
				
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
