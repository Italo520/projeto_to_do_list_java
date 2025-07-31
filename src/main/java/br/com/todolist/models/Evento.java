package br.com.todolist.models;

import java.time.LocalDate;

public class Evento {
    private String titulo;
    private String descricao;
    private LocalDate dataCadastro;
    private LocalDate dataEvento;

    public Evento(String titulo, String descricao, LocalDate dataCadastro, LocalDate dataEvento) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataCadastro = LocalDate.now();
        this.dataEvento = dataEvento;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public LocalDate getDataEvento() {
        return dataEvento;
    }

    public void setDataEvento(LocalDate dataEvento) {
        this.dataEvento = dataEvento;
    }

    @Override
    public String toString() {
        return "Evento [titulo=" + titulo + ", dataEvento=" + dataEvento + "]";
    }



    
}

