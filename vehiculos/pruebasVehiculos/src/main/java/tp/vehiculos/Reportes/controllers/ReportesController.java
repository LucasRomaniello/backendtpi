package tp.vehiculos.Reportes.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.resource.ResourceUrlProvider;
import tp.vehiculos.auth.JwTService;
import tp.vehiculos.Reportes.Services.ServiceReports;
import tp.vehiculos.Reportes.dto.ReporteDTO;
import tp.vehiculos.vehiculos.dtos.InformeKmRequest;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
public class ReportesController {

    private final ServiceReports serviceReports;
    private final ResourceUrlProvider resourceUrlProvider;
    private final JwTService jwTService;

    @Autowired
    public ReportesController(ServiceReports serviceReports, ResourceUrlProvider resourceUrlProvider, JwTService jwTService) {
        this.serviceReports = serviceReports;
        this.resourceUrlProvider = resourceUrlProvider;
        this.jwTService = jwTService;
    }

    @GetMapping("/incidentes")
    public ResponseEntity<String> generarReporteIncidentes(HttpServletRequest request) {
        try {
            serviceReports.generarReporteIncidentes(request);
            return ResponseEntity.ok("Reporte generado con éxito.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Hubo un error al generar el reporte.");
        }
    }

    @GetMapping("/incidentesEmpleado/{id}")
    public ResponseEntity<String> generarReporteIncidentesEmpleado(@PathVariable Integer id,
                                                                   HttpServletRequest request) {
        try {
            serviceReports.generarReporteIncidentesEmpleado(id, request);
            // Si tod sale bien, devolvemos un mensaje de éxito
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
//    @PostMapping("/informeKmRecorridos")
//    public ResponseEntity<String> calcularKMParaVehiculoEnPeriodo(@RequestBody InformeKmRequest informeKmRequest){
//            serviceReports.generarReporteCantidadKm(informeKmRequest.getFechaDesde(),informeKmRequest.getFechaHasta(), informeKmRequest.getId_vehiculo());
//            return ResponseEntity.ok("Reporte generado con éxito");
//    }

    @GetMapping("/informeKmRecorridos")
    public ResponseEntity<String> calcularKMParaVehiculoEnPeriodo(@RequestBody InformeKmRequest informeKmRequest){
        try {
            System.out.println(informeKmRequest);
            serviceReports.generarReporteCantidadKm(informeKmRequest.getFechaDesde(),informeKmRequest.getFechaHasta(), informeKmRequest.getId_vehiculo());
            return ResponseEntity.ok("Reporte generado con éxito");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    @GetMapping("/detallesPruebas/{id}")
    public ResponseEntity<List<ReporteDTO>> generarReporteDetallePrueba(@PathVariable Integer id,
            HttpServletRequest request) {
        List<ReporteDTO> reporteDTOS = null;
        try {
            reporteDTOS = serviceReports.generarReportePruebasDetalle(id, request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (reporteDTOS.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(reporteDTOS);
        }
    }

}


