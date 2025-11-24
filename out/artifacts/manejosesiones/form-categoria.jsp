<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 23/11/2025
  Time: 19:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, com.judith.aplicacionweb.manejosesiones.models.Categoria" %>

<%
    // Recuperar el mapa de errores y el objeto Categoria (para edición/pre-llenado)
    Map<String, String> errores = (Map<String, String>) request.getAttribute("errores");
    Categoria categoria = (Categoria) request.getAttribute("categoria");
%>

<html>
<head>
    <title>Formulario Categoría</title>
</head>
<body>

<h1><%= categoria != null && categoria.getId() != null ? "Editar Categoría" : "Crear Nueva Categoría" %></h1>

<form action="<%= request.getContextPath() %>/categoria/form" method="post">

    <%-- Campo Oculto para el ID (Necesario para la edición) --%>
    <% if (categoria != null && categoria.getId() != null) { %>
        <input type="hidden" name="id" value="<%= categoria.getId() %>">
    <% } %>

    <div>
        <label for="nombre">Nombre de la Categoría:</label>
    <div>
    <input type="text" id="nombre" name="nombre" value="<%= categoria != null ? categoria.getNombreCategoria() : "" %>"
                       placeholder="Ej: Electrónica, Computación, etc">
    </div>

    <%-- Manejo de Errores para el campo nombre --%>
    <% if (errores !=null && errores.containsKey("nombre")){ %>
    <div style = "color: red">
    <%=errores.get("nombre") %>
    </div>
    <% } %>
    </div>

    <div>
    <label for="descripcion">Descripción:</label>
    <div>
    <textarea id="descripcion" name="descripcion" cols="40" rows="5"><%= categoria != null && categoria.getDescripcion() != null ? categoria.getDescripcion() : "" %></textarea>
    </div>
    <%-- Manejo de Errores para el campo descripcion --%>
    <% if (errores != null && errores.containsKey("descripcion")){ %>
    <div style = "color: red">
        <%=errores.get("descripcion") %>
    </div>
    <% } %>
    </div>

    <div>
    <label for="condicion">Estado (Condición):</label>
    <div>
    <select id="condicion" name="condicion">
    <option value="">-- Seleccionar Estado --</option>
    <option value="1"
        <%= categoria != null && categoria.getCondicion() != null
                && categoria.getCondicion().equals(1) ? "selected" : "" %>>
    Activo (1)
</option>
    <option value="0"
            <%= categoria != null && categoria.getCondicion() != null
                    && categoria.getCondicion().equals(0) ? "selected" : "" %>>
        Inactivo (0)
    </option>
    </select>
    </div>
        <%-- Manejo de Errores para el campo condicion --%>
        <% if (errores != null && errores.containsKey("condicion")){ %>
        <div style = "color: red">
            <%=errores.get("condicion") %>
        </div>
        <% } %>
    </div>

    <div>
        <br>
        <input type="submit" value="Guardar Categoría">
    </div>

    
</form>

</body>
</html>