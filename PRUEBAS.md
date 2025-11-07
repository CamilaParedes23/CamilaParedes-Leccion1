# Pruebas del Sistema CRUD Multiservicio

## 📋 Objetivo

Validar el correcto funcionamiento de las tres implementaciones del sistema CRUD de gestión de clientes utilizando diferentes mecanismos de comunicación distribuida: SOAP, RPC (gRPC) y Sockets.

---

## 🧪 1. PRUEBAS API SOAP (Puerto 8080)

### 1.1 Configuración de Pruebas

**Herramientas:**
- Cliente Java incluido: `ClienteSOAPClient.java`
- SoapUI (recomendado para pruebas exhaustivas)
- Postman con soporte SOAP
- Navegador web para WSDL

**WSDL URL:** `http://localhost:8080/ClienteService?wsdl`

### 1.2 Casos de Prueba

#### CP-SOAP-01: Verificación del Servicio (ping)
**Objetivo:** Validar que el servicio esté activo y responda correctamente

**Solicitud SOAP:**
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:soap="http://soap.universidad.ec/">
   <soapenv:Header/>
   <soapenv:Body>
      <soap:ping/>
   </soapenv:Body>
</soapenv:Envelope>
```

**Respuesta Esperada:**
```xml
<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
   <S:Body>
      <ns2:pingResponse xmlns:ns2="http://soap.universidad.ec/">
         <return>Servicio SOAP de Clientes funcionando correctamente</return>
      </ns2:pingResponse>
   </S:Body>
</S:Envelope>
```

**Resultado esperado:** ✅ Código 200, mensaje confirmando disponibilidad

---

#### CP-SOAP-02: Crear Cliente - Caso Exitoso
**Objetivo:** Insertar un nuevo cliente con datos válidos

**Solicitud SOAP:**
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:soap="http://soap.universidad.ec/">
   <soapenv:Header/>
   <soapenv:Body>
      <soap:crearCliente>
         <cedula>1234567890</cedula>
         <nombre>María Rodríguez</nombre>
         <telefono>0991234567</telefono>
         <email>maria.rodriguez@email.com</email>
         <direccion>Calle Principal 123, Quito</direccion>
      </soap:crearCliente>
   </soapenv:Body>
</soapenv:Envelope>
```

**Respuesta Esperada:**
```xml
<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
   <S:Body>
      <ns2:crearClienteResponse xmlns:ns2="http://soap.universidad.ec/">
         <return>15</return>  <!-- ID generado -->
      </ns2:crearClienteResponse>
   </S:Body>
</S:Envelope>
```

**Validaciones:**
- ✅ Código HTTP: 200
- ✅ Retorna ID numérico válido (> 0)
- ✅ Registro insertado en base de datos
- ✅ Fecha de registro generada automáticamente

---

#### CP-SOAP-03: Crear Cliente - Cédula Duplicada (Fallo)
**Objetivo:** Validar manejo de error al insertar cédula existente

**Solicitud SOAP:** (Usar misma cédula del CP-SOAP-02)
```xml
<soap:crearCliente>
   <cedula>1234567890</cedula>
   <nombre>Pedro González</nombre>
   <!-- ... resto de datos ... -->
</soap:crearCliente>
```

**Respuesta Esperada:**
```xml
<S:Fault>
   <faultcode>S:Server</faultcode>
   <faultstring>Error al crear cliente: Duplicate entry '1234567890' for key 'cedula'</faultstring>
</S:Fault>
```

**Validaciones:**
- ✅ SOAP Fault generado correctamente
- ✅ Mensaje de error descriptivo
- ✅ No se duplica el registro en BD

---

#### CP-SOAP-04: Obtener Todos los Clientes
**Objetivo:** Listar todos los registros de clientes

**Solicitud SOAP:**
```xml
<soap:obtenerTodosClientes/>
```

**Respuesta Esperada:**
```xml
<ns2:obtenerTodosClientesResponse>
   <return>
      <cedula>1234567890</cedula>
      <direccion>Calle Principal 123, Quito</direccion>
      <email>maria.rodriguez@email.com</email>
      <fechaRegistro>2025-11-07T14:30:00</fechaRegistro>
      <idCliente>15</idCliente>
      <nombre>María Rodríguez</nombre>
      <telefono>0991234567</telefono>
   </return>
   <!-- Más registros... -->
</ns2:obtenerTodosClientesResponse>
```

