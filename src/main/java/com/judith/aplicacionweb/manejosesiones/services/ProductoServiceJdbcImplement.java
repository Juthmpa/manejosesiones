package com.judith.aplicacionweb.manejosesiones.services;


import com.judith.aplicacionweb.manejosesiones.repositories.Repository;
import com.judith.aplicacionweb.manejosesiones.models.Categoria;
import com.judith.aplicacionweb.manejosesiones.models.Producto;
import com.judith.aplicacionweb.manejosesiones.repositories.CategoriaRepositoryJdbcImplement;
import com.judith.aplicacionweb.manejosesiones.repositories.ProductoRepositoryJdbcImplement;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de Productos que utiliza la capa Repository
 * con JDBC. Encapsula las excepciones de SQL en una excepción de servicio.
 */
public class ProductoServiceJdbcImplement implements ProductoService {

    private Repository<Producto> repositoryJdbc;
    private Repository<Categoria> repositoryCategoriaJdbc;

    public ProductoServiceJdbcImplement(Connection connection) {
        this.repositoryJdbc = new ProductoRepositoryJdbcImplement(connection);
        this.repositoryCategoriaJdbc = new CategoriaRepositoryJdbcImplement(connection);
    }

    @Override
    public List<Producto> listar() {
        try {
            return repositoryJdbc.listar();
        } catch (SQLException throwables) {
            // Pasamos la excepción completa (throwables) como causa.
            throw new ServiceJdbcException(throwables.getMessage(), throwables);
        }
    }

    @Override
    public Optional<Producto> porId(Long id) {
        try {
            // El Repositorio debe devolver Optional<Producto>
            return repositoryJdbc.porId(id);
        } catch (SQLException throwables) {
            throw new ServiceJdbcException(throwables.getMessage(), throwables);
        }
    }

    @Override
    public void guardar(Producto producto) {
        try{
            repositoryJdbc.guardar(producto);
        } catch (SQLException throwables) {
            throw new ServiceJdbcException(throwables.getMessage(), throwables);
        }
    }

    @Override
    public void eliminar(Long id) {
        try{
            repositoryJdbc.eliminar(id);
        } catch (SQLException throwables) {
            throw new ServiceJdbcException(throwables.getMessage(), throwables);
        }
    }

    @Override
    public List<Categoria> listarCategoria(){
        try{
            return repositoryCategoriaJdbc.listar();
        } catch (SQLException throwables) {
            throw new ServiceJdbcException(throwables.getMessage(), throwables);
        }
    }

    @Override
    public Optional<Categoria> porIdCategoria(Long id) {
        try{
            // El Repositorio debe devolver Optional<Categoria>
            return repositoryCategoriaJdbc.porId(id);
        } catch (SQLException throwables) {
            throw new ServiceJdbcException(throwables.getMessage(), throwables);
        }
    }
}