package br.com.todolist.repository;

import java.lang.reflect.Type;
import java.util.List;

import br.com.todolist.entity.Usuario;

public interface Repositorio {
    void salvar(Object objeto);
    <T> List<T> carregar(Type tipoDeDados);
    Usuario buscarUsuarioPorEmail(String email);
}