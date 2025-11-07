-- ============================================
-- Script de Base de Datos
-- Sistema CRUD Multiservicio
-- Gestión de Clientes
-- ============================================

-- Eliminar base de datos si existe (cuidado en producción)
DROP DATABASE IF EXISTS gestion_clientes;

-- Crear base de datos
CREATE DATABASE IF NOT EXISTS gestion_clientes CHARACTER
SET
    utf8mb4 COLLATE utf8mb4_unicode_ci;

USE gestion_clientes;

-- ============================================
-- Tabla: clientes
-- ============================================
CREATE TABLE clientes (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY COMMENT 'Clave primaria autoincremental',
    cedula VARCHAR(20) UNIQUE NOT NULL COMMENT 'Cédula de identidad única',
    nombre VARCHAR(100) NOT NULL COMMENT 'Nombre completo del cliente',
    telefono VARCHAR(20) COMMENT 'Número de teléfono',
    email VARCHAR(100) COMMENT 'Correo electrónico',
    direccion VARCHAR(200) COMMENT 'Dirección física',
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha y hora de registro',

-- Índices para mejorar rendimiento
INDEX idx_cedula (cedula),
    INDEX idx_nombre (nombre),
    INDEX idx_fecha (fecha_registro)
    
) ENGINE=InnoDB 
  DEFAULT CHARSET=utf8mb4 
  COLLATE=utf8mb4_unicode_ci
  COMMENT='Tabla de clientes del sistema';

-- ============================================
-- Datos de Prueba
-- ============================================

INSERT INTO
    clientes (
        cedula,
        nombre,
        telefono,
        email,
        direccion
    )
VALUES (
        '1234567890',
        'Juan Pérez García',
        '0991234567',
        'juan.perez@email.com',
        'Av. 10 de Agosto 234, Quito'
    ),
    (
        '0987654321',
        'María Rodríguez López',
        '0987654321',
        'maria.rodriguez@email.com',
        'Calle Principal 123, Guayaquil'
    ),
    (
        '1122334455',
        'Carlos Sánchez Torres',
        '0998877665',
        'carlos.sanchez@email.com',
        'Av. Amazonas 456, Cuenca'
    ),
    (
        '5566778899',
        'Laura Morales Vega',
        '0993344556',
        'laura.morales@email.com',
        'Calle Flores 321, Loja'
    ),
    (
        '9988776655',
        'Ana Martínez Ruiz',
        '0996655443',
        'ana.martinez@email.com',
        'Av. República 789, Ambato'
    ),
    (
        '1357924680',
        'Pedro González Díaz',
        '0995544332',
        'pedro.gonzalez@email.com',
        'Calle Bolívar 567, Riobamba'
    ),
    (
        '2468135790',
        'Sofía Ramírez Castro',
        '0994433221',
        'sofia.ramirez@email.com',
        'Av. Colón 890, Quito'
    ),
    (
        '3691472580',
        'Diego Torres Flores',
        '0997766554',
        'diego.torres@email.com',
        'Calle Sucre 234, Guayaquil'
    ),
    (
        '7412589630',
        'Carmen López Herrera',
        '0992233445',
        'carmen.lopez@email.com',
        'Av. América 456, Quito'
    ),
    (
        '8523697410',
        'Roberto Vásquez Mora',
        '0998877664',
        'roberto.vasquez@email.com',
        'Calle Pichincha 678, Cuenca'
    );

-- ============================================
-- Verificación de Datos
-- ============================================

-- Mostrar todos los registros insertados
SELECT
    id_cliente,
    cedula,
    nombre,
    telefono,
    email,
    direccion,
    fecha_registro
FROM clientes
ORDER BY id_cliente;

-- Contar total de registros
SELECT COUNT(*) AS total_clientes FROM clientes;

-- ============================================
-- Consultas de Ejemplo
-- ============================================

-- Buscar por cédula
-- SELECT * FROM clientes WHERE cedula = '1234567890';

-- Buscar por ID
-- SELECT * FROM clientes WHERE id_cliente = 1;

-- Buscar por nombre (like)
-- SELECT * FROM clientes WHERE nombre LIKE '%Juan%';

-- Obtener clientes registrados hoy
-- SELECT * FROM clientes WHERE DATE(fecha_registro) = CURDATE();

-- ============================================
-- Información del Usuario para Conexión
-- ============================================

-- Usuario: root
-- Contraseña: (vacía o la configurada)
-- Host: localhost
-- Puerto: 3306
-- Base de datos: gestion_clientes

-- Cadena de conexión Java:
-- jdbc:mysql://localhost:3306/gestion_clientes?user=root&password=&useSSL=false&serverTimezone=UTC

-- ============================================
-- Notas
-- ============================================

/*
1. Esta base de datos es compartida por los tres servicios:
- SOAP (Puerto 8080)
- gRPC (Puerto 5001)
- Sockets (Puerto 5002)

2. El campo 'cedula' tiene constraint UNIQUE para evitar duplicados

3. El campo 'fecha_registro' se genera automáticamente con CURRENT_TIMESTAMP

4. Todos los servicios implementan las mismas operaciones CRUD:
- Crear (INSERT)
- Obtener Todos (SELECT *)
- Obtener por ID (SELECT WHERE id_cliente = ?)
- Obtener por Cédula (SELECT WHERE cedula = ?)
- Actualizar (UPDATE)
- Eliminar (DELETE)

5. Para pruebas, puedes ejecutar:
mysql -u root -p < database/schema.sql
*/