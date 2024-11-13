package people.demo.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import people.demo.domain.Promocion;

public interface PromocionRepository extends JpaRepository<Promocion, Integer> {

}
