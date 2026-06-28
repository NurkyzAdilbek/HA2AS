package mcc;

import io.grpc.stub.StreamObserver;

public class AdminServiceImpl extends AdminServiceGrpc.AdminServiceImplBase {

private final ShopRepository repository;
public AdminServiceImpl(ShopRepository repository) {
    this.repository = repository;
}

    @Override
    public void createProduct(ProductOuterClass.NewProductRequest request,
                              StreamObserver<ProductOuterClass.ProductResponse> responseObserver) {
        ProductOuterClass.ProductResponse response=repository.createProduct(request);
    responseObserver.onNext(response);
    responseObserver.onCompleted();
}

    @Override
    public void deleteProduct(ProductOuterClass.ProductRequest request, StreamObserver<ProductOuterClass.ProductResponse> responseObserver) {
        ProductOuterClass.ProductResponse response=repository.deleteProduct(request.getId());
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getProduct(ProductOuterClass.ProductRequest request, StreamObserver<ProductOuterClass.Product> responseObserver) {
    ProductOuterClass.Product response=repository.getProduct(request.getId());
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getStatistik(ProductOuterClass.Empty request, StreamObserver<ProductOuterClass.StatistikResponse> responseObserver) {
      ProductOuterClass.StatistikResponse response=repository.getStatistik();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }

    @Override
    public void updateProduct(ProductOuterClass.UpdateProductRequest request, StreamObserver<ProductOuterClass.ProductResponse> responseObserver) {
    ProductOuterClass.ProductResponse response=repository.updateProduct(request);
    responseObserver.onNext(response);
    responseObserver.onCompleted();
    }

    @Override
    public void listProducts(ProductOuterClass.Empty request, StreamObserver<ProductOuterClass.ProductList> responseObserver) {
        ProductOuterClass.ProductList response=repository.products();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
