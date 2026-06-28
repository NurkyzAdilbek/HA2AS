package mcc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class CustomerClient {
    public static void main(String[] args) {
        ManagedChannel channel= ManagedChannelBuilder
                .forAddress("Localhost",50051)
                .usePlaintext()
                .build();

        CustomerServiceGrpc.CustomerServiceBlockingStub stub = CustomerServiceGrpc.newBlockingStub(channel);

        ProductOuterClass.ProductList productList=stub.listProducts(
                ProductOuterClass.Empty.newBuilder().build());

        System.out.println("Verf[gbare Produkte: ");
        for(ProductOuterClass.Product product:productList.getProductsList()){
            System.out.println(product);
        }


        ProductOuterClass.BuyProductRequest buyRequest=ProductOuterClass.BuyProductRequest.newBuilder()
                .setId("0")
                .setMenge(2)
                .build();
        ProductOuterClass.ProductResponse response=stub.buyProduct(buyRequest);
        System.out.println(" Antwort vom SErver: " + response.getMessage());
        channel.shutdownNow();
    }
}
