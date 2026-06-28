package mcc;

import io.grpc.stub.StreamObserver;

public class CustomerServiceImpl extends CustomerServiceGrpc.CustomerServiceImplBase{
    private final ShopRepository repository;

    public CustomerServiceImpl(ShopRepository repository) {
        this.repository = repository;
    }

    @Override
    public void buyProduct(ProductOuterClass.BuyProductRequest request, StreamObserver<ProductOuterClass.ProductResponse> responseObserver) {
        ProductOuterClass.ProductResponse response=repository.buyProduct(request.getId(),request.getMenge());
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void listProducts(ProductOuterClass.Empty request, StreamObserver<ProductOuterClass.ProductList> responseObserver) {
       ProductOuterClass.ProductList response= repository.products();
       responseObserver.onNext(response);
       responseObserver.onCompleted();
    }
}
