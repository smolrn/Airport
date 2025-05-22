package airport.controller;

import airport.Flight;
import org.json.JSONObject;

import java.util.List;

public class ControladorParaDelayFlight {

    private final List<Flight> vuelos;

    public ControladorParaDelayFlight(List<Flight> vuelos) {
        this.vuelos = vuelos;
    }

    public JSONObject retrasarVuelo(String idVuelo, int horas, int minutos) {
        JSONObject r = new JSONObject();

        Flight vuelo = buscarVueloPorId(idVuelo);
        if (vuelo == null) {
            return error("Vuelo no encontrado.");
        }

        if (horas < 0 || minutos < 0 || (horas == 0 && minutos == 0)) {
            return error("El tiempo de retraso debe ser mayor que 00:00.");
        }

        vuelo.delay(horas, minutos);

        r.put("estado", "exito");
        r.put("mensaje", "Vuelo retrasado correctamente.");
        return r;
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