**Validaciones:**
- ✅ Array de clientes retornado
- ✅ Todos los campos presentes
- ✅ Fechas en formato ISO 8601

---

#### CP-SOAP-05: Obtener Cliente por ID - Caso Exitoso
**Objetivo:** Consultar cliente existente por ID

**Solicitud SOAP:**
```xml
<soap:obtenerClientePorId>
   <id>15</id>
</soap:obtenerClientePorId>
```

**Respuesta Esperada:**
```xml
<ns2:obtenerClientePorIdResponse>
   <return>
      <idCliente>15</idCliente>
      <cedula>1234567890</cedula>
      <nombre>María Rodríguez</nombre>
      <!-- ... resto de datos ... -->
   </return>
</ns2:obtenerClientePorIdResponse>
```

**Validaciones:**
- ✅ Cliente correcto retornado
- ✅ Datos coinciden con BD

---

#### CP-SOAP-06: Obtener Cliente por ID - ID Inexistente (Fallo)
**Objetivo:** Validar manejo de cliente no encontrado

**Solicitud SOAP:**
```xml
<soap:obtenerClientePorId>
   <id>99999</id>
</soap:obtenerClientePorId>
```

**Respuesta Esperada:**
```xml
<S:Fault>
   <faultcode>S:Server</faultcode>
   <faultstring>Cliente con ID 99999 no encontrado</faultstring>
</S:Fault>
```

**Validaciones:**
- ✅ SOAP Fault apropiado
- ✅ Mensaje descriptivo del error

---

#### CP-SOAP-07: Obtener Cliente por Cédula
**Objetivo:** Buscar cliente por cédula única

**Solicitud SOAP:**
```xml
<soap:obtenerClientePorCedula>
   <cedula>1234567890</cedula>
</soap:obtenerClientePorCedula>
```

**Validaciones:**
- ✅ Cliente correcto retornado
- ✅ Búsqueda por índice único eficiente

---

#### CP-SOAP-08: Actualizar Cliente - Caso Exitoso
**Objetivo:** Modificar datos de cliente existente

**Solicitud SOAP:**
```xml
<soap:actualizarCliente>
   <idCliente>15</idCliente>
   <cedula>1234567890</cedula>
   <nombre>María Rodríguez García</nombre>
   <telefono>0998765432</telefono>
   <email>maria.garcia@email.com</email>
   <direccion>Av. Principal 456, Quito</direccion>
</soap:actualizarCliente>
```

**Respuesta Esperada:**
```xml
<ns2:actualizarClienteResponse>
   <return>true</return>
</ns2:actualizarClienteResponse>
```

**Validaciones:**
- ✅ Retorna `true`
- ✅ Datos actualizados en BD
- ✅ Fecha de registro no cambia

---

#### CP-SOAP-09: Actualizar Cliente - ID Inexistente (Fallo)
**Objetivo:** Validar error al actualizar cliente no existente

**Solicitud SOAP:**
```xml
<soap:actualizarCliente>
   <idCliente>99999</idCliente>
   <!-- ... datos ... -->
</soap:actualizarCliente>
```

**Respuesta Esperada:**
```xml
<S:Fault>
   <faultcode>S:Server</faultcode>
   <faultstring>Cliente con ID 99999 no encontrado</faultstring>
</S:Fault>
```

---

#### CP-SOAP-10: Eliminar Cliente - Caso Exitoso
**Objetivo:** Borrar cliente existente

**Solicitud SOAP:**
```xml
<soap:eliminarCliente>
   <id>15</id>
</soap:eliminarCliente>
```

**Respuesta Esperada:**
```xml
<ns2:eliminarClienteResponse>
   <return>true</return>
</ns2:eliminarClienteResponse>
```

**Validaciones:**
- ✅ Retorna `true`
- ✅ Registro eliminado de BD
- ✅ Consulta posterior retorna SOAP Fault

---

#### CP-SOAP-11: Formato XML Incorrecto (Fallo)
**Objetivo:** Validar manejo de XML malformado

