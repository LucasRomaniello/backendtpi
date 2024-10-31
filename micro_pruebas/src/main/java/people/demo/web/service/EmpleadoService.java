package people.demo.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import people.demo.dal.EmpleadoRepository;
import people.demo.dal.PruebaReposotory;
import people.demo.domain.Empleado;
import people.demo.domain.Empleado;
import people.demo.domain.Prueba;
import people.demo.web.api.dto.EmpleadoDTO;
import people.demo.web.api.dto.EmpleadoDTO;
import people.demo.web.api.dto.EmpleadoDTO;
import people.demo.web.api.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

    @Service
    public class EmpleadoService {
        private EmpleadoRepository empleadoRepository;
        @Autowired
        public EmpleadoService(EmpleadoRepository empleadoRepository){
            this.empleadoRepository = empleadoRepository;
        };

        public List<EmpleadoDTO> findAll(){
            return empleadoRepository.findAll().stream().map(empleado -> new EmpleadoDTO(empleado)).toList();
        };

        public Optional<EmpleadoDTO> findById(Integer pid) {
            Optional<Empleado> empleado = empleadoRepository.findById(pid);

            return empleado.isEmpty()
                    ? Optional.empty()
                    : Optional.of(new EmpleadoDTO(empleado.get()));
        }

        public EmpleadoDTO add(EmpleadoDTO empleadoDTO) {
            Empleado interesado = empleadoRepository.save(empleadoDTO.toEntity(empleadoDTO));
            return new EmpleadoDTO(Empleado);
        }

        public List<PersonaDTO> addAll(List<PersonaDTO> personaDTOS) {
            List<PersonaEntity> personas = rep.saveAll(personaDTOS.stream().map(PersonaDTO::toPersonaEntity).toList());
            return personas.stream().map(persona -> new PersonaDTO(persona)).toList();
        }

        public PersonaDTO update(Integer pid, PersonaDTO personaDTO) {
            PersonaEntity persona = rep.findById(pid).orElseThrow(
                    () -> new ResourceNotFoundException(String.format("Persona [%d] inexistente", pid))
            );

            persona = rep.save(persona.update(personaDTO.toPersonaEntity()));

            return new PersonaDTO(persona);
        }

        public boolean deleteById(Integer pid) {
            if (!rep.existsById(pid)) {
                return false;
            }

            rep.deleteById(pid);
            return true;
        }
    }


}

}
