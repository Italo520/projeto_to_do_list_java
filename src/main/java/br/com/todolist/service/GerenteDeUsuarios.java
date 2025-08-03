package br.com.todolist.service;
import br.com.todolist.models.Usuario;
import br.com.todolist.persistence.GerenciadorDePersistenciaJson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

public class GerenteDeUsuarios {

    private static final String ARQUIVO_USUARIOS = "arquivos/usuarios.json";
    private GerenciadorDePersistenciaJson persistencia;
    private List<Usuario> usuarios;

    public GerenteDeUsuarios() {
        criarPastaDeArquivos(); 
        this.persistencia = new GerenciadorDePersistenciaJson(ARQUIVO_USUARIOS);
        this.usuarios = carregarUsuarios();
    }

    private void criarPastaDeArquivos() {
        File pasta = new File("arquivos");
        if (!pasta.exists()) {
            if (pasta.mkdir()) {
                System.out.println("Pasta 'arquivos' criada com sucesso.");
            } else {
                System.err.println("Erro ao criar a pasta 'arquivos'.");
            }
        }
    }

    private List<Usuario> carregarUsuarios() {
        List<Usuario> lista = (List<Usuario>) persistencia.carregar(ArrayList.class);
        return lista != null ? lista : new ArrayList<>();
    }

    private void salvarUsuarios() {
        persistencia.salvar(this.usuarios);
    }

    public boolean criarNovoUsuario(String nome, String email, String password) {
        if (buscarUsuarioPorEmail(email) != null) {
            System.err.println("Erro: Já existe um usuário com este email.");
            return false;
        }
        Usuario novoUsuario = new Usuario(nome, email, password);
        usuarios.add(novoUsuario);
        salvarUsuarios();
        System.out.println("Usuário " + nome + " criado com sucesso!");
        return true;
    }

    public Usuario autenticarUsuario(String email, String password) {
        Usuario usuario = buscarUsuarioPorEmail(email);
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
        for (Usuario usuario : usuarios) {
            if (usuario.getEmail().equals(email)) {
                return usuario;
            }
        }
        return null;
    }
}