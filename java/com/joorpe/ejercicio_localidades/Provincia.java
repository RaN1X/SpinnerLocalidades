package com.joorpe.ejercicio_localidades;

public class Provincia extends Ubicacion {
    int comunidad;
    public Provincia(int id, String descripcion) {
        super(id, descripcion);
    }

    public Provincia(int id, String descripcion, int comunidad) {
        super(id, descripcion);
        this.comunidad = comunidad;
    }

    public int getComunidad() {
        return comunidad;
    }

    public void setComunidad(int comunidad) {
        this.comunidad = comunidad;
    }
}