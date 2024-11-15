package people.demo.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import people.demo.domain.Empleado;
import people.demo.domain.Prueba;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import   java.util.List;
import java.util.Optional;

public interface PruebaRepository extends JpaRepository<Prueba, Integer> {
    //Optional<Prueba> findByid_VehiculoAndFechaHoraFinIsNull(Integer id_vehiculo);
    @Query("SELECT p FROM Prueba p WHERE p.fechaHoraFin IS NULL AND p.id_vehiculo = :id_vehiculo")
    Optional<Prueba> findPruebaActual(@Param("id_vehiculo") Integer id_vehiculo);

    @Query("SELECT p FROM Prueba p WHERE p.fechaHoraFin IS NULL AND p.empleado.legajo = :legajo_empleado")
    Optional<Prueba> findPruebaActualEmpleado(@Param("legajo_empleado") Integer id_empleado);

    @Query("SELECT p FROM Prueba p WHERE p.fechaHoraFin IS NULL")
    List<Prueba> findPruebaEnCurso();

    //Query ya definida por el repository
    Optional<Prueba> findById(Integer id);


    //Query para obtener las pruebas ya finalizadas
    @Query("SELECT p FROM Prueba p WHERE p.fechaHoraFin IS NOT NULL")
    List<Prueba> findPruebaFinaliza();


//
//    @Query("SELECT p FROM Prueba p WHERE p.id_vehiculo = :idVehiculo AND p.fechaHoraFin IS NULL")
//    Prueba findPruebaActual(@Param("id_vehiculo") int idVehiculo);


}
