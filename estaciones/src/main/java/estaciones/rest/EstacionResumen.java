package estaciones.rest;

import estaciones.modelo.Estacion;

public class EstacionResumen {
    private String id;
    private String nombre;
    private int numPuestos;
    private int numBicicletas;
    private boolean huecosLibres;
    private String direccion;
    private double latitud;
    private double longitud;

    public EstacionResumen(String id, String nombre, int numPuestos, int numBicicletas, boolean huecosLibres,
                           String direccion, double latitud, double longitud) {
        this.id = id;
        this.nombre = nombre;
        this.numPuestos = numPuestos;
        this.numBicicletas = numBicicletas;
        this.huecosLibres = huecosLibres;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getNumPuestos() {
        return numPuestos;
    }

    public void setNumPuestos(int numPuestos) {
        this.numPuestos = numPuestos;
    }

    public int getNumBicicletas() {
        return numBicicletas;
    }

    public void setNumBicicletas(int numBicicletas) {
        this.numBicicletas = numBicicletas;
    }

    public boolean isHuecosLibres() {
        return huecosLibres;
    }

    public void setHuecosLibres(boolean huecosLibres) {
        this.huecosLibres = huecosLibres;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
    
    public static EstacionResumen fromEntity(Estacion estacion) {
        int numBicicletas = estacion.getBicicletas().size();
        boolean huecosLibres = estacion.estacionamientoDisponible();
        return new EstacionResumen(
                estacion.getId(),
                estacion.getNombre(),
                estacion.getPuestos(),
                numBicicletas,
                huecosLibres,
                estacion.getDireccion(),
                estacion.getLatitud(),
                estacion.getLongitud()
        );
    }
}