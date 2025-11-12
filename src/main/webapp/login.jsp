<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 12/11/2025
  Time: 15:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login Usuario</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container mt-5 col-md-4">
    <h2 class="text-center mb-4">Inicio de Sesión</h2>
    <form action="login" method="post" class="border p-4 bg-white shadow-sm rounded">
        <div class="mb-3">
            <label for="user" class="form-label">Usuario</label>
            <input type="text" id="user" name="user" class="form-control" required>
        </div>
        <div class="mb-3">
            <label for="password" class="form-label">Contraseña</label>
            <input type="password" id="password" name="password" class="form-control" required>
        </div>
        <div class="mb-2">
            <button type="submit" class="btn btn-primary w-100">Iniciar Sesión</button>
        </div>

        <div class="mt-3">
            <a href="index.html" class="btn btn-secondary w-100">Regresar al inicio</a>
        </div>
    </form>
</div>
</body>
</html>