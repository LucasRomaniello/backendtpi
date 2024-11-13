package tp.vehiculos.Reportes.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class InteresadoDTO {
    //@NotBlank(message = "Debe ingresar el ID obligatoriamente")
    private Integer id;

    private String tipo_documento;

    private String documento;

    private String nombre;

    private String apellido;

    private Boolean restringido;

    private Integer nro_licencia;

    private Date fecha_vencimiento_licencia;
    private String email;}