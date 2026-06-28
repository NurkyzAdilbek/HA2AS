package mcc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class AdminClient {
    public static void main(String[] args) {
        ManagedChannel channel= ManagedChannelBuilder
                .forAddress("localhost",50051)
                .usePlaintext()
                .build();

        AdminServiceGrpc.AdminServiceBlockingStub stub = AdminServiceGrpc.newBlockingStub(channel);
        ProductOuterClass.NewProductRequest request=ProductOuterClass.NewProductRequest.newBuilder()
                .setName("T Shirt Schwarz")
                .setPrice(12.99)
                .setCategory(ProductOuterClass.Category.T_SHIRT)
                .setStock(123)
                .build();
        ProductOuterClass.ProductResponse response=stub.createProduct(request);
        System.out.println("Anwort vom Server "+response.getMessage());
        channel.shutdownNow();
    }
}
