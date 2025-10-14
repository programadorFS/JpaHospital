package org.example.entidades;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "medicos")
@Getter
@Setter(AccessLevel.PROTECTED)
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, exclude = {"citas", "departamento"})
public class Medico extends Persona {

    @Embedded
    private Matricula matricula;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private EspecialidadMedica especialidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;

    @OneToMany(mappedBy = "medico", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Cita> citas;

    protected Medico(MedicoBuilder<?, ?> builder) {
        super(builder);
        this.matricula = builder.matricula;
        this.especialidad = builder.especialidad;
        this.citas = new ArrayList<>();
    }

    public void setMatricula(String numero) {
        this.matricula = new Matricula(numero);
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

    void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }
}