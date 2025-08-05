// Em: src/main/java/br/com/todolist/models/Usuario.java
package br.com.todolist.models;

import org.mindrot.jbcrypt.BCrypt;

public class Usuario {
    private String nome;
    private String email;
    private String password;

    // Construtor vazio para a desserialização do Jackson
    public Usuario() {
    }

    public Usuario(String nome, String email, String password) {
        this.nome = nome;
        this.email = email;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    // CORREÇÃO: O setter agora apenas define o valor, sem hashear novamente.
    public void setPassword(String password) {
        this.password = password;
    }

}