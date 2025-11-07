package ec.universidad.grpc.service;

import ec.universidad.grpc.*;
import ec.universidad.grpc.models.Cliente;
import ec.universidad.grpc.repository.ClienteRepository;
import io.grpc.stub.StreamObserver;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Implementación del servicio gRPC para gestión de clientes
 */
public class ClienteServiceImpl extends ClienteServiceGrpc.ClienteServiceImplBase {
    
    private final ClienteRepository repository;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ClienteServiceImpl(String connectionString) {
        this.repository = new ClienteRepository(connectionString);
    }

    @Override
    public void crearCliente(CrearClienteRequest request, StreamObserver<ClienteResponse> responseObserver) {
        try {
            Cliente cliente = new Cliente();
            cliente.setCedula(request.getCedula());
            cliente.setNombre(request.getNombre());
            cliente.setTelefono(request.getTelefono());
            cliente.setEmail(request.getEmail());
            cliente.setDireccion(request.getDireccion());
            cliente.setFechaRegistro(new Date());
            
            int id = repository.crear(cliente);
            cliente.setIdCliente(id);
            
            ClienteResponse response = mapearAClienteResponse(cliente);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
            System.out.println("✓ Cliente creado con ID: " + id);
            
        } catch (Exception e) {
            System.err.println("✗ Error al crear cliente: " + e.getMessage());
            responseObserver.onError(e);
        }
    }

    @Override
    public void obtenerTodosClientes(Empty request, StreamObserver<ClientesResponse> responseObserver) {
        try {
            List<Cliente> clientes = repository.obtenerTodos();
            
            ClientesResponse.Builder builder = ClientesResponse.newBuilder();
            for (Cliente cliente : clientes) {
                builder.addClientes(mapearAClienteResponse(cliente));
            }
            
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
            
            System.out.println("✓ Se obtuvieron " + clientes.size() + " clientes");
            
        } catch (Exception e) {
            System.err.println("✗ Error al obtener clientes: " + e.getMessage());
            responseObserver.onError(e);
        }
    }

    @Override
    public void obtenerClientePorId(IdRequest request, StreamObserver<ClienteResponse> responseObserver) {
        try {
            Cliente cliente = repository.obtenerPorId(request.getId());
            
            if (cliente == null) {
                responseObserver.onError(new RuntimeException("Cliente no encontrado"));
                return;
            }
            
            ClienteResponse response = mapearAClienteResponse(cliente);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
            System.out.println("✓ Cliente encontrado: " + cliente.getNombre());
            
        } catch (Exception e) {
            System.err.println("✗ Error al obtener cliente: " + e.getMessage());
            responseObserver.onError(e);
        }
    }

    @Override
    public void obtenerClientePorCedula(CedulaRequest request, StreamObserver<ClienteResponse> responseObserver) {
        try {
            Cliente cliente = repository.obtenerPorCedula(request.getCedula());
            
            if (cliente == null) {
                responseObserver.onError(new RuntimeException("Cliente no encontrado"));
                return;
            }
            
            ClienteResponse response = mapearAClienteResponse(cliente);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
            System.out.println("✓ Cliente encontrado: " + cliente.getNombre());
            
        } catch (Exception e) {
            System.err.println("✗ Error al obtener cliente: " + e.getMessage());
            responseObserver.onError(e);
        }
    }

    @Override
    public void actualizarCliente(ActualizarClienteRequest request, StreamObserver<ClienteResponse> responseObserver) {
        try {
            Cliente cliente = new Cliente();
            cliente.setIdCliente(request.getIdCliente());
            cliente.setCedula(request.getCedula());
            cliente.setNombre(request.getNombre());
            cliente.setTelefono(request.getTelefono());
            cliente.setEmail(request.getEmail());
            cliente.setDireccion(request.getDireccion());
            
            boolean actualizado = repository.actualizar(cliente);
            
            if (!actualizado) {
                responseObserver.onError(new RuntimeException("Cliente no encontrado"));
                return;
            }
            
            Cliente clienteActualizado = repository.obtenerPorId(request.getIdCliente());
            ClienteResponse response = mapearAClienteResponse(clienteActualizado);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
            System.out.println("✓ Cliente actualizado: " + cliente.getNombre());
            
        } catch (Exception e) {
            System.err.println("✗ Error al actualizar cliente: " + e.getMessage());
            responseObserver.onError(e);
        }
    }

    @Override
    public void eliminarCliente(IdRequest request, StreamObserver<DeleteResponse> responseObserver) {
        try {
            boolean eliminado = repository.eliminar(request.getId());
            
            DeleteResponse response = DeleteResponse.newBuilder()
                    .setSuccess(eliminado)
                    .setMessage(eliminado ? "Cliente eliminado correctamente" : "Cliente no encontrado")
                    .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
            if (eliminado) {
                System.out.println("✓ Cliente eliminado con ID: " + request.getId());
            }
            
        } catch (Exception e) {
            System.err.println("✗ Error al eliminar cliente: " + e.getMessage());
            responseObserver.onError(e);
        }
    }

    @Override
    public void ping(Empty request, StreamObserver<PingResponse> responseObserver) {
        PingResponse response = PingResponse.newBuilder()
                .setMessage("Servicio gRPC de Clientes funcionando correctamente")
                .build();
        
        responseObserver.onNext(response);
        responseObserver.onCompleted();
        
        System.out.println("✓ Ping recibido");
    }

    /**
     * Mapear Cliente a ClienteResponse
     */
    private ClienteResponse mapearAClienteResponse(Cliente cliente) {
        return ClienteResponse.newBuilder()
                .setIdCliente(cliente.getIdCliente())
                .setCedula(cliente.getCedula())
                .setNombre(cliente.getNombre())
                .setTelefono(cliente.getTelefono())
                .setEmail(cliente.getEmail())
                .setDireccion(cliente.getDireccion())
                .setFechaRegistro(dateFormat.format(cliente.getFechaRegistro()))
                .build();
    }
}
