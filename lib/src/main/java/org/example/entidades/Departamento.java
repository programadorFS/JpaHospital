package org.example.entidades;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "departamentos")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"hospital", "medicos", "salas"})
public class Departamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private EspecialidadMedica especialidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @OneToMany(mappedBy = "departamento", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Medico> medicos;

    @OneToMany(mappedBy = "departamento", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Sala> salas;

    @PrePersist
    protected void onCreate() {
        if (medicos == null) {
            medicos = new ArrayList<>();
        }
        if (salas == null) {
            salas = new ArrayList<>();
        }
    }

    public void agregarMedico(Medico medico) {
        if (!medico.getEspecialidad().equals(this.especialidad)) {
            throw new IllegalArgumentException(
                "El médico debe tener la especialidad " + this.especialidad.getDescripcion()
            );
        }
        if (medicos == null) {
            medicos = new ArrayList<>();
        }
        medicos.add(medico);
        medico.setDepartamento(this);
    }

    public Sala crearSala(String numero, String tipo) {
        Sala sala = Sala.builder()
                .numero(numero)
                .tipo(tipo)
                .departamento(this)
                .build();
        if (salas == null) {
            salas = new ArrayList<>();
        }
        salas.add(sala);
        return sala;
    }

    public List<Medico> getMedicos() {
        return medicos == null ? Collections.emptyList() : Collections.unmodifiableList(medicos);
    }

    public List<Sala> getSalas() {
        return salas == null ? Collections.emptyList() : Collections.unmodifiableList(salas);
    }

    void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }
}