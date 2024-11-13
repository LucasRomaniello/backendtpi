package people.demo.web.api;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import people.demo.web.api.dto.InteresadoDTO;
import people.demo.web.service.InteresadosService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/interesados")
public class InteresadosAPI {
    private InteresadosService interesadosService;

    @Autowired
    public InteresadosAPI(InteresadosService service) {
        this.interesadosService = service;
    }

    @GetMapping
    public ResponseEntity<List<InteresadoDTO>> findInteresados() {
        List<InteresadoDTO> interesadoDTOS = interesadosService.findAll();

        return interesadoDTOS.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(interesadoDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InteresadoDTO> findInteresado(@PathVariable Integer id) {
        Optional<InteresadoDTO> InteresadoDTO = interesadosService.findById(id);

        return InteresadoDTO.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(InteresadoDTO.get());
    }

    @PostMapping
    public ResponseEntity<InteresadoDTO> addInteresado(@RequestBody @Valid InteresadoDTO interesadoDTO) {
        return new ResponseEntity<>(interesadosService.add(interesadoDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InteresadoDTO> updateInteresado(
            @PathVariable Integer id,
            @Valid @RequestBody InteresadoDTO interesadoDTO
    ) {
        return new ResponseEntity<>(interesadosService.update(id, interesadoDTO), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteInteresado(@PathVariable Integer id) {
        return interesadosService.deleteById(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
