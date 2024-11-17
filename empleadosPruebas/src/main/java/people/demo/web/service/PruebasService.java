package people.demo.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import people.demo.dal.EmpleadoRepository;
import people.demo.dal.InteresadoRepository;
import people.demo.dal.PruebaRepository;
import people.demo.domain.Empleado;
import people.demo.domain.Interesado;
import people.demo.domain.Prueba;
import people.demo.web.api.dto.InteresadoDTO;
import people.demo.web.api.dto.PruebaDTO;
import people.demo.web.controller.exception.AssignedTestException;
import people.demo.web.controller.exception.ResourceNotFoundException;
import people.demo.web.controller.exception.UnavailableForDrivingException;

import java.lang.module.ResolutionException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PruebasService {
    private final PruebaRepository pruebaRepository;
    private final InteresadoRepository interesadoRepository;
    private final EmpleadoRepository empleadoRepository;

    @Autowired
    public PruebasService(PruebaRepository pruebaReposotoryosotory, InteresadoRepository interesadoRepository, EmpleadoRepository empleadoRepository) {
        this.pruebaRepository = pruebaReposotoryosotory;
        this.interesadoRepository = interesadoRepository;
        this.empleadoRepository = empleadoRepository;
    }

    public List<PruebaDTO> findAll() {
        return pruebaRepository.findAll().stream().map(PruebaDTO::new).toList();
    }

    //Obtener todas las pruebas finalizadas
    public List<PruebaDTO> findAllFinalizadas() {
        return pruebaRepository.findPruebaFinaliza().stream().map(PruebaDTO::new).toList();

    }

    //Obtener las pruebas finalizadas para un empleado
    public List<PruebaDTO> findAllPruebasForEmployee(Integer id){
        return pruebaRepository.findPruebaFinalizaPorEmpleado(id).stream().map(PruebaDTO::new).toList();
    }



    public Optional<PruebaDTO> findById(Integer pid) {
        Optional<Prueba> prueba = pruebaRepository.findById(pid);

        return prueba.isEmpty()
                ? Optional.empty()
                : Optional.of(new PruebaDTO(prueba.get()));
    }

    public Interesado buscarSiExisteInteresado(Integer id) {
        return interesadoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Interesado no encontrado"));
    }

    public Empleado buscarSiExisteEmpleado(Integer id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado"));
    }

    public PruebaDTO add(PruebaDTO pruebaDTO) {

        Empleado empleado = buscarSiExisteEmpleado(pruebaDTO.getLegajo_empleado());
        Interesado interesado = buscarSiExisteInteresado(pruebaDTO.getId_interesado());

        if (pruebaRepository.findPruebaActual(pruebaDTO.getId_vehiculo()).isPresent() ||
                pruebaRepository.findPruebaActualEmpleado(empleado.getLegajo()).isPresent()) {
            throw new AssignedTestException("Ya hay una prueba asignada para ese empleado o vehículo");
        }

        if (!interesado.verificarLicencia() || interesado.getRestringido()) {
            throw new UnavailableForDrivingException("El interesado no está disponible para conducir");
        }

        pruebaRepository.save(pruebaDTO.toEntity(pruebaDTO, interesado, empleado));
        return pruebaDTO;
    }


    //public Interesado buscarInteresado(int id) {
    //  Optional<Interesado> Optinteresado = interesadoRepository.findById(id);
    //return Optinteresado.orElse(null);
    //}

    //  public Empleado buscarEmpleado(int id) {
    //    Optional<Empleado> optEmpleado = empleadoRepository.findById(id);
    //  return optEmpleado.orElse(null);
    //}

    //public List<PruebaDTO> addAll(List<PruebaDTO> pruebaDTOS) {

    //  List<Prueba> pruebas = pruebaRepository.saveAll(
    //       pruebaDTOS.stream()
        //.map(pruebaDTO -> pruebaDTO.toEntity(pruebaDTO, buscarSiExisteInteresado(pruebaDTO.getId_interesado()),
          //                      buscarSiExisteEmpleado(pruebaDTO.getLegajo_empleado())))
            //            .toList() // Convert the Stream to a List
        //);

        //return pruebas.stream().map(PruebaDTO::new).toList();
    //}


    public List<PruebaDTO> findAllWithFechaFinIsNull() {
        List<Prueba> listPrueba = pruebaRepository.findPruebaEnCurso();
        return listPrueba.stream().map(PruebaDTO::new).toList();

    }

    public Optional<PruebaDTO> findPruebaByIdVehiculo(Integer idVehiculo) {
        Optional<Prueba> optPrueba = pruebaRepository.findPruebaActual(idVehiculo);

        return optPrueba.stream().map(PruebaDTO::new).toList().stream().findFirst();
    }


    //Finalizar una prueba ya comenzada
    public PruebaDTO update(PruebaDTO pruebaDTO, Integer id) {

        //REVISAR----------------------------------------------------
        //Interesado interesado = buscarSiExisteInteresado(pruebaDTO.getId_interesado());
        //Empleado empleado = buscarSiExisteEmpleado(pruebaDTO.getLegajo_empleado());
        Optional<Prueba> pruebaOptional = pruebaRepository.findById(id);
        if (pruebaOptional.isPresent()) {
            Prueba prueba = pruebaOptional.get(); //Revisar
            prueba.setFechaHoraFin(LocalDateTime.now());
            prueba.setComentarios(pruebaDTO.getComentarios());
            pruebaRepository.save(prueba);
            return new PruebaDTO(prueba);
        }
        throw new ResourceNotFoundException("No se ha encontrado una prueba con ese id");
        //interesado


        //-------------------------------------------------------------
    }

    public boolean deleteById(Integer pid) {
        if (!pruebaRepository.existsById(pid)) {
            return false;
        }

        pruebaRepository.deleteById(pid);
        return true;
    }

    public PruebaDTO terminarPrueba(PruebaDTO pruebaDTO, Integer id){
        Optional<Prueba> pruebaOptional = pruebaRepository.findPruebaParaTerminar(id);
        if (pruebaOptional.isPresent()) {
            Prueba prueba = pruebaOptional.get(); //Revisar
            prueba.setFechaHoraFin(LocalDateTime.now());
            prueba.setComentarios(pruebaDTO.getComentarios());
            pruebaRepository.save(prueba);
            return new PruebaDTO(prueba);
        }
        throw new ResourceNotFoundException("No se ha encontrado una prueba cancelable con ese id");
    }

}



