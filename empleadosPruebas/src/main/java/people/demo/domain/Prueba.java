package people.demo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter

@ToString
@Entity
@Table(name = "pruebas")
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
    private LocalDateTime fechaHoraInicio;

    @Basic @Column(name = "FECHA_HORA_FIN")
    private LocalDateTime fechaHoraFin;

    @Basic
    private String comentarios;

    @Basic
    private Integer id_vehiculo;

    public Prueba() {}

    public Prueba(Integer id, Interesado interesado, Empleado empleado, LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin, String comentarios, Integer id_vehiculo) {
        this.id = id;
        this.interesado = interesado;
        this.empleado = empleado;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
        this.comentarios = comentarios;
        this.id_vehiculo = id_vehiculo;
    }

    public Prueba update(Prueba prueba) {
        id = prueba.id;
        id_vehiculo = prueba.id_vehiculo;
        interesado = prueba.interesado;
        empleado = prueba.empleado;
        fechaHoraInicio = prueba.fechaHoraInicio;
        fechaHoraFin = prueba.fechaHoraFin;
        comentarios= prueba.comentarios;
        return this;
    }
}
