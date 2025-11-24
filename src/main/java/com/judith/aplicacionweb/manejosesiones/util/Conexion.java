package com.judith.aplicacionweb.manejosesiones.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * Clase de utilidad para establecer y gestionar la conexión a la base de datos.
 * Utiliza JDBC para obtener una nueva conexión.
 */
public class Conexion {
    //Declaramos e inicializamos tres variables
    // privadas para la conexión
    private static String url = "jdbc:mysql://localhost:3306/syscompraventa" ;
    private static String username = "root" ;
    private static String password = "" ;

    /* Implementamos un métodos de tipo Connection para obtenerl
     * la conexión mediante las variables que inicializamos
     * patron de diseño singleton, solo esta clase la puede usar
     */
    public static Connection getConnection()
            throws SQLException {
        // La clase obtiene la conexión utilizando las constantes de URL, usuario y contraseña.
        return DriverManager.getConnection(url,username,password);
    }
    /*
    public static void main(String[] args) {
        // Bloque try-with-resources para asegurar que la conexión se cierre automáticamente
        try (Connection con = getConnection()) {
            if (con != null) {
                System.out.println(" ¡Conexión exitosa a la base de datos!");
            } else {
                // Esta parte rara vez se ejecuta si no hay excepción, pero es una buena práctica.
                System.out.println(" No se pudo establecer la conexión.");
            }
        } catch (SQLException e) {
            // Captura cualquier excepción de SQL (ej. credenciales incorrectas, BD no disponible, URL mal formada)
            System.err.println(" Error de conexión a la base de datos:");
            System.err.println("Código de error: " + e.getErrorCode());
            System.err.println("Mensaje: " + e.getMessage());
            // Opcional: e.printStackTrace(); para ver la traza completa
        }
    }*/
}
