package org.example;

import jakarta.persistence.*;
import org.example.entidades.*;
import org.example.servicio.CitaException;
import org.example.servicio.CitaManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hospital-persistence-unit");
        EntityManager em = emf.createEntityManager();

        try {
            System.out.println("=== INICIANDO SISTEMA DE GESTIÓN HOSPITALARIA ===\n");

            // ========== 1. CREAR HOSPITAL ==========
            em.getTransaction().begin();
            
            Hospital hospital = Hospital.builder()
                    .nombre("Hospital Central")
                    .direccion("Av. Libertador 1234, Mendoza")
                    .telefono("0261-4567890")
                    .build();
            
            em.persist(hospital);
            em.getTransaction().commit();
            System.out.println("✓ Hospital creado: " + hospital.getNombre());

            // ========== 2. CREAR DEPARTAMENTOS ==========
            em.getTransaction().begin();
            
            Departamento cardiologia = Departamento.builder()
                    .nombre("Cardiología")
                    .especialidad(EspecialidadMedica.CARDIOLOGIA)
                    .build();
            hospital.agregarDepartamento(cardiologia);
            
            Departamento pediatria = Departamento.builder()
                    .nombre("Pediatría")
                    .especialidad(EspecialidadMedica.PEDIATRIA)
                    .build();
            hospital.agregarDepartamento(pediatria);
            
            Departamento traumatologia = Departamento.builder()
                    .nombre("Traumatología")
                    .especialidad(EspecialidadMedica.TRAUMATOLOGIA)
                    .build();
            hospital.agregarDepartamento(traumatologia);
            
            em.merge(hospital);
            em.getTransaction().commit();
            System.out.println("✓ Departamentos creados: " + hospital.getDepartamentos().size());

            // ========== 3. CREAR SALAS ==========
            em.getTransaction().begin();
            
            Sala salaCardio1 = Sala.builder()
                    .numero("CARD-101")
                    .tipo("Consultorio")
                    .departamento(cardiologia)
                    .build();
            em.persist(salaCardio1);
            
            Sala salaPed1 = Sala.builder()
                    .numero("PED-201")
                    .tipo("Consultorio")
                    .departamento(pediatria)
                    .build();
            em.persist(salaPed1);
            
            Sala salaTrauma1 = Sala.builder()
                    .numero("TRAUMA-301")
                    .tipo("Sala de Emergencias")
                    .departamento(traumatologia)
                    .build();
            em.persist(salaTrauma1);
            
            em.getTransaction().commit();
            System.out.println("✓ Salas creadas: 3");

            // ========== 4. CREAR MÉDICOS ==========
            em.getTransaction().begin();
            
            Medico cardiologo = Medico.builder()
                    .nombre("Carlos")
                    .apellido("González")
                    .dni("12345678")
                    .fechaNacimiento(LocalDate.of(1975, 5, 15))
                    .tipoSangre(TipoSangre.A_POSITIVO)
                    .especialidad(EspecialidadMedica.CARDIOLOGIA)
                    .build();
            cardiologo.setMatricula("MP-12345");
            cardiologia.agregarMedico(cardiologo);
            em.persist(cardiologo);
            
            Medico pediatra = Medico.builder()
                    .nombre("Ana")
                    .apellido("Martínez")
                    .dni("23456789")
                    .fechaNacimiento(LocalDate.of(1980, 8, 20))
                    .tipoSangre(TipoSangre.O_POSITIVO)
                    .especialidad(EspecialidadMedica.PEDIATRIA)
                    .build();
            pediatra.setMatricula("MP-23456");
            pediatria.agregarMedico(pediatra);
            em.persist(pediatra);
            
            Medico traumatologo = Medico.builder()
                    .nombre("Roberto")
                    .apellido("Fernández")
                    .dni("34567890")
                    .fechaNacimiento(LocalDate.of(1978, 3, 10))
                    .tipoSangre(TipoSangre.B_POSITIVO)
                    .especialidad(EspecialidadMedica.TRAUMATOLOGIA)
                    .build();
            traumatologo.setMatricula("MP-34567");
            traumatologia.agregarMedico(traumatologo);
            em.persist(traumatologo);
            
            em.getTransaction().commit();
            System.out.println("✓ Médicos creados: 3");

            // ========== 5. CREAR PACIENTES ==========
            em.getTransaction().begin();
            
            Paciente paciente1 = Paciente.builder()
                    .nombre("María")
                    .apellido("López")
                    .dni("11111111")
                    .fechaNacimiento(LocalDate.of(1985, 12, 5))
                    .tipoSangre(TipoSangre.A_POSITIVO)
                    .telefono("0261-1111111")
                    .direccion("Calle San Martín 100, Mendoza")
                    .build();
            hospital.agregarPaciente(paciente1);
            em.persist(paciente1);
            
            Paciente paciente2 = Paciente.builder()
                    .nombre("Juan")
                    .apellido("Pérez")
                    .dni("22222222")
                    .fechaNacimiento(LocalDate.of(1990, 6, 15))
                    .tipoSangre(TipoSangre.O_NEGATIVO)
                    .telefono("0261-2222222")
                    .direccion("Av. San Martín 200, Mendoza")
                    .build();
            hospital.agregarPaciente(paciente2);
            em.persist(paciente2);
            
            Paciente paciente3 = Paciente.builder()
                    .nombre("Sofía")
                    .apellido("Rodríguez")
                    .dni("33333333")
                    .fechaNacimiento(LocalDate.of(2010, 3, 25))
                    .tipoSangre(TipoSangre.AB_POSITIVO)
                    .telefono("0261-3333333")
                    .direccion("Calle Belgrano 300, Mendoza")
                    .build();
            hospital.agregarPaciente(paciente3);
            em.persist(paciente3);
            
            em.merge(hospital);
            em.getTransaction().commit();
            System.out.println("✓ Pacientes creados: 3");

            // ========== 6. AGREGAR INFORMACIÓN A HISTORIAS CLÍNICAS ==========
            em.getTransaction().begin();
            
            HistoriaClinica historia1 = paciente1.getHistoriaClinica();
            historia1.agregarDiagnostico("Hipertensión arterial");
            historia1.agregarTratamiento("Enalapril 10mg cada 12 horas");
            historia1.agregarAlergia("Penicilina");
            
            HistoriaClinica historia2 = paciente2.getHistoriaClinica();
            historia2.agregarDiagnostico("Control preventivo");
            historia2.agregarTratamiento("Vida saludable");
            
            HistoriaClinica historia3 = paciente3.getHistoriaClinica();
            historia3.agregarDiagnostico("Asma leve");
            historia3.agregarTratamiento("Salbutamol inhalador");
            historia3.agregarAlergia("Polen");
            historia3.agregarAlergia("Ácaros");
            
            em.merge(historia1);
            em.merge(historia2);
            em.merge(historia3);
            em.getTransaction().commit();
            System.out.println("✓ Historias clínicas actualizadas");

            // ========== 7. PROGRAMAR CITAS ==========
            em.getTransaction().begin();
            
            CitaManager citaManager = new CitaManager();
            
            try {
                Cita cita1 = citaManager.programarCita(
                        paciente1, cardiologo, salaCardio1,
                        LocalDateTime.now().plusDays(1).withHour(10).withMinute(0),
                        new BigDecimal("15000.00"),
                        "Control de presión arterial"
                );
                em.persist(cita1);
                System.out.println("✓ Cita 1 programada: " + paciente1.getNombreCompleto() + 
                        " con Dr. " + cardiologo.getNombreCompleto());
                
                Cita cita2 = citaManager.programarCita(
                        paciente3, pediatra, salaPed1,
                        LocalDateTime.now().plusDays(2).withHour(14).withMinute(0),
                        new BigDecimal("12000.00"),
                        "Control de asma infantil"
                );
                em.persist(cita2);
                System.out.println("✓ Cita 2 programada: " + paciente3.getNombreCompleto() + 
                        " con Dra. " + pediatra.getNombreCompleto());
                
                Cita cita3 = citaManager.programarCita(
                        paciente2, traumatologo, salaTrauma1,
                        LocalDateTime.now().plusDays(3).withHour(16).withMinute(0),
                        new BigDecimal("18000.00"),
                        "Evaluación de esguince"
                );
                em.persist(cita3);
                System.out.println("✓ Cita 3 programada: " + paciente2.getNombreCompleto() + 
                        " con Dr. " + traumatologo.getNombreCompleto());
                
            } catch (CitaException e) {
                System.err.println("Error al programar cita: " + e.getMessage());
            }
            
            em.getTransaction().commit();

            // ========== 8. CONSULTAS JPQL ==========
            System.out.println("\n=== CONSULTAS Y REPORTES ===\n");
            
            // Total de médicos por especialidad
            System.out.println("--- Médicos por Especialidad ---");
            for (EspecialidadMedica esp : EspecialidadMedica.values()) {
                Long count = em.createQuery(
                        "SELECT COUNT(m) FROM Medico m WHERE m.especialidad = :esp", Long.class)
                        .setParameter("esp", esp)
                        .getSingleResult();
                if (count > 0) {
                    System.out.println(esp.getDescripcion() + ": " + count);
                }
            }
            
            // Total de citas por estado
            System.out.println("\n--- Citas por Estado ---");
            for (EstadoCita estado : EstadoCita.values()) {
                Long count = em.createQuery(
                        "SELECT COUNT(c) FROM Cita c WHERE c.estado = :estado", Long.class)
                        .setParameter("estado", estado)
                        .getSingleResult();
                if (count > 0) {
                    System.out.println(estado + ": " + count);
                }
            }
            
            // Pacientes con alergias
            System.out.println("\n--- Pacientes con Alergias ---");
            TypedQuery<Paciente> queryAlergias = em.createQuery(
                    "SELECT DISTINCT p FROM Paciente p JOIN p.historiaClinica h WHERE SIZE(h.alergias) > 0",
                    Paciente.class
            );
            List<Paciente> pacientesConAlergias = queryAlergias.getResultList();
            for (Paciente p : pacientesConAlergias) {
                System.out.println(p.getNombreCompleto() + " - Alergias: " + 
                        p.getHistoriaClinica().getAlergias());
            }
            
            // Totales generales
            System.out.println("\n--- Estadísticas Generales ---");
            Long totalHospitales = em.createQuery("SELECT COUNT(h) FROM Hospital h", Long.class)
                    .getSingleResult();
            Long totalMedicos = em.createQuery("SELECT COUNT(m) FROM Medico m", Long.class)
                    .getSingleResult();
            Long totalPacientes = em.createQuery("SELECT COUNT(p) FROM Paciente p", Long.class)
                    .getSingleResult();
            Long totalSalas = em.createQuery("SELECT COUNT(s) FROM Sala s", Long.class)
                    .getSingleResult();
            
            System.out.println("Total Hospitales: " + totalHospitales);
            System.out.println("Total Médicos: " + totalMedicos);
            System.out.println("Total Pacientes: " + totalPacientes);
            System.out.println("Total Salas: " + totalSalas);

            System.out.println("\n=== ✓ SISTEMA EJECUTADO EXITOSAMENTE ===");

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}
