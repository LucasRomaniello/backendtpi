package tp.vehiculos.Reportes.Services;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import tp.vehiculos.Controller.NoPruebasEncontradasException;
import tp.vehiculos.Reportes.dto.EmpleadoDTO;
import tp.vehiculos.Reportes.dto.InteresadoDTO;
import tp.vehiculos.Reportes.dto.PruebaDTO;
import tp.vehiculos.Reportes.dto.ReporteDTO;
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

    public void generarReporteIncidentes(HttpServletRequest request) throws Exception {

        List<PruebaDTO> pruebas = new ArrayList<>();
        List<Posicion> incidenList = new ArrayList<>();

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Extraer el token JWT del encabezado (quitar "Bearer ")
            String token = authHeader.substring(7);

        pruebas = jwTService.getWithJwt(token, APIPRUEBAS, new ParameterizedTypeReference<List<PruebaDTO>>() {});



        for (PruebaDTO pruebaDTO : pruebas) {
            Optional<Posicion> incidente = posicionService.obtenerEntreFechasIncidente
                    (pruebaDTO.getFechaInicio(), pruebaDTO.getFechaFin(), pruebaDTO.getIdvehiculo());
            if (incidente.isPresent()) {
                incidenList.add(incidente.get());
            }
        }
        System.out.println("PASO EL FOR de pruebas dto");
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
            });
            System.out.println("Creado con éxito!");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        }else {
            throw new Exception ("Token JWT no proporcionado o inválido.");
        }
    }

    public void generarReporteIncidentesEmpleado(Integer id) {

        // List<PruebaDTO> pruebas = restTemplate.getForObject(APIPRUEBAEMPLEADO, List.class);

        List<Posicion> incidenList = new ArrayList<>();
        List<PruebaDTO> pruebas = restTemplate.exchange(
                APIPRUEBAS_PARAEMPLEADO + "/" + id, // URL de la API
                HttpMethod.GET, // Método HTTP
                null, // Headers o request body si se necesita
                new ParameterizedTypeReference<List<PruebaDTO>>() {
                } // Tipo esperado
        ).getBody();

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
            });
        } catch (FileNotFoundException e) {
            throw new RuntimeException("No se ha encontrado el archivo");
        }
    }

    public void generarReporteCantidadKm(LocalDateTime fechaDesde, LocalDateTime fechaHasta, int idVehiculo) {
        double cantidadKm = posicionService.calcularCantidadKm(fechaDesde, fechaHasta, idVehiculo);
        String cantidadKmRedondeada = String.format("%.2f", cantidadKm);
        String file = "CantidadDeKmRecorridos";
        try (PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.println("  Fecha Inicio: " + fechaDesde +
                    "  Fecha Fin: " + fechaHasta +
                    "  Id Vehiculo: " + idVehiculo +
                    "  Km Recorridos: " + cantidadKmRedondeada
            );
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public List<ReporteDTO> generarReportePruebasDetalle() {
        // Obtener las pruebas desde la API
        ResponseEntity<List<PruebaDTO>> responsePruebas = restTemplate.exchange(
                APIPRUEBAS, // URL de la API
                HttpMethod.GET, // Método HTTP
                null, // Headers o request body si se necesita
                new ParameterizedTypeReference<List<PruebaDTO>>() {
                } // Tipo esperado
        );

        // Verificar si la respuesta fue exitosa
        if (!responsePruebas.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Error al obtener las pruebas");
        }

        List<PruebaDTO> pruebas = responsePruebas.getBody();
        List<ReporteDTO> reporteDTOS = new ArrayList<>();
        // Ruta y nombre del archivo
        String fileName = "reportePruebasConDetalle.csv";
        File file = new File(filePath + "/" + fileName);

        try (PrintWriter printWriter = new PrintWriter(file)) { // Abrir el archivo en modo write

            // Escribir el encabezado del archivo CSV si el archivo está vacío
            if (file.length() == 0) {
                printWriter.println("\"Fecha Inicio\",\"Fecha Fin\",\"Apellido Interesado\",\"Nombre Interesado\",\"Documento Interesado\",\"Licencia Interesado\",\"Nombre Empleado\",\"Apellido Empleado\",\"Telefono Empleado\"");
            }

            // Recorrer las pruebas y agregar la información en el archivo
            for (PruebaDTO prueba : pruebas) {

                // Obtener detalles de empleado e interesado usando exchange() para manejar ResponseEntity
                ResponseEntity<EmpleadoDTO> empleadoResponse = restTemplate.exchange(
                        APIEMPLEADO + "/" + prueba.getLegajo_empleado(),
                        HttpMethod.GET,
                        null,
                        EmpleadoDTO.class
                );

                // Imprimir la respuesta para verificar su contenido
                System.out.println("Respuesta de la API de EmpleadoDTO: " + empleadoResponse.getBody());

                EmpleadoDTO empleadoDTO = empleadoResponse.getBody();
                ResponseEntity<InteresadoDTO> interesadoResponse = restTemplate.exchange(
                        APIPRUEBAINTERESADO + "/" + prueba.getId_interesado(),
                        HttpMethod.GET,
                        null,
                        InteresadoDTO.class
                );
                InteresadoDTO interesadoDTO = interesadoResponse.getBody();

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
                    reporteDTOS.add(new ReporteDTO(
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
                    printWriter.flush();
                } else {
                    // Manejo de error si no se encuentra el empleado o el interesado
                    System.out.println("Empleado o interesado no encontrado para la prueba: " + prueba.getId());
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("No se ha encontrado el archivo o no se pudo crear: " + e.getMessage());
        }

        return reporteDTOS;

    }}