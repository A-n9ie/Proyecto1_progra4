package org.example.medicalappointment.logic;

import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Entity
@Table(name = "horarios_medicos")
public class HorariosMedico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id")
    private Medico medico;

    @Lob
    @Column(name = "dia")
    private String dia;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    @Override
    public String toString() {
        return "HorariosMedico{" +
                "id=" + id +
                ", medico=" + medico +
                ", dia='" + dia + '\'' +
                '}';
    }

    public int obtenerDiaDeLaSemana() {
        try {
            DayOfWeek dayOfWeek = DayOfWeek.valueOf(dia.toUpperCase());
            return dayOfWeek.getValue();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Día inválido: " + dia);
        }
    }

    public static LocalDate obtenerFechaDiaSemana(String diaSemana) {

            LocalDate hoy = LocalDate.now();
            int diaActual = hoy.getDayOfWeek().getValue();
            DayOfWeek diaDeseado = DayOfWeek.valueOf(diaSemana.toUpperCase());
            int diaDeseadoValor = diaDeseado.getValue();
            int diasDiferencia = diaDeseadoValor - diaActual;
            if (diasDiferencia <= 0) {
                diasDiferencia += 7;
            }
            LocalDate fechaDeseada = hoy.plusDays(diasDiferencia);
            return fechaDeseada;
        }

        public LocalDate getDiaDeLaSemanaDisponible(){
        return obtenerFechaDiaSemana(dia);
        }


}