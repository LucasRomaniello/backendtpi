package people.demo.web.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import people.demo.domain.Interesado;

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
        tipo_documento = entity.getTipo_documento();
        documento = entity.getDocumento();
        nombre = entity.getNombre();
        apellido = entity.getApellido();
        restringido = entity.getRestringido();
        nro_licencia =entity.getNro_licencia();
        fecha_vencimiento_licencia = entity.getFecha_vencimiento_licencia();
    }
    public Interesado toEntity(InteresadoDTO interesadoDTO) {
        if (interesadoDTO == null) return null;

        Interesado interesado = new Interesado();
        interesado.setId(interesadoDTO.getId());
        interesado.setDocumento(interesadoDTO.getDocumento());
        interesado.setNombre(interesadoDTO.getNombre());
        interesado.setNro_licencia(interesadoDTO.getNro_licencia());
        interesado.setRestringido(interesadoDTO.getRestringido());
        interesado.setApellido(interesadoDTO.getApellido());
        interesado.setFecha_vencimiento_licencia(interesadoDTO.getFecha_vencimiento_licencia());
        return interesado;
    }
}
