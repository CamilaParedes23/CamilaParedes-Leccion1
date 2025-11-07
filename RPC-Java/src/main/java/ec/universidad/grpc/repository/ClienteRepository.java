package ec.universidad.grpc.repository;

import ec.universidad.grpc.models.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio para operaciones CRUD de Cliente
 */
public class ClienteRepository {
    private final String connectionString;

    public ClienteRepository(String connectionString) {
        this.connectionString = connectionString;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionString);
    }

    /**
     * Crear un nuevo cliente
     */
    public int crear(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO clientes (cedula, nombre, telefono, email, direccion, fecha_registro) " +
                     "VALUES (?, ?, ?, ?, ?, NOW())";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, cliente.getCedula());
            stmt.setString(2, cliente.getNombre());
            stmt.setString(3, cliente.getTelefono());
            stmt.setString(4, cliente.getEmail());
            stmt.setString(5, cliente.getDireccion());
            
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException("Error al obtener el ID generado");
        }
    }

    /**
     * Obtener todos los clientes
     */
    public List<Cliente> obtenerTodos() throws SQLException {
        String sql = "SELECT * FROM clientes ORDER BY id_cliente";
        List<Cliente> clientes = new ArrayList<>();
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                clientes.add(mapearCliente(rs));
            }
        }
        
        return clientes;
    }

    /**
     * Obtener cliente por ID
     */
    public Cliente obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE id_cliente = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearCliente(rs);
            }
            return null;
        }
    }

    /**
     * Obtener cliente por cédula
     */
    public Cliente obtenerPorCedula(String cedula) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE cedula = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cedula);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearCliente(rs);
            }
            return null;
        }
    }

    /**
     * Actualizar un cliente existente
     */
    public boolean actualizar(Cliente cliente) throws SQLException {
        String sql = "UPDATE clientes SET cedula = ?, nombre = ?, telefono = ?, " +
                     "email = ?, direccion = ? WHERE id_cliente = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cliente.getCedula());
            stmt.setString(2, cliente.getNombre());
            stmt.setString(3, cliente.getTelefono());
            stmt.setString(4, cliente.getEmail());
            stmt.setString(5, cliente.getDireccion());
            stmt.setInt(6, cliente.getIdCliente());
            
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Eliminar un cliente por ID
     */
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM clientes WHERE id_cliente = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Mapear ResultSet a objeto Cliente
     */
    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(rs.getInt("id_cliente"));
        cliente.setCedula(rs.getString("cedula"));
        cliente.setNombre(rs.getString("nombre"));
        cliente.setTelefono(rs.getString("telefono"));
        cliente.setEmail(rs.getString("email"));
        cliente.setDireccion(rs.getString("direccion"));
        cliente.setFechaRegistro(rs.getTimestamp("fecha_registro"));
        return cliente;
    }
}
