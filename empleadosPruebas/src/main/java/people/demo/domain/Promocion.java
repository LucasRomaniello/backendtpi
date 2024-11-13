package people.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
public class Promocion {
    @Id
    private int promocion_id;

    private String descripcion; //invento

    private double porc;

    private LocalDateTime fecha_hasta;

    @Transient
    private int cantidad_meses;

    @ElementCollection
    @CollectionTable(
            name = "promocion_telefonos",
            joinColumns = @JoinColumn(name = "promocion_id")
    )
    private Set<Integer> telefonos = new HashSet<>();




}
