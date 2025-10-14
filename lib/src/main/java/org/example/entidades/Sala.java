package org.example.entidades;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "salas")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"departamento", "citas"})
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String numero;

    @Column(nullable = false, length = 100)
    private String tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departamento_id", nullable = false)
    private Departamento departamento;

    @OneToMany(mappedBy = "sala", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Cita> citas;

    @PrePersist
    protected void onCreate() {
        if (citas == null) {
            citas = new ArrayList<>();
        }
    }

    void addCita(Cita cita) {
        if (citas == null) {
            citas = new ArrayList<>();
        }
        citas.add(cita);
    }

    public List<Cita> getCitas() {
        return citas == null ? Collections.emptyList() : Collections.unmodifiableList(citas);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sala sala = (Sala) o;
        return Objects.equals(numero, sala.numero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }
}