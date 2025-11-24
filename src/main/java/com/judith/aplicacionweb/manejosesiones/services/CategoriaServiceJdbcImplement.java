package com.judith.aplicacionweb.manejosesiones.services;

import com.judith.aplicacionweb.manejosesiones.models.Categoria;
import com.judith.aplicacionweb.manejosesiones.repositories.CategoriaRepositoryJdbcImplement;
import com.judith.aplicacionweb.manejosesiones.repositories.Repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CategoriaServiceJdbcImplement implements CategoriaService {

    // Se recomienda usar 'final' si solo se inicializa en el constructor
    private final Repository<Categoria> repositoryCategoriaJdbc;

    public CategoriaServiceJdbcImplement(Connection connection) {
        this.repositoryCategoriaJdbc = new CategoriaRepositoryJdbcImplement(connection);
    }

    @Override
    public List<Categoria> listarCategoria() {
        try {
            return repositoryCategoriaJdbc.listar();
        } catch (SQLException throwables) {
            // Se lanza ServiceJdbcException para forzar el rollback en el filtro
            throw new ServiceJdbcException(throwables.getMessage(), throwables);
        }
    }

    @Override
    public Optional<Categoria> porId(Long id) {
        try {
            // El repositorio ya devuelve Optional, se retorna directamente.
            return repositoryCategoriaJdbc.porId(id);
        } catch (SQLException throwables) {
            throw new ServiceJdbcException(throwables.getMessage(), throwables);
        }
    }

    @Override
    public void guardar(Categoria categoria) {
        try{
            repositoryCategoriaJdbc.guardar(categoria);
        } catch (SQLException throwables) {
            throw new ServiceJdbcException(throwables.getMessage(), throwables);
        }
    }

    @Override
    public void eliminar(Long id) {
        try{
            repositoryCategoriaJdbc.eliminar(id);
        } catch (SQLException throwables) {
            throw new ServiceJdbcException(throwables.getMessage(), throwables);
        }
    }
}