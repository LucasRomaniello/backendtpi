package tp.vehiculos.Reportes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("id_vehiculo")
    private Integer idVehiculo;

    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    
    private String comentarios;


    public PruebaDTO(String comentarios, LocalDateTime fechaHoraFin, LocalDateTime fechaHoraInicio, Integer idVehiculo, Integer legajo_empleado, Integer id_interesado, Integer id) {
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

        return fechaHoraInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaHoraFin;
    }

    public Integer getIdvehiculo() {
        return idVehiculo;
    }

    public Integer getIdVehiculo() {
        return idVehiculo;
    }

    

}