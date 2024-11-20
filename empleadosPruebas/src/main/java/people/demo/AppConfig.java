package people.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
public class AppConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                // Esta ruta puede ser accedida por ADMINISTRADORES Y EMPLEADOS
                .requestMatchers("/empleados/**")
                .hasAnyAuthority("ADMIN", "EMPLEADO")
                // Esta ruta puede ser accedida por ADMINISTRADORES Y EMPLEADOS
                .requestMatchers("/interesados/**")
                .hasAnyAuthority("ADMIN", "EMPLEADO")
                // Esta ruta puede ser accedida por EMPLEADOS
                .requestMatchers("/notificarVehiculo/**")
                .hasAnyAuthority("EMPLEADO", "VEHICULO")
                // Esta ruta puede ser accedida por EMPLEADOS
                .requestMatchers("/notificarPromocion/**")
                .hasAnyAuthority("EMPLEADO")
                // Esta ruta puede ser accedida por cualquier ROL VEHICULO Y ADMIN POR LO QUE NECESITAN COMUNICACION CON EL OTRO MS
                .requestMatchers("/pruebas/**")
                .hasAnyAuthority("EMPLEADO","VEHICULO", "ADMIN")

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
    JwtAuthenticationConverter authenticationConverter(
            Converter<Map<String, Object>, Collection<GrantedAuthority>> authoritiesConverter) {
        var authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            return authoritiesConverter.convert(jwt.getClaims());
        });
        return authenticationConverter;
    }

}
