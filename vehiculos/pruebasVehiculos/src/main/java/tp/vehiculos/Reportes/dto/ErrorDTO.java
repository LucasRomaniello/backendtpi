package tp.vehiculos.Reportes.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class ErrorDTO {
    private String message;
}
