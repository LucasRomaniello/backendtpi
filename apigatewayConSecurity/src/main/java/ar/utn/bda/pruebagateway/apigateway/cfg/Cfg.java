package ar.utn.bda.pruebagateway.apigateway.cfg;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;


@Configuration
@EnableWebSecurity

public class Cfg {
    // Definir un JwtDecoder con la URL de tu servidor de autorización
    @Bean
    public JwtDecoder jwtDecoder() {
        String issuerUri = "https://labsys.frc.utn.edu.ar/aim/realms/backend-tps";  // Reemplaza con tu URL de autorización
        return JwtDecoders.fromIssuerLocation(issuerUri);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                // Esta ruta puede ser accedida por cualquier usuario asociado a un vehículo
                .requestMatchers("/api/posicion/**")
                .hasAuthority("VEHICULO")

                // Esta ruta puede ser accedida por ADMINISTRADORES Y EMPLEADOS
                .requestMatchers("/api/vehiculos/**")
                .hasAnyAuthority("ADMIN", "EMPLEADO")

                // Esta ruta puede ser accedida por Administradores
                .requestMatchers("/api/reportes/**")
                .hasAuthority("ADMIN")

                // Esta ruta puede ser accedida por ADMINISTRADORES Y EMPLEADOS
                .requestMatchers("/empleados/**")
                .hasAnyAuthority("ADMIN", "EMPLEADO")

                // Esta ruta puede ser accedida por ADMINISTRADORES Y EMPLEADOS
                .requestMatchers("/interesados/**")
                .hasAnyAuthority("ADMIN", "EMPLEADO")

                // Esta ruta puede ser accedida por EMPLEADOS
                .requestMatchers("/notificarVehiculo/**")
                .hasAuthority("EMPLEADO")

                // Esta ruta puede ser accedida por EMPLEADOS
                .requestMatchers("/notificarPromocion/**")
                .hasAnyAuthority("EMPLEADO")

                // Esta ruta puede ser accedida por cualquier ROL
                .requestMatchers("/pruebas/**")
                .hasAnyAuthority("EMPLEADO")

                .anyRequest()
                .authenticated()

        ).oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        return http.build();
    }

    interface AuthoritiesConverter extends Converter<Map<String, Object>, Collection<GrantedAuthority>> {}

    @Bean
    AuthoritiesConverter realmRolesAuthoritiesConverter() {
        return claims -> {
            var realmAccess = Optional.ofNullable((Map<String, Object>) claims.get("realm_access"));
            var roles = realmAccess.flatMap(map -> Optional.ofNullable((List<String>) map.get("roles")));
            List<GrantedAuthority> list = roles.map(List::stream)
                    .orElse(Stream.empty())
                    .map(SimpleGrantedAuthority::new)
                    .map(GrantedAuthority.class::cast)
                    .toList();

            return list;
        };
    }


    @Bean
    public RouteLocator configurarRutas(RouteLocatorBuilder builder,
                                        @Value("${auth}") String uriAuth,
                                        @Value("${empleadosPruebas}") String uriEmpPruebas,
                                        @Value("${pruebasVehiculos}") String uriPruebasVehiculos
                                        ){
        return builder.routes()
                .route(p -> p.path("/empleados/**").uri(uriEmpPruebas))
                .route(p -> p.path("/interesados/**").uri(uriEmpPruebas))
                .route(p -> p.path("/notificarVehiculo/**").uri(uriEmpPruebas))
                .route(p -> p.path("/notificarPromocion/**").uri(uriEmpPruebas))
                .route(p -> p.path("/pruebas/**").uri(uriEmpPruebas))
                .route(p -> p.path("/api/reportes/**").uri(uriPruebasVehiculos))
                .route(p -> p.path("/api/posicion/**").uri(uriPruebasVehiculos))
                .route(p -> p.path("/api/vehiculos/**").uri(uriPruebasVehiculos))
                .build();
    }

}
