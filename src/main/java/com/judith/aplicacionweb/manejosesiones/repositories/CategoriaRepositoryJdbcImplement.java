package com.judith.aplicacionweb.manejosesiones.repositories;

import com.judith.aplicacionweb.manejosesiones.models.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación de la interfaz Repository para la entidad Categoria,
 * utilizando JDBC para la persistencia de datos.
 */
public class CategoriaRepositoryJdbcImplement implements Repository<Categoria>{

    // Campo que almacena la conexión activa a la base de datos.
    private Connection conn;

    /**
     * Constructor que inicializa el repositorio con la conexión JDBC.
     * @param conn La conexión a la BBDD.
     */
    public CategoriaRepositoryJdbcImplement(Connection conn) {
        this.conn = conn;
    }

    /**
     * Lista todas las categorías activas (condicion = 1) de la base de datos.
     * @return Una lista de objetos Categoria.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    @Override
    public List<Categoria> listar() throws SQLException {
        // La clase crea la lista de categorías.
        List<Categoria> categorias = new ArrayList<>();
        // La clase define la consulta SQL para obtener categorías activas (condicion = 1).
        String sql = "SELECT * FROM categoria WHERE condicion = 1 ORDER BY id ASC";

        // Se utiliza try-with-resources.
        try(Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            // Mientras haya resultados.
            while(rs.next()){
                // La clase mapea el resultado a un objeto Categoria.
                Categoria categoria = getCategoria(rs);
                // La clase añade la categoría a la lista.
                categorias.add(categoria);
            }
        }
        return categorias;
    }

    /**
     * Busca una categoría por su ID en la base de datos, solo si está activa (condicion = 1).
     * @param id El ID de la categoría a buscar.
     * @return El objeto Categoria si se encuentra y está activa, o null.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    @Override
    public Optional<Categoria> porId(Long id) throws SQLException {
        // La clase inicializa la categoría a null.
        Categoria categoria = null;
        // La clase define la consulta SQL con filtro por ID y condicion activa.
        String sql = "SELECT * FROM categoria WHERE id = ? AND condicion = 1";

        // Se utiliza try-with-resources para PreparedStatement.
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // La clase establece el ID.
            stmt.setLong(1, id);

            // La clase ejecuta la consulta.
            try (ResultSet rs = stmt.executeQuery()) {
                // Si se encuentra una fila.
                if (rs.next()) {
                    // La clase mapea el resultado a un objeto Categoria.
                    categoria = getCategoria(rs);
                }
            }
        }
        return Optional.ofNullable(categoria);
    }

    /**
     * Guarda una nueva categoría o actualiza una existente.
     * @param categoria El objeto Categoria a guardar.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    @Override
    public void guardar(Categoria categoria) throws SQLException {
        // La clase determina la sentencia SQL (INSERT o UPDATE).
        String sql = null;

        // La clase establece la condición por defecto si es nula (para nuevos registros).
        int condicion = (categoria.getCondicion() != null) ? categoria.getCondicion() : 1;

        if (categoria.getId() != null && categoria.getId() > 0) {
            // Actualización
            sql = "UPDATE categoria SET nombreCategoria = ?, descripcion = ?, condicion = ? WHERE id = ?";
        } else {
            // Inserción
            sql = "INSERT INTO categoria(nombreCategoria, descripcion, condicion) VALUES(?, ?, ?)";
        }

        // Se utiliza try-with-resources para PreparedStatement.
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            // La clase establece los valores para la sentencia SQL.
            stmt.setString(1, categoria.getNombreCategoria());
            stmt.setString(2, categoria.getDescripcion());
            stmt.setInt(3, condicion); // Usar la condición determinada.

            // Si es una actualización, la clase añade el ID como último parámetro.
            if (categoria.getId() != null && categoria.getId() > 0) {
                stmt.setLong(4, categoria.getId());
            }

            // La clase ejecuta la sentencia.
            stmt.executeUpdate();
        }
    }

    /**
     * Implementa el borrado lógico de una categoría, estableciendo su condición a 0.
     * @param id El ID de la categoría a eliminar.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    @Override
    public void eliminar(Long id) throws SQLException {
        // La clase delega la tarea de eliminación lógica al método desactivar.
        desactivar(id);
    }

    /**
     * Desactiva (pone condicion a 0) una categoría por su ID.
     * @param id El ID de la categoría a desactivar.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    @Override
    public void desactivar(Long id) throws SQLException {
        // La clase define la consulta para cambiar la condición a 0.
        String sql = "UPDATE categoria SET condicion = 0 WHERE id = ?";

        // Se utiliza try-with-resources.
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            // La clase establece el ID.
            stmt.setLong(1, id);
            // La clase ejecuta la actualización.
            stmt.executeUpdate();
        }
    }

    /**
     * Activa (pone condicion a 1) una categoría por su ID.
     * @param id El ID de la categoría a activar.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    @Override
    public void activar(Long id) throws SQLException {
        // La clase define la consulta para cambiar la condición a 1.
        String sql = "UPDATE categoria SET condicion = 1 WHERE id = ?";

        // Se utiliza try-with-resources.
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            // La clase establece el ID.
            stmt.setLong(1, id);
            // La clase ejecuta la actualización.
            stmt.executeUpdate();
        }
    }

    /**
     * Método auxiliar privado para mapear un ResultSet a un objeto Categoria.
     * @param rs El ResultSet de la consulta.
     * @return El objeto Categoria creado.
     * @throws SQLException Si ocurre un error al leer del ResultSet.
     */
    private Categoria getCategoria(ResultSet rs) throws SQLException {
        // La clase crea un nuevo objeto de tipo Categoria.
        Categoria categoria = new Categoria();

        // La clase establece los atributos del objeto con los datos del ResultSet.
        categoria.setId(rs.getLong("id"));
        categoria.setNombreCategoria(rs.getString("nombreCategoria"));
        categoria.setDescripcion(rs.getString("descripcion"));
        categoria.setCondicion(rs.getInt("condicion"));

        // La clase retorna el objeto Categoria.
        return categoria;
    }
}