package people.demo.web.api;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import people.demo.web.api.dto.PruebaDTO;
import people.demo.web.service.PruebasService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pruebas")
public class PruebasAPI {
    private PruebasService pruebasService;

    @Autowired
    public PruebasAPI(PruebasService service) {
        this.pruebasService = service;
    }

    @GetMapping
    public ResponseEntity<List<PruebaDTO>> findPruebas() {
        List<PruebaDTO> pruebaDTOS = pruebasService.findAll();

        return pruebaDTOS.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(pruebaDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PruebaDTO> findPrueba(@PathVariable Integer id) {
        Optional<PruebaDTO> PruebaDTO = pruebasService.findById(id);

        return PruebaDTO.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(PruebaDTO.get());
    }

    @PostMapping
    public ResponseEntity<PruebaDTO> addPrueba(@RequestBody @Valid PruebaDTO pruebaDTO) {
        return new ResponseEntity<>(pruebasService.add(pruebaDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PruebaDTO> updatePrueba(
            @PathVariable Integer id,
            @Valid @RequestBody PruebaDTO pruebaDTO
    ) {
        return new ResponseEntity<>(pruebasService.update(id, pruebaDTO), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletePrueba(@PathVariable Integer id) {
        return pruebasService.deleteById(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
