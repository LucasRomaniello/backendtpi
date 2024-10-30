package people.demo.web.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpleadoDTO {
    @NotBlank(message = "Debe ingresar el Legajo obligatoriamente")
    private Integer legajo;

    private String nombre;

    private String apellido;

    private int telefonoContacto;

}
