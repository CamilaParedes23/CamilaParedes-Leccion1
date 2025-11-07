# Sistemas Distribuidos - Gestión de Clientes

## 📋 Descripción del Proyecto

Proyecto académico que implementa **tres versiones de una misma API** para gestión de clientes, cada una utilizando un **mecanismo de comunicación distribuida diferente**:

1. **Servicio SOAP** - Arquitectura orientada a servicios (SOA)
2. **Servicio gRPC** - Invocación remota de procedimientos (RPC)
3. **Servicio Socket** - Comunicación de bajo nivel (Capa de transporte)

Todos los servicios implementan las mismas operaciones CRUD sobre una base de datos MySQL compartida, permitiendo comparar el rendimiento, complejidad y características de cada enfoque.

## 🏗️ Arquitectura General

```
┌─────────────────────────────────────────────────────────────┐
│                    BASE DE DATOS MYSQL                      │
│                  gestion_clientes.clientes                  │
└─────────────────────────────────────────────────────────────┘
                            │
        ┌───────────────────┼───────────────────┐
        │                   │                   │
┌───────▼───────┐   ┌───────▼───────┐   ┌───────▼───────┐
│  SERVICIO     │   │  SERVICIO     │   │  SERVICIO     │
│  SOAP         │   │  gRPC         │   │  SOCKET       │
│  Puerto 8080  │   │  Puerto 5001  │   │  Puerto 5002  │
│  XML/HTTP     │   │  Protobuf/H2  │   │  JSON/TCP     │
└───────┬───────┘   └───────┬───────┘   └───────┬───────┘
        │                   │                   │
        │                   │                   │
┌───────▼───────────────────▼───────────────────▼───────┐
│              CLIENTES / CONSUMIDORES                  │
│    ClienteSOAPClient  │  GrpcClient  │  SocketClient │
└───────────────────────────────────────────────────────┘
```

## 📂 Estructura del Repositorio

```
PRUEBA/
├── SOAP-Java/                 # Servicio SOAP con JAX-WS
│   ├── src/main/java/ec/universidad/soap/
│   │   ├── models/            # Entidades con @XmlRootElement
│   │   ├── repository/        # Acceso a datos JDBC
│   │   ├── service/           # @WebService con operaciones SOAP
│   │   ├── Main.java          # Servidor SOAP (Endpoint.publish)
│   │   └── client/            # Cliente de pruebas
│   └── pom.xml                # Maven: JAX-WS, MySQL
│
├── RPC-Java/                  # Servicio gRPC con Protocol Buffers
│   ├── src/main/
│   │   ├── proto/             # Definiciones .proto
│   │   └── java/ec/universidad/grpc/
│   │       ├── models/        # Entidades Java
│   │       ├── repository/    # ClienteRepository JDBC
│   │       ├── service/       # ClienteServiceImpl gRPC
│   │       ├── server/        # GrpcServer (ServerBuilder)
│   │       └── client/        # GrpcClient
│   └── pom.xml                # Maven: gRPC, Protobuf
│
├── Sockets-Java/              # Servicio Socket TCP/IP
│   ├── src/main/java/ec/universidad/sockets/
│   │   ├── models/            # Cliente, SocketRequest, SocketResponse
│   │   ├── repository/        # ClienteRepository JDBC
│   │   ├── server/            # SocketServer (ServerSocket)
│   │   └── client/            # SocketClient
│   └── pom.xml                # Maven: Gson, MySQL
│
└── README.md                  # Este archivo
```

## 🗄️ Base de Datos

Todos los servicios comparten la misma base de datos MySQL:

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

## 🚀 Ejecución de los Servicios

### Prerrequisitos Generales

- **Java 11** o superior
- **MySQL 8.0** ejecutándose en localhost
- **IntelliJ IDEA 2025** (recomendado)
- Cadena de conexión: `jdbc:mysql://localhost:3306/gestion_clientes?user=root&password=&useSSL=false&serverTimezone=UTC`

### 1. Servicio SOAP (Puerto 8080)

**Iniciar servidor:**

```
IntelliJ → SOAP-Java → src/main/java/ec/universidad/soap/Main.java → Run
```

**Probar cliente:**

```
IntelliJ → SOAP-Java → src/main/java/ec/universidad/soap/client/ClienteSOAPClient.java → Run
```

**Verificar WSDL:**

```
Navegador → http://localhost:8080/ClienteService?wsdl
```

### 2. Servicio gRPC (Puerto 5001)

**Iniciar servidor:**

```
IntelliJ → RPC-Java → src/main/java/ec/universidad/grpc/server/GrpcServer.java → Run
```

**Probar cliente:**

```
IntelliJ → RPC-Java → src/main/java/ec/universidad/grpc/client/GrpcClient.java → Run
```

### 3. Servicio Socket (Puerto 5002)

**Iniciar servidor:**

```
IntelliJ → Sockets-Java → src/main/java/ec/universidad/sockets/server/SocketServer.java → Run
```

**Probar cliente:**

```
IntelliJ → Sockets-Java → src/main/java/ec/universidad/sockets/client/SocketClient.java → Run
```

## 📊 Comparación de Protocolos

