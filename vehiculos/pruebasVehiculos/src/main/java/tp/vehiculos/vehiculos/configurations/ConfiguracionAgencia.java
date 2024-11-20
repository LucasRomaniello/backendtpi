package tp.vehiculos.vehiculos.configurations;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import tp.vehiculos.vehiculos.models.Posicion;

import java.util.List;

import static tp.vehiculos.vehiculos.configurations.Geolocalizacion.calcularDistanciaEuclidiana;

public class ConfiguracionAgencia {
    @JsonProperty("coordenadasAgencia")
    private Coordenadas coordenadasAgencia;
    @JsonProperty("radioAdmitidoKm")
    private double radioAdmitidoKm;
    @JsonProperty("zonasRestringidas")
    private List<ZonaRestringida> zonasRestringidas;

    public boolean asegurarCumplimientoNormas(Posicion posicion){
        //devuelve true si es necesario notificar y false si todo esta en orden
        if (fueraDeRadio(posicion) || enZonaRestringida(posicion)){
            return true;
        } else {
            return false;
        }

    }

    public boolean fueraDeRadio(Posicion posicion){
        double distancia = Geolocalizacion.calcularDistanciaEuclidiana(coordenadasAgencia.getLatitud(), coordenadasAgencia.getLongitud(), posicion.getLatitud(), posicion.getLongitud());

        if(distancia>radioAdmitidoKm){
            posicion.fueraDeRadio();
            System.out.println("fuera de radio");
            return true;
        }
        return false;

    }

    public boolean enZonaRestringida(Posicion posicion) {
        double lat = posicion.getLatitud();
        double lon = posicion.getLongitud();


        for (ZonaRestringida zona : zonasRestringidas) {
            if (zona.estaEnZonaRestringida(lat, lon)) {
                posicion.enZonaRestringida();
                System.out.println("zona restringida");
                return true;
            }
        }
        return false;
    }



    @Data
    public static class Coordenadas {
        @JsonProperty("lat")
        private double lat;
        @JsonProperty("lon")
        private double lon;

        public double getLatitud() {
            return lat;
        }

        public double getLongitud() {
            return lon;
        }

        @Override
        public String toString() {
            return "Coordenadas{" +
                    "lat=" + lat +
                    ", lon=" + lon +
                    '}';
        }
    }

    @Data
    public static class ZonaRestringida {
        @JsonProperty("noroeste")
        private Coordenadas noroeste;
        @JsonProperty("sureste")
        private Coordenadas sureste;



        public boolean estaEnZonaRestringida(double latitud, double longitud){
            return latitud <= noroeste.getLatitud() && latitud >= sureste.getLatitud()
                    && longitud >= noroeste.getLongitud() && longitud <= sureste.getLongitud();



        }

        @Override
        public String toString() {
            return "ZonaRestringida{" +
                    "noroeste=" + noroeste +
                    ", sureste=" + sureste +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ConfiguracionAgencia{" +
                "coordenadasAgencia=" + coordenadasAgencia +
                ", radioAdmitidoKm=" + radioAdmitidoKm +
                ", zonasRestringidas=" + zonasRestringidas +
                '}';
    }
}

