package br.com.todolist.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.lang.reflect.Type;
import java.util.List;

public class RepositorioDB implements Repositorio {

    @Override
    public void salvar(Object objeto) {
        EntityManager entityManager = DatabaseManager.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(objeto);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public <T> List<T> carregar(Type tipoDeDados) {
        EntityManager entityManager = DatabaseManager.getEntityManagerFactory().createEntityManager();
        try {
            Class<T> clazz = (Class<T>) Class.forName(tipoDeDados.getTypeName());
            return entityManager.createQuery("FROM " + clazz.getSimpleName(), clazz).getResultList();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public br.com.todolist.entity.Usuario buscarUsuarioPorEmail(String email) {
        EntityManager entityManager = DatabaseManager.getEntityManagerFactory().createEntityManager();
        try {
            return entityManager.createQuery("FROM Usuario WHERE email = :email", br.com.todolist.entity.Usuario.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        } finally {
            entityManager.close();
        }
    }
}