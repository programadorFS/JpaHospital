package org.example.entidades;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "hospitales")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"departamentos", "pacientes"})
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 200)
    private String nombre;

    @Column(nullable = false, length = 300)
    private String direccion;

    @Column(nullable = false, length = 20)
    private String telefono;

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Departamento> departamentos;

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Paciente> pacientes;

    @PrePersist
    protected void onCreate() {
        if (departamentos == null) {
            departamentos = new ArrayList<>();
        }
        if (pacientes == null) {
            pacientes = new ArrayList<>();
        }
    }

    public void agregarDepartamento(Departamento departamento) {
        if (departamentos == null) {
            departamentos = new ArrayList<>();
        }
        departamentos.add(departamento);
        departamento.setHospital(this);
    }

    public void agregarPaciente(Paciente paciente) {
        if (pacientes == null) {
            pacientes = new ArrayList<>();
        }
        pacientes.add(paciente);
        paciente.setHospital(this);
    }

    public List<Departamento> getDepartamentos() {
        return departamentos == null ? Collections.emptyList() : Collections.unmodifiableList(departamentos);
    }

    public List<Paciente> getPacientes() {
        return pacientes == null ? Collections.emptyList() : Collections.unmodifiableList(pacientes);
    }

    List<Departamento> getInternalDepartamentos() {
        return departamentos;
    }

    List<Paciente> getInternalPacientes() {
        return pacientes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hospital hospital = (Hospital) o;
        return Objects.equals(nombre, hospital.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }
}