package org.example.entidades;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "historias_clinicas")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "paciente")
public class HistoriaClinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_historia", unique = true, nullable = false, length = 50)
    private String numeroHistoria;

    @OneToOne
    @JoinColumn(name = "paciente_id", nullable = false, unique = true)
    private Paciente paciente;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @ElementCollection
    @CollectionTable(name = "diagnosticos", joinColumns = @JoinColumn(name = "historia_clinica_id"))
    @Column(name = "diagnostico", length = 500)
    private List<String> diagnosticos;

    @ElementCollection
    @CollectionTable(name = "tratamientos", joinColumns = @JoinColumn(name = "historia_clinica_id"))
    @Column(name = "tratamiento", length = 500)
    private List<String> tratamientos;

    @ElementCollection
    @CollectionTable(name = "alergias", joinColumns = @JoinColumn(name = "historia_clinica_id"))
    @Column(name = "alergia", length = 200)
    private List<String> alergias;

    @PrePersist
    protected void onCreate() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
        if (numeroHistoria == null && paciente != null) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            numeroHistoria = "HC-" + paciente.getDni() + "-" + timestamp;
        }
        if (diagnosticos == null) {
            diagnosticos = new ArrayList<>();
        }
        if (tratamientos == null) {
            tratamientos = new ArrayList<>();
        }
        if (alergias == null) {
            alergias = new ArrayList<>();
        }
    }

    public void agregarDiagnostico(String diagnostico) {
        if (diagnostico == null || diagnostico.trim().isEmpty()) {
            throw new IllegalArgumentException("El diagnóstico no puede estar vacío");
        }
        if (diagnosticos == null) {
            diagnosticos = new ArrayList<>();
        }
        diagnosticos.add(diagnostico.trim());
    }

    public void agregarTratamiento(String tratamiento) {
        if (tratamiento == null || tratamiento.trim().isEmpty()) {
            throw new IllegalArgumentException("El tratamiento no puede estar vacío");
        }
        if (tratamientos == null) {
            tratamientos = new ArrayList<>();
        }
        tratamientos.add(tratamiento.trim());
    }

    public void agregarAlergia(String alergia) {
        if (alergia == null || alergia.trim().isEmpty()) {
            throw new IllegalArgumentException("La alergia no puede estar vacía");
        }
        if (alergias == null) {
            alergias = new ArrayList<>();
        }
        alergias.add(alergia.trim());
    }

    public List<String> getDiagnosticos() {
        return diagnosticos == null ? Collections.emptyList() : Collections.unmodifiableList(diagnosticos);
    }

    public List<String> getTratamientos() {
        return tratamientos == null ? Collections.emptyList() : Collections.unmodifiableList(tratamientos);
    }

    public List<String> getAlergias() {
        return alergias == null ? Collections.emptyList() : Collections.unmodifiableList(alergias);
    }
}