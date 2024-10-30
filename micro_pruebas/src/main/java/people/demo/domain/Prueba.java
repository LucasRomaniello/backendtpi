package people.demo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Entity
@Table(name = "prueba")
public class Prueba {
    @Id
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_INTERESADO")
    Interesado interesado;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_EMPLEADO")
    Empleado empleado;

    @Basic @Column(name = "FECHA_HORA_INICIO")
    private Date fechaHoraInicio;

    @Basic @Column(name = "FECHA_HORA_FIN")
    private Date fechaHoraFin;

    @Basic
    private String comentarios;


    public Prueba() {}
}
