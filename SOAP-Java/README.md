# Servicio SOAP en Java - Gestión de Clientes

## 📋 Descripción

Servicio Web SOAP implementado en Java usando JAX-WS (Java API for XML Web Services) para operaciones CRUD sobre la entidad Cliente.

## 🛠️ Tecnologías

- **Java 11+**
- **JAX-WS 2.3.5** - Framework para SOAP
- **MySQL Connector 8.0.33**
- **Maven** - Gestión de dependencias

## 📦 Estructura del Proyecto

```
SOAP-Java/
├── src/
│   └── main/
│       └── java/
│           └── ec/universidad/soap/
│               ├── models/
│               │   └── Cliente.java          # Modelo de datos
│               ├── repository/
│               │   └── ClienteRepository.java # Acceso a datos
│               ├── service/
│               │   └── ClienteWebService.java # Servicio SOAP
│               ├── client/
│               │   └── ClienteSOAPClient.java # Cliente de prueba
│               └── Main.java                  # Servidor SOAP
├── pom.xml                                    # Configuración Maven
└── README.md
```

## ⚙️ Requisitos Previos

1. **JDK 11 o superior**

   ```bash
   java -version
   ```

2. **Maven 3.6+**

   ```bash
   mvn -version
   ```

3. **MySQL Server** ejecutándose con la base de datos `gestion_clientes`

## 🚀 Instalación y Ejecución

### 1. Configurar Base de Datos

Edita el archivo `src/main/java/ec/universidad/soap/service/ClienteWebService.java` línea 29:

```java
String connectionString = "jdbc:mysql://localhost:3306/gestion_clientes?user=root&password=TU_PASSWORD&useSSL=false&serverTimezone=UTC";
```

**Si no tienes contraseña**, déjala vacía:

```java
String connectionString = "jdbc:mysql://localhost:3306/gestion_clientes?user=root&password=&useSSL=false&serverTimezone=UTC";
```

### 2. Compilar el Proyecto

```bash
cd SOAP-Java
mvn clean compile
```

### 3. Ejecutar el Servidor SOAP

```bash
mvn exec:java -Dexec.mainClass="ec.universidad.soap.Main"
```

**Verás:**

```
╔═══════════════════════════════════════════════════╗
║   SERVIDOR SOAP - GESTIÓN DE CLIENTES            ║
║   Arquitectura Orientada a Servicios (SOA)       ║
║   Implementación: Java + JAX-WS                  ║
╚═══════════════════════════════════════════════════╝

✓ Servicio SOAP iniciado exitosamente

═══════════════════════════════════════════════════
  WSDL disponible en: http://localhost:8080/ClienteService?wsdl
  Endpoint: http://localhost:8080/ClienteService
═══════════════════════════════════════════════════

Presione Ctrl+C para detener el servidor...
```

### 4. Verificar el WSDL

Abre en tu navegador:

```
http://localhost:8080/ClienteService?wsdl
```

Deberías ver el documento WSDL con las definiciones del servicio.

### 5. Ejecutar Cliente de Prueba

En **otra terminal**:

```bash
cd SOAP-Java
mvn exec:java -Dexec.mainClass="ec.universidad.soap.client.ClienteSOAPClient"
```

## 📡 Operaciones SOAP Disponibles

| Operación                 | Descripción                               |
| ------------------------- | ----------------------------------------- |
| `crearCliente`            | Crea un nuevo cliente                     |
| `obtenerTodosClientes`    | Obtiene lista de todos los clientes       |
| `obtenerClientePorId`     | Obtiene un cliente por su ID              |
| `obtenerClientePorCedula` | Obtiene un cliente por su cédula          |
| `actualizarCliente`       | Actualiza datos de un cliente             |
| `eliminarCliente`         | Elimina un cliente por ID                 |
| `ping`                    | Verifica que el servicio esté funcionando |

## 🧪 Probar con SoapUI

1. Descarga e instala [SoapUI](https://www.soapui.org/downloads/soapui/)
2. Crea nuevo proyecto SOAP
3. WSDL: `http://localhost:8080/ClienteService?wsdl`
4. Prueba las operaciones disponibles

## 📝 Ejemplo de Mensaje SOAP

### Solicitud - Crear Cliente:

```xml
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
               xmlns:soa="http://soap.universidad.ec/">
   <soap:Header/>
   <soap:Body>
      <soa:crearCliente>
         <cedula>1234567890</cedula>
         <nombre>Juan Pérez</nombre>
         <telefono>0999123456</telefono>
         <email>juan@email.com</email>
         <direccion>Quito, Ecuador</direccion>
      </soa:crearCliente>
   </soap:Body>
</soap:Envelope>
```

### Respuesta:

```xml
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
   <soap:Body>
      <ns2:crearClienteResponse xmlns:ns2="http://soap.universidad.ec/">
         <return>4</return>
      </ns2:crearClienteResponse>
   </soap:Body>
</soap:Envelope>
```

## 🔧 Solución de Problemas

### Error: "Address already in use"

El puerto 8080 está ocupado. Cambia el puerto en `Main.java` línea 13:

```java
private static final int PORT = 9090; // Cambiar a otro puerto
```

### Error: "Access denied for user 'root'@'localhost'"

Verifica tu usuario y contraseña de MySQL en `ClienteWebService.java`.

### Error: "No suitable driver found"

Asegúrate de que `mysql-connector-java` esté en el `pom.xml` y ejecuta:

```bash
mvn clean install
```

## 📊 Comparación con Otras Implementaciones

| Característica    | SOAP (Java) | gRPC (.NET)       | Sockets (.NET) |
| ----------------- | ----------- | ----------------- | -------------- |
| Protocolo         | HTTP + XML  | HTTP/2 + Protobuf | TCP + JSON     |
| Puerto            | 8080        | 5001              | 5002           |
| WSDL              | ✅ Sí       | ❌ No (.proto)    | ❌ No          |
| Interoperabilidad | ⭐⭐⭐⭐⭐  | ⭐⭐⭐            | ⭐⭐           |
| Rendimiento       | ⭐⭐⭐      | ⭐⭐⭐⭐⭐        | ⭐⭐⭐⭐⭐     |

## 🎯 Endpoints

- **Servicio**: `http://localhost:8080/ClienteService`
- **WSDL**: `http://localhost:8080/ClienteService?wsdl`

## 📚 Recursos

- [JAX-WS Documentation](https://docs.oracle.com/javaee/7/tutorial/jaxws.htm)
- [SOAP Web Services Tutorial](https://www.baeldung.com/jax-ws)
- [Maven Documentation](https://maven.apache.org/guides/)

---

**Proyecto académico - Universidad 7mo Semestre - Sistemas Distribuidos**
