package br.com.todolist.models;

public class Usuario {
    private String nome;
    private String email;


    
    public Usuario(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }



    public String getNome() {
        return nome;
    }



    public void setNome(String nome) {
        this.nome = nome;
    }



    public String getEmail() {
        return email;
    }



    public void setEmail(String email) {
        this.email = email;
    }
    
    

    
}
