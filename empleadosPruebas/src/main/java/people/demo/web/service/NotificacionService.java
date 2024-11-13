package people.demo.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import people.demo.dal.NotificacionRepository;
import people.demo.dal.PruebaRepository;
import people.demo.domain.Notificacion;
import people.demo.domain.Prueba;
import people.demo.web.api.dto.NotificacionDTO;

import java.util.Optional;

@Service
public class NotificacionService {
    private NotificacionRepository notificacionRepository;
    private PruebaRepository pruebaRepository;
    @Autowired
    public NotificacionService(NotificacionRepository notificacionRepository, PruebaRepository pruebaRepository){
        this.notificacionRepository = notificacionRepository;
        this.pruebaRepository = pruebaRepository;
    };

    public NotificacionDTO add(NotificacionDTO notificacionDTO) {
     Optional<Prueba> pruebaActual = pruebaRepository.findPruebaActual(notificacionDTO.getId_vehiculo());
     if (pruebaActual.isPresent()) {
         notificacionDTO.setNroTelefono(pruebaActual.get().getEmpleado().getTelefonoContacto());
         Notificacion notificacion = notificacionRepository.save(notificacionDTO.toEntity(notificacionDTO));
         return new NotificacionDTO(notificacion);
     }
     return new NotificacionDTO();

    }

}
