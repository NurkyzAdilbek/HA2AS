package mcc;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        ShopRepository shopRepository = new ShopRepository();
        Server server = ServerBuilder.forPort(50051)
                .addService(new AdminServiceImpl(shopRepository))
                .addService(new CustomerServiceImpl(shopRepository))
                .build()
                .start();

        System.out.println("Server läuft auf Port 50051");

        server.awaitTermination();
    }
}