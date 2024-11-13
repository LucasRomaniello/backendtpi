package tp.vehiculos.Reportes.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneId;



import java.util.Date;

@Data
public class PruebaDTO {
    private Integer id;

    private Integer id_interesado;

    private Integer legajo_empleado;

    private Integer idVehiculo;

    private Date fechaHoraInicio;
    private Date fechaHoraFin;
    
    private String comentarios;


    public PruebaDTO(String comentarios, Date fechaHoraFin, Date fechaHoraInicio, Integer idVehiculo, Integer legajo_empleado, Integer id_interesado, Integer id) {
        this.comentarios = comentarios;
        this.fechaHoraFin = fechaHoraFin;
        this.fechaHoraInicio = fechaHoraInicio;
        this.idVehiculo = idVehiculo;
        this.legajo_empleado = legajo_empleado;
        this.id_interesado = id_interesado;
        this.id = id;
    }


    public Integer getId() {
        return id;
    }

    public LocalDateTime getFechaInicio() {

        return fechaHoraInicio.toInstant().atZone(ZoneId.systemDefault()) .toLocalDateTime(); 
    }

    public LocalDateTime getFechaFin() {
        return fechaHoraFin.toInstant().atZone(ZoneId.systemDefault()) .toLocalDateTime(); 
    }

    public int getIdVehiculo() {
        return idVehiculo;
    }

    

}