package people.demo.web.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import people.demo.domain.Empleado;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpleadoDTO {
    @NotBlank(message = "Debe ingresar el Legajo obligatoriamente")
    private Integer legajo;

    private String nombre;

    private String apellido;

    private int telefonoContacto;

    public EmpleadoDTO(Empleado entity) {
        this.legajo = entity.getLegajo();
        this.nombre = entity.getNombre();
        this.apellido = entity.getApellido();
        this.telefonoContacto = entity.getTelefonoContacto();
    }

    public EmpleadoDTO toDTO(Empleado entity) {
        if (entity == null) return null;

        return new EmpleadoDTO(entity);
    }

    public Empleado toEntity(EmpleadoDTO empleadoDTO) {
        if (empleadoDTO == null) return null;

        Empleado empleado = new Empleado();
        empleado.setLegajo(empleadoDTO.getLegajo());
        empleado.setNombre(empleadoDTO.getNombre());
        empleado.setNombre(empleadoDTO.getNombre());
        empleado.setTelefonoContacto(empleadoDTO.getTelefonoContacto());
        return empleado;
    }

}
