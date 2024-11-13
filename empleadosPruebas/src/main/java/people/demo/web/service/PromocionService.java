package people.demo.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import people.demo.dal.EmpleadoRepository;
import people.demo.dal.InteresadoRepository;
import people.demo.dal.PromocionRepository;
import people.demo.domain.Empleado;
import people.demo.domain.Interesado;
import people.demo.domain.Promocion;
import people.demo.web.api.dto.PromocionDTO;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PromocionService {
    private final PromocionRepository promocionRepository;
    private EmpleadoRepository empleadoRepository;
    @Autowired
    public PromocionService(PromocionRepository promocionRepository, EmpleadoRepository empleadoRepository){
        this.promocionRepository  = promocionRepository;
        this.empleadoRepository = empleadoRepository;
    }

    public PromocionDTO add(PromocionDTO promocionDTO){
        Set<Integer> telefonoEmpleados = empleadoRepository.findAll()
                .stream()
                .map(Empleado::getTelefonoContacto) // Transformar cada Interesado a su nombre
                .collect(Collectors.toSet());

        Promocion promocion = promocionDTO.toEntity(promocionDTO);
        promocion.setTelefonos(telefonoEmpleados);
        promocionRepository.save(promocion);

        return new PromocionDTO(promocion);
    }

}
