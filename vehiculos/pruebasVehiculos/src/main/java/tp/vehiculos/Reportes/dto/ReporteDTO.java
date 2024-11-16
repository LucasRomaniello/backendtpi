package tp.vehiculos.Reportes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor @NoArgsConstructor @Data @Builder
public class ReporteDTO {
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private String apellidoInteresado;
    private String nombreInteresado;
    private String documentoInteresado;
    private Integer nro_licenciaInteresado;
    private String apellidoEmpleado;
    private String nombreEmpleado;
    private Integer telefonoContacto;
}