**Solicitud SOAP:**
```xml
<soap:crearCliente>
   <cedula>1234567890
   <!-- XML sin cerrar correctamente -->
</soap:crearCliente>
```

**Respuesta Esperada:**
- HTTP 500 o SOAP Fault
- Mensaje indicando error de parsing XML

---

### 1.3 Ejecución de Pruebas SOAP

**Usando el Cliente Java:**
```bash
# En IntelliJ IDEA
1. Ejecutar Main.java (servidor)
2. Ejecutar ClienteSOAPClient.java (cliente)
3. Verificar salida en consola
```

**Usando SoapUI:**
```
1. New SOAP Project
2. WSDL: http://localhost:8080/ClienteService?wsdl
3. SoapUI genera automáticamente peticiones
4. Ejecutar cada operación y validar respuestas
```

---

## 🧪 2. PRUEBAS API RPC (gRPC - Puerto 5001)

### 2.1 Configuración de Pruebas

**Herramientas:**
- Cliente Java incluido: `GrpcClient.java`
- grpcurl (CLI para gRPC)
- BloomRPC (GUI para gRPC)

### 2.2 Casos de Prueba

#### CP-RPC-01: Verificación del Servicio (Ping)
**Objetivo:** Validar conexión gRPC

**Usando Cliente Java:**
```java
PingResponse response = blockingStub.ping(Empty.newBuilder().build());
```

**Usando grpcurl:**
```bash
grpcurl -plaintext localhost:5001 cliente.ClienteService/Ping
```

**Respuesta Esperada:**
```json
{
  "message": "Servicio gRPC de Clientes funcionando correctamente"
}
```

**Validaciones:**
- ✅ Conexión HTTP/2 establecida
- ✅ Respuesta Protobuf deserializada correctamente
- ✅ Mensaje de confirmación recibido

---

#### CP-RPC-02: Crear Cliente - Caso Exitoso
**Objetivo:** Insertar nuevo cliente mediante RPC

**Request (Protobuf):**
```protobuf
CrearClienteRequest {
  cedula: "0987654321"
  nombre: "Juan Pérez"
  telefono: "0991112233"
  email: "juan.perez@email.com"
  direccion: "Av. Amazonas 789, Quito"
}
```

**Response Esperada:**
```protobuf
ClienteResponse {
  id_cliente: 20
  cedula: "0987654321"
  nombre: "Juan Pérez"
  telefono: "0991112233"
  email: "juan.perez@email.com"
  direccion: "Av. Amazonas 789, Quito"
  fecha_registro: "2025-11-07 15:45:00"
}
```

**Validaciones:**
- ✅ ID generado > 0
- ✅ Todos los campos retornados
- ✅ Registro en BD verificado

---

#### CP-RPC-03: Crear Cliente - Cédula Duplicada (Fallo)
**Objetivo:** Validar manejo de excepciones gRPC

**Request:** (Misma cédula del CP-RPC-02)

**Respuesta Esperada:**
```
StatusRuntimeException: INTERNAL
  Error al crear cliente: Duplicate entry '0987654321' for key 'cedula'
```

**Validaciones:**
- ✅ Exception capturada correctamente
- ✅ Status Code: INTERNAL o ALREADY_EXISTS
- ✅ Mensaje descriptivo

---

#### CP-RPC-04: Obtener Todos los Clientes
**Objetivo:** Listar clientes mediante streaming

**Request:**
```protobuf
Empty {}
```

**Response Esperada:**
```protobuf
ClientesResponse {
  clientes: [
    ClienteResponse { id_cliente: 1, ... },
    ClienteResponse { id_cliente: 2, ... },
    // ...
  ]
}
```

**Validaciones:**
- ✅ Array de clientes poblado
- ✅ Eficiencia de serialización Protobuf
- ✅ Todos los registros presentes

---

#### CP-RPC-05: Obtener Cliente por ID - Caso Exitoso
**Objetivo:** RPC de consulta por clave primaria

**Request:**
```protobuf
IdRequest {
  id: 20
}
```

**Validaciones:**
- ✅ Cliente correcto retornado
- ✅ Latencia baja (< 50ms típicamente)

---

#### CP-RPC-06: Obtener Cliente por ID - Fallo
**Objetivo:** Manejo de ID inexistente

**Request:**
```protobuf
IdRequest {
  id: 99999
}
```

**Respuesta Esperada:**
```
StatusRuntimeException: INTERNAL
  Cliente no encontrado
```

---

#### CP-RPC-07: Actualizar Cliente
**Objetivo:** RPC de actualización

**Request:**
```protobuf
ActualizarClienteRequest {
  id_cliente: 20
  cedula: "0987654321"
  nombre: "Juan Pérez Actualizado"
  telefono: "0999888777"
  email: "juan.nuevo@email.com"
  direccion: "Nueva dirección"
}
```

**Validaciones:**
- ✅ ClienteResponse con datos actualizados
- ✅ Cambios persistidos en BD

---

#### CP-RPC-08: Eliminar Cliente
**Objetivo:** RPC de eliminación

**Request:**
```protobuf
IdRequest {
  id: 20
}
```

**Response Esperada:**
```protobuf
DeleteResponse {
  success: true
  message: "Cliente eliminado correctamente"
}
```

**Validaciones:**
- ✅ success = true
- ✅ Registro borrado de BD

---

#### CP-RPC-09: Estabilidad de Conexión
**Objetivo:** Validar múltiples invocaciones consecutivas

**Procedimiento:**
1. Ejecutar 100 llamadas a `Ping`
2. Medir tiempo de respuesta promedio
3. Verificar 0 errores de conexión

**Resultado esperado:**
- ✅ 100% de llamadas exitosas
- ✅ Latencia promedio < 10ms
- ✅ Conexión HTTP/2 mantenida

---

#### CP-RPC-10: Manejo de Desconexión del Servidor
**Objetivo:** Validar comportamiento ante servidor caído

**Procedimiento:**
1. Detener GrpcServer
2. Intentar invocar operación
3. Capturar excepción

**Resultado esperado:**
```
StatusRuntimeException: UNAVAILABLE
  io exception: Connection refused
```

**Validaciones:**
- ✅ Exception apropiada lanzada
- ✅ Mensaje descriptivo
- ✅ Cliente no se bloquea indefinidamente

---

### 2.3 Ejecución de Pruebas gRPC

**Usando el Cliente Java:**
```bash
# Terminal 1: Servidor
cd RPC-Java
# Run GrpcServer.java en IntelliJ

# Terminal 2: Cliente
# Run GrpcClient.java en IntelliJ
```

**Usando grpcurl:**
```bash
# Listar servicios
grpcurl -plaintext localhost:5001 list

# Ping
grpcurl -plaintext localhost:5001 cliente.ClienteService/Ping

# Crear cliente
grpcurl -plaintext -d '{
  "cedula": "1122334455",
  "nombre": "Test User",
  "telefono": "0991234567",
  "email": "test@email.com",
  "direccion": "Test Address"
}' localhost:5001 cliente.ClienteService/CrearCliente
```

---

## 🧪 3. PRUEBAS API SOCKETS (Puerto 5002)

### 3.1 Configuración de Pruebas

**Herramientas:**
- Cliente Java incluido: `SocketClient.java`
- Telnet (para pruebas manuales)
- Netcat (nc)
- Scripts de prueba personalizados

### 3.2 Casos de Prueba

#### CP-SOCK-01: Verificación del Servicio (Ping)
**Objetivo:** Validar comunicación TCP/IP

**Request JSON:**
```json
{"operacion":"PING","parametros":null}
```

**Response Esperada:**
```json
{
  "exito": true,
  "mensaje": "Servidor Socket funcionando correctamente",
  "datos": null
}
```

**Validaciones:**
- ✅ Conexión TCP establecida
- ✅ JSON bien formado
- ✅ exito = true

---

#### CP-SOCK-02: Crear Cliente - Caso Exitoso
**Objetivo:** Insertar mediante sockets

**Request JSON:**
```json
{
  "operacion": "CREAR",
  "parametros": {
    "cedula": "5566778899",
    "nombre": "Laura Morales",
    "telefono": "0993344556",
    "email": "laura.morales@email.com",
    "direccion": "Calle Flores 321, Guayaquil"
  }
}
```

**Response Esperada:**
```json
{
  "exito": true,
  "mensaje": "Cliente creado exitosamente",
  "datos": {
    "idCliente": 25,
    "cedula": "5566778899",
    "nombre": "Laura Morales",
    "telefono": "0993344556",
    "email": "laura.morales@email.com",
    "direccion": "Calle Flores 321, Guayaquil",
    "fechaRegistro": "2025-11-07 16:00:00"
  }
}
```

**Validaciones:**
- ✅ exito = true
- ✅ datos contiene objeto Cliente completo
- ✅ ID generado presente

---

#### CP-SOCK-03: Crear Cliente - Cédula Duplicada (Fallo)
**Objetivo:** Manejo de errores en protocolo custom

**Request JSON:**
```json
{
  "operacion": "CREAR",
  "parametros": {
    "cedula": "5566778899",
    "nombre": "Otro Cliente",
    "telefono": "0991111111",
    "email": "otro@email.com",
    "direccion": "Otra dirección"
  }
}
```

**Response Esperada:**
```json
{
  "exito": false,
  "mensaje": "Error al crear cliente: Duplicate entry '5566778899' for key 'cedula'",
  "datos": null
}
```

**Validaciones:**
- ✅ exito = false
- ✅ mensaje descriptivo del error
- ✅ datos = null

---

#### CP-SOCK-04: Obtener Todos los Clientes
**Objetivo:** Listar mediante protocolo Socket

**Request JSON:**
```json
{"operacion":"OBTENERTODOS","parametros":null}
```

**Response Esperada:**
```json
{
  "exito": true,
  "mensaje": "Clientes obtenidos exitosamente",
  "datos": [
    {
      "idCliente": 1,
      "cedula": "1234567890",
      "nombre": "Cliente 1",
      ...
    },
    {
      "idCliente": 2,
      ...
    }
  ]
}
```

**Validaciones:**
- ✅ Array de clientes en datos
- ✅ Todos los campos presentes

---

#### CP-SOCK-05: Obtener Cliente por ID
**Objetivo:** Consulta específica

**Request JSON:**
```json
{
  "operacion": "OBTENERID",
  "parametros": {
    "id": 25
  }
}
```

**Validaciones:**
- ✅ Cliente correcto retornado
- ✅ Estructura JSON válida

---

#### CP-SOCK-06: Obtener por ID - ID Inexistente (Fallo)
**Request JSON:**
```json
{
  "operacion": "OBTENERID",
  "parametros": {
    "id": 99999
  }
}
```

**Response Esperada:**
```json
{
  "exito": false,
  "mensaje": "Cliente no encontrado",
  "datos": null
}
```

---

#### CP-SOCK-07: Obtener Cliente por Cédula
**Request JSON:**
```json
{
  "operacion": "OBTENERCEDULA",
  "parametros": {
    "cedula": "5566778899"
  }
}
```

**Validaciones:**
- ✅ Cliente encontrado
- ✅ Búsqueda por índice único

---

#### CP-SOCK-08: Actualizar Cliente
**Request JSON:**
```json
{
  "operacion": "ACTUALIZAR",
  "parametros": {
    "idCliente": 25,
    "cedula": "5566778899",
    "nombre": "Laura Morales Actualizada",
    "telefono": "0999999999",
    "email": "laura.nueva@email.com",
    "direccion": "Nueva dirección actualizada"
  }
}
```

**Response Esperada:**
```json
{
  "exito": true,
  "mensaje": "Cliente actualizado exitosamente",
  "datos": {
    "idCliente": 25,
    "nombre": "Laura Morales Actualizada",
    ...
  }
}
```

---

#### CP-SOCK-09: Eliminar Cliente
**Request JSON:**
```json
{
  "operacion": "ELIMINAR",
  "parametros": {
    "id": 25
  }
}
```

**Response Esperada:**
```json
{
  "exito": true,
  "mensaje": "Cliente eliminado exitosamente",
  "datos": null
}
```

---

#### CP-SOCK-10: JSON Malformado (Fallo)
**Objetivo:** Validar manejo de formato incorrecto

**Request:**
```
{operacion:"PING"parametros:null}  // JSON inválido
```

**Resultado esperado:**
- Conexión cerrada o error de parsing
- Servidor mantiene estabilidad

---

#### CP-SOCK-11: Operación No Soportada (Fallo)
**Request JSON:**
```json
{
  "operacion": "OPERACION_INEXISTENTE",
  "parametros": {}
}
```

