package airport.controller;

import airport.Flight;
import airport.Passenger;
import org.json.JSONObject;

import java.util.List;

public class ControladorParaAddToFlight {

    private final List<Passenger> pasajeros;
    private final List<Flight> vuelos;

    public ControladorParaAddToFlight(List<Passenger> pasajeros, List<Flight> vuelos) {
        this.pasajeros = pasajeros;
        this.vuelos = vuelos;
    }

    public JSONObject agregarPasajeroAVuelo(long idPasajero, String idVuelo) {
        JSONObject r = new JSONObject();

        Passenger pasajero = buscarPasajeroPorId(idPasajero);
        if (pasajero == null) {
            return error("Pasajero no encontrado.");
        }

        Flight vuelo = buscarVueloPorId(idVuelo);
        if (vuelo == null) {
            return error("Vuelo no encontrado.");
        }

        
        if (!pasajero.getFlights().contains(vuelo)) {
            pasajero.addFlight(vuelo);
            vuelo.addPassenger(pasajero);
        }

        r.put("estado", "exito");
        r.put("mensaje", "Pasajero añadido al vuelo correctamente.");
        return r;
    }

    private Passenger buscarPasajeroPorId(long id) {
        for (Passenger p : pasajeros) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    private Flight buscarVueloPorId(String id) {
        for (Flight f : vuelos) {
            if (f.getId().equalsIgnoreCase(id)) {
                return f;
            }
        }
        return null;
    }

    private JSONObject error(String mensaje) {
        JSONObject r = new JSONObject();
        r.put("estado", "error");
        r.put("mensaje", mensaje);
        return r;
    }
}
