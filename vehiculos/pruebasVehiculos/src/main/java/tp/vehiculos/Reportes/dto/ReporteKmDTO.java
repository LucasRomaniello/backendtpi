package tp.vehiculos.Reportes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReporteKmDTO {
    public LocalDateTime fechaDesde;
    public LocalDateTime fechaHasta;
    public Integer idVehiculo;
    public String cantidadKms;
}
