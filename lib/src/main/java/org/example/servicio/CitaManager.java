package org.example.servicio;

import org.example.entidades.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class CitaManager {
    
    private List<Cita> citas;
    private Map<Paciente, List<Cita>> citasPorPaciente;
    private Map<Medico, List<Cita>> citasPorMedico;
    private Map<Sala, List<Cita>> citasPorSala;

    public CitaManager() {
        this.citas = new ArrayList<>();
        this.citasPorPaciente = new HashMap<>();
        this.citasPorMedico = new HashMap<>();
        this.citasPorSala = new HashMap<>();
    }

    public Cita programarCita(Paciente paciente, Medico medico, Sala sala, 
                              LocalDateTime fechaHora, BigDecimal costo) {
        return programarCita(paciente, medico, sala, fechaHora, costo, null);
    }

    public Cita programarCita(Paciente paciente, Medico medico, Sala sala,
                              LocalDateTime fechaHora, BigDecimal costo, String observaciones) {
        
        validarCita(paciente, medico, sala, fechaHora, costo);

        Cita cita = Cita.builder()
                .paciente(paciente)
                .medico(medico)
                .sala(sala)
                .fechaHora(fechaHora)
                .costo(costo)
                .estado(EstadoCita.PROGRAMADA)
                .observaciones(observaciones)
                .build();

        registrarCita(cita);
        return cita;
    }

    private void validarCita(Paciente paciente, Medico medico, Sala sala,
                             LocalDateTime fechaHora, BigDecimal costo) {
        
        if (paciente == null || medico == null || sala == null) {
            throw new CitaException("Paciente, médico y sala son obligatorios");
        }

        if (fechaHora == null || fechaHora.isBefore(LocalDateTime.now())) {
            throw new CitaException("La fecha y hora deben ser futuras");
        }

        if (costo == null || costo.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CitaException("El costo debe ser mayor que cero");
        }

        if (!medico.getEspecialidad().equals(sala.getDepartamento().getEspecialidad())) {
            throw new CitaException(
                "La especialidad del médico (" + medico.getEspecialidad().getDescripcion() + 
                ") no coincide con la especialidad del departamento de la sala (" + 
                sala.getDepartamento().getEspecialidad().getDescripcion() + ")"
            );
        }

        if (!esMedicoDisponible(medico, fechaHora)) {
            throw new CitaException("El médico no está disponible en ese horario");
        }

        if (!esSalaDisponible(sala, fechaHora)) {
            throw new CitaException("La sala no está disponible en ese horario");
        }
    }

    private boolean esMedicoDisponible(Medico medico, LocalDateTime fechaHora) {
        List<Cita> citasMedico = citasPorMedico.getOrDefault(medico, Collections.emptyList());
        
        for (Cita citaExistente : citasMedico) {
            if (citaExistente.getEstado() == EstadoCita.CANCELADA) {
                continue;
            }
            
            long horasDiferencia = Math.abs(
                java.time.Duration.between(citaExistente.getFechaHora(), fechaHora).toHours()
            );
            
            if (horasDiferencia < 2) {
                return false;
            }
        }
        return true;
    }

    private boolean esSalaDisponible(Sala sala, LocalDateTime fechaHora) {
        List<Cita> citasSala = citasPorSala.getOrDefault(sala, Collections.emptyList());
        
        for (Cita citaExistente : citasSala) {
            if (citaExistente.getEstado() == EstadoCita.CANCELADA) {
                continue;
            }
            
            long horasDiferencia = Math.abs(
                java.time.Duration.between(citaExistente.getFechaHora(), fechaHora).toHours()
            );
            
            if (horasDiferencia < 2) {
                return false;
            }
        }
        return true;
    }

    private void registrarCita(Cita cita) {
        citas.add(cita);
        
        citasPorPaciente
            .computeIfAbsent(cita.getPaciente(), k -> new ArrayList<>())
            .add(cita);
        
        citasPorMedico
            .computeIfAbsent(cita.getMedico(), k -> new ArrayList<>())
            .add(cita);
        
        citasPorSala
            .computeIfAbsent(cita.getSala(), k -> new ArrayList<>())
            .add(cita);
    }

    public List<Cita> getCitasPorPaciente(Paciente paciente) {
        return Collections.unmodifiableList(
            citasPorPaciente.getOrDefault(paciente, Collections.emptyList())
        );
    }

    public List<Cita> getCitasPorMedico(Medico medico) {
        return Collections.unmodifiableList(
            citasPorMedico.getOrDefault(medico, Collections.emptyList())
        );
    }

    public List<Cita> getCitasPorSala(Sala sala) {
        return Collections.unmodifiableList(
            citasPorSala.getOrDefault(sala, Collections.emptyList())
        );
    }

    public List<Cita> getAllCitas() {
        return Collections.unmodifiableList(citas);
    }
}