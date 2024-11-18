package tp.vehiculos.auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class JwTService {

    private final RestTemplate restTemplate;

    @Autowired
    public JwTService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Método para hacer una solicitud GET con el token JWT.
     *
     * @param token JWT para autenticación.
     * @param url URL del microservicio destino.
     * @param responseType Tipo de la respuesta esperada (por ejemplo, Empleado.class).
     * @param <T> Tipo de respuesta
     * @return El cuerpo de la respuesta del microservicio
     * @throws Exception si hay un error en la comunicación.
     */
    public <T> T getWithJwt(String token, String url, ParameterizedTypeReference<T> responseType) throws Exception {

        if (token == null || token.isEmpty()) {
            throw new Exception("Token JWT no proporcionado o inválido.");
        }

        // Configurar los encabezados con el token JWT
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);  // Setea el token JWT

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Realizar la solicitud GET al microservicio
        ResponseEntity<T> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                responseType
        );

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();  // Retorna el cuerpo de la respuesta (ej. Empleado)
        } else {
            throw new Exception("Error en la comunicación con el microservicio externo.");
        }
    }
}
