package tp.vehiculos.vehiculos.configurations;

public class Geolocalizacion {
    static double CONVERSION_GRADOS_A_KM = 111.0;

    public static double calcularDistanciaEuclidiana(double lat1, double lon1, double lat2, double lon2) {
        double lat1Km = lat1 * CONVERSION_GRADOS_A_KM;
        double lon1Km = lon1 * CONVERSION_GRADOS_A_KM;
        double lat2Km = lat2 * CONVERSION_GRADOS_A_KM;
        double lon2Km = lon2 * CONVERSION_GRADOS_A_KM;

        // Usar el teorema de Pitágoras para calcular la distancia euclidiana
        double distancia = Math.sqrt(Math.pow(lat2Km - lat1Km, 2) + Math.pow(lon2Km - lon1Km, 2));
        return distancia;
    }
}
