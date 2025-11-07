# Servicio Socket TCP - Gestión de Clientes (Java)

## 📋 Descripción

Servicio de comunicación distribuida basado en **Sockets TCP/IP** con protocolo JSON para gestión de clientes. Implementa comunicación de bajo nivel en la capa de transporte, permitiendo operaciones CRUD mediante un protocolo personalizado.

## 🏗️ Arquitectura

- **Protocolo**: TCP/IP (Capa de Transporte)
- **Formato de Datos**: JSON
- **Puerto**: 5002
- **Base de Datos**: MySQL 8.0
- **Lenguaje**: Java 11+
- **Build Tool**: Maven

## 📁 Estructura del Proyecto

```
Sockets-Java/
├── pom.xml                          # Configuración Maven con Gson
├── src/main/java/ec/universidad/sockets/
│   ├── models/
│   │   ├── Cliente.java             # Modelo de entidad
│   │   ├── SocketRequest.java       # Mensaje de solicitud
│   │   └── SocketResponse.java      # Mensaje de respuesta
│   ├── repository/
│   │   └── ClienteRepository.java   # Acceso a datos MySQL
│   ├── server/
│   │   └── SocketServer.java        # Servidor TCP
│   └── client/
│       └── SocketClient.java        # Cliente de pruebas
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

1. Abre el proyecto `Sockets-Java`
2. Maven descargará las dependencias automáticamente

**Desde terminal (si tienes Maven instalado):**

```bash
cd Sockets-Java
mvn clean compile
```

### Ejecutar el Servidor

**En IntelliJ IDEA:**

1. Abre `SocketServer.java`
2. Clic derecho → `Run 'SocketServer.main()'`

Salida esperada:

```
╔═══════════════════════════════════════════════════╗
║   SERVIDOR SOCKET - GESTIÓN DE CLIENTES          ║
║   Comunicación TCP/IP con protocolo JSON         ║
╚═══════════════════════════════════════════════════╝

✓ Servidor Socket iniciado exitosamente
✓ Puerto: 5002
✓ Protocolo: TCP/IP + JSON

Esperando conexiones de clientes...
```

### Ejecutar el Cliente de Pruebas

**En IntelliJ IDEA:**

1. Asegúrate de que el servidor esté ejecutándose
2. Abre `SocketClient.java`
3. Clic derecho → `Run 'SocketClient.main()'`

El cliente probará automáticamente todas las operaciones CRUD.

## 📡 Protocolo de Comunicación

### Formato de Solicitud (JSON)

```json
{
  "operacion": "CREAR",
  "parametros": {
    "cedula": "1234567890",
    "nombre": "Juan Pérez",
    "telefono": "0991234567",
    "email": "juan@email.com",
    "direccion": "Quito, Ecuador"
  }
}
```

### Formato de Respuesta (JSON)

```json
{
  "exito": true,
  "mensaje": "Cliente creado exitosamente",
  "datos": {
    "idCliente": 1,
    "cedula": "1234567890",
    "nombre": "Juan Pérez",
    "telefono": "0991234567",
    "email": "juan@email.com",
    "direccion": "Quito, Ecuador",
    "fechaRegistro": "2025-11-07 14:30:00"
  }
}
```

## 📋 Operaciones Disponibles

| Operación       | Descripción               | Parámetros                                            |
| --------------- | ------------------------- | ----------------------------------------------------- |
| `PING`          | Verificar servicio        | Ninguno                                               |
| `CREAR`         | Crear cliente             | cedula, nombre, telefono, email, direccion            |
| `OBTENERTODOS`  | Listar todos los clientes | Ninguno                                               |
| `OBTENERID`     | Buscar por ID             | id                                                    |
| `OBTENERCEDULA` | Buscar por cédula         | cedula                                                |
| `ACTUALIZAR`    | Modificar cliente         | idCliente, cedula, nombre, telefono, email, direccion |
| `ELIMINAR`      | Eliminar cliente          | id                                                    |

## 🔧 Tecnologías Utilizadas

- **Java ServerSocket/Socket**: API de sockets TCP nativos
- **Gson 2.10.1**: Serialización/deserialización JSON
- **MySQL Connector/J 8.0.33**: Driver JDBC para MySQL
- **Multi-threading**: Manejo concurrente de múltiples clientes

## 🎯 Características del Protocolo Socket

✅ **Bajo nivel**: Control directo de la capa de transporte  
✅ **Protocolo personalizado**: Formato JSON legible  
✅ **Concurrencia**: Múltiples clientes simultáneos con threads  
✅ **Flexibilidad**: Independiente de frameworks pesados  
✅ **Simplicidad**: Comunicación request-response sincrónica

## 🔄 Flujo de Comunicación

1. **Cliente** se conecta al servidor (localhost:5002)
2. **Cliente** envía solicitud JSON (una línea)
3. **Servidor** procesa la operación y consulta MySQL
4. **Servidor** envía respuesta JSON (una línea)
5. **Conexión** se cierra automáticamente

## 📝 Notas

- El servidor escucha en `localhost:5002`
- Cada cliente se maneja en un hilo separado
- La conexión se cierra después de cada solicitud
- La cadena de conexión MySQL está en `SocketServer.java` línea 120
- Formato de fecha: `yyyy-MM-dd HH:mm:ss`

## 🐛 Solución de Problemas

**Error: "Connection refused"**

- Verifica que el servidor esté ejecutándose
- Confirma que el puerto 5002 no esté bloqueado

**Error: "Address already in use"**

- El puerto 5002 está ocupado
- Cambia `PORT` en `SocketServer.java` y `SocketClient.java`

**Error de JSON**

- Verifica el formato de la solicitud
- Asegúrate de que los nombres de parámetros coincidan

**Error de conexión MySQL**

- Verifica que MySQL esté ejecutándose
- Confirma la contraseña en la cadena de conexión

## 🆚 Comparación con otros protocolos

| Aspecto     | Sockets TCP    | gRPC           | SOAP           |
| ----------- | -------------- | -------------- | -------------- |
| Nivel OSI   | Transporte (4) | Aplicación (7) | Aplicación (7) |
| Formato     | JSON           | Protobuf       | XML            |
| Overhead    | Bajo           | Muy bajo       | Alto           |
| Complejidad | Baja           | Media          | Alta           |
| Tipado      | Dinámico       | Fuerte         | Fuerte         |
