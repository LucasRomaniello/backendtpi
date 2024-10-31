package people.demo.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@ToString

@Entity @Table(name = "Empleados")
public class Empleado {
    @Id //@Column(name = "LEGAJO")
    private Integer legajo;
    @Basic //@Column(name = "NOMBRE")
    private String nombre;
    @Basic //@Column(name = "APELLIDO")
    private String apellido;
    @Basic  @Column(name = "TELEFONO_CONTACTO")
    private int telefonoContacto;

    @OneToMany(mappedBy = "empleado")
    private Set<Prueba> pruebas = new HashSet<>();


    //ADD PRUEBA---------------------------------//////

    public Empleado() {

    }
}
