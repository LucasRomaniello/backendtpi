package tp.vehiculos.Reportes.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tp.vehiculos.Reportes.Services.ServiceReports;
import tp.vehiculos.vehiculos.dtos.InformeKmRequest;

import java.util.Optional;

@RestController
@RequestMapping("/api/reportes")
public class ReportesController {

    private final ServiceReports serviceReports;

    @Autowired
    public ReportesController(ServiceReports serviceReports) {
        this.serviceReports = serviceReports;
    }

    @GetMapping("/incidentes")
    public ResponseEntity<Void> generarReporteIncidentes() {
        serviceReports.generarReporteIncidentes();
        return ResponseEntity.ok().build();
    }


    //http://localhost:8084/api/reportes/incidentesEmpleado/3 Esta seria la consulta que deberias que mandar
    // Al postman para que ser genere el reporte, cambiar el 3 por el id del empleado
    @GetMapping("/incidentesEmpleado/{id}")
    public ResponseEntity<Void> generarReporteIncidentesEmpleado(@PathVariable Integer id)
    { serviceReports.generarReporteIncidentesEmpleado(id);
        return ResponseEntity.ok().build(); }



    @GetMapping("/detallesPruebas")
    public ResponseEntity<Void> generarReporteDetallePrueba()
    { serviceReports.generarReportePruebasDetalle();
        return ResponseEntity.ok().build(); }

    @PostMapping("/informeKmRecorridos")
    public ResponseEntity<Double>  calcularKMParaVehiculoEnPeriodo(@RequestBody InformeKmRequest informeKmRequest){
        serviceReports.generarReporteCantidadKm(informeKmRequest.getFechaDesde(),informeKmRequest.getFechaHasta(), informeKmRequest.getId_vehiculo());
        return ResponseEntity.ok().build();



    }
}

