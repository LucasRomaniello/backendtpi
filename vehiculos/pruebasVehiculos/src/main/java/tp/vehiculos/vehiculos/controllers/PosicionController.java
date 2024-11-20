package tp.vehiculos.vehiculos.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tp.vehiculos.vehiculos.dtos.InformeKmRequest;
import tp.vehiculos.vehiculos.dtos.PosicionDto;
import tp.vehiculos.vehiculos.services.PosicionService;

@RestController
@RequestMapping("/api/posicion")
public class PosicionController {
    private final PosicionService servicePosicion;

    @Autowired
    public PosicionController(PosicionService servicePosicion) {
        this.servicePosicion = servicePosicion;
    }

    @PostMapping()
    public void recibirPosicion(@RequestBody PosicionDto posicionDto, HttpServletRequest request) {
        try {
            servicePosicion.procesarPosicion(posicionDto, request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    

    











}
