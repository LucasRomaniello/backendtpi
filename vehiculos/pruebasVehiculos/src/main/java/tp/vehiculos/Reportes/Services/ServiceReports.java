package tp.vehiculos.Reportes.Services;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import tp.vehiculos.Controller.NoPruebasEncontradasException;
import tp.vehiculos.Reportes.dto.*;
import tp.vehiculos.auth.JwTService;
import tp.vehiculos.vehiculos.models.Posicion;
import tp.vehiculos.vehiculos.services.PosicionService;

import static java.lang.String.format;

@Service
public class ServiceReports {

    private static final Logger log = LoggerFactory.getLogger(ServiceReports.class);
    private final PosicionService posicionService;
    private final RestTemplate restTemplate;
    private static final String APIPRUEBAS = "http://localhost:8001/pruebas/finalizadas";
    private static final String APIPRUEBAS_PARAEMPLEADO = "http://localhost:8001/pruebas/empleado";
    private static final String APIPRUEBAS_PARAVEHICULO = "http://localhost:8001/pruebas/vehiculo";
    private static final String APIEMPLEADO = "http://localhost:8001/empleados";
    private static final String APIPRUEBAINTERESADO = "http://localhost:8001/interesados";
    private final String filePath = System.getProperty("user.dir");
    private final JwTService jwTService;

    @Autowired
    public ServiceReports(RestTemplate restTemplate, PosicionService posicionService, JwTService jwTService) {
        this.posicionService = posicionService;
        this.restTemplate = restTemplate;
        this.jwTService = jwTService;
    }

    public List<ReporteIncidentesDTO> generarReporteIncidentes(HttpServletRequest request) throws Exception {

        List<Posicion> incidenList = new ArrayList<>();
        List<ReporteIncidentesDTO> incidentesDTO = new ArrayList<>();

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Extraer el token JWT del encabezado (quitar "Bearer ")
            String token = authHeader.substring(7);

            List<PruebaDTO>  pruebas = jwTService.getWithJwt(token, APIPRUEBAS, new ParameterizedTypeReference<List<PruebaDTO>>() {});

            if (pruebas.isEmpty()) throw new NoPruebasEncontradasException("No se encontraron pruebas!");

            for (PruebaDTO pruebaDTO : pruebas) {
                Optional<Posicion> incidente = posicionService.obtenerEntreFechasIncidente(pruebaDTO.getFechaInicio(),
                        pruebaDTO.getFechaFin(), pruebaDTO.getIdvehiculo());
                incidente.ifPresent(incidenList::add);
            }

            // Especificar el nombre del archivo
            String fileName = "reporteTotalIncidentes.csv";
            File file = new File(filePath + "/" + fileName);
            System.out.println("Generando reporte con cantidad de incidentes: " + incidenList.size());
            try (PrintWriter printWriter = new PrintWriter(file)) {
                printWriter.println(format("%s %s %s %s %s", "Tipo Incidente", "Patente Vehiculo", "Fecha", "Latitud", "Longitud"));
                incidenList.forEach(inc -> {
                    String tipoIncidente = "";
                    if (inc.estaFueraDeRadio()) {
                        tipoIncidente = "Salió del radio permitido";
                    } else {
                        tipoIncidente = "Entró a zona peligrosa";
                    }
                    printWriter.println(String.format("%s,%s,%s,%s,%s",
                            tipoIncidente,
                            inc.getVehiculo().getPatente(),
                            inc.getFecha().toString(),
                            inc.getLatitud(),
                            inc.getLongitud()
                    ));

                    incidentesDTO.add(new ReporteIncidentesDTO(tipoIncidente,
                            inc.getVehiculo().getPatente(),
                            inc.getFecha().toString(),
                            inc.getLatitud(),
                            inc.getLongitud()));

                });
                System.out.println("Creado con éxito!");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new Exception("Token JWT no proporcionado o inválido.");
        }
        return incidentesDTO;
    }

