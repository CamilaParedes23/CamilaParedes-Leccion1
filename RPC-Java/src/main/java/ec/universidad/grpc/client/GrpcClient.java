package ec.universidad.grpc.client;

import ec.universidad.grpc.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.concurrent.TimeUnit;

/**
 * Cliente gRPC para probar el servicio de clientes
 */
public class GrpcClient {
    
    private final ManagedChannel channel;
    private final ClienteServiceGrpc.ClienteServiceBlockingStub blockingStub;

    public GrpcClient(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        this.blockingStub = ClienteServiceGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void probarServicios() {
        try {
            System.out.println("╔═══════════════════════════════════════════════════╗");
            System.out.println("║   CLIENTE gRPC - GESTIÓN DE CLIENTES             ║");
            System.out.println("║   Consumidor de Servicio RPC                     ║");
            System.out.println("╚═══════════════════════════════════════════════════╝");
            System.out.println();
            System.out.println("=== INICIANDO PRUEBAS DEL SERVICIO gRPC ===\n");

            // 0. Ping
            System.out.println("→ 0. Verificando servicio...");
            PingResponse pingResponse = blockingStub.ping(Empty.newBuilder().build());
            System.out.println("  ✓ " + pingResponse.getMessage() + "\n");

            // 1. Crear Cliente
            System.out.println("→ 1. Creando nuevo cliente...");
            CrearClienteRequest crearRequest = CrearClienteRequest.newBuilder()
                    .setCedula("1234567890")
                    .setNombre("Ana Martínez López")
                    .setTelefono("0987654321")
                    .setEmail("ana.martinez@email.com")
                    .setDireccion("Calle Principal 123, Quito")
                    .build();
            
            ClienteResponse clienteCreado = blockingStub.crearCliente(crearRequest);
            System.out.println("  ✓ Cliente creado con ID: " + clienteCreado.getIdCliente());
            System.out.println("    Nombre: " + clienteCreado.getNombre() + "\n");

            // 2. Obtener Todos los Clientes
            System.out.println("→ 2. Obteniendo todos los clientes...");
            ClientesResponse todosClientes = blockingStub.obtenerTodosClientes(Empty.newBuilder().build());
            System.out.println("  ✓ Se encontraron " + todosClientes.getClientesCount() + " clientes");
            for (int i = 0; i < Math.min(3, todosClientes.getClientesCount()); i++) {
                ClienteResponse c = todosClientes.getClientes(i);
                System.out.println("    - [" + c.getIdCliente() + "] " + c.getNombre());
            }
            System.out.println();

            // 3. Obtener Cliente por ID
            System.out.println("→ 3. Obteniendo cliente por ID " + clienteCreado.getIdCliente() + "...");
            IdRequest idRequest = IdRequest.newBuilder()
                    .setId(clienteCreado.getIdCliente())
                    .build();
            ClienteResponse clientePorId = blockingStub.obtenerClientePorId(idRequest);
            System.out.println("  ✓ Cliente encontrado: " + clientePorId.getNombre());
            System.out.println("    Email: " + clientePorId.getEmail() + "\n");

            // 4. Obtener Cliente por Cédula
            System.out.println("→ 4. Obteniendo cliente por cédula...");
            CedulaRequest cedulaRequest = CedulaRequest.newBuilder()
                    .setCedula("1234567890")
                    .build();
            ClienteResponse clientePorCedula = blockingStub.obtenerClientePorCedula(cedulaRequest);
            System.out.println("  ✓ Cliente encontrado: " + clientePorCedula.getNombre() + "\n");

            // 5. Actualizar Cliente
            System.out.println("→ 5. Actualizando cliente ID " + clienteCreado.getIdCliente() + "...");
            ActualizarClienteRequest actualizarRequest = ActualizarClienteRequest.newBuilder()
                    .setIdCliente(clienteCreado.getIdCliente())
                    .setCedula("1234567890")
                    .setNombre("Ana Martínez García")
                    .setTelefono("0987999888")
                    .setEmail("ana.garcia@email.com")
                    .setDireccion("Calle Principal 123, Oficina 2B, Quito")
                    .build();
            
            ClienteResponse clienteActualizado = blockingStub.actualizarCliente(actualizarRequest);
            System.out.println("  ✓ Cliente actualizado: " + clienteActualizado.getNombre());
            System.out.println("    Nuevo teléfono: " + clienteActualizado.getTelefono() + "\n");

            // 6. Eliminar Cliente
            System.out.println("→ 6. Eliminando cliente ID " + clienteCreado.getIdCliente() + "...");
            DeleteResponse deleteResponse = blockingStub.eliminarCliente(idRequest);
            System.out.println("  ✓ " + deleteResponse.getMessage() + "\n");

            System.out.println("═══════════════════════════════════════════════════");
            System.out.println("  ✓ PRUEBAS COMPLETADAS EXITOSAMENTE");
            System.out.println("═══════════════════════════════════════════════════");

        } catch (StatusRuntimeException e) {
            System.err.println("✗ Error RPC: " + e.getStatus());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GrpcClient client = new GrpcClient("localhost", 5001);
        
        try {
            client.probarServicios();
        } catch (Exception e) {
            System.err.println("\n✗ Error: " + e.getMessage());
            System.err.println("\nAsegúrate de que:");
            System.err.println("  1. El servidor gRPC esté ejecutándose (GrpcServer.java)");
            System.err.println("  2. El servidor esté escuchando en el puerto 5001");
            System.err.println("  3. MySQL esté ejecutándose");
            e.printStackTrace();
        } finally {
            try {
                client.shutdown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
