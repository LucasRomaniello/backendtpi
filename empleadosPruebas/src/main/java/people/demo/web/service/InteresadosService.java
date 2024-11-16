package people.demo.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import people.demo.dal.InteresadoRepository;
import people.demo.domain.Interesado;
import people.demo.web.api.dto.InteresadoDTO;
import people.demo.web.controller.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class InteresadosService{
    private InteresadoRepository interesadoRepository;

    @Autowired
    public InteresadosService(InteresadoRepository rep){this.interesadoRepository = rep;}

    public List<InteresadoDTO> findAll(){
        return interesadoRepository.findAll().stream().map(InteresadoDTO::new).toList();
    }

    public Optional<InteresadoDTO> findById(Integer iid) {
        Optional<Interesado> interesado = interesadoRepository.findById(iid);

        return interesado.isEmpty()
                ? Optional.empty()
                : Optional.of(new InteresadoDTO(interesado.get()));
    }

    public InteresadoDTO add(InteresadoDTO interesadoDTO) {
        Interesado interesado = interesadoRepository.save(interesadoDTO.toEntity(interesadoDTO));
        return new InteresadoDTO(interesado);
    }

    public List<InteresadoDTO> addAll(List<InteresadoDTO> interesadoDTOS) {
        List<Interesado> interesados = interesadoRepository.saveAll(interesadoDTOS.stream().map(indto-> indto.toEntity(indto)).toList());
        return interesados.stream().map(InteresadoDTO::new).toList();
    }

    public InteresadoDTO update(Integer iid, InteresadoDTO interesadoDTO) {
        Interesado interesado = interesadoRepository.findById(iid).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Interesado [%d] inexistente", iid))
        );

        interesado = interesadoRepository.save(interesado.update(interesadoDTO.toEntity(interesadoDTO)));

        return new InteresadoDTO(interesado);
    }

    public boolean deleteById(Integer iid) {
        if (!interesadoRepository.existsById(iid)) {
            return false;
        }

        interesadoRepository.deleteById(iid);
        return true;
    }
}
