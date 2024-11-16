package tp.vehiculos.Reportes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder @AllArgsConstructor

public class ErrorDTO {
    private String message;
}
