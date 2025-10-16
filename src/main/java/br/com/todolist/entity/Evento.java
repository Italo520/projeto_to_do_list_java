package br.com.todolist.entity;

import java.time.LocalDate;

public class Evento extends Itens {

    public Evento(String titulo, String descricao, String criado_por, LocalDate deadline) {
        super(titulo, descricao, "Evento", criado_por, deadline);
    }

    public Evento() {
    }

    public String toString() {
        return getTitulo();
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