package com.judith.aplicacionweb.manejosesiones.filter;

import com.judith.aplicacionweb.manejosesiones.services.ServiceJdbcException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import com.judith.aplicacionweb.manejosesiones.util.Conexion;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/* Implementamos una anotación, que sirve para poder
 * utilizar la conexión en cualquier parte de mi
 * aplicación
 */
@WebFilter ("/*")
public class ConexionFilter implements Filter {
    /* Una clase filter en java es un objeto que realiza tareas de filtrado
     * en las solicitudes de petición y respuesta a un recurso
     * Los filtros se pueden ejecutar de manera dinámica para transformar la
     * información que contiene.
     * El filtrado se realiza mediante el
     * métodos doFilter()
     */

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        /*
         * request: petición del cliente
         * response: respuesta del servidor
         * chain: Es una clase de filtro que representa el
         * flujo de procesamiento, llama al métodos chain.doFilter(request, response)
         * dentro de un filtro pasa la solicitudal siguiente filtro al
         * recurso destino (Servlet, jsp, pdf u otro)
         */

        // Llamamos a la conexión
        try (Connection connection = Conexion.getConnection()){
            // Verificamos que la conexión no se realice automáticamente
            if(connection.getAutoCommit()){
                // Cambia a una conexión manual
                connection.setAutoCommit(false);
            }
            try{
                /* Agregamos la conección como un atributo en la solicitud
                 * Esto nos permite que otros componentes como servlet o DAOs
                 * puedan acceder a la conexión */
                request.setAttribute("conn", connection);
                // Pasa la solicitud y la respuesta al siguiente filtro o al recurso destino
                chain.doFilter(request, response);
                /* Si el procesamiento se realizó correctamente sin lanzar excepciones
                 * se confirma la solicitud, y se aplica todos los cambios a la base de datos
                 */
                connection.commit();
                /* Si ocurre algún error durante el procesamiento (dentro del doFilter),
                 * se captura la excepción
                 */
            } catch (SQLException | ServiceJdbcException e) {
                // Se usa este métodos en caso de que exista un error y la base de datos no se cambie
                connection.rollback();
                /* Se envía un código de error Http 500 al cliente
                 * indicando un problema interno del servicio*/
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        e.getMessage());
                e.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}