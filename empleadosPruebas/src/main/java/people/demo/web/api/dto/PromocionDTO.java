package people.demo.web.api.dto;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import people.demo.domain.Promocion;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PromocionDTO {
   // @NotBlank(message = "La promocion debe tener un id no nulo")

    private int PromocionId;
    private String descripcion;
    private double porc;
    private LocalDateTime fechaFin;

    private int cantidad_meses;

    public PromocionDTO(Promocion entity){
        PromocionId = entity.getPromocion_id();
        descripcion = entity.getDescripcion();
        porc = entity.getPorc();
        fechaFin = entity.getFecha_hasta();
        cantidad_meses = entity.getCantidad_meses();
    }
    public Promocion toEntity(PromocionDTO promocionDTO) {
        if (promocionDTO == null) return null;
        Promocion promocion = new Promocion();
        promocion.setPromocion_id(promocionDTO.PromocionId);
        promocion.setPorc(promocionDTO.porc);
        promocion.setDescripcion(promocionDTO.descripcion);
        promocion.setFecha_hasta(promocionDTO.fechaFin);
        promocion.setCantidad_meses(promocionDTO.cantidad_meses);

        return promocion;
    }

}
