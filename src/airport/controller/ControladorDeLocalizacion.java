package airport.controller;

import airport.Location;
import org.json.JSONObject;

import java.util.List;

public class ControladorDeLocalizacion {

    private final List<Location> localizaciones;

    public ControladorDeLocalizacion(List<Location> localizaciones) {
        this.localizaciones = localizaciones;
    }

    public JSONObject registrarLocalizacion(String id, String nombre, String ciudad, String pais, double latitud, double longitud) {
        JSONObject r = new JSONObject();

        if (id == null || !id.matches("^[A-Z]{3}$")) {
            return error("El ID del aeropuerto debe tener exactamente 3 letras mayusculas.");
        }

        if (existeId(id)) {
            return error("El ID del aeropuerto ya existe.");
        }

        if (nombre == null || nombre.trim().isEmpty()
                || ciudad == null || ciudad.trim().isEmpty()
                || pais == null || pais.trim().isEmpty()) {
            return error("Los campos no pueden estar vacíos.");
        }

        if (latitud < -90 || latitud > 90) {
            return error("La latitud debe estar entre -90 y 90.");
        }

        if (longitud < -180 || longitud > 180) {
            return error("La longitud debe estar entre -180 y 180.");
        }

        if (!tieneMax4Decimales(latitud) || !tieneMax4Decimales(longitud)) {
            return error("La latitud y longitud deben tener maximo 4 decimales.");
        }

        r.put("estado", "exito");
        r.put("mensaje", "Localizacion registrada correctamente.");
        Location nueva = new Location(id, nombre, ciudad, pais, latitud, longitud);
        localizaciones.add(nueva);
        return r;
    }

    private boolean tieneMax4Decimales(double valor) {
        String[] partes = String.valueOf(valor).split("\\.");
        return partes.length < 2 || partes[1].length() <= 4;
    }

    private boolean existeId(String id) {
        for (Location l : localizaciones) {
            if (l.getAirportId().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    private JSONObject error(String mensaje) {
        JSONObject r = new JSONObject();
        r.put("estado", "error");
        r.put("mensaje", mensaje);
        return r;
    }
}
