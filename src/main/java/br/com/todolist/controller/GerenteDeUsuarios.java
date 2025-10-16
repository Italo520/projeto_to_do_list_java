package br.com.todolist.controller;

import br.com.todolist.entity.Usuario;
import br.com.todolist.repository.Repositorio;
import org.mindrot.jbcrypt.BCrypt;

public class GerenteDeUsuarios {

    private Repositorio repositorio;

    public GerenteDeUsuarios(Repositorio repositorio) {
        this.repositorio = repositorio;
    }

    private String hashSenha(String senhaPura) {
        return BCrypt.hashpw(senhaPura, BCrypt.gensalt());
    }

    public boolean criarNovoUsuario(String nome, String email, String password) {
        if (repositorio.buscarUsuarioPorEmail(email) != null) {
            return false;
        }

        String senhaHasheada = hashSenha(password);
        Usuario novoUsuario = new Usuario(nome, email, senhaHasheada);
        repositorio.salvar(novoUsuario);
        return true;
    }

    public Usuario autenticarUsuario(String email, String password) {
        Usuario usuario = repositorio.buscarUsuarioPorEmail(email);
        if (usuario != null && verificarSenha(password, usuario.getPassword())) {
            System.out.println("Autenticação bem-sucedida para " + usuario.getNome());
            return usuario;
        }

        System.err.println("Erro: Email ou senha incorretos.");
        return null;
    }

    public boolean verificarSenha(String senhaFornecida, String hashSalvo) {
        return BCrypt.checkpw(senhaFornecida, hashSalvo);
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        return repositorio.buscarUsuarioPorEmail(email);
    }
}