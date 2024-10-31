package people.demo.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import people.demo.dal.InteresadoRepository;
import people.demo.domain.Interesado;
import people.demo.web.api.dto.InteresadoDTO;
import people.demo.web.api.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class InteresadosService{
    private InteresadoRepository rep;

    @Autowired
    public InteresadosService(InteresadoRepository rep){this.rep = rep;}

    public List<InteresadoDTO> findAll(){
        return rep.findAll().stream().map(InteresadoDTO::new).toList();
    }

    public Optional<InteresadoDTO> findById(Integer iid) {
        Optional<Interesado> interesado = rep.findById(iid);

        return interesado.isEmpty()
                ? Optional.empty()
                : Optional.of(new InteresadoDTO(interesado.get()));
    }

    public InteresadoDTO add(InteresadoDTO interesadoDTO) {
        Interesado interesado = rep.save(interesadoDTO.toEntity(interesadoDTO));
        return new InteresadoDTO(interesado);
    }

    public List<InteresadoDTO> addAll(List<InteresadoDTO> interesadoDTOS) {
        List<Interesado> interesados = rep.saveAll(interesadoDTOS.stream().map(indto-> indto.toEntity(indto)).toList());
        return interesados.stream().map(InteresadoDTO::new).toList();
    }

    public InteresadoDTO update(Integer iid, InteresadoDTO interesadoDTO) {
        Interesado interesado = rep.findById(iid).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Interesado [%d] inexistente", iid))
        );

        interesado = rep.save(interesado.update(interesadoDTO.toEntity(interesadoDTO)));

        return new InteresadoDTO(interesado);
    }

    public boolean deleteById(Integer iid) {
        if (!rep.existsById(iid)) {
            return false;
        }

        rep.deleteById(iid);
        return true;
    }
}