**Response Esperada:**
```json
{
  "exito": false,
  "mensaje": "Operación no soportada: OPERACION_INEXISTENTE",
  "datos": null
}
```

---

#### CP-SOCK-12: Múltiples Conexiones Simultáneas
**Objetivo:** Validar concurrencia con threads

**Procedimiento:**
1. Abrir 10 conexiones simultáneas
2. Cada una ejecuta una operación CRUD
3. Verificar que todas completen exitosamente

**Resultado esperado:**
- ✅ 10/10 operaciones exitosas
- ✅ Sin conflictos de concurrencia
- ✅ Servidor estable

---

#### CP-SOCK-13: Desconexión Abrupta del Cliente
**Objetivo:** Manejo de conexiones perdidas

**Procedimiento:**
1. Conectar cliente
2. Enviar request incompleto
3. Cerrar conexión abruptamente

**Resultado esperado:**
- ✅ Servidor captura IOException
- ✅ Thread del cliente termina correctamente
- ✅ Servidor continúa escuchando nuevas conexiones

---

#### CP-SOCK-14: Parámetros Faltantes (Fallo)
**Request JSON:**
```json
{
  "operacion": "CREAR",
  "parametros": {
    "cedula": "1111111111"
    // Faltan otros campos requeridos
  }
}
```

**Resultado esperado:**
- Error SQL o NullPointerException manejado
- Respuesta con exito=false

---

### 3.3 Ejecución de Pruebas Socket

**Usando el Cliente Java:**
```bash
# Terminal 1: Servidor
# Run SocketServer.java en IntelliJ

# Terminal 2: Cliente
# Run SocketClient.java en IntelliJ
```

**Usando Telnet:**
```bash
telnet localhost 5002

# Escribir (una línea):
{"operacion":"PING","parametros":null}

# Presionar Enter
# Leer respuesta
```

**Usando Netcat:**
```bash
echo '{"operacion":"PING","parametros":null}' | nc localhost 5002
```

**Script PowerShell de Prueba:**
```powershell
$client = New-Object System.Net.Sockets.TcpClient("localhost", 5002)
$stream = $client.GetStream()
$writer = New-Object System.IO.StreamWriter($stream)
$reader = New-Object System.IO.StreamReader($stream)

$request = '{"operacion":"PING","parametros":null}'
$writer.WriteLine($request)
$writer.Flush()

$response = $reader.ReadLine()
Write-Host $response

$client.Close()
```

---

## 📊 Resumen de Validaciones

### Criterios de Aceptación Generales

| Criterio | SOAP | gRPC | Sockets |
|----------|------|------|---------|
| **Inserción exitosa** | ✅ | ✅ | ✅ |
| **Inserción duplicada controlada** | ✅ | ✅ | ✅ |
| **Consulta por ID existente** | ✅ | ✅ | ✅ |
| **Consulta por ID inexistente** | ✅ | ✅ | ✅ |
| **Listar todos** | ✅ | ✅ | ✅ |
| **Actualización exitosa** | ✅ | ✅ | ✅ |
| **Actualización ID inexistente** | ✅ | ✅ | ✅ |
| **Eliminación exitosa** | ✅ | ✅ | ✅ |
| **Formato incorrecto manejado** | ✅ | ✅ | ✅ |
| **Manejo de excepciones** | SOAP Fault | StatusException | JSON error |
| **Concurrencia** | N/A | HTTP/2 Mux | Multi-thread |
| **Desconexión servidor** | N/A | UNAVAILABLE | Connection refused |

---

## 🎯 Matriz de Trazabilidad

| ID Prueba | Requisito | SOAP | gRPC | Sockets |
|-----------|-----------|------|------|---------|
| CP-01 | Verificar servicio activo | ✓ | ✓ | ✓ |
| CP-02 | Insertar registro válido | ✓ | ✓ | ✓ |
| CP-03 | Rechazar duplicados | ✓ | ✓ | ✓ |
| CP-04 | Listar todos | ✓ | ✓ | ✓ |
| CP-05 | Consultar existente | ✓ | ✓ | ✓ |
| CP-06 | Consultar inexistente | ✓ | ✓ | ✓ |
| CP-07 | Actualizar existente | ✓ | ✓ | ✓ |
| CP-08 | Actualizar inexistente | ✓ | ✓ | ✓ |
| CP-09 | Eliminar existente | ✓ | ✓ | ✓ |
| CP-10 | Formato inválido | ✓ | ✓ | ✓ |
| CP-11 | Operación no soportada | N/A | N/A | ✓ |
| CP-12 | Concurrencia | N/A | ✓ | ✓ |
| CP-13 | Desconexión | N/A | ✓ | ✓ |

