package br.com.todolist.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CalculadoraDeTempo {

    public static long calcularDiasRestantes(LocalDate dataDoEvento) {
        LocalDate hoje = LocalDate.now();
        return ChronoUnit.DAYS.between(hoje, dataDoEvento);
    }
}