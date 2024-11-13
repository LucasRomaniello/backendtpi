package people.demo.web.api;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import people.demo.web.api.dto.PromocionDTO;
import people.demo.web.service.PromocionService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/notificarPromocion")
public class PromocionAPI {
    private PromocionService promocionService;
    @Autowired
    public PromocionAPI(PromocionService promocionService) {
        this.promocionService = promocionService;
    }

    @PostMapping
    public ResponseEntity<PromocionDTO> addNotificacion(@RequestBody PromocionDTO promocionDTO) {
            LocalDateTime fechaActual = LocalDateTime.now();
            LocalDateTime fechaEnSeisMeses = fechaActual.plusMonths(promocionDTO.getCantidad_meses());
        promocionDTO.setFechaFin(fechaEnSeisMeses);
        return new ResponseEntity<>(promocionService.add(promocionDTO), HttpStatus.CREATED);
    }

}
