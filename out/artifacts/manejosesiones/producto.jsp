<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 24/11/2025
  Time: 08:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Listado de Productos</title>
</head>
<body>
<h1>Listado de Productos</h1>

<p><a href="<%= request.getContextPath() %>/producto/form">
    <button type="button">Ingresar Nuevo Producto</button>
</a></p>

<c:choose>
    <c:when test="${not empty productos}">
        <table border="1">
            <thead>
            <tr>
                <th>ID</th>
                <th>Nombre</th>
                <th>Código</th>
                <th>Precio</th>
                <th>Stock</th>
                <th>Categoría</th>
                <th>Fecha Elaboración</th>
                <th>Acciones</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${productos}" var="producto">
                <tr>
                    <td>${producto.id}</td>
                    <td>${producto.nombreProducto}</td>
                    <td>${producto.codigo}</td>
                    <td><fmt:formatNumber value="${producto.precio}" type="currency" currencySymbol="$" /></td>
                    <td>${producto.stock}</td>
                    <td>${producto.categoria.nombreCategoria}</td>
                    <td>${producto.fechaElaboracion}</td>
                    <td>
                        <a href="<%= request.getContextPath() %>/producto/editar?id=${producto.id}">Editar</a>
                        <a href="<%= request.getContextPath() %>/producto/eliminar?id=${producto.id}" onclick="return confirm('¿Seguro de eliminar?');">Eliminar</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:when>
    <c:otherwise>
        <p>No hay productos registrados.</p>
    </c:otherwise>
</c:choose>
</body>
</html>
