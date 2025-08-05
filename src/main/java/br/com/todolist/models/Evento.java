package br.com.todolist.models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Evento extends Itens {

    public Evento(String titulo, String descricao, String criado_por, LocalDate deadline) {
        super(titulo, descricao, "Evento", criado_por, deadline);
    }

    public Evento() {
    }

    public String toString() {
        long diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), getDeadline());

        if (diasRestantes > 0) {
            return getTitulo() + " (Faltam " + diasRestantes + " dias)";
        } else if (diasRestantes == 0) {
            return getTitulo() + " (É hoje!)";
        } else {
            return getTitulo() + " (Atrasado)";
        }
    }

    public String getTitulo() {
        return super.getTitulo();
    }

    public void setTitulo(String titulo) {
        super.setTitulo(titulo);
    }

    public String getDescricao() {
        return super.getDescricao();
    }

    public void setDescricao(String descricao) {
        super.setDescricao(descricao);
    }

    public LocalDate getDataCadastro() {
        return super.getDataCadastro();
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        super.setDataCadastro(dataCadastro);
    }

    public LocalDate getDeadline() {
        return super.getDeadline();
    }

    public void setDeadLine(LocalDate deadline) {
        super.setDeadLine(deadline);
    }

}