# Servicio gRPC - Gestión de Clientes (Java)

## 📋 Descripción

Servicio RPC (Remote Procedure Call) implementado con **gRPC** y **Protocol Buffers** para gestión de clientes. Permite realizar operaciones CRUD sobre una base de datos MySQL mediante llamadas a procedimientos remotos con serialización binaria eficiente.

## 🏗️ Arquitectura

- **Protocolo**: gRPC sobre HTTP/2
- **Serialización**: Protocol Buffers (Protobuf)
- **Puerto**: 5001
- **Base de Datos**: MySQL 8.0
- **Lenguaje**: Java 11+
- **Build Tool**: Maven

## 📁 Estructura del Proyecto

```
RPC-Java/
├── pom.xml                          # Configuración Maven con dependencias gRPC
├── src/main/
│   ├── proto/
│   │   └── cliente.proto            # Definición de servicios y mensajes Protobuf
│   └── java/ec/universidad/grpc/
│       ├── models/
│       │   └── Cliente.java         # Modelo de datos
│       ├── repository/
│       │   └── ClienteRepository.java  # Acceso a datos MySQL
│       ├── service/
│       │   └── ClienteServiceImpl.java # Implementación del servicio gRPC
│       ├── server/
│       │   └── GrpcServer.java      # Servidor gRPC
│       └── client/
│           └── GrpcClient.java      # Cliente de pruebas
```

## 🚀 Instalación y Ejecución

### Prerrequisitos

- Java 11 o superior
- MySQL 8.0 ejecutándose en localhost
- IntelliJ IDEA (o Maven instalado)

### Configuración de Base de Datos

Asegúrate de tener la base de datos `gestion_clientes` con la tabla `clientes`:

```sql
CREATE DATABASE IF NOT EXISTS gestion_clientes;
USE gestion_clientes;

CREATE TABLE clientes (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    cedula VARCHAR(20) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    email VARCHAR(100),
    direccion VARCHAR(200),
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

### Compilar el Proyecto

**En IntelliJ IDEA:**

1. Abre el proyecto `RPC-Java`
2. Maven descargará las dependencias automáticamente
3. El plugin de Protobuf generará las clases Java desde `cliente.proto`

**Desde terminal (si tienes Maven instalado):**

```bash
cd RPC-Java
mvn clean compile
```

### Ejecutar el Servidor

**En IntelliJ IDEA:**

1. Abre `GrpcServer.java`
2. Clic derecho → `Run 'GrpcServer.main()'`

Salida esperada:

```
╔═══════════════════════════════════════════════════╗
║   SERVIDOR gRPC - GESTIÓN DE CLIENTES            ║
║   Remote Procedure Call sobre HTTP/2             ║
╚═══════════════════════════════════════════════════╝

✓ Servidor gRPC iniciado exitosamente
✓ Puerto: 5001
✓ Protocolo: gRPC (HTTP/2 + Protocol Buffers)

Esperando solicitudes de clientes...
```

### Ejecutar el Cliente de Pruebas

**En IntelliJ IDEA:**

1. Asegúrate de que el servidor esté ejecutándose
2. Abre `GrpcClient.java`
3. Clic derecho → `Run 'GrpcClient.main()'`

El cliente probará automáticamente todas las operaciones CRUD.

## 📡 Operaciones Disponibles

El servicio gRPC expone las siguientes operaciones:

| Operación                 | Descripción                             |
| ------------------------- | --------------------------------------- |
| `Ping`                    | Verificar que el servicio está activo   |
| `CrearCliente`            | Crear un nuevo cliente                  |
| `ObtenerTodosClientes`    | Obtener lista de todos los clientes     |
| `ObtenerClientePorId`     | Buscar cliente por ID                   |
| `ObtenerClientePorCedula` | Buscar cliente por cédula               |
| `ActualizarCliente`       | Modificar datos de un cliente existente |
| `EliminarCliente`         | Eliminar un cliente por ID              |

## 🔧 Tecnologías Utilizadas

- **gRPC 1.58.0**: Framework RPC de alto rendimiento
- **Protocol Buffers 3.24.0**: Serialización binaria
- **MySQL Connector/J 8.0.33**: Driver JDBC para MySQL
- **Maven**: Gestión de dependencias y build

## 🎯 Ventajas de gRPC

✅ **Alto rendimiento**: HTTP/2 + serialización binaria  
✅ **Tipado fuerte**: Contratos definidos en `.proto`  
✅ **Streaming**: Soporte para streaming bidireccional  
✅ **Multiplexación**: Múltiples llamadas en una conexión  
✅ **Generación de código**: Clientes en múltiples lenguajes

## 📝 Notas

- El servidor escucha en `localhost:5001`
- La cadena de conexión MySQL está en `GrpcServer.java` línea 18
- Los archivos generados por Protobuf están en `target/generated-sources/protobuf/`
- Para detener el servidor: Presiona `Ctrl+C` en IntelliJ

## 🐛 Solución de Problemas

**Error: "Failed to bind to address"**

- Verifica que el puerto 5001 no esté en uso
- Cambia `PORT` en `GrpcServer.java` si es necesario

**Error: "UNAVAILABLE: io exception"**

- Asegúrate de que el servidor esté ejecutándose antes de ejecutar el cliente

**Error de conexión MySQL**

- Verifica que MySQL esté ejecutándose
- Confirma la contraseña en la cadena de conexión
