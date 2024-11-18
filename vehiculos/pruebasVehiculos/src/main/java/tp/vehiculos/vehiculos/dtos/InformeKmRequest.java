package tp.vehiculos.vehiculos.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class InformeKmRequest {
    @JsonProperty("id_vehiculo")
    private int id_vehiculo;
    @JsonProperty("fecha_desde")
    private LocalDateTime fechaDesde;
    @JsonProperty("fecha_hasta")
    private LocalDateTime fechaHasta;

    public int getId_vehiculo() {
        return id_vehiculo;
    }
    public LocalDateTime getFechaDesde() {
        return fechaDesde;
    }
    public LocalDateTime getFechaHasta() {
        return fechaHasta;
    }

//    Formato del body request
//    {
//       "id_vehiculo": 1,
//       "fecha_desde": "2024-11-07T19:47:19.385313900",
//       "fecha_hasta":"2024-11-07T19:47:26.564607200"
//    }

}
