package people.demo.web.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import people.demo.domain.Empleado;
import people.demo.domain.Interesado;
import people.demo.domain.Prueba;

import java.util.Date;

@Setter
@Getter
public class PruebaDTO {
    @NotBlank(message = "Debe ingresar el ID obligatoriamente")
    private Integer id;

    private Integer id_interesado;

    private Integer legajo_empleado;

    private Date fechaHoraInicio;

    private Date fechaHoraFin;

    private String comentarios;

    public PruebaDTO(Prueba prueba){
        this.id = prueba.getId();
        this.id_interesado = prueba.getInteresado().getId();
        this.legajo_empleado = prueba.getEmpleado().getLegajo();
        this.fechaHoraInicio = prueba.getFechaHoraInicio();
        this.fechaHoraFin = prueba.getFechaHoraFin();
        this.comentarios = prueba.getComentarios();
    }

    public static PruebaDTO toDTO(Prueba prueba) {
        if (prueba == null) return null;

        return new PruebaDTO(prueba);
    }

    public static Prueba toEntity(PruebaDTO pruebaDTO, Interesado interesado, Empleado empleado) {
        if (pruebaDTO == null) return null;

        Prueba prueba = new Prueba();
        prueba.setId(pruebaDTO.getId());
        prueba.setInteresado(interesado);
        prueba.setEmpleado(empleado);
        prueba.setFechaHoraInicio(pruebaDTO.getFechaHoraInicio());
        prueba.setFechaHoraFin(pruebaDTO.getFechaHoraFin());
        prueba.setComentarios(pruebaDTO.getComentarios());

        return prueba;
    }


    //public static PruebaDTO toDTO(Prueba prueba) {
       // if (prueba == null) return null;

        //return new PruebaDTO(
          //      prueba.getId(),
            //    prueba.getInteresado()!= null ? prueba.getInteresado().getId() : null,
              //  prueba.getEmpleado() != null ? prueba.getEmpleado().getLegajo() : null,
                //prueba.getFechaHoraInicio(),
               // prueba.getFechaHoraFin(),
               // prueba.getComentarios()
        //);
    //}

    //DTO empleado id, interesado id.
    //post
    // crearDto
    // servicio buscar(DTO)
    //crear
    //buscar
    //Setear prueba
}
