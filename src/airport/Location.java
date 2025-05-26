/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airport;

/**
 *
 * @author edangulo
 */
public class Location {

    private final String airportId;
    private String airportName;
    private String airportCity;
    private String airportCountry;
    private double airportLatitude;
    private double airportLongitude;

    public Location(String airportId, String airportName, String airportCity, String airportCountry, double airportLatitude, double airportLongitude) {
        if (airportId == null || !airportId.matches("^[A-Z]{3}$")) {
            throw new IllegalArgumentException("ID de localización inválido");
        }
        if (airportName == null || airportName.isBlank()
                || airportCity == null || airportCity.isBlank()
                || airportCountry == null || airportCountry.isBlank()) {
            throw new IllegalArgumentException("Campos vacíos");
        }
        if (airportLatitude < -90 || airportLatitude > 90) {
            throw new IllegalArgumentException("Latitud inválida");
        }
        if (airportLongitude < -180 || airportLongitude > 180) {
            throw new IllegalArgumentException("Longitud inválida");
        }

        this.airportId = airportId;
        this.airportName = airportName;
        this.airportCity = airportCity;
        this.airportCountry = airportCountry;
        this.airportLatitude = airportLatitude;
        this.airportLongitude = airportLongitude;
    }

    public String getAirportId() {
        return airportId;
    }

    public String getAirportName() {
        return airportName;
    }

    public String getAirportCity() {
        return airportCity;
    }

    public String getAirportCountry() {
        return airportCountry;
    }

    public double getAirportLatitude() {
        return airportLatitude;
    }

    public double getAirportLongitude() {
        return airportLongitude;
    }

    public Location(Location otro) {
        this.airportId = otro.airportId;
        this.airportName = otro.airportName;
        this.airportCity = otro.airportCity;
        this.airportCountry = otro.airportCountry;
        this.airportLatitude = otro.airportLatitude;
        this.airportLongitude = otro.airportLongitude;
    }

}