| Característica        | SOAP            | gRPC               | Sockets TCP          |
| --------------------- | --------------- | ------------------ | -------------------- |
| **Puerto**            | 8080            | 5001               | 5002                 |
| **Protocolo Base**    | HTTP/HTTPS      | HTTP/2             | TCP/IP               |
| **Formato Datos**     | XML             | Protocol Buffers   | JSON (personalizado) |
| **Nivel OSI**         | Aplicación (7)  | Aplicación (7)     | Transporte (4)       |
| **Overhead**          | Alto            | Muy bajo           | Bajo                 |
| **Legibilidad**       | Alta (XML)      | Baja (binario)     | Alta (JSON)          |
| **Tipado**            | Fuerte (XSD)    | Fuerte (.proto)    | Dinámico             |
| **Complejidad**       | Alta            | Media              | Baja                 |
| **Estándares**        | W3C WS-\*       | gRPC/Protobuf      | Custom               |
| **Interoperabilidad** | Excelente       | Muy buena          | Limitada             |
| **Rendimiento**       | Bajo            | Alto               | Medio                |
| **Streaming**         | No              | Sí (bidireccional) | Sí (manual)          |
| **Tooling**           | SoapUI, Postman | grpcurl, BloomRPC  | Telnet, netcat       |
| **Casos de Uso**      | Enterprise SOA  | Microservicios     | IoT, Gaming          |

## 🎯 Operaciones CRUD Disponibles

Todos los servicios implementan las mismas 7 operaciones:

1. **Ping** - Verificar disponibilidad del servicio
2. **Crear Cliente** - Insertar nuevo registro
3. **Obtener Todos** - Listar todos los clientes
4. **Obtener por ID** - Buscar por clave primaria
5. **Obtener por Cédula** - Buscar por clave única
6. **Actualizar** - Modificar registro existente
7. **Eliminar** - Borrar registro por ID

## 🔬 Pruebas y Validación

### Pruebas Automáticas (Clientes Java)

Cada proyecto incluye un cliente de pruebas que ejecuta todas las operaciones CRUD automáticamente:

- `ClienteSOAPClient.java` - Verifica WSDL y muestra operaciones
- `GrpcClient.java` - Prueba completa de operaciones gRPC
- `SocketClient.java` - Prueba completa de protocolo Socket

### Pruebas Manuales

**SOAP con SoapUI:**

1. Crear proyecto SOAP en SoapUI
2. WSDL: `http://localhost:8080/ClienteService?wsdl`
3. Ejecutar peticiones XML generadas

**gRPC con grpcurl:**

```bash
# Listar servicios
grpcurl -plaintext localhost:5001 list

# Llamar a Ping
grpcurl -plaintext localhost:5001 cliente.ClienteService/Ping
```

**Sockets con telnet:**

```bash
telnet localhost 5002
{"operacion":"PING","parametros":null}
```

## 📈 Resultados y Análisis

### Ventajas de SOAP

✅ Estándares robustos (WS-Security, WS-ReliableMessaging)  
✅ Excelente para integración empresarial  
✅ Soporte de transacciones distribuidas  
✅ Independiente del lenguaje

### Ventajas de gRPC

✅ Alto rendimiento (HTTP/2 + binario)  
✅ Streaming bidireccional  
✅ Multiplexación de conexiones  
✅ Generación automática de clientes

### Ventajas de Sockets TCP

✅ Control total sobre el protocolo  
✅ Mínimo overhead  
✅ Flexibilidad máxima  
✅ Ideal para aplicaciones en tiempo real

## 🛠️ Tecnologías Utilizadas

| Componente    | Tecnología        | Versión  |
| ------------- | ----------------- | -------- |
| Lenguaje      | Java              | 11+      |
| SOAP          | JAX-WS            | 2.3.5    |
| RPC           | gRPC              | 1.58.0   |
| Serialización | Protocol Buffers  | 3.24.0   |
| JSON          | Gson              | 2.10.1   |
| Base de Datos | MySQL             | 8.0      |
| Driver JDBC   | MySQL Connector/J | 8.0.33   |
| Build Tool    | Maven             | 3.x      |
| IDE           | IntelliJ IDEA     | 2025.2.4 |

## 📚 Referencias

- [JAX-WS Documentation](https://javaee.github.io/metro-jax-ws/)
- [gRPC Java Documentation](https://grpc.io/docs/languages/java/)
- [Protocol Buffers Guide](https://developers.google.com/protocol-buffers)
- [Java Socket Programming](https://docs.oracle.com/javase/tutorial/networking/sockets/)

## 👨‍💻 Autor

Proyecto académico - Sistemas Distribuidos  
Universidad: **[Tu Universidad]**  
Fecha: Noviembre 2025

## 📝 Licencia

Este proyecto es de uso académico y educativo.

---

## 🚦 Inicio Rápido

1. **Crear base de datos MySQL** (script SQL arriba)
2. **Abrir IntelliJ IDEA** y cargar los 3 proyectos
3. **Ejecutar servidores** (Main.java, GrpcServer.java, SocketServer.java)
4. **Ejecutar clientes** para probar cada servicio
5. **Comparar** resultados y rendimiento

¡Todos los servicios deberían estar funcionando en puertos diferentes! 🎉
