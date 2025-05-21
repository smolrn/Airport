package airport.controller;

import airport.Passenger;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.List;

public class ControladorDePasajero {

    private final List<Passenger> pasajeros;

    public ControladorDePasajero(List<Passenger> pasajeros) {
        this.pasajeros = pasajeros;
    }

    public JSONObject registrarPasajero(long id, String nombre, String apellido, String fechaNacimiento, int codigo, long telefono, String pais) {
        JSONObject respuesta = new JSONObject();

        if (id < 0 || String.valueOf(id).length() > 15 || existeId(id)) {
            return error("El ID debe ser único, mayor o igual a 0 y tener máximo 15 dígitos.");
        }

        if (codigo < 0 || String.valueOf(codigo).length() > 3) {
            return error("El código telefónico debe tener máximo 3 dígitos.");
        }

        if (telefono < 0 || String.valueOf(telefono).length() > 11) {
            return error("El teléfono debe tener máximo 11 dígitos.");
        }

        if (nombre == null || nombre.trim().isEmpty() ||
            apellido == null || apellido.trim().isEmpty() ||
            pais == null || pais.trim().isEmpty()) {
            return error("Los campos no pueden estar vacíos.");
        }

        try {
            LocalDate.parse(fechaNacimiento);
        } catch (Exception e) {
            return error("Fecha de nacimiento inválida.");
        }

        respuesta.put("estado", "exito");
        respuesta.put("mensaje", "Pasajero registrado correctamente.");
        return respuesta;
    }

    private boolean existeId(long id) {
        for (Passenger p : pasajeros) {
            if (p.getId() == id) return true;
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
