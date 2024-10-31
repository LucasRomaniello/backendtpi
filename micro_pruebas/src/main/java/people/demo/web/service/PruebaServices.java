package people.demo.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import people.demo.dal.EmpleadoRepository;
import people.demo.dal.InteresadoRepository;
import people.demo.dal.PruebaReposotory;
import people.demo.domain.Empleado;
import people.demo.domain.Interesado;
import people.demo.domain.Prueba;
import people.demo.web.api.dto.PruebaDTO;
import people.demo.web.api.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class PruebaServices {
    private PruebaReposotory pruebaReposotory;
    private InteresadoRepository interesadoRepository;
    private EmpleadoRepository empleadoRepository;
    @Autowired
    public PruebaServices(PruebaReposotory pruebaReposotoryosotory, InteresadoRepository interesadoRepository, EmpleadoRepository empleadoRepository){
        this.pruebaReposotory = pruebaReposotoryosotory;
        this.interesadoRepository = interesadoRepository;
        this.empleadoRepository = empleadoRepository;
    };

    public List<PruebaDTO> findAll(){
        return pruebaReposotory.findAll().stream().map(PruebaDTO::new).toList();
    };

    public Optional<PruebaDTO> findById(Integer pid) {
        Optional<Prueba> prueba = pruebaReposotory.findById(pid);

        return prueba.isEmpty()
                ? Optional.empty()
                : Optional.of(new PruebaDTO(prueba.get()));
    }

    public PruebaDTO add(PruebaDTO pruebaDTO) throws NullPointerException {

        Optional<Interesado> interesadoOpt = interesadoRepository.findById(pruebaDTO.getId_interesado());
        Optional<Empleado> empleadoOpt = empleadoRepository.findById(pruebaDTO.getLegajo_empleado());
        //interesado

        Interesado interesado = interesadoOpt.get(); // Lanza excepción si está vacío
        Empleado empleado = empleadoOpt.get();
        Prueba prueba = pruebaReposotory.save(pruebaDTO.toEntity(pruebaDTO, interesado, empleado)); //Interesado to entity viola la ley de demeter
        return new PruebaDTO(prueba);
    }

    //public List<PruebaDTO> addAll(List<PruebaDTO> pruebaDTOS) {
      //  List<Prueba> pruebas = pruebaReposotory.saveAll(pruebaDTOS.stream().map((PruebaDTO pruebaDTO) -> PruebaDTO.toEntity(pruebaDTO)).toList());
       // return pruebas.stream().map(prueba -> new PruebaDTO(prueba)).toList();
    //}

    public PruebaDTO update (Integer pid, PruebaDTO pruebaDTO) throws NullPointerException{

        //REVISAR----------------------------------------------------
        Optional<Interesado> interesadoOpt = interesadoRepository.findById(pruebaDTO.getId_interesado());
        Optional<Empleado> empleadoOpt = empleadoRepository.findById(pruebaDTO.getLegajo_empleado());
        //interesado

        Interesado interesado = interesadoOpt.get(); // Lanza excepción si está vacío
        Empleado empleado = empleadoOpt.get();
        //-------------------------------------------------------------

        Prueba prueba = pruebaReposotory.findById(pid).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Persona [%d] inexistente", pid))
        );

        prueba = pruebaReposotory.save(prueba.update(pruebaDTO.toEntity(pruebaDTO, interesado, empleado)));

        return new PruebaDTO(prueba);
    }

    public boolean deleteById(Integer pid) {
        if (!pruebaReposotory.existsById(pid)) {
            return false;
        }

        pruebaReposotory.deleteById(pid);
        return true;
    }
}