---

## 📝 Reporte de Resultados

### Plantilla de Reporte por Prueba

```
ID: CP-SOAP-02
Nombre: Crear Cliente - Caso Exitoso
Fecha: 2025-11-07 16:30:00
Ejecutor: [Nombre]
Resultado: ✅ PASS / ❌ FAIL

Detalles:
- Request enviado: [copiar XML/JSON]
- Response recibido: [copiar respuesta]
- Validaciones:
  ✅ ID generado: 15
  ✅ Registro en BD verificado
  ✅ Todos los campos correctos

Observaciones:
- Tiempo de respuesta: 45ms
- Sin errores

Evidencia: [screenshot o log]
```

---

## 🚀 Ejecución Completa de Pruebas

### Script de Ejecución Automática

```bash
#!/bin/bash
# test-all.sh

echo "=== INICIANDO PRUEBAS SISTEMA CRUD MULTISERVICIO ==="

# 1. Verificar MySQL
mysql -u root -p -e "USE gestion_clientes;" || exit 1

# 2. Iniciar servidores en background
echo "Iniciando servidores..."
cd SOAP-Java && mvn exec:java -Dexec.mainClass="ec.universidad.soap.Main" &
SOAP_PID=$!

cd ../RPC-Java && mvn exec:java -Dexec.mainClass="ec.universidad.grpc.server.GrpcServer" &
GRPC_PID=$!

cd ../Sockets-Java && mvn exec:java -Dexec.mainClass="ec.universidad.sockets.server.SocketServer" &
SOCKET_PID=$!

sleep 5

# 3. Ejecutar clientes de prueba
echo "Ejecutando pruebas SOAP..."
cd SOAP-Java && mvn exec:java -Dexec.mainClass="ec.universidad.soap.client.ClienteSOAPClient"

echo "Ejecutando pruebas gRPC..."
cd ../RPC-Java && mvn exec:java -Dexec.mainClass="ec.universidad.grpc.client.GrpcClient"

echo "Ejecutando pruebas Sockets..."
cd ../Sockets-Java && mvn exec:java -Dexec.mainClass="ec.universidad.sockets.client.SocketClient"

# 4. Detener servidores
kill $SOAP_PID $GRPC_PID $SOCKET_PID

echo "=== PRUEBAS COMPLETADAS ==="
```

---

## ✅ Checklist Final de Validación

### SOAP
- [ ] WSDL accesible y bien formado
- [ ] Todas las operaciones CRUD funcionales
- [ ] SOAP Faults apropiados en errores
- [ ] XML bien estructurado en respuestas
- [ ] Cliente Java ejecuta sin errores

### gRPC
- [ ] Servidor gRPC acepta conexiones HTTP/2
- [ ] Protocol Buffers deserializan correctamente
- [ ] Todas las RPC responden
- [ ] Excepciones manejadas (StatusRuntimeException)
- [ ] Latencia aceptable (< 50ms promedio)
- [ ] Cliente Java ejecuta sin errores

### Sockets
- [ ] Servidor acepta conexiones TCP
- [ ] Protocolo JSON parsea correctamente
- [ ] Múltiples clientes concurrentes funcionan
- [ ] Desconexiones manejadas sin crash
- [ ] Todas las operaciones CRUD ejecutan
- [ ] Cliente Java ejecuta sin errores

### Base de Datos
- [ ] Todas las operaciones persisten datos
- [ ] Constraints (UNIQUE) respetadas
- [ ] Sin fugas de conexiones
- [ ] Transacciones correctas

---

**Documento preparado para validación académica**  
**Fecha:** 7 de noviembre de 2025  
**Proyecto:** Sistema CRUD Multiservicio con SOAP, gRPC y Sockets
