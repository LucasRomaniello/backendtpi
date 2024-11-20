package tp.vehiculos.Reportes.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.resource.ResourceUrlProvider;
import tp.vehiculos.Reportes.dto.ReporteIncidentesDTO;
import tp.vehiculos.Reportes.dto.ReporteKmDTO;
import tp.vehiculos.auth.JwTService;
import tp.vehiculos.Reportes.Services.ServiceReports;
import tp.vehiculos.Reportes.dto.ReporteDetalleDTO;

import java.time.LocalDateTime;
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
    public ResponseEntity<List<ReporteIncidentesDTO>> generarReporteIncidentes(HttpServletRequest request) {
        try {
            List<ReporteIncidentesDTO> reporteIncidentesDTOS = serviceReports.generarReporteIncidentes(request);
            return ResponseEntity.ok(reporteIncidentesDTOS);
        } catch (Exception e) {
            throw new RuntimeException("No se ha podido generar el reporte");
        }
    }

    @GetMapping("/incidentesEmpleado/{id}")
    public ResponseEntity<List<ReporteIncidentesDTO>> generarReporteIncidentesEmpleado(@PathVariable Integer id,
                                                                                       HttpServletRequest request) {
        try {
            List<ReporteIncidentesDTO> reporteIncidentesDTOS = serviceReports.generarReporteIncidentesEmpleado(id, request);
            // Si tod sale bien, devolvemos un mensaje de éxito
            return ResponseEntity.ok(reporteIncidentesDTOS);
        } catch (Exception e) {
            // Si hay algún error, devolvemos un mensaje de error
            throw new RuntimeException("Hubo un error al generar el reporte.", e);
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

//    @PostMapping("/informeKmRecorridos")
//    public ResponseEntity<String> calcularKMParaVehiculoEnPeriodo(@RequestBody InformeKmRequest informeKmRequest){
//        try {
//            System.out.println(informeKmRequest);
//            serviceReports.generarReporteCantidadKm(informeKmRequest.getFechaDesde(),informeKmRequest.getFechaHasta(), informeKmRequest.getId_vehiculo());
//            return ResponseEntity.ok("Reporte generado con éxito");
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//    }

    //ejemplo de prueba /informeKmRecorridos?id_vehiculo=1&fecha_desde=2024-09-07T19:47:19.385313900&fecha_hasta=2024-11-07T19:47:26.564607200
    @GetMapping("/informeKmRecorridos")
    public ResponseEntity<ReporteKmDTO> calcularKMParaVehiculoEnPeriodo(
            @RequestParam("id_vehiculo") int id_vehiculo,
            @RequestParam("fecha_desde") LocalDateTime fecha_desde,
            @RequestParam("fecha_hasta") LocalDateTime fecha_hasta) {
        try {
            ReporteKmDTO reporte = serviceReports.generarReporteCantidadKm(fecha_desde, fecha_hasta, id_vehiculo);
            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
            throw new RuntimeException("Hubo un error al generar el reporte.", e);
        }
    }


    @GetMapping("/detallesPruebas/{id}")
    public ResponseEntity<List<ReporteDetalleDTO>> generarReporteDetallePrueba(@PathVariable Integer id,
                                                                               HttpServletRequest request) {
        List<ReporteDetalleDTO> reporteDetalleDTOS = null;
        try {
            reporteDetalleDTOS = serviceReports.generarReportePruebasDetalle(id, request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (reporteDetalleDTOS.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(reporteDetalleDTOS);
        }
    }

}


