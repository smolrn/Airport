/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package airport;

import airport.controller.ControladorDeActualizacionDePasajero;
import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.Color;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import airport.controller.ControladorDePasajero;
import airport.controller.ControladorDeAvion;
import org.json.JSONObject;
import airport.controller.ControladorDeLocalizacion;
import airport.controller.ControladorDeVuelo;
import airport.controller.ControladorParaAddToFlight;
import airport.controller.ControladorParaDelayFlight;
import org.json.JSONObject;
import java.time.LocalDateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import org.json.JSONArray;
import org.json.JSONObject;

public class AirportFrame extends javax.swing.JFrame implements Observador {

    @Override
    public void actualizar() {

        actualizarTablas();
    }

    private void cargarPasajerosDesdeArchivo() {
        try {
            File archivo = new File("pasajeros.json");
            if (!archivo.exists()) {
                return;
            }

            BufferedReader br = new BufferedReader(new FileReader(archivo));
            StringBuilder sb = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) {
                sb.append(linea);
            }
            br.close();

            JSONArray arreglo = new JSONArray(sb.toString());
            for (int i = 0; i < arreglo.length(); i++) {
                JSONObject obj = arreglo.getJSONObject(i);
                long id = obj.getLong("id");
                String firstname = obj.getString("firstname");
                String lastname = obj.getString("lastname");
                String birthdate = obj.getString("birthdate");
                int phoneCode = obj.getInt("phoneCode");
                long phone = obj.getLong("phone");
                String country = obj.getString("country");

                // Asegúrate de que el formato de la fecha de nacimiento coincida con el formato esperado
                Passenger pasajero = new Passenger(id, firstname, lastname, LocalDate.parse(birthdate), phoneCode, phone, country);
                this.passengers.add(pasajero);
                this.userSelect.addItem(String.valueOf(id));
            }

            actualizarTablas();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void actualizarTablas() {

        DefaultTableModel modelAviones = (DefaultTableModel) jTable4.getModel();
        modelAviones.setRowCount(0);
        for (Plane p : this.planes) {
            modelAviones.addRow(new Object[]{p.getId(), p.getBrand(), p.getModel(), p.getMaxCapacity(), p.getAirline(), p.getNumFlights()});
        }

        DefaultTableModel modelVuelos = (DefaultTableModel) jTable3.getModel();
        modelVuelos.setRowCount(0);
        for (Flight f : this.flights) {
            modelVuelos.addRow(new Object[]{
                f.getId(),
                f.getDepartureLocation().getAirportId(),
                f.getArrivalLocation().getAirportId(),
                (f.getScaleLocation() == null ? "-" : f.getScaleLocation().getAirportId()),
                f.getDepartureDate(),
                f.calculateArrivalDate(),
                f.getPlane().getId(),
                f.getNumPassengers()
            });
        }

        DefaultTableModel modelPasajeros = (DefaultTableModel) jTable2.getModel();
        modelPasajeros.setRowCount(0);
        for (Passenger p : this.passengers) {
            modelPasajeros.addRow(new Object[]{
                p.getId(),
                p.getFullname(),
                p.getBirthDate(),
                p.calculateAge(),
                p.generateFullPhone(),
                p.getCountry(),
                p.getNumFlights()
            });
        }
    }

    private void actualizarBloqueoPestanas(String tipoUsuario) {

        int totalPestanas = jTabbedPane1.getTabCount();

        for (int i = 0; i < totalPestanas; i++) {

            if (i == 0) {
                jTabbedPane1.setEnabledAt(i, true);
                continue;
            }

            if ("Administrador".equalsIgnoreCase(tipoUsuario)) {

                if (i == 5 || i == 6 || i == 7) {
                    jTabbedPane1.setEnabledAt(i, false);
                } else {
                    jTabbedPane1.setEnabledAt(i, true);
                }
            } else if ("Usuario".equalsIgnoreCase(tipoUsuario)) {

                if (i == 5 || i == 6 || i == 7 || i == 8 || i == 9) {
                    jTabbedPane1.setEnabledAt(i, true);
                } else {
                    jTabbedPane1.setEnabledAt(i, false);
                }
            } else {

                jTabbedPane1.setEnabledAt(i, false);
            }
        }
    }

    private void cargarAvionesDesdeArchivo() {
        try {
            File archivo = new File("aviones.json");
            if (!archivo.exists()) {
                return;
            }

            BufferedReader br = new BufferedReader(new FileReader(archivo));
            StringBuilder sb = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) {
                sb.append(linea);
            }
            br.close();

            JSONArray arreglo = new JSONArray(sb.toString());
            for (int i = 0; i < arreglo.length(); i++) {
                JSONObject obj = arreglo.getJSONObject(i);
                String id = obj.getString("id");
                String marca = obj.getString("brand");
                String modelo = obj.getString("model");
                int capacidadMax = obj.getInt("maxCapacity");
                String aerolinea = obj.getString("airline");

                Plane p = new Plane(id, marca, modelo, capacidadMax, aerolinea);
                this.planes.add(p);
                this.jComboBox1.addItem(id);
            }

            actualizarTablas();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void guardarAvionesEnArchivo() {
        try {
            JSONArray arreglo = new JSONArray();
            for (Plane p : this.planes) {
                JSONObject obj = new JSONObject();
                obj.put("id", p.getId());
                obj.put("brand", p.getBrand());
                obj.put("model", p.getModel());
                obj.put("maxCapacity", p.getMaxCapacity());
                obj.put("airline", p.getAirline());
                arreglo.put(obj);
            }

            FileWriter writer = new FileWriter("aviones.json");
            writer.write(arreglo.toString(4));
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarLocalizacionesDesdeArchivo() {
        try {
            File archivo = new File("localizaciones.json");
            if (!archivo.exists()) {
                return;
            }

            BufferedReader br = new BufferedReader(new FileReader(archivo));
            StringBuilder sb = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) {
                sb.append(linea);
            }
            br.close();

            JSONArray arreglo = new JSONArray(sb.toString());
            for (int i = 0; i < arreglo.length(); i++) {
                JSONObject obj = arreglo.getJSONObject(i);
                String id = obj.getString("id");
                String nombre = obj.getString("name");
                String ciudad = obj.getString("city");
                String pais = obj.getString("country");
                double latitud = obj.getDouble("latitude");
                double longitud = obj.getDouble("longitude");

                Location loc = new Location(id, nombre, ciudad, pais, latitud, longitud);
                this.locations.add(loc);
                this.jComboBox2.addItem(id);
                this.jComboBox3.addItem(id);
                this.jComboBox4.addItem(id);
            }

            actualizarTablas();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void guardarLocalizacionesEnArchivo() {
        try {
            JSONArray arreglo = new JSONArray();
            for (Location loc : this.locations) {
                JSONObject obj = new JSONObject();
                obj.put("id", loc.getAirportId());
                obj.put("name", loc.getAirportName());
                obj.put("city", loc.getAirportCity());
                obj.put("country", loc.getAirportCountry());
                obj.put("latitude", loc.getAirportLatitude());
                obj.put("longitude", loc.getAirportLongitude());
                arreglo.put(obj);
            }

            FileWriter writer = new FileWriter("localizaciones.json");
            writer.write(arreglo.toString(4));
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarVuelosDesdeArchivo() {
        try {
            File archivo = new File("vuelos.json");
            if (!archivo.exists()) {
                return;
            }

            BufferedReader br = new BufferedReader(new FileReader(archivo));
            StringBuilder sb = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) {
                sb.append(linea);
            }
            br.close();

            JSONArray arreglo = new JSONArray(sb.toString());
            for (int i = 0; i < arreglo.length(); i++) {
                JSONObject obj = arreglo.getJSONObject(i);

                String id = obj.getString("id");
                String planeId = obj.getString("planeId");
                String departureId = obj.getString("departureId");
                String arrivalId = obj.getString("arrivalId");
                String scaleId = obj.optString("scaleId", null); // Puede ser null
                String departureDateStr = obj.getString("departureDate");

                int hoursDurArrival = obj.getInt("hoursDurArrival");
                int minutesDurArrival = obj.getInt("minutesDurArrival");
                int hoursDurScale = obj.optInt("hoursDurScale", 0);
                int minutesDurScale = obj.optInt("minutesDurScale", 0);

                Plane plane = null;
                Location departure = null;
                Location arrival = null;
                Location scale = null;

                for (Plane p : this.planes) {
                    if (p.getId().equalsIgnoreCase(planeId)) {
                        plane = p;
                        break;
                    }
                }

                for (Location loc : this.locations) {
                    if (loc.getAirportId().equalsIgnoreCase(departureId)) {
                        departure = loc;
                    }
                    if (loc.getAirportId().equalsIgnoreCase(arrivalId)) {
                        arrival = loc;
                    }
                    if (scaleId != null && loc.getAirportId().equalsIgnoreCase(scaleId)) {
                        scale = loc;
                    }
                }

                LocalDateTime departureDate = LocalDateTime.parse(departureDateStr);

                Flight vuelo;

                if (scale == null) {
                    vuelo = new Flight(id, plane, departure, arrival, departureDate, hoursDurArrival, minutesDurArrival);
                } else {
                    vuelo = new Flight(id, plane, departure, scale, arrival, departureDate, hoursDurArrival, minutesDurArrival, hoursDurScale, minutesDurScale);
                }

                this.flights.add(vuelo);
                this.jComboBox5.addItem(id);
                this.jComboBox7.addItem(id);
            }

            actualizarTablas();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void guardarVuelosEnArchivo() {
        try {
            JSONArray arreglo = new JSONArray();
            for (Flight vuelo : this.flights) {
                JSONObject obj = new JSONObject();
                obj.put("id", vuelo.getId());
                obj.put("planeId", vuelo.getPlane().getId());
                obj.put("departureId", vuelo.getDepartureLocation().getAirportId());
                obj.put("arrivalId", vuelo.getArrivalLocation().getAirportId());
                obj.put("scaleId", vuelo.getScaleLocation() != null ? vuelo.getScaleLocation().getAirportId() : JSONObject.NULL);
                obj.put("departureDate", vuelo.getDepartureDate().toString());
                obj.put("hoursDurArrival", vuelo.getHoursDurationArrival());
                obj.put("minutesDurArrival", vuelo.getMinutesDurationArrival());
                obj.put("hoursDurScale", vuelo.getHoursDurationScale());
                obj.put("minutesDurScale", vuelo.getMinutesDurationScale());

                arreglo.put(obj);
            }

            FileWriter writer = new FileWriter("vuelos.json");
            writer.write(arreglo.toString(4));
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int x, y;
    private ArrayList<Passenger> passengers;
    private ArrayList<Plane> planes;
    private ArrayList<Location> locations;
    private ArrayList<Flight> flights;

    public AirportFrame() {
        initComponents();
        this.planes = new ArrayList<>();
        this.locations = new ArrayList<>();
        this.passengers = new ArrayList<>();
        this.flights = new ArrayList<>();

        this.cargarAvionesDesdeArchivo();
        this.cargarLocalizacionesDesdeArchivo();
        this.cargarVuelosDesdeArchivo();
        this.cargarVuelosDesdeArchivo();
        this.cargarAvionesDesdeArchivo();
        this.cargarLocalizacionesDesdeArchivo();
        this.cargarPasajerosDesdeArchivo();
        this.passengers = new ArrayList<>();
        this.planes = new ArrayList<>();
        this.locations = new ArrayList<>();
        this.flights = new ArrayList<>();

        this.setBackground(new Color(0, 0, 0, 0));
        this.setLocationRelativeTo(null);

        this.generateMonths();
        this.generateDays();
        this.generateHours();
        this.generateMinutes();
        this.blockPanels();
    }

    private void blockPanels() {
        //9, 11
        for (int i = 1; i < jTabbedPane1.getTabCount(); i++) {
            if (i != 9 && i != 11) {
                jTabbedPane1.setEnabledAt(i, false);
            }
        }
    }

    private void generateMonths() {
        for (int i = 1; i < 13; i++) {
            MONTH.addItem("" + i);
            MONTH1.addItem("" + i);
            MONTH5.addItem("" + i);
        }
    }

    private void generateDays() {
        for (int i = 1; i < 32; i++) {
            DAY.addItem("" + i);
            DAY1.addItem("" + i);
            DAY5.addItem("" + i);
        }
    }

    private void generateHours() {
        for (int i = 0; i < 24; i++) {
            MONTH2.addItem("" + i);
            MONTH3.addItem("" + i);
            MONTH4.addItem("" + i);
            jComboBox6.addItem("" + i);
        }
    }

    private void generateMinutes() {
        for (int i = 0; i < 60; i++) {
            DAY2.addItem("" + i);
            DAY3.addItem("" + i);
            DAY4.addItem("" + i);
            jComboBox8.addItem("" + i);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelRound1 = new airport.PanelRound();
        panelRound2 = new airport.PanelRound();
        jButton13 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        user = new javax.swing.JRadioButton();
        administrator = new javax.swing.JRadioButton();
        userSelect = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        MONTH = new javax.swing.JComboBox<>();
        jTextField7 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        DAY = new javax.swing.JComboBox<>();
        jButton8 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        jTextField15 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jTextField16 = new javax.swing.JTextField();
        jTextField17 = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jTextField18 = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jTextField19 = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel24 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jTextField21 = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        MONTH1 = new javax.swing.JComboBox<>();
        jLabel31 = new javax.swing.JLabel();
        DAY1 = new javax.swing.JComboBox<>();
        jLabel32 = new javax.swing.JLabel();
        MONTH2 = new javax.swing.JComboBox<>();
        jLabel33 = new javax.swing.JLabel();
        DAY2 = new javax.swing.JComboBox<>();
        MONTH3 = new javax.swing.JComboBox<>();
        jLabel34 = new javax.swing.JLabel();
        DAY3 = new javax.swing.JComboBox<>();
        jLabel35 = new javax.swing.JLabel();
        MONTH4 = new javax.swing.JComboBox<>();
        DAY4 = new javax.swing.JComboBox<>();
        jButton11 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jTextField20 = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        jTextField22 = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        jTextField23 = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jTextField24 = new javax.swing.JTextField();
        MONTH5 = new javax.swing.JComboBox<>();
        DAY5 = new javax.swing.JComboBox<>();
        jTextField25 = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        jTextField26 = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jTextField27 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jTextField28 = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox<>();
        jButton12 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jButton6 = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jComboBox6 = new javax.swing.JComboBox<>();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jComboBox7 = new javax.swing.JComboBox<>();
        jLabel48 = new javax.swing.JLabel();
        jComboBox8 = new javax.swing.JComboBox<>();
        jButton7 = new javax.swing.JButton();
        panelRound3 = new airport.PanelRound();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        panelRound1.setRadius(40);
        panelRound1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelRound2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                panelRound2MouseDragged(evt);
            }
        });
        panelRound2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                panelRound2MousePressed(evt);
            }
        });

        jButton13.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jButton13.setText("X");
        jButton13.setBorderPainted(false);
        jButton13.setContentAreaFilled(false);
        jButton13.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelRound2Layout = new javax.swing.GroupLayout(panelRound2);
        panelRound2.setLayout(panelRound2Layout);
        panelRound2Layout.setHorizontalGroup(
            panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRound2Layout.createSequentialGroup()
                .addContainerGap(1083, Short.MAX_VALUE)
                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );
        panelRound2Layout.setVerticalGroup(
            panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound2Layout.createSequentialGroup()
                .addComponent(jButton13)
                .addGap(0, 12, Short.MAX_VALUE))
        );

        panelRound1.add(panelRound2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1150, -1));

        jTabbedPane1.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        user.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        user.setText("User");
        user.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userActionPerformed(evt);
            }
        });
        jPanel1.add(user, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 230, -1, -1));

