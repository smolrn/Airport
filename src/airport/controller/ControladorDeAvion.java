package airport.controller;

import airport.Plane;
import org.json.JSONObject;

import java.util.List;

public class ControladorDeAvion {

    private final List<Plane> aviones;

    public ControladorDeAvion(List<Plane> aviones) {
        this.aviones = aviones;
    }

    public JSONObject registrarAvion(String id, String marca, String modelo, int capacidad, String aerolinea) {
        JSONObject respuesta = new JSONObject();

        if (id == null || !id.matches("^[A-Z]{2}\\d{5}$")) {
            return error("El ID del avión debe tener el formato XXYYYYY (2 letras + 5 dígitos).");
        }

        if (existeId(id)) {
            return error("El ID del avión ya está en uso.");
        }

        if (marca == null || marca.trim().isEmpty() ||
            modelo == null || modelo.trim().isEmpty() ||
            aerolinea == null || aerolinea.trim().isEmpty()) {
            return error("Ningún campo puede estar vacío.");
        }

        if (capacidad <= 0) {
            return error("La capacidad debe ser un número mayor que 0.");
        }

        respuesta.put("estado", "exito");
        respuesta.put("mensaje", "Avión registrado correctamente.");
        return respuesta;
    }

    private boolean existeId(String id) {
        for (Plane p : aviones) {
            if (p.getId().equalsIgnoreCase(id)) {
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
