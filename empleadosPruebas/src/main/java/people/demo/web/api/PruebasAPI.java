package people.demo.web.api;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import people.demo.web.api.dto.PruebaDTO;
import people.demo.web.service.PruebasService;

import java.time.LocalDateTime;
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

    //Api para obtener todas las pruebas finalizadas
    @GetMapping("/finalizadas")
    public ResponseEntity<List<PruebaDTO>> findAllPruebasFinalizadas(){
        List<PruebaDTO> pruebaDTOS = pruebasService.findAllFinalizadas();
        return pruebaDTOS.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(pruebaDTOS);
    }

    //Api para obtener todas las pruebas para un empleado
    @GetMapping("/empleado/{id}")
    public ResponseEntity<List<PruebaDTO>> findPruebaByID(@PathVariable int id) {
        List<PruebaDTO> pruebaDTOS = pruebasService.findAllPruebasForEmployee(id);
        return pruebaDTOS.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(pruebaDTOS);
    }



    @GetMapping("/pruebaActual/{id}")
    public ResponseEntity<Optional<PruebaDTO>> findPruebaByIdVehiculo( @PathVariable Integer id) {
        Optional<PruebaDTO> pruebaDTO = pruebasService.findPruebaByIdVehiculo(id);
        return pruebaDTO.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(pruebaDTO);
    }


    @GetMapping("/enCurso")
    public ResponseEntity<List<PruebaDTO>> findPruebasEnCurso() {
        List<PruebaDTO> pruebaDTOS = pruebasService.findAllWithFechaFinIsNull();
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
        pruebaDTO.setFechaHoraInicio(LocalDateTime.now());
        //Si esta prueba al momento de hacer el post por medio de postman te vuelve todo null
        //Es porque tener un empleado en una prueba actual o bien el interesado esta restringido
        //O porque el vehiculo esta en uso o bien el carnet del interesado esta vencidoç
        //Fijarse en la base de datos

        return new ResponseEntity<>(pruebasService.add(pruebaDTO), HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<PruebaDTO> updatePrueba(
            @Valid @RequestBody PruebaDTO pruebaDTO
    ) {
        return new ResponseEntity<>(pruebasService.update(pruebaDTO), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletePrueba(@PathVariable Integer id) {
        return pruebasService.deleteById(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