    public List<ReporteIncidentesDTO> generarReporteIncidentesEmpleado(Integer id, HttpServletRequest request) throws Exception {

        List<PruebaDTO> pruebas = new ArrayList<>();
        List<Posicion> incidenList = new ArrayList<>();
        List<ReporteIncidentesDTO> reporteIncidentes = new ArrayList<>();

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Extraer el token JWT del encabezado (quitar "Bearer ")
            String token = authHeader.substring(7);
            String url = APIPRUEBAS_PARAEMPLEADO + "/" + id;
            pruebas = jwTService.getWithJwt(token, url, new ParameterizedTypeReference<List<PruebaDTO>>() {
            });

            if (pruebas.isEmpty()) {
                throw new NoPruebasEncontradasException("No se encontraron pruebas para el empleado con id: " + id);
            }

            for (PruebaDTO pruebaDTO : pruebas) {
                Optional<Posicion> incidente = posicionService.obtenerEntreFechasIncidente
                        (pruebaDTO.getFechaInicio(), pruebaDTO.getFechaFin(), pruebaDTO.getIdvehiculo());
                if (incidente.isPresent()) {
                    incidenList.add(incidente.get());
                }
            }

            String fileName = "reporteIncidentesEmpleado.csv";

            File file = new File(filePath + "/" + fileName);

            System.out.println("Generando reporte de incidentes empleados, cantidad: " + incidenList.size());

            try (PrintWriter printWriter = new PrintWriter(file)) {
                printWriter.println(format("%s %s %s %s %s", "TipoIncidente", "Patente", "Fecha", "Latitud", "Longitud"));
                incidenList.forEach(inc -> {
                    String tipoIncidente = "";
                    if (inc.estaFueraDeRadio()) {
                        tipoIncidente = "Salió del radio permitido";
                    } else {
                        tipoIncidente = "Entró a zona peligrosa";
                    }


                    printWriter.println(String.format("%s,%s,%s,%s,%s",
                            tipoIncidente,
                            inc.getVehiculo().getPatente(),
                            inc.getFecha().toString(),
                            inc.getLatitud(),
                            inc.getLongitud()
                    ));

                    reporteIncidentes.add(new ReporteIncidentesDTO(tipoIncidente,
                            inc.getVehiculo().getPatente(),
                            inc.getFecha().toString(),
                            inc.getLatitud(),
                            inc.getLongitud()));
                });
            } catch (FileNotFoundException e) {
                throw new RuntimeException("No se ha encontrado el archivo");
            }
        } else {
            throw new RuntimeException("Token JWT no proporcionado o inválido.");
        }
        return reporteIncidentes;
    }

    public ReporteKmDTO generarReporteCantidadKm(LocalDateTime fechaDesde, LocalDateTime fechaHasta, int idVehiculo) {
        double cantidadKm = posicionService.calcularCantidadKm(fechaDesde, fechaHasta, idVehiculo);
        String cantidadKmRedondeada = String.format("%.2f", cantidadKm);
        String file = "CantidadDeKmRecorridos";
        ReporteKmDTO reporte;
        try (PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.println("  Fecha Inicio: " + fechaDesde +
                    "  Fecha Fin: " + fechaHasta +
                    "  Id Vehiculo: " + idVehiculo +
                    "  Km Recorridos: " + cantidadKmRedondeada
            );

            reporte = new ReporteKmDTO(fechaDesde, fechaHasta, idVehiculo, cantidadKmRedondeada);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return reporte;
    }

    public List<ReporteDetalleDTO> generarReportePruebasDetalle(Integer id, HttpServletRequest request) throws Exception {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token JWT no proporcionado o inválido.");
        }

        String token = authHeader.substring(7);
        List<PruebaDTO> pruebas = new ArrayList<>();
        String url = APIPRUEBAS_PARAVEHICULO + "/" + id;

        //Este try se hace por si el vehiculo se encuentra en una prueba sin finalizar
        try {
            pruebas = jwTService.getWithJwt(token, url, new ParameterizedTypeReference<List<PruebaDTO>>() {
            });
        } catch (Exception e){
            throw new NoPruebasEncontradasException(
                    String.format("No se encontraron pruebas finalizadas para el vehículo con ID: %d en la URL: %s", id, url)
            );
        }
        if (pruebas.isEmpty()) {
            throw new NoPruebasEncontradasException(
                    String.format("No se encontraron pruebas para el vehículo con ID: %d en la URL: %s", id, url)
            );
        }

        List<ReporteDetalleDTO> reporteDetalleDTOS = new ArrayList<>();
        // Ruta y nombre del archivo
        String fileName = String.format("reportePruebasConDetalle_%d.csv", id);
        File file = new File(filePath + "/" + fileName);

        // Usar try-with-resources para garantizar el cierre del archivo
        try (PrintWriter printWriter = new PrintWriter(file)) {
            // Escribir el encabezado del archivo CSV siempre al iniciar
            printWriter.println("\"Fecha Inicio\",\"Fecha Fin\",\"Apellido Interesado\",\"Nombre Interesado\",\"Documento Interesado\",\"Licencia Interesado\",\"Nombre Empleado\",\"Apellido Empleado\",\"Telefono Empleado\"");

            // Recorrer las pruebas y agregar la información en el archivo
            for (PruebaDTO prueba : pruebas) {

                String URIEmpleado = APIEMPLEADO + "/" + prueba.getLegajo_empleado();
                EmpleadoDTO empleadoDTO = jwTService.getWithJwt(token, URIEmpleado, new ParameterizedTypeReference<EmpleadoDTO>() {
                });

                String UriInteresado = APIPRUEBAINTERESADO + "/" + prueba.getId_interesado();
                InteresadoDTO interesadoDTO = jwTService.getWithJwt(token, UriInteresado, new ParameterizedTypeReference<InteresadoDTO>() {
                });

                // Validar si los objetos no son null
                if (empleadoDTO != null && interesadoDTO != null) {
                    // Formatear y escribir la línea en el archivo CSV, protegiendo los datos con comillas si contienen comas
                    printWriter.println(String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                            prueba.getFechaHoraInicio(),
                            prueba.getFechaHoraFin(),
                            interesadoDTO.getApellido(),
                            interesadoDTO.getNombre(),
                            interesadoDTO.getDocumento(),
                            interesadoDTO.getNro_licencia(),
                            empleadoDTO.getNombre(),
                            empleadoDTO.getApellido(),
                            empleadoDTO.getTelefonoContacto()
                    ));

                    reporteDetalleDTOS.add(new ReporteDetalleDTO(
                            prueba.getFechaHoraInicio(),
                            prueba.getFechaHoraFin(),
                            interesadoDTO.getApellido(),
                            interesadoDTO.getNombre(),
                            interesadoDTO.getDocumento(),
                            interesadoDTO.getNro_licencia(),
                            empleadoDTO.getNombre(),
                            empleadoDTO.getApellido(),
                            empleadoDTO.getTelefonoContacto()
                    ));
                } else {
                    // Manejo de error si no se encuentra el empleado o el interesado
                    System.out.println("Empleado o interesado no encontrado para la prueba: " + prueba.getId());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al escribir el archivo CSV: " + e.getMessage(), e);
        }

        return reporteDetalleDTOS;
    }

}