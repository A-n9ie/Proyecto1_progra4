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



}