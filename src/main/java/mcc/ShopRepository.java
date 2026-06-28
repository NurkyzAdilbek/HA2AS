package mcc;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ShopRepository {
    private final ConcurrentHashMap<String,ProductOuterClass.Product> products = new ConcurrentHashMap<>();
    private final AtomicInteger id = new AtomicInteger(0);
    private long verkaufteEinheit=0;
    private double gesamtUmsatz=0.0;
    public synchronized ProductOuterClass.ProductResponse createProduct(ProductOuterClass.NewProductRequest request){
        String newId=String.valueOf(id.getAndIncrement());
        ProductOuterClass.Product newProduct=ProductOuterClass.Product.newBuilder().
                setId(newId)
                .setName(request.getName())
                .setPrice(request.getPrice())
                .setCategory(request.getCategory())
                .setStock(request.getStock())
                .build();
        products.put(newId,newProduct);
        return ProductOuterClass.ProductResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Product wurde mit ID: "+newId+ " erstellt")
                .build();
    }
public synchronized ProductOuterClass.ProductResponse deleteProduct(String id){
        ProductOuterClass.Product deletedProduct=products.remove(id);
        return ProductOuterClass.ProductResponse.newBuilder()
                .setSuccess(deletedProduct!=null)
                .setMessage( deletedProduct !=null ? "Product deleted":"Product not found")
                .build();
}
public synchronized ProductOuterClass.Product getProduct(String id){

ProductOuterClass.Product product=products.get(id);
    return product !=null ? product:ProductOuterClass.Product.newBuilder().build();
}
public synchronized ProductOuterClass.ProductResponse updateProduct(ProductOuterClass.UpdateProductRequest request){
        ProductOuterClass.Product existing=products.get(request.getId());
        if(existing==null){
            return  ProductOuterClass.ProductResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Product nicht gefunden")
                    .build();}

            ProductOuterClass.Product updated=existing.toBuilder()
                    .setPrice(request.getPrice())
                    .setStock(request.getStock())
                    .build();
            products.put(request.getId(),updated);
            return  ProductOuterClass.ProductResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Product aktualiesiert")
                    .build();
}
public  synchronized ProductOuterClass.StatistikResponse getStatistik(){
        return ProductOuterClass.StatistikResponse.newBuilder()
                .setVerkaufteEinheit(verkaufteEinheit)
                .setGesamtUmsatz(gesamtUmsatz)
                .build();
}
public synchronized  ProductOuterClass.ProductList products(){
      return ProductOuterClass.ProductList.newBuilder()
              .addAllProducts(products.values()).build();
}
public synchronized ProductOuterClass.ProductResponse buyProduct(String id, int anzahl){
        ProductOuterClass.Product product=products.get(id);
        if(product==null){
            return  ProductOuterClass.ProductResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Product not found")
                    .build();
        }
        if(product.getStock()<anzahl){
            return ProductOuterClass.ProductResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Nicht genug Lagebestand")
                    .build();
        }
        ProductOuterClass.Product updated=product.toBuilder()
                .setStock(product.getStock()-anzahl)
                .build();
        products.put(id,updated);
        verkaufteEinheit+=anzahl;
        gesamtUmsatz+=anzahl*product.getPrice();

        return ProductOuterClass.ProductResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Kauf erfolgreich").build();
}
}
