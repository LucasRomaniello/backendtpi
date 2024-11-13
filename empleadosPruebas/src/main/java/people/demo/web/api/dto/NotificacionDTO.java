package people.demo.web.api.dto;

import lombok.Getter;
import lombok.Setter;
import people.demo.domain.Notificacion;

@Setter
@Getter
public class NotificacionDTO {

    //@NotBlank(message = "La notificacion debe tener un vehiculo asociado")
    private int id_notificacion;
    private int id_vehiculo;

    private double latitud;

    private double longitud;

    private int nroTelefono;

    private boolean enZonaRestringida;

    private boolean fueraDeRadioPermitido;

    private String tipo;

    public NotificacionDTO(){}

    public NotificacionDTO(Notificacion notificacion){
        this.id_notificacion = notificacion.getId();
        this.id_vehiculo = notificacion.getIdVehiculo();
        this.latitud = notificacion.getLatitud();
        this.longitud = notificacion.getLongitud();
        this.nroTelefono = notificacion.getNro_contacto();
        this.fueraDeRadioPermitido = notificacion.getFueraDeRadioPermitido();
        this.enZonaRestringida = notificacion.getEnZonaRestringida();
        this.tipo = notificacion.getTipo();

    }

    public static NotificacionDTO toDTO(Notificacion notificacion) {
        if (notificacion == null) return null;
        return new NotificacionDTO(notificacion);
    }

    public Notificacion toEntity(NotificacionDTO notificacionDTO) {
        if (notificacionDTO == null) return null;

        Notificacion notificacion = new Notificacion();
        notificacion.setId(notificacionDTO.getId_notificacion());
        notificacion.setIdVehiculo(notificacionDTO.getId_vehiculo());
        notificacion.setLatitud(notificacionDTO.getLatitud());
        notificacion.setLongitud(notificacionDTO.getLongitud());
        notificacion.setNro_contacto(notificacionDTO.getNroTelefono());
        notificacion.setFueraDeRadioPermitido(notificacionDTO.isFueraDeRadioPermitido());
        notificacion.setEnZonaRestringida(notificacionDTO.isEnZonaRestringida());

        if(notificacionDTO.enZonaRestringida){
            notificacion.setTipo("En zona restringida");
        }
        else if(notificacionDTO.fueraDeRadioPermitido){
            notificacion.setTipo("Fuera de radio permitido");
        }

        return notificacion;
    }
}
