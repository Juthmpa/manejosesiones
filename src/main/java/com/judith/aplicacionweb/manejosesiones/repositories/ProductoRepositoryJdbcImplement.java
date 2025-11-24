package com.judith.aplicacionweb.manejosesiones.repositories;

import com.judith.aplicacionweb.manejosesiones.models.Categoria;
import com.judith.aplicacionweb.manejosesiones.models.Producto;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación de la interfaz Repository para la entidad Producto,
 * utilizando JDBC para el acceso a la base de datos.
 */
public class ProductoRepositoryJdbcImplement implements Repository<Producto> {

    // Campo que almacena la conexión activa a la base de datos.
    private Connection conn;

    /**
     * Constructor que inicializa el repositorio con la conexión JDBC.
     * @param conn La conexión a la BBDD.
     */
    public ProductoRepositoryJdbcImplement(Connection conn) {
        this.conn = conn;
    }

    /**
     * Lista todos los productos activos de la base de datos.
     * @return Una lista de objetos Producto.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    @Override
    public List<Producto> listar() throws SQLException {
        // La clase crea la lista de productos.
        List<Producto> productos = new ArrayList<>();
        // La clase define la consulta SQL con JOIN a la tabla de categorías.
        String sql = " SELECT p.* , c.nombreCategoria as categoria\n" +
                "FROM producto as p INNER JOIN categoria as c ON (p.idCategoria=c.id)\n" +
                "ORDER BY p.id ASC;";

        // Se utiliza try-with-resources para asegurar el cierre de recursos.
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            // Mientras haya resultados.
            while (rs.next()) {
                // La clase mapea el resultado a un objeto Producto.
                Producto p = getProducto(rs);
                // La clase añade el producto a la lista.
                productos.add(p);
            }
        }
        return productos;
    }

    /**
     * Busca un producto por su ID en la base de datos.
     * @param id El ID del producto a buscar.
     * @return El objeto Producto si se encuentra, o null.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    @Override
    public Optional<Producto> porId(Long id) throws SQLException {
        // La clase inicializa el producto a null.
        Producto producto = null;
        // La clase define la consulta SQL con JOIN y filtro por ID.
        String sql = "SELECT p.*, c.nombreCategoria as categoria" +
                "FROM producto p " +
                "INNER JOIN categoria c ON p.idCategoria = c.id " +
                "WHERE p.id = ? "; //AND p.condicion = 1

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // La clase establece el ID en el primer marcador de posición.
            stmt.setLong(1, id);

            // La clase ejecuta la consulta.
            try (ResultSet rs = stmt.executeQuery()) {
                // Si se encuentra una fila.
                if (rs.next()) {
                    // La clase mapea el resultado a un objeto Producto.
                    producto = getProducto(rs);
                }
            }
        }
        return Optional.ofNullable(producto);
    }

    // Se omiten los métodos guardar y eliminar por brevedad y complejidad de mapeo,
    // pero deben implementarse usando PreparedStatement.

    @Override
    public void guardar(Producto producto) throws SQLException {
        String sql;
        if(producto.getId() != null && producto.getId() > 0){
            sql = " UPDATE producto set nombreProducto = ? , idCategoria = ?, stock = ?, precio = ?," +
                    " descripcion = ?, codigo = ?, fecha_elaboracion = ?, fecha_caducidad = ?" +
                    " WHERE id = ?;";
        } else {
            sql = " insert into producto (id, nombreProducto, idCategoria, stock,\n" +
                    " precio, descripcion, codigo, fecha_elaboracion,\n" +
                    " fecha_caducidad, condicion)\n" +
                    " VALUES(?, ?, ?, ?,\n" +
                    " ?, ?, ?, ?,\n" +
                    "       ?, 1)";
        }
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, producto.getNombreProducto());
            stmt.setInt(2, producto.getStock());
            stmt.setDouble(3, producto.getPrecio());
            stmt.setString(4, producto.getDescripcion());
            stmt.setString(5, producto.getCodigo());
            stmt.setLong(6, producto.getCategoria().getId());
            if(producto.getId() != null && producto.getId() > 0){
                stmt.setLong(7, producto.getId());
            } else{
                stmt.setDate(8, Date.valueOf(producto.getFechaElaboracion()));
                stmt.setDate(9, Date.valueOf(producto.getFechaCaducidad()));
            }
            stmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(Long id) throws SQLException {
        String sql = "DELETE FROM producto WHERE id = ?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    // Métodos Adicionales de la Interfaz Repository que la clase implementa

    @Override
    public void desactivar(Long id) throws SQLException {
        // Lógica de desactivación (condicion = 0)
    }

    @Override
    public void activar(Long id) throws SQLException {
        // Lógica de activación (estado = 1)
    }

    /**
     * Método auxiliar privado para mapear un ResultSet a un objeto Producto.
     * @param rs El ResultSet de la consulta.
     * @return El objeto Producto creado.
     * @throws SQLException Si ocurre un error al leer del ResultSet.
     */
    private Producto getProducto(ResultSet rs) throws SQLException {
        // La clase crea un nuevo objeto de tipo Producto.
        Producto p = new Producto();

        // La clase establece los atributos del Producto.
        p.setId(rs.getLong("id"));
        p.setNombreProducto(rs.getString("nombreProducto"));
        p.setPrecio(rs.getDouble("precio"));
        p.setStock(rs.getInt("stock"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setCodigo(rs.getString("codigo"));

        // La clase maneja el mapeo de fechas, verificando si son nulas en la BBDD.
        Date fechaElaboracion = rs.getDate("fecha_elaboracion");
        p.setFechaElaboracion(fechaElaboracion != null ? fechaElaboracion.toLocalDate() : null);

        Date fechaCaducidad = rs.getDate("fecha_caducidad");
        p.setFechaCaducidad(fechaCaducidad != null ? fechaCaducidad.toLocalDate() : null);

        // La clase mapea el condicion
        p.setCondicion(rs.getInt("condicion"));

        // La clase crea y setea el objeto Categoria asociado (Relación).
        Categoria c = new Categoria();
        c.setId(rs.getLong("idCategoria"));
        c.setNombreCategoria(rs.getString("categoria")); // Obtenido del JOIN
        p.setCategoria(c);

        // La clase retorna el objeto Producto.
        return p;
    }
}