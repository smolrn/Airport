package airport.controller;

import airport.Passenger;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.List;

public class ControladorDeActualizacionDePasajero {

    private final List<Passenger> pasajeros;

    public ControladorDeActualizacionDePasajero(List<Passenger> pasajeros) {
        this.pasajeros = pasajeros;
    }

    public JSONObject actualizarPasajero(long id, String nombre, String apellido, String fechaNacimiento, int codigo, long telefono, String pais) {
        JSONObject r = new JSONObject();

        Passenger p = buscarPasajeroPorId(id);
        if (p == null) {
            return error("Pasajero no encontrado.");
        }

        if (codigo < 0 || String.valueOf(codigo).length() > 3) {
            return error("Codigo telefonico invalido.");
        }

        if (telefono < 0 || String.valueOf(telefono).length() > 11) {
            return error("Telefono invalido.");
        }

        if (nombre == null || nombre.trim().isEmpty()
                || apellido == null || apellido.trim().isEmpty()
                || pais == null || pais.trim().isEmpty()) {
            return error("No puede haber campos vacios.");
        }

        try {
            LocalDate.parse(fechaNacimiento);
        } catch (Exception e) {
            return error("Fecha de nacimiento invalida.");
        }

        p.setFirstname(nombre);
        p.setLastname(apellido);
        p.setBirthDate(LocalDate.parse(fechaNacimiento));
        p.setCountryPhoneCode(codigo);
        p.setPhone(telefono);
        p.setCountry(pais);

        r.put("estado", "exito");
        r.put("mensaje", "Pasajero actualizado correctamente.");
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

    private JSONObject error(String mensaje) {
        JSONObject r = new JSONObject();
        r.put("estado", "error");
        r.put("mensaje", mensaje);
        return r;
    }
}
