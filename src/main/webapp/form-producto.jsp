<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 21/11/2025
  Time: 18:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, java.time.format.*, com.judith.aplicacionweb.manejosesiones.models.*" %>


<% List<Categoria> categorias = (List<Categoria>) request.getAttribute("categorias");
    Map<String, String> errores = (Map<String, String>) request.getAttribute("errores");
    Producto producto = (Producto) request.getAttribute("producto");
    String fechaElaboracion = producto.getFechaElaboracion()!=null?
            producto.getFechaElaboracion().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")):"";
    String fechaCaducidad = producto.getFechaCaducidad() != null?
            producto.getFechaCaducidad().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")):"";
%>
<html>
<head>
    <title> Formulario Productos</title>
</head>
<body>
<form action="<%= request.getContextPath() %>/producto/form" method="post">
    <div>
        <label for="nombre">Nombre:</label>
        <div>
            <input type="text" id="name" name="nombre">
        </div>
        <% if (errores !=null && errores.containsKey("nombre")){ %>
        <div style = "color: red"><%=errores.get("nombre") %>
        </div>
        <% } %>
    </div>

    <div>
        <label for="categoria">Categoría:</label>
        <div>
            <select id = "categoria" name="categoriaId" >
                <option value = ""> ---------- Seleccionar --------</option>
                <% for (Categoria c: categorias) { %>
                <option value = "<%= c.getId() %>"
                <%=producto.getCategoria() != null && c.getId().equals(producto.getCategoria().getId()) ?"selected":"" %>
                <%=c.getNombreCategoria() %>
                </option>
                <%}%>
            </select>
        </div>
        <% if (errores !=null && errores.containsKey("categoria")) { %>
        <div style = "color: red"><%=errores.get("nombre") %>
        </div>
        <%}%>
    </div>

    <div>
        <label for="stock">Ingrese el stock</label>
        <div>
            <input type="number" id="stock" name="stock">
        </div>
        <% if (errores !=null && errores.containsKey("stock")){ %>
        <div style = "color: red"><%=errores.get("stock") %>
        </div>
        <% } %>
    </div>

    <div>
        <label for="precio">Precio:</label>
        <div>
            <input type="number" id="name" name="stock">
        </div>
        <% if (errores !=null && errores.containsKey("stock")){ %>
        <div style = "color: red"><%=errores.get("stock") %>
        </div>
        <% } %>
    </div>

    <div>
        <label for="descripcion">Descripción:</label>
        <div>
            <textarea id="descripcion" name="descripcion" cols="30" rows="10">
            </textarea>
        </div>
    </div>

    <div>
        <label for="codigo">Código:</label>
        <div>
            <input type="text" id="codigo" name="codigo">
        </div>
        <% if (errores !=null && errores.containsKey("nombre")){ %>
        <div style = "color: red"><%=errores.get("nombre") %>
        </div>
        <% } %>
    </div>

    <div>
        <label for="fecha_elaboriacion">fecha de Elaboración:</label>
        <div>
            <input type="date" id="fecha_elaboracion" name="fecha_elaboracion">
        </div>
        <% if (errores !=null && errores.containsKey("nombre")){ %>
        <div style = "color: red"><%=errores.get("nombre") %>
        </div>
        <% } %>
    </div>

    <div>
        <label for="fecha_caducidad">Fecha Caducidad:</label>
        <div>
            <input type="date" id="fecha_caducidad" name="fecha_caducidad">
        </div>
        <% if (errores !=null && errores.containsKey("nombre")){ %>
        <div style = "color: red"><%=errores.get("nombre") %>
        </div>
        <% } %>
    </div>

    <div class="form-group">
        <button type="submit" class="btn-primary">Guardar Producto</button>
    </div>
</form>

</body>
</html>
