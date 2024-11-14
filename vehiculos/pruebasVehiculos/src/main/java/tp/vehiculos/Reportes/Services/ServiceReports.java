package tp.vehiculos.Reportes.Services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import tp.vehiculos.Reportes.dto.EmpleadoDTO;
import tp.vehiculos.Reportes.dto.InteresadoDTO;
import tp.vehiculos.Reportes.dto.PruebaDTO;
import tp.vehiculos.vehiculos.models.Marca;
import tp.vehiculos.vehiculos.models.Modelo;
import tp.vehiculos.vehiculos.models.Posicion;
import tp.vehiculos.vehiculos.models.Vehiculo;
import tp.vehiculos.vehiculos.repositories.PosicionRepository;
import tp.vehiculos.vehiculos.services.ConfiguracionService;
import tp.vehiculos.vehiculos.services.PosicionService;
import tp.vehiculos.vehiculos.services.VehiculoService;

import static java.lang.String.format;

@Service
public class ServiceReports {

    private final PosicionService posicionService;
    private final RestTemplate restTemplate;
    private static final String APIPRUEBAS = "http://localhost:8001/pruebas/finalizadas";
    private static final String APIPRUEBAEMPLEADO = "http://localhost:8001/empleados";
    private static final String APIPRUEBAINTERESADO = "http://localhost:8001/interesados";
    private final String filePath = "C:/Users/Gonzalo/Desktop/backendtpi/vehiculos/pruebasVehiculos/";


    @Autowired
    public ServiceReports(RestTemplate restTemplate, PosicionService posicionService) {
        this.posicionService = posicionService;
        this.restTemplate = restTemplate;
    }

    public void generarReporteIncidentes(){

        List<PruebaDTO> pruebas = restTemplate.getForObject(APIPRUEBAS, List.class);
        List<Posicion> incidenList = new ArrayList<>();



        for (PruebaDTO pruebaDTO : pruebas) {
            Optional<Posicion> incidente = posicionService.obtenerEntreFechasIncidente(pruebaDTO.getFechaFin(), pruebaDTO.getFechaInicio(), pruebaDTO.getIdVehiculo());
            if(incidente.isPresent()){
                incidenList.add(incidente.get());
            }
        }
        // Especificar el nombre del archivo

        String fileName = "reporteTotalIncidentes.csv"; 
        File file = new File(filePath + fileName);
        try (PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.println(format("%s %s %s %s %s", "Tipo Incidente","Patente Vehiculo","Fecha","Latitud","Longitud"));
            incidenList.forEach(inc -> {
                String tipoIncidente = "";
                if(inc.estaFueraDeRadio()){tipoIncidente = "Salió del radio permitido";
                }else{tipoIncidente= "Entró a zona peligrosa";}
                printWriter.println(String.format("%s,%s,%s,%s,%s",
                        tipoIncidente,
                        inc.getVehiculo().getPatente(),
                        inc.getFecha().toString(),
                        inc.getLatitud(),
                        inc.getLongitud()
                        ));
            });
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }   
    }


    public void generarReporteIncidentesEmpleado(){

        List<PruebaDTO> pruebas = restTemplate.getForObject(APIPRUEBAEMPLEADO, List.class); 
        List<Posicion> incidenList = new ArrayList<>();

        for (PruebaDTO pruebaDTO : pruebas) {
            Optional<Posicion> incidente = posicionService.obtenerEntreFechasIncidente(pruebaDTO.getFechaInicio(), pruebaDTO.getFechaFin(), pruebaDTO.getIdVehiculo());
            if(incidente.isPresent()){
                incidenList.add(incidente.get());
            }
        }
        String fileName = "reporteIncidentesEmpleado.csv"; 
        File file = new File(fileName + filePath);
        try (PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.println(format("%s %s %s %s %s", "Tipo Incidente","Patente Vehiculo","Fecha","Latitud","Longitud"));
            incidenList.forEach(inc -> {
                String tipoIncidente = "";
                if(inc.estaFueraDeRadio()){tipoIncidente = "Salió del radio permitido";
                }else{tipoIncidente= "Entró a zona peligrosa";}


                printWriter.println(String.format("%s,%s,%s,%s,%s",
                        tipoIncidente,
                        inc.getVehiculo().getPatente(),
                        inc.getFecha().toString(),
                        inc.getLatitud(),
                        inc.getLongitud()
                ));
            });
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void generarReporteCantidadKm(LocalDateTime fechaDesde, LocalDateTime fechaHasta, int idVehiculo){
        double cantidadKm = posicionService.calcularCantidadKm(fechaDesde, fechaHasta, idVehiculo);
        String file = "CantidadDeKmRecorridos";
        try (PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.println("  Fecha Inicio: " + fechaDesde +
                                "  Fecha Fin:" + fechaHasta +
                                "  Id Vehiculo" + idVehiculo +
                                "  Km Recorridos: " + cantidadKm
            );}
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public void generarReportePruebasDetalle() {
        List<PruebaDTO> pruebas = restTemplate.getForObject(APIPRUEBAS, List.class);

        String fileName = "reportePruebasConDetalle.csv";
        File file = new File(filePath + fileName);
        try (PrintWriter printWriter = new PrintWriter(file)) {
            pruebas.forEach(prueba -> {

                EmpleadoDTO empleadoDTO = restTemplate.getForObject(APIPRUEBAEMPLEADO + "/" + prueba.getLegajo_empleado(), EmpleadoDTO.class);
                InteresadoDTO interesadoDTO = restTemplate.getForObject(APIPRUEBAINTERESADO + "/" + prueba.getId_interesado(), InteresadoDTO.class);
                printWriter.println(
                        "- Prueba: " + prueba.getId() + "\n " +
                                "Fecha Inicio: " + prueba.getFechaInicio() + "Fecha Fin: " + prueba.getFechaFin() + "\n"
                                + "Interesado: " + interesadoDTO.getApellido() + " " + interesadoDTO.getNombre() +
                                "Identidad: " + interesadoDTO.getDocumento() + "Licencia: " + interesadoDTO.getNro_licencia() + "\n"
                                + "Empleado a cargo " + empleadoDTO.getApellido() + " " + empleadoDTO.getNombre() + "Telefono: " + empleadoDTO.getTelefonoContacto()


                );  });


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
  }

    }
}