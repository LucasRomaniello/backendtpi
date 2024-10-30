package people.demo.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import people.demo.domain.Prueba;

public interface PruebaReposotory extends JpaRepository<Prueba, Integer> {
}
