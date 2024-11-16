package tp.vehiculos.Reportes.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tp.vehiculos.Reportes.Services.ServiceReports;
import tp.vehiculos.vehiculos.dtos.InformeKmRequest;

@RestController
@RequestMapping("/api/reportes")
public class ReportesController {

    private final ServiceReports serviceReports;

    @Autowired
    public ReportesController(ServiceReports serviceReports) {
        this.serviceReports = serviceReports;
    }
     /*
    @GetMapping("/incidentes")
    public ResponseEntity<Void> generarReporteIncidentes() {
        serviceReports.generarReporteIncidentes();
        return ResponseEntity.ok().build();
    }*/

    @GetMapping("/incidentes")
    public ResponseEntity<String> generarReporteIncidentes() {
        try {
            serviceReports.generarReporteIncidentes();
            return ResponseEntity.ok("Reporte generado con éxito.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Hubo un error al generar el reporte.");
        }
    }

    //http://localhost:8084/api/reportes/incidentesEmpleado/3 Esta seria la consulta que deberias que mandar
    // Al postman para que ser genere el reporte, cambiar el 3 por el id del empleado
    /*
    @GetMapping("/incidentesEmpleado/{id}")
    public ResponseEntity<Void> generarReporteIncidentesEmpleado(@PathVariable Integer id){
        serviceReports.generarReporteIncidentesEmpleado(id);
        return ResponseEntity.ok().build();
    } */

    @GetMapping("/incidentesEmpleado/{id}")
    public ResponseEntity<String> generarReporteIncidentesEmpleado(@PathVariable Integer id) {
        try {
            // Intentamos generar el reporte para el empleado con el ID proporcionado
            serviceReports.generarReporteIncidentesEmpleado(id);
            // Si todo sale bien, devolvemos un mensaje de éxito
            return ResponseEntity.ok("Reporte generado con éxito para el empleado con ID " + id);
        } catch (Exception e) {
            // Si hay algún error, devolvemos un mensaje de error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Hubo un error al generar el reporte para el empleado con ID " + id);
        }
    }
/*
    @PostMapping("/informeKmRecorridos")
    public ResponseEntity<Double>  calcularKMParaVehiculoEnPeriodo(@RequestBody InformeKmRequest informeKmRequest){
        serviceReports.generarReporteCantidadKm(informeKmRequest.getFechaDesde(),informeKmRequest.getFechaHasta(), informeKmRequest.getId_vehiculo());
        return ResponseEntity.ok().build();
    }
*/
    @PostMapping("/informeKmRecorridos")
    public ResponseEntity<String> calcularKMParaVehiculoEnPeriodo(@RequestBody InformeKmRequest informeKmRequest){
            serviceReports.generarReporteCantidadKm(informeKmRequest.getFechaDesde(),informeKmRequest.getFechaHasta(), informeKmRequest.getId_vehiculo());
            return ResponseEntity.ok("Reporte generado con éxito");
    }



    @GetMapping("/detallesPruebas")
    public ResponseEntity<Void> generarReporteDetallePrueba()
    { serviceReports.generarReportePruebasDetalle();
        return ResponseEntity.ok().build(); }


}

