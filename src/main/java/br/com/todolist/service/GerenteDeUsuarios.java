package br.com.todolist.service;
import br.com.todolist.models.Usuario;
import br.com.todolist.persistence.GerenciadorDePersistenciaJson;


import java.lang.reflect.Type;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;
import com.google.gson.reflect.TypeToken;

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
        Type tipoListaDeUsuarios = new TypeToken<ArrayList<Usuario>>() {}.getType();
        List<Usuario> lista = persistencia.carregar(tipoListaDeUsuarios);
        return lista != null ? lista : new ArrayList<>();
    }


    private void salvarUsuarios() {
        persistencia.salvar(this.usuarios);
    }

    private String hashSenha(String senhaPura) {
        
        return BCrypt.hashpw(senhaPura, BCrypt.gensalt());
    }

    public boolean criarNovoUsuario(String nome, String email, String password) {
    if (buscarUsuarioPorEmail(email) != null) {
        return false;
    }
    
    String senhaHasheada = hashSenha(password);
    Usuario novoUsuario = new Usuario(nome, email, senhaHasheada);
    usuarios.add(novoUsuario);
    salvarUsuarios();
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