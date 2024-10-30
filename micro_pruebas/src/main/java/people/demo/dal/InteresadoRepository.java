package people.demo.dal;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import people.demo.domain.Interesado;

public interface InteresadoRepository extends JpaRepository<Interesado, Integer> {

}
