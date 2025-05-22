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

   
    public void agregarAvion(Plane avion) {
       
        this.notificarObservadores();  
    }

    private final String id;
    private String brand;
    private String model;
    private final int maxCapacity;
    private String airline;
    private ArrayList<Flight> flights;

    public Plane(String id, String brand, String model, int maxCapacity, String airline) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.maxCapacity = maxCapacity;
        this.airline = airline;
        this.flights = new ArrayList<>();
    }

    public void addFlight(Flight flight) {
        this.flights.add(flight);
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
    
}
