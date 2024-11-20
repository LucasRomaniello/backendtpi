package tp.vehiculos.Reportes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class ReporteIncidentesDTO {
    private String tipoIncidente;
    private String patente;
    private String fecha;
    private Double latitud;
    private Double longitud;
}
