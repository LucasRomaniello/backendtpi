package people.demo.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import people.demo.domain.Empleado;

public interface EmpleadoRepository extends JpaRepository<Empleado,Integer> {
}
