package br.com.todolist.repository;

import java.lang.reflect.Type;

public interface Repositorio {
    void salvar(Object objeto);
    <T> T carregar(Type tipoDeDados);
}