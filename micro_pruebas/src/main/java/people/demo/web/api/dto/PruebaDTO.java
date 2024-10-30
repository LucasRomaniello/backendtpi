package people.demo.web.api.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public class PruebaDTO {
    @NotBlank(message = "Debe ingresar el ID obligatoriamente")
    private Integer id;



    private Date fechaHoraInicio;

    private Date fechaHoraFin;

    private String comentarios;

   //DTO empleado id, interesado id.
    //post
    // crearDto
    // servicio buscar(DTO)
    //crear
    //buscar
    //Setear prueba
}
