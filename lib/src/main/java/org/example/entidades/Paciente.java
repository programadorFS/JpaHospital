package org.example.entidades;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "pacientes")
@Getter
@Setter(AccessLevel.PROTECTED)
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, exclude = {"citas", "historiaClinica", "hospital"})
public class Paciente extends Persona {

    @Column(nullable = false, length = 20)
    private String telefono;

    @Column(nullable = false, length = 300)
    private String direccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @OneToOne(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    private HistoriaClinica historiaClinica;

    @OneToMany(mappedBy = "paciente", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Cita> citas;

    protected Paciente(PacienteBuilder<?, ?> builder) {
        super(builder);
        this.telefono = validarString(builder.telefono, "El teléfono no puede ser nulo ni vacío");
        this.direccion = validarString(builder.direccion, "La dirección no puede ser nula ni vacía");
        this.citas = new ArrayList<>();
        
        // CRÍTICO: Historia clínica auto-generada
        this.historiaClinica = HistoriaClinica.builder()
                .paciente(this)
                .build();
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

    void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }
}