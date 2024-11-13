package people.demo.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import people.demo.domain.Prueba;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PruebaReposotory extends JpaRepository<Prueba, Integer> {
    //Optional<Prueba> findByid_VehiculoAndFechaHoraFinIsNull(Integer id_vehiculo);
    @Query("SELECT p FROM Prueba p WHERE p.fechaHoraFin IS NULL AND p.id_vehiculo = :id_vehiculo")
    Optional<Prueba> findAllWithFechaHoraFinIsNull(@Param("id_vehiculo") Integer id_vehiculo);


    @Query("SELECT p FROM Prueba p WHERE p.fechaHoraFin IS NULL")
    List<Prueba> findAllWithFechaHoraFinIsNull();




}
