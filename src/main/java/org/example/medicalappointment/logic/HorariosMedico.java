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

        public static LocalDate obtenerFechaDiaSemana(String diaSemana) {
            // Obtener la fecha actual
            LocalDate hoy = LocalDate.now();

            // Obtener el número del día actual
            int diaActual = hoy.getDayOfWeek().getValue(); // 1 = Lunes, 7 = Domingo

            // Convertir el nombre del día en el valor correspondiente
            DayOfWeek diaDeseado = DayOfWeek.valueOf(diaSemana.toUpperCase());

            // Obtener el número del día deseado (1 = Lunes, 7 = Domingo)
            int diaDeseadoValor = diaDeseado.getValue();

            // Calcular los días que faltan para el día deseado
            int diasDiferencia = diaDeseadoValor - diaActual;

            // Si el día deseado es en la semana siguiente, ajustamos la diferencia
            if (diasDiferencia <= 0) {
                diasDiferencia += 7;
            }

            // Calcular la fecha del día deseado
            LocalDate fechaDeseada = hoy.plusDays(diasDiferencia);
            return fechaDeseada;
        }


}