package people.demo.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import people.demo.domain.Notificacion;

public interface NotificacionRepository extends JpaRepository<Notificacion, Integer>{

}
