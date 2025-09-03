package br.com.todolist.models;

import java.time.LocalDate;

public abstract class Itens {
    private String titulo;
    private String descricao;
    private String tipo;
    private String criado_por;
    private LocalDate dataCadastro;
    private LocalDate deadline;

    public Itens(String titulo, String descricao, String tipo, String criado_por, LocalDate deadline) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.tipo = tipo;
        this.criado_por = criado_por;
        this.deadline = deadline;
        this.dataCadastro = LocalDate.now();
    }

    public Itens() {
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCriado_por() {
        return criado_por;
    }

    public void setCriado_por(String criado_por) {
        this.criado_por = criado_por;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadLine(LocalDate deadline) {
        this.deadline = deadline;
    }
}