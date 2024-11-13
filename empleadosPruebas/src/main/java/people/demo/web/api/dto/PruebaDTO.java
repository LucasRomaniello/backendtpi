package people.demo.web.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import people.demo.domain.Empleado;
import people.demo.domain.Interesado;
import people.demo.domain.Prueba;

import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
public class PruebaDTO {
    //@NotBlank(message = "Debe ingresar el ID obligatoriamente")
    private Integer id;

    private Integer id_interesado;

    private Integer  id_vehiculo;

    private Integer legajo_empleado;

    private LocalDateTime fechaHoraInicio;

    private LocalDateTime fechaHoraFin;

    private String comentarios;

    public PruebaDTO() {}

//    public PruebaDTO(Prueba prueba){
//        this.id = prueba.getId();
//        this.id_interesado = prueba.getInteresado().getId();
//        this.id_vehiculo = prueba.getId_vehiculo();
//        this.legajo_empleado = prueba.getEmpleado().getLegajo();
//        this.fechaHoraInicio = LocalDateTime.now();
//        this.comentarios = prueba.getComentarios();
//    }

    public PruebaDTO(String comentarios,Integer idVehiculo, Integer legajo_empleado, Integer id_interesado, Integer id) {
        this.comentarios = comentarios;
        this.fechaHoraInicio = LocalDateTime.now();
        this.id_vehiculo = idVehiculo;
        this.legajo_empleado = legajo_empleado;
        this.id_interesado = id_interesado;
        this.id = id;
    }


    public static PruebaDTO toDTO(Prueba prueba) {
        if (prueba == null) return null;

        return new PruebaDTO(prueba);
    }

    public static Prueba toEntity(PruebaDTO pruebaDTO, Interesado interesado, Empleado empleado) {
        if (pruebaDTO == null) return null;
        return new Prueba(pruebaDTO.getId(), interesado, empleado, pruebaDTO.getFechaHoraInicio(),null, pruebaDTO.getComentarios(), pruebaDTO.getId_vehiculo());
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
