package tp.vehiculos.vehiculos.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PosicionDto {
    //private int id;
    private int id_vehiculo;
    //private LocalDateTime fecha_hora;
    private double latitud;
    private double longitud;
    private boolean enZonaRestringida;
    private boolean fueraDeRadioPermitido;

    public PosicionDto(int id_vehiculo, double latitud, double longitud, boolean enZonaRestringida, boolean fueraDeRadioPermitido) {
        this.id_vehiculo = id_vehiculo;
        this.latitud = latitud;
        this.longitud = longitud;
        this.enZonaRestringida = enZonaRestringida;
        this.fueraDeRadioPermitido = fueraDeRadioPermitido;
    }

    public int getId_vehiculo() {
        return id_vehiculo;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }
}
