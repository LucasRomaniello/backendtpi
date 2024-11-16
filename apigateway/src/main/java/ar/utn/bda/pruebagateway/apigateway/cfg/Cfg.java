package ar.utn.bda.pruebagateway.apigateway.cfg;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration

public class Cfg {

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
