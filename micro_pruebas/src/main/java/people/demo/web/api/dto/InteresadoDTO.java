package people.demo.web.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import people.demo.domain.Empleado;
import people.demo.domain.Interesado;
import people.demo.domain.Prueba;

import java.util.Date;


@Data
@NoArgsConstructor
public class InteresadoDTO {
    @NotBlank(message = "Debe ingresar el ID obligatoriamente")
    private Integer id;

    private String tipo_documento;

    private String documento;

    private String nombre;

    private String apellido;

    private Boolean restringido;

    private Integer nro_licencia;

    private Date fecha_vencimiento_licencia;

    public InteresadoDTO(Interesado entity){
        id = entity.getId();
    }

    public Interesado toEntity(){ return new Interesado(id, tipo_documento, documento, nombre, apellido, restringido, nro_licencia, fecha_vencimiento_licencia);}
}
