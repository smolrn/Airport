/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airport;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author edangulo
 */
public class Plane {

    private List<Observador> observadores = new ArrayList<>();

    public void agregarObservador(Observador observador) {
        this.observadores.add(observador);
    }

    public void notificarObservadores() {
        for (Observador observador : observadores) {
            observador.actualizar();
        }
    }

    private final String id;
    private String brand;
    private String model;
    private final int maxCapacity;
    private String airline;
    private ArrayList<Flight> flights;

    public Plane(String id, String brand, String model, int maxCapacity, String airline) {
        if (id == null || !id.matches("^[A-Z]{2}\\d{5}$")) {
            throw new IllegalArgumentException("ID de avión inválido");
        }
        if (brand == null || brand.isBlank() || model == null || model.isBlank() || airline == null || airline.isBlank()) {
            throw new IllegalArgumentException("Campos vacíos");
        }
        if (maxCapacity <= 0) {
            throw new IllegalArgumentException("Capacidad inválida");
        }

        this.id = id;
        this.brand = brand;
        this.model = model;
        this.maxCapacity = maxCapacity;
        this.airline = airline;
        this.flights = new ArrayList<>();
    }

    public void addFlight(Flight flight) {
        this.flights.add(flight);
        notificarObservadores();
    }

    public String getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public String getAirline() {
        return airline;
    }

    public ArrayList<Flight> getFlights() {
        return flights;
    }

    public int getNumFlights() {
        return flights.size();
    }

    public Plane(Plane otro) {
        this.id = otro.id;
        this.brand = otro.brand;
        this.model = otro.model;
        this.maxCapacity = otro.maxCapacity;
        this.airline = otro.airline;
        this.flights = new ArrayList<>(otro.flights);
    }

}
