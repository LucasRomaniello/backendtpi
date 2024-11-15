package ar.edu.utn.frc.bso.security.domain;

import java.util.Objects;

public class Empleado {
    private String nombre;
    private Integer legajo;

    public Empleado(){}

    public Empleado(String nombre, Integer legajo) {
        this.nombre = nombre;
        this.legajo = legajo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Empleado empleado = (Empleado) o;
        return Objects.equals(nombre, empleado.nombre) && Objects.equals(legajo, empleado.legajo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, legajo);
    }

    @Override
    public String toString() {
        return "Empleado{" +
                "nombre='" + nombre + '\'' +
                ", legajo=" + legajo +
                '}';
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getLegajo() {
        return legajo;
    }

    public void setLegajo(Integer legajo) {
        this.legajo = legajo;
    }
}
