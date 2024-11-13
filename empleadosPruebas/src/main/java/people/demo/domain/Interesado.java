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
@Table(name = "interesados")
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

    //ADD PRUEBA---------------------------------//////

    public Interesado() {

    }

    public Interesado(Integer id, String tipo_documento, String documento, String nombre, String apellido, Boolean restringido, Integer nro_licencia, Date fecha_vencimiento_licencia) {
        this.id = id;
        this.tipo_documento = Interesado.this.tipo_documento;
        this.documento = documento;
        this.nombre = nombre;
        this.apellido = apellido;
        this.restringido = restringido;
        this.nro_licencia = nro_licencia;
        this.fecha_vencimiento_licencia = fecha_vencimiento_licencia;
    }

    public Interesado update(Interesado interesado) {
        id = interesado.id;
        tipo_documento = interesado.tipo_documento;
        documento = interesado.documento;
        nombre = interesado.nombre;
        apellido = interesado.apellido;
        restringido = interesado.restringido;
        nro_licencia = interesado.nro_licencia;
        fecha_vencimiento_licencia = interesado.fecha_vencimiento_licencia;
        //this.pruebas = empleado.pruebas;
        return this;
    }
}
