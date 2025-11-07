package ec.universidad.grpc.server;

import ec.universidad.grpc.service.ClienteServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

/**
 * Servidor gRPC para el servicio de clientes
 */
public class GrpcServer {
    
    private static final int PORT = 5001;
    private Server server;

    public void start() throws IOException {
        String connectionString = "jdbc:mysql://localhost:3306/gestion_clientes?user=root&password=&useSSL=false&serverTimezone=UTC";
        
        server = ServerBuilder.forPort(PORT)
                .addService(new ClienteServiceImpl(connectionString))
                .build()
                .start();
        
        System.out.println("╔═══════════════════════════════════════════════════╗");
        System.out.println("║   SERVIDOR gRPC - GESTIÓN DE CLIENTES            ║");
        System.out.println("║   Remote Procedure Call sobre HTTP/2             ║");
        System.out.println("╚═══════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("✓ Servidor gRPC iniciado exitosamente");
        System.out.println("✓ Puerto: " + PORT);
        System.out.println("✓ Protocolo: gRPC (HTTP/2 + Protocol Buffers)");
        System.out.println();
        System.out.println("Esperando solicitudes de clientes...");
        System.out.println("Presiona Ctrl+C para detener el servidor");
        System.out.println("═══════════════════════════════════════════════════");
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("\n✓ Apagando servidor gRPC...");
            GrpcServer.this.stop();
            System.err.println("✓ Servidor detenido");
        }));
    }

    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final GrpcServer server = new GrpcServer();
        server.start();
        server.blockUntilShutdown();
    }
}
