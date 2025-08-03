package br.com.todolist.models;

import java.time.LocalDate;

public abstract class Itens{
    private String titulo;
    private String descricao;
    private LocalDate dataCadastro;
    private LocalDate deadline;

    public Itens(String titulo, String descricao, LocalDate deadline) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.deadline = deadline;
        this.dataCadastro = LocalDate.now();
    }

    public String getTitulo(){
        return titulo;
    }

    public String getDescricao(){
        return descricao;
    }

    public LocalDate getDataCadastro(){
        return dataCadastro;
    }

    public LocalDate getDeadline(){
        return deadline;
    }

    public void setTitulo(String titulo){
        this.titulo = titulo;
    }

    public void setDescricao(String descricao){
        this.descricao = descricao;
    }

    public void setDataCadastro(LocalDate dataCadastro){
        this.dataCadastro = dataCadastro;
    }

    public void setDeadLine(LocalDate deadline){
        this.deadline = deadline;
    }
}