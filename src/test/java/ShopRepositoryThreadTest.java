import mcc.ProductOuterClass;
import mcc.ShopRepository;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShopRepositoryThreadTest {

    @Test
    public void testParalleleKaufen() throws Exception {
        ShopRepository repo = new ShopRepository();
        ProductOuterClass.NewProductRequest newProductRequest=ProductOuterClass.NewProductRequest.newBuilder()
                .setName("Test Produkt")
                .setPrice(23.99)
                .setCategory(ProductOuterClass.Category.T_SHIRT)
                .setStock(5).build();
        repo.createProduct(newProductRequest);
        String pID="0";

        int threads=12;
        ExecutorService executorService= Executors.newFixedThreadPool(threads);
        CountDownLatch countDownLatch=new CountDownLatch(threads);
        AtomicInteger errorCount=new AtomicInteger(0);
        AtomicInteger successCount=new AtomicInteger(0);
        for(int i=0;i<threads;i++){
            executorService.submit(()->{
                try {
                    ProductOuterClass.ProductResponse response=repo.buyProduct(pID,1);
                    if (response.getSuccess()){
                        successCount.incrementAndGet();
                    }
                    else {
                        errorCount.incrementAndGet();
                    }
                }
                finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        executorService.shutdown();

        ProductOuterClass.Product finalProduct=repo.getProduct(pID);
        assertEquals(5,successCount.get(),"Es sollten nur 5 Kaufe erfolgen");
        assertEquals(7, errorCount.get(),"es sollten 7 fehschlagen werden");
  assertEquals(0,finalProduct.getStock(),"Lagerbestand sollte 0 sein");
    }

}
