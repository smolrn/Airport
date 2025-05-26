package airport.controller;

import airport.Flight;
import airport.Location;
import airport.Plane;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.List;

public class ControladorDeVuelo {

    private final List<Flight> vuelos;
    private final List<Plane> aviones;
    private final List<Location> localizaciones;

    public ControladorDeVuelo(List<Flight> vuelos, List<Plane> aviones, List<Location> localizaciones) {
        this.vuelos = vuelos;
        this.aviones = aviones;
        this.localizaciones = localizaciones;
    }

    public JSONObject registrarVuelo(String id, String idAvion,
            String idSalida, String idLlegada, String idEscala,
            LocalDateTime fechaSalida,
            int horasDuracion, int minutosDuracion,
            int horasEscala, int minutosEscala) {

        JSONObject r = new JSONObject();

        Plane avion = buscarAvion(idAvion);
        Location salida = buscarLocalizacion(idSalida);
        Location llegada = buscarLocalizacion(idLlegada);
        Location escala = (idEscala == null || idEscala.trim().isEmpty()) ? null : buscarLocalizacion(idEscala);

        if (id == null || !id.matches("^[A-Z]{3}\\d{3}$")) {
            return error("El ID del vuelo debe tener el formato XXXYYY.");
        }

        if (existeId(id)) {
            return error("El ID del vuelo ya existe.");
        }

        if (avion == null) {
            return error("El avion especificado no existe.");
        }

        if (salida == null || llegada == null) {
            return error("Las localizaciones de salida y llegada deben ser validas.");
        }

        if (fechaSalida == null) {
            return error("La fecha de salida es inválida.");
        }

        int totalMinutos = (horasDuracion * 60) + minutosDuracion;
        if (totalMinutos <= 0) {
            return error("La duración del vuelo debe ser mayor que 00:00.");
        }

        if (escala == null && (horasEscala != 0 || minutosEscala != 0)) {
            return error("No puede haber duración de escala si no hay localización de escala.");
        }

        Flight vuelo;
        if (escala == null) {
            vuelo = new Flight(id, avion, salida, llegada, fechaSalida, horasDuracion, minutosDuracion);
        } else {
            vuelo = new Flight(id, avion, salida, escala, llegada, fechaSalida, horasDuracion, minutosDuracion, horasEscala, minutosEscala);
        }
        vuelos.add(vuelo);

        r.put("estado", "exito");
        r.put("mensaje", "Vuelo registrado correctamente.");
        return r;

    }

    private boolean existeId(String id) {
        for (Flight f : vuelos) {
            if (f.getId().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    private Plane buscarAvion(String id) {
        for (Plane p : aviones) {
            if (p.getId().equalsIgnoreCase(id)) {
                return p;
            }
        }
        return null;
    }

    private Location buscarLocalizacion(String id) {
        for (Location l : localizaciones) {
            if (l.getAirportId().equalsIgnoreCase(id)) {
                return l;
            }
        }
        return null;
    }

    private JSONObject error(String mensaje) {
        JSONObject r = new JSONObject();
        r.put("estado", "exito");
        r.put("mensaje", "Vuelo registrado correctamente.");
        return r;

    }
}
