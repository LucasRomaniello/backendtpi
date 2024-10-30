package people.demo.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Entity
@Table(name = "interesado")
public class Interesado {
    @Id
    private Integer id;

    private String tipo_documento;

    private String documento;

    private String nombre;

    private String apellido;

    private Boolean restringido;

    private Integer nro_licencia;

    private Date fecha_vencimiento_licencia;

    @OneToMany(mappedBy = "interesado")
    private Set<Prueba> pruebas = new HashSet<>();

    public Interesado() {

    }

    public Interesado(Integer id, String tipoDocumento, String documento, String nombre, String apellido, Boolean restringido, Integer nroLicencia, Date fechaVencimientoLicencia) {
        this.id = id;
        this.tipo_documento = tipoDocumento;
        this.documento = documento;
        this.nombre = nombre;
        this.apellido = apellido;
        this.restringido= restringido;
        this.nro_licencia = nroLicencia;
        this.fecha_vencimiento_licencia = fechaVencimientoLicencia;
    }
}