        administrator.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        administrator.setText("Administrator");
        administrator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                administratorActionPerformed(evt);
            }
        });
        jPanel1.add(administrator, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 164, -1, -1));

        userSelect.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        userSelect.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select User" }));
        userSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userSelectActionPerformed(evt);
            }
        });
        jPanel1.add(userSelect, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 300, 130, -1));

        jTabbedPane1.addTab("Administration", jPanel1);

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel1.setText("Country:");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 400, -1, -1));

        jLabel2.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel2.setText("ID:");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 90, -1, -1));

        jLabel3.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel3.setText("First Name:");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, -1, -1));

        jLabel4.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel4.setText("Last Name:");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 220, -1, -1));

        jLabel5.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel5.setText("Birthdate:");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 280, -1, -1));

        jLabel6.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel6.setText("+");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 340, 20, -1));

        jTextField1.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jPanel2.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 340, 50, -1));

        jTextField2.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jPanel2.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 90, 130, -1));

        jTextField3.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jPanel2.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 280, 90, -1));

        jTextField4.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jPanel2.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 400, 130, -1));

        jTextField5.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jPanel2.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 340, 130, -1));

        jLabel7.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel7.setText("Phone:");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 340, -1, -1));

        jLabel8.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel8.setText("-");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 280, 30, -1));

        jTextField6.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jPanel2.add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 220, 130, -1));

        jLabel9.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel9.setText("-");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 340, 30, -1));

        MONTH.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        MONTH.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Month" }));
        jPanel2.add(MONTH, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 280, -1, -1));

        jTextField7.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jPanel2.add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 160, 130, -1));

        jLabel10.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel10.setText("-");
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 280, 30, -1));

        DAY.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        DAY.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Day" }));
        jPanel2.add(DAY, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 280, -1, -1));

        jButton8.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jButton8.setText("Register");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 480, -1, -1));

        jTabbedPane1.addTab("Passenger registration", jPanel2);

        jPanel3.setLayout(null);

        jLabel11.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel11.setText("ID:");
        jPanel3.add(jLabel11);
        jLabel11.setBounds(53, 96, 22, 25);

        jTextField8.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jPanel3.add(jTextField8);
        jTextField8.setBounds(180, 93, 130, 31);

        jLabel12.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel12.setText("Brand:");
        jPanel3.add(jLabel12);
        jLabel12.setBounds(53, 157, 50, 25);

        jTextField9.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jPanel3.add(jTextField9);
        jTextField9.setBounds(180, 154, 130, 31);

        jTextField10.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jPanel3.add(jTextField10);
        jTextField10.setBounds(180, 213, 130, 31);

        jLabel13.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel13.setText("Model:");
        jPanel3.add(jLabel13);
        jLabel13.setBounds(53, 216, 55, 25);

        jTextField11.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jPanel3.add(jTextField11);
        jTextField11.setBounds(180, 273, 130, 31);

        jLabel14.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel14.setText("Max Capacity:");
        jPanel3.add(jLabel14);
        jLabel14.setBounds(53, 276, 109, 25);

        jTextField12.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jPanel3.add(jTextField12);
        jTextField12.setBounds(180, 333, 130, 31);

        jLabel15.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel15.setText("Airline:");
        jPanel3.add(jLabel15);
        jLabel15.setBounds(53, 336, 70, 25);

        jButton9.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jButton9.setText("Create");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton9);
        jButton9.setBounds(490, 480, 120, 40);

        jTabbedPane1.addTab("Airplane registration", jPanel3);

        jLabel16.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel16.setText("Airport ID:");

        jTextField13.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        jLabel17.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel17.setText("Airport name:");

        jTextField14.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        jTextField15.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        jLabel18.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel18.setText("Airport city:");

        jLabel19.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel19.setText("Airport country:");

        jTextField16.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        jTextField17.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        jLabel20.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel20.setText("Airport latitude:");

        jLabel21.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel21.setText("Airport longitude:");

        jTextField18.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        jButton10.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jButton10.setText("Create");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel17)
                            .addComponent(jLabel18)
                            .addComponent(jLabel19)
                            .addComponent(jLabel20)
                            .addComponent(jLabel21))
                        .addGap(80, 80, 80)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(515, 515, 515)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(515, 515, 515))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addGap(36, 36, 36)
                        .addComponent(jLabel17)
                        .addGap(34, 34, 34)
                        .addComponent(jLabel18)
                        .addGap(35, 35, 35)
                        .addComponent(jLabel19)
                        .addGap(35, 35, 35)
                        .addComponent(jLabel20))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(44, 44, 44)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47))
        );

        jTabbedPane1.addTab("Location registration", jPanel13);

        jLabel22.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel22.setText("ID:");

        jTextField19.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        jLabel23.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel23.setText("Plane:");

        jComboBox1.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Plane" }));

        jComboBox2.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Location" }));

        jLabel24.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel24.setText("Departure location:");

        jComboBox3.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Location" }));

        jLabel25.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel25.setText("Arrival location:");

        jLabel26.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel26.setText("Scale location:");

        jComboBox4.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Location" }));

        jLabel27.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel27.setText("Duration:");

        jLabel28.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel28.setText("Duration:");

        jLabel29.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel29.setText("Departure date:");

        jTextField21.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        jLabel30.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel30.setText("-");

        MONTH1.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        MONTH1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Month" }));

        jLabel31.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel31.setText("-");

        DAY1.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        DAY1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Day" }));

        jLabel32.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel32.setText("-");

        MONTH2.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        MONTH2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hour" }));

        jLabel33.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel33.setText("-");

        DAY2.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        DAY2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Minute" }));

        MONTH3.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        MONTH3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hour" }));

        jLabel34.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel34.setText("-");

        DAY3.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        DAY3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Minute" }));

        jLabel35.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel35.setText("-");

        MONTH4.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        MONTH4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hour" }));

        DAY4.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        DAY4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Minute" }));

        jButton11.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jButton11.setText("Create");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addGap(46, 46, 46)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22)
                            .addComponent(jLabel23))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField19)
                            .addComponent(jComboBox1, 0, 130, Short.MAX_VALUE))))
                .addGap(45, 45, 45)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel27)
                    .addComponent(jLabel28)
                    .addComponent(jLabel29))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(MONTH1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14, 14, 14)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(DAY1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(MONTH2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14, 14, 14)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(DAY2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(30, 30, 30))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(MONTH3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(14, 14, 14)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(20, 20, 20)
                                        .addComponent(DAY3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(MONTH4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(14, 14, 14)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(20, 20, 20)
                                        .addComponent(DAY4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(530, 530, 530))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel22))
                    .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(MONTH2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32)
                    .addComponent(jLabel33)
                    .addComponent(DAY2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel24)
                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel29))
                            .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(MONTH1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel30)
                            .addComponent(jLabel31)
                            .addComponent(DAY1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(38, 38, 38)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel25)
                                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel28))
                            .addComponent(MONTH3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel34)
                            .addComponent(DAY3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(MONTH4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel35)
                            .addComponent(DAY4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel26)
                                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel27)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 134, Short.MAX_VALUE)
                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50))
        );

        jTabbedPane1.addTab("Flight registration", jPanel4);

        jLabel36.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel36.setText("ID:");

        jTextField20.setEditable(false);
        jTextField20.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jTextField20.setEnabled(false);

        jLabel37.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel37.setText("First Name:");

        jTextField22.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        jLabel38.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel38.setText("Last Name:");

        jTextField23.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        jLabel39.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel39.setText("Birthdate:");

        jTextField24.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        MONTH5.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        MONTH5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Month" }));

        DAY5.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        DAY5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Day" }));

        jTextField25.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        jLabel40.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel40.setText("-");

        jTextField26.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        jLabel41.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel41.setText("+");

        jLabel42.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel42.setText("Phone:");

        jLabel43.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel43.setText("Country:");

        jTextField27.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        jButton1.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jButton1.setText("Update");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel36)
                                .addGap(108, 108, 108)
                                .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel37)
                                .addGap(41, 41, 41)
                                .addComponent(jTextField22, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel38)
                                .addGap(43, 43, 43)
                                .addComponent(jTextField23, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel39)
                                .addGap(55, 55, 55)
                                .addComponent(jTextField24, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(MONTH5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)
                                .addComponent(DAY5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel42)
                                .addGap(56, 56, 56)
                                .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jTextField26, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jTextField25, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel43)
                                .addGap(63, 63, 63)
                                .addComponent(jTextField27, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(507, 507, 507)
                        .addComponent(jButton1)))
                .addContainerGap(555, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel36)
                    .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel37)
                    .addComponent(jTextField22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel38)
                    .addComponent(jTextField23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel39)
                    .addComponent(jTextField24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(MONTH5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(DAY5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel42)
                    .addComponent(jLabel41)
                    .addComponent(jTextField26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel40)
                    .addComponent(jTextField25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel43)
                    .addComponent(jTextField27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(113, 113, 113))
        );

        jTabbedPane1.addTab("Update info", jPanel5);

        jTextField28.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jTextField28.setEnabled(false);

        jLabel44.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel44.setText("ID:");

        jLabel45.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel45.setText("Flight:");

        jComboBox5.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Flight" }));

        jButton12.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jButton12.setText("Add");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel44)
                    .addComponent(jLabel45))
                .addGap(79, 79, 79)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField28, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(829, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(509, 509, 509))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel44))
                    .addComponent(jTextField28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel45)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 288, Short.MAX_VALUE)
                .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(85, 85, 85))
        );

        jTabbedPane1.addTab("Add to flight", jPanel6);

        jTable1.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Departure Date", "Arrival Date"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButton2.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jButton2.setText("Refresh");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(269, 269, 269)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 590, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(291, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(527, 527, 527))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Show my flights", jPanel7);

        jTable2.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Birthdate", "Age", "Phone", "Country", "Num Flight"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Long.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jButton3.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jButton3.setText("Refresh");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(489, 489, 489)
                        .addComponent(jButton3))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1078, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(72, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton3)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Show all passengers", jPanel8);

        jTable3.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Departure Airport ID", "Arrival Airport ID", "Scale Airport ID", "Departure Date", "Arrival Date", "Plane ID", "Number Passengers"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTable3);

        jButton4.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jButton4.setText("Refresh");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(521, 521, 521)
                        .addComponent(jButton4)))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton4)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Show all flights", jPanel9);

        jButton5.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jButton5.setText("Refresh");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Brand", "Model", "Max Capacity", "Airline", "Number Flights"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(jTable4);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(508, 508, 508)
                        .addComponent(jButton5))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(145, 145, 145)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 816, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(189, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(45, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(jButton5)
                .addGap(17, 17, 17))
        );

        jTabbedPane1.addTab("Show all planes", jPanel10);

        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Airport ID", "Airport Name", "City", "Country"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(jTable5);

        jButton6.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jButton6.setText("Refresh");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(508, 508, 508)
                        .addComponent(jButton6))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(226, 226, 226)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 652, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(272, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(48, Short.MAX_VALUE)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jButton6)
                .addGap(17, 17, 17))
        );

        jTabbedPane1.addTab("Show all locations", jPanel11);

        jComboBox6.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hour" }));

        jLabel46.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel46.setText("Hours:");

        jLabel47.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel47.setText("ID:");

        jComboBox7.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID" }));

        jLabel48.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel48.setText("Minutes:");

        jComboBox8.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jComboBox8.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Minute" }));

        jButton7.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jButton7.setText("Delay");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(94, 94, 94)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel48)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel47)
                            .addComponent(jLabel46))
                        .addGap(79, 79, 79)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox6, 0, 105, Short.MAX_VALUE)
                            .addComponent(jComboBox7, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(820, 820, 820))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton7)
                .addGap(531, 531, 531))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel47)
                    .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel46)
                    .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel48)
                    .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 307, Short.MAX_VALUE)
                .addComponent(jButton7)
                .addGap(33, 33, 33))
        );

        jTabbedPane1.addTab("Delay flight", jPanel12);

        panelRound1.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 41, 1150, 620));

        javax.swing.GroupLayout panelRound3Layout = new javax.swing.GroupLayout(panelRound3);
        panelRound3.setLayout(panelRound3Layout);
        panelRound3Layout.setHorizontalGroup(
            panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1150, Short.MAX_VALUE)
        );
        panelRound3Layout.setVerticalGroup(
            panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 36, Short.MAX_VALUE)
        );

        panelRound1.add(panelRound3, new org.netbeans.lib.awtextra.AbsoluteConstraints(-2, 660, 1150, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelRound1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelRound1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void panelRound2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRound2MousePressed
        x = evt.getX();
        y = evt.getY();
    }//GEN-LAST:event_panelRound2MousePressed

    private void panelRound2MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRound2MouseDragged
        this.setLocation(this.getLocation().x + evt.getX() - x, this.getLocation().y + evt.getY() - y);
    }//GEN-LAST:event_panelRound2MouseDragged

    private void administratorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_administratorActionPerformed
        if (user.isSelected()) {
            user.setSelected(false);
            userSelect.setSelectedIndex(0);
        }
        actualizarBloqueoPestanas("Administrador");
    }//GEN-LAST:event_administratorActionPerformed

    private void userActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userActionPerformed
        if (administrator.isSelected()) {
            administrator.setSelected(false);
        }
        actualizarBloqueoPestanas("Usuario");
    }//GEN-LAST:event_userActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        try {
            // Leer los datos del formulario
            long id = Long.parseLong(jTextField2.getText());
            String firstname = jTextField7.getText();
            String lastname = jTextField6.getText();
            int year = Integer.parseInt(jTextField3.getText());
            int month = Integer.parseInt(MONTH.getSelectedItem().toString());
            int day = Integer.parseInt(DAY.getSelectedItem().toString());
            int phoneCode = Integer.parseInt(jTextField1.getText());
            long phone = Long.parseLong(jTextField5.getText());
            String country = jTextField4.getText();

            String fechaNacimiento = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);

            airport.controller.ControladorDePasajero controlador = new airport.controller.ControladorDePasajero(this.passengers);
            org.json.JSONObject respuesta = controlador.registrarPasajero(id, firstname, lastname, fechaNacimiento, phoneCode, phone, country);

            if (respuesta.getString("estado").equals("exito")) {

                java.time.LocalDate birthDate = java.time.LocalDate.of(year, month, day);
                Passenger nuevo = new Passenger(id, firstname, lastname, birthDate, phoneCode, phone, country);
                this.passengers.add(nuevo);
                this.userSelect.addItem("" + id);

                javax.swing.JOptionPane.showMessageDialog(this, respuesta.getString("mensaje"));
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, respuesta.getString("mensaje"), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        try {
            String id = jTextField8.getText().trim();
            String brand = jTextField9.getText().trim();
            String model = jTextField10.getText().trim();
            int maxCapacity = Integer.parseInt(jTextField11.getText().trim());
            String airline = jTextField12.getText().trim(); // Aerolínea

            ControladorDeAvion controlador = new ControladorDeAvion(this.planes);
            JSONObject respuesta = controlador.registrarAvion(id, brand, model, maxCapacity, airline);

            if (respuesta.getString("estado").equals("exito")) {

                Plane avion = new Plane(id, brand, model, maxCapacity, airline);
                this.planes.add(avion);
                this.jComboBox1.addItem(id);

                this.guardarAvionesEnArchivo();

                javax.swing.JOptionPane.showMessageDialog(this, respuesta.getString("mensaje"));

                jTextField8.setText("");
                jTextField9.setText("");
                jTextField10.setText("");
                jTextField11.setText("");
                jTextField12.setText("");
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, respuesta.getString("mensaje"), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        try {
            String id = jTextField13.getText().trim();
            String name = jTextField14.getText().trim();
            String city = jTextField15.getText().trim();
            String country = jTextField16.getText().trim();
            double latitude = Double.parseDouble(jTextField17.getText().trim());
            double longitude = Double.parseDouble(jTextField18.getText().trim());

            ControladorDeLocalizacion controlador = new ControladorDeLocalizacion(this.locations);
            JSONObject respuesta = controlador.registrarLocalizacion(id, name, city, country, latitude, longitude);

            if (respuesta.getString("estado").equals("exito")) {
                Location nueva = new Location(id, name, city, country, latitude, longitude);
                this.locations.add(nueva);

                this.jComboBox2.addItem(id);
                this.jComboBox3.addItem(id);
                this.jComboBox4.addItem(id);

                this.guardarLocalizacionesEnArchivo();

                javax.swing.JOptionPane.showMessageDialog(this, respuesta.getString("mensaje"));

                // Limpiar campos
                jTextField13.setText("");
                jTextField14.setText("");
                jTextField15.setText("");
                jTextField16.setText("");
                jTextField17.setText("");
                jTextField18.setText("");
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, respuesta.getString("mensaje"), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        try {
            String id = jTextField19.getText().trim();
            String planeId = jComboBox1.getSelectedItem().toString();
            String departureId = jComboBox2.getSelectedItem().toString();
            String arrivalId = jComboBox3.getSelectedItem().toString();
            String scaleId = jComboBox4.getSelectedItem().toString();

            int year = Integer.parseInt(jTextField21.getText().trim());
            int month = Integer.parseInt(MONTH1.getSelectedItem().toString());
            int day = Integer.parseInt(DAY1.getSelectedItem().toString());
            int hour = Integer.parseInt(MONTH2.getSelectedItem().toString());
            int minute = Integer.parseInt(DAY2.getSelectedItem().toString());

            int durHour = Integer.parseInt(MONTH3.getSelectedItem().toString());
            int durMin = Integer.parseInt(DAY3.getSelectedItem().toString());

            int scaleHour = Integer.parseInt(MONTH4.getSelectedItem().toString());
            int scaleMin = Integer.parseInt(DAY4.getSelectedItem().toString());

            LocalDateTime departureDate = LocalDateTime.of(year, month, day, hour, minute);

            ControladorDeVuelo controlador = new ControladorDeVuelo(this.flights, this.planes, this.locations);
            JSONObject respuesta = controlador.registrarVuelo(
                    id, planeId,
                    departureId, arrivalId, scaleId,
                    departureDate,
                    durHour, durMin,
                    scaleHour, scaleMin
            );

            if (respuesta.getString("estado").equals("exito")) {

                Plane plane = null;
                Location departure = null, arrival = null, scale = null;

                for (Plane p : planes) {
                    if (p.getId().equalsIgnoreCase(planeId)) {
                        plane = p;
                    }
                }
                for (Location loc : locations) {
                    if (loc.getAirportId().equalsIgnoreCase(departureId)) {
                        departure = loc;
                    }
                    if (loc.getAirportId().equalsIgnoreCase(arrivalId)) {
                        arrival = loc;
                    }
                    if (loc.getAirportId().equalsIgnoreCase(scaleId)) {
                        scale = loc;
                    }
                }

                Flight nuevo;
                if (scaleId.equals("Location")) {
                    nuevo = new Flight(id, plane, departure, arrival, departureDate, durHour, durMin);
                } else {
                    nuevo = new Flight(id, plane, departure, scale, arrival, departureDate, durHour, durMin, scaleHour, scaleMin);
                }

                flights.add(nuevo);
                jComboBox5.addItem(id);
                jComboBox7.addItem(id);
                this.guardarVuelosEnArchivo();
                javax.swing.JOptionPane.showMessageDialog(this, respuesta.getString("mensaje"));

                jTextField19.setText("");
                jTextField21.setText("");
                jComboBox1.setSelectedIndex(0);
                jComboBox2.setSelectedIndex(0);
                jComboBox3.setSelectedIndex(0);
                jComboBox4.setSelectedIndex(0);
                MONTH1.setSelectedIndex(0);
                DAY1.setSelectedIndex(0);
                MONTH2.setSelectedIndex(0);
                DAY2.setSelectedIndex(0);
                MONTH3.setSelectedIndex(0);
                DAY3.setSelectedIndex(0);
                MONTH4.setSelectedIndex(0);
                DAY4.setSelectedIndex(0);

            } else {
                javax.swing.JOptionPane.showMessageDialog(this, respuesta.getString("mensaje"), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            long id = Long.parseLong(jTextField20.getText().trim());
            String firstname = jTextField22.getText().trim();
            String lastname = jTextField23.getText().trim();
            String fecha = jTextField24.getText().trim();
            int month = Integer.parseInt(MONTH5.getSelectedItem().toString());
            int day = Integer.parseInt(DAY5.getSelectedItem().toString());
            String fechaNacimiento = fecha + "-" + month + "-" + day;

            int phoneCode = Integer.parseInt(jTextField26.getText().trim());
            long phone = Long.parseLong(jTextField25.getText().trim());
            String country = jTextField27.getText().trim();

            ControladorDeActualizacionDePasajero controlador = new ControladorDeActualizacionDePasajero(this.passengers);
            JSONObject respuesta = controlador.actualizarPasajero(id, firstname, lastname, fechaNacimiento, phoneCode, phone, country);

            if (respuesta.getString("estado").equals("exito")) {
                javax.swing.JOptionPane.showMessageDialog(this, respuesta.getString("mensaje"));

                jTextField20.setText("");
                jTextField22.setText("");
                jTextField23.setText("");
                jTextField24.setText("");
                MONTH5.setSelectedIndex(0);
                DAY5.setSelectedIndex(0);
                jTextField26.setText("");
                jTextField25.setText("");
                jTextField27.setText("");
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, respuesta.getString("mensaje"), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        try {
            long idPasajero = Long.parseLong(jTextField28.getText().trim());
            String idVuelo = jComboBox5.getSelectedItem().toString();

            ControladorParaAddToFlight controlador = new ControladorParaAddToFlight(this.passengers, this.flights);
            JSONObject respuesta = controlador.agregarPasajeroAVuelo(idPasajero, idVuelo);

            if (respuesta.getString("estado").equals("exito")) {
                javax.swing.JOptionPane.showMessageDialog(this, respuesta.getString("mensaje"));

                // Limpiar campo ID pasajero
                jTextField28.setText("");
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, respuesta.getString("mensaje"), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        try {
            String idVuelo = jComboBox7.getSelectedItem().toString();
            int horas = Integer.parseInt(jComboBox6.getSelectedItem().toString());
            int minutos = Integer.parseInt(jComboBox8.getSelectedItem().toString());

            ControladorParaDelayFlight controlador = new ControladorParaDelayFlight(this.flights);
            JSONObject respuesta = controlador.retrasarVuelo(idVuelo, horas, minutos);

            if (respuesta.getString("estado").equals("exito")) {
                javax.swing.JOptionPane.showMessageDialog(this, respuesta.getString("mensaje"));
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, respuesta.getString("mensaje"), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        long passengerId = Long.parseLong(userSelect.getItemAt(userSelect.getSelectedIndex()));

        Passenger passenger = null;
        for (Passenger p : this.passengers) {
            if (p.getId() == passengerId) {
                passenger = p;
            }
        }

        ArrayList<Flight> flights = passenger.getFlights();
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        for (Flight flight : flights) {
            model.addRow(new Object[]{flight.getId(), flight.getDepartureDate(), flight.calculateArrivalDate()});
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        model.setRowCount(0);
        for (Passenger passenger : this.passengers) {
            model.addRow(new Object[]{passenger.getId(), passenger.getFullname(), passenger.getBirthDate(), passenger.calculateAge(), passenger.generateFullPhone(), passenger.getCountry(), passenger.getNumFlights()});
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) jTable3.getModel();
        model.setRowCount(0);
        for (Flight flight : this.flights) {
            model.addRow(new Object[]{flight.getId(), flight.getDepartureLocation().getAirportId(), flight.getArrivalLocation().getAirportId(), (flight.getScaleLocation() == null ? "-" : flight.getScaleLocation().getAirportId()), flight.getDepartureDate(), flight.calculateArrivalDate(), flight.getPlane().getId(), flight.getNumPassengers()});
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) jTable4.getModel();
        model.setRowCount(0);
        for (Plane plane : this.planes) {
            model.addRow(new Object[]{plane.getId(), plane.getBrand(), plane.getModel(), plane.getMaxCapacity(), plane.getAirline(), plane.getNumFlights()});
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) jTable5.getModel();
        model.setRowCount(0);
        for (Location location : this.locations) {
            model.addRow(new Object[]{location.getAirportId(), location.getAirportName(), location.getAirportCity(), location.getAirportCountry()});
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jButton13ActionPerformed

    private void userSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userSelectActionPerformed
        try {
            String id = userSelect.getSelectedItem().toString();
            if (!id.equals(userSelect.getItemAt(0))) {
                jTextField20.setText(id);
                jTextField28.setText(id);
            } else {
                jTextField20.setText("");
                jTextField28.setText("");
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_userSelectActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        System.setProperty("flatlaf.useNativeLibrary", "false");

        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AirportFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> DAY;
    private javax.swing.JComboBox<String> DAY1;
    private javax.swing.JComboBox<String> DAY2;
    private javax.swing.JComboBox<String> DAY3;
    private javax.swing.JComboBox<String> DAY4;
    private javax.swing.JComboBox<String> DAY5;
    private javax.swing.JComboBox<String> MONTH;
    private javax.swing.JComboBox<String> MONTH1;
    private javax.swing.JComboBox<String> MONTH2;
    private javax.swing.JComboBox<String> MONTH3;
    private javax.swing.JComboBox<String> MONTH4;
    private javax.swing.JComboBox<String> MONTH5;
    private javax.swing.JRadioButton administrator;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JComboBox<String> jComboBox7;
    private javax.swing.JComboBox<String> jComboBox8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextField22;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField24;
    private javax.swing.JTextField jTextField25;
    private javax.swing.JTextField jTextField26;
    private javax.swing.JTextField jTextField27;
    private javax.swing.JTextField jTextField28;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private airport.PanelRound panelRound1;
    private airport.PanelRound panelRound2;
    private airport.PanelRound panelRound3;
    private javax.swing.JRadioButton user;
    private javax.swing.JComboBox<String> userSelect;
    // End of variables declaration//GEN-END:variables
}
