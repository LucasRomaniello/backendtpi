package people.demo.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "Notificaciones_riesgos")
@Getter
@Setter
@ToString

public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int idVehiculo;

    private double latitud;

    private double longitud;

    private String tipo;

    private int nro_contacto;

  //  @Transient
    private Boolean enZonaRestringida;

//    @Transient
    private Boolean fueraDeRadioPermitido;


    public Notificacion(){};


//    public Notificacion(int id, int idVehiculo, double latitud, double longitud, String tipo, int nro_contacto) {
//        this.id = id;
//        this.idVehiculo = idVehiculo;
//        this.latitud = latitud;
//        this.longitud = longitud;
//        this.tipo = tipo;
//        this.nro_contacto = nro_contacto;
//    }

    public Notificacion(int id, int idVehiculo, double latitud, double longitud, int nro_contacto, boolean enZonaRestringida, boolean fueraDeRadioPermitido) {
        this.id = id;
        this.idVehiculo = idVehiculo;
        this.latitud = latitud;
        this.longitud = longitud;
        this.nro_contacto = nro_contacto;
        this.enZonaRestringida = enZonaRestringida;
        this.fueraDeRadioPermitido = fueraDeRadioPermitido;

        if(this.enZonaRestringida){
            this.setTipo("En zona restringida");
        }
        else if(this.fueraDeRadioPermitido){
            this.setTipo("Fuera de radio permitido");
        }

    }
}
