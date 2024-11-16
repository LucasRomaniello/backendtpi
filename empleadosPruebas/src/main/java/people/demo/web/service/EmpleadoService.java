package people.demo.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import people.demo.dal.EmpleadoRepository;
import people.demo.domain.Empleado;
import people.demo.web.api.dto.EmpleadoDTO;
import people.demo.web.controller.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoService {
    private EmpleadoRepository empleadoRepository;

    @Autowired
    public EmpleadoService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    public List<EmpleadoDTO> findAll() {
        return empleadoRepository.findAll().stream().map(EmpleadoDTO::new).toList();
    }

    public Optional<EmpleadoDTO> findById(Integer pid) {
        Optional<Empleado> empleado = empleadoRepository.findById(pid);

        return empleado.isEmpty()
                ? Optional.empty()
                : Optional.of(new EmpleadoDTO(empleado.get()));
    }

    public EmpleadoDTO add(EmpleadoDTO empleadoDTO) {
        Empleado empleado = empleadoRepository.save(empleadoDTO.toEntity(empleadoDTO));
        return new EmpleadoDTO(empleado);
    }

    public List<EmpleadoDTO> addAll(List<EmpleadoDTO> empleadosDTO) {
        List<Empleado> empleados = empleadoRepository.saveAll(empleadosDTO.stream().map(empleadoDTO -> empleadoDTO.toEntity(empleadoDTO)).toList());
        return empleados.stream().map(EmpleadoDTO::new).toList();
    }

    public EmpleadoDTO update(Integer legajo, EmpleadoDTO empleadoDTO) {
        Empleado empleado = empleadoRepository.findById(legajo).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Empleado [%d] inexistente", legajo))
        );

        empleado = empleadoRepository.save(empleado.update(empleadoDTO.toEntity(empleadoDTO)));

        return new EmpleadoDTO(empleado);
    }

    public boolean deleteById(Integer empid) {
        if (!empleadoRepository.existsById(empid)) {
            return false;
        }

        empleadoRepository.deleteById(empid);
        return true;
    }
}



