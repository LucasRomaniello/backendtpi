package tp.vehiculos.vehiculos.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tp.vehiculos.Controller.PosicionesNoEncontradas;
import tp.vehiculos.Reportes.dto.PruebaDTO;
import tp.vehiculos.auth.JwTService;
import tp.vehiculos.vehiculos.configurations.Geolocalizacion;
import tp.vehiculos.vehiculos.dtos.PosicionDto;
import tp.vehiculos.vehiculos.configurations.ConfiguracionAgencia;
import tp.vehiculos.vehiculos.models.Posicion;
import tp.vehiculos.vehiculos.models.Vehiculo;
import tp.vehiculos.vehiculos.repositories.PosicionRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PosicionService {
    private final VehiculoService serviceVehiculos;
    private final ConfiguracionService serviceConfiguracion;
    private final PosicionRepository repository;
    private static final String API_URL = "http://localhost:8001/notificarVehiculo";
    private static final String API_VEHICULO_EN_PRUEBA = "http://localhost:8001/pruebas/pruebaActual/";
    private final RestTemplate restTemplate;
    private final JwTService jwTService;

    @Autowired
    public PosicionService(VehiculoService serviceVehiculos, ConfiguracionService serviceConfiguracion, PosicionRepository repository, RestTemplate restTemplate, JwTService jwTService) {
        this.serviceVehiculos = serviceVehiculos;
        this.serviceConfiguracion = serviceConfiguracion;
        this.repository = repository;
        this.restTemplate = restTemplate;
        this.jwTService = jwTService;
    }

    public void procesarPosicion(PosicionDto posicionDto, HttpServletRequest request) throws Exception {
        Optional<Vehiculo> vehiculo = serviceVehiculos.getVehiculoById(posicionDto.getId_vehiculo());
        if (vehiculo.isEmpty())
            throw new RuntimeException("No vehículo registrado con id: " + posicionDto.getId_vehiculo());

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            System.out.println("Dentro del if authHeader" + vehiculo.toString());
            // Extraer el token JWT del encabezado (quitar "Bearer ")
            String token = authHeader.substring(7);

            //Ver si el vehiculo esta en una prueba acorde a su id
            //Para resolver lo mencionado se realiza la consulta al otro microservicio con la API definida

            Optional<PruebaDTO> pruebaOptional = Optional.ofNullable(jwTService.getWithJwt(token, API_VEHICULO_EN_PRUEBA + vehiculo.get().getId(), new ParameterizedTypeReference<PruebaDTO>() {
            }));

            if (pruebaOptional.isPresent()) {
                if (vehiculo.isPresent()) {
                    Posicion posicion = new Posicion(vehiculo.get(), posicionDto.getLatitud(), posicionDto.getLongitud());
                    ConfiguracionAgencia agencia = serviceConfiguracion.obtenerConfiguration();
                    boolean necesarioNotificar = agencia.asegurarCumplimientoNormas(posicion);
                    guardarPosicion(posicion);
                    if (necesarioNotificar) {
                        System.out.println("Enviando notificacion");
                        //Descomentar lo que esta abajo para notificar
                        //Este envia una peticion al otro microservicio con el cuerpo de NOTIFICACIONDTO que coincide con POSICIONDTO
                        //Y al momento de ejecutarse se guarda en la base de datos. El id de la notificacion es autoincremental
                        //ResponseEntity<Void> response = restTemplate.postForEntity(API_URL, posicion.toDto(), Void.class);
                        sendRequestWithToken(posicion, token);
                    } else {
                        System.out.println("No enviar notificacion");
                    }
                } else {
                    throw new RuntimeException("No vehículo registrado con id: " + posicionDto.getId_vehiculo());
                }
            } else {
                throw new Exception("Token JWT no proporcionado o inválido.");
            }
        }

    }

    public double calcularCantidadKm(LocalDateTime fechaInicio, LocalDateTime fechaFin, int idVehiculo) {
        List<Posicion> posiciones = obtenerEntreFechas(fechaInicio, fechaFin, idVehiculo);

        if (posiciones.isEmpty()) {
            throw new PosicionesNoEncontradas("Posiciones no encontradas para ese vehiculo");
        }
        ;

        System.out.println(posiciones.getFirst());

        Posicion posicion1 = null;
        double distanciaTotal = 0;
        double distancia = 0;
        for (Posicion posicion : posiciones) {
            if (posicion1 != null) {
                distancia = Geolocalizacion.calcularDistanciaEuclidiana(posicion.getLatitud(), posicion.getLongitud(), posicion1.getLatitud(), posicion1.getLongitud());
            }
            distanciaTotal += distancia;
            posicion1 = posicion;
        }

        return distanciaTotal;
    }

    public List<Posicion> obtenerEntreFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin, int idVehiculo) {
        return repository.findByFechaBetweenAndVehiculoId(fechaInicio, fechaFin, idVehiculo);

    }

    public Optional<Posicion> obtenerEntreFechasIncidente(LocalDateTime fechaInicio, LocalDateTime fechaFin, int idVehiculo) {
        Optional<Posicion> incidente = repository.findIncidente(fechaInicio, fechaFin, idVehiculo);

        return incidente;


    }

    public void guardarPosicion(Posicion posicion) {
        repository.save(posicion);

    }

    public void sendRequestWithToken(Posicion posicion, String jwtToken) {
        // Crear los encabezados HTTP
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);  // Agregar el token JWT

        // Crear un HttpEntity con los encabezados y el cuerpo (si lo hubiera)
        HttpEntity<PosicionDto> entity = new HttpEntity<>(posicion.toDto(), headers);

        // Realizar la solicitud POST con los encabezados y el cuerpo
        ResponseEntity<Void> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, Void.class);

        // Manejo de la respuesta (según lo que necesites)
        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Solicitud exitosa");
        } else {
            System.out.println("Error en la solicitud");
        }
    }

}



