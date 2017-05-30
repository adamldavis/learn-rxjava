package learn_rxjava;

import io.reactivex.*;
import io.reactivex.schedulers.Schedulers;
import java.util.*;

public class Flows {

    public static List doSquares() {
        List squares = new ArrayList();
        Flowable.range(1, 64)
          .observeOn(Schedulers.computation())
          .map(v -> v * v)
          .blockingSubscribe(squares::add);
        
        return squares;
    }
    
    public static List doParallelSquares() {
        List squares = new ArrayList();
        Flowable.range(1, 64)
            .flatMap(v ->
              Flowable.just(v)
                .subscribeOn(Schedulers.computation())
                .map(w -> w * w)
            )
            .blockingSubscribe(squares::add);
            
        return squares;
    }
            
    public static void runComputation() throws Exception {
        StringBuffer sb = new StringBuffer();
        Flowable<String> source = Flowable.fromCallable(() -> {
            Thread.sleep(1000); //  imitate expensive computation
            return "Done";
        });
        Flowable<String> runBackground = source.subscribeOn(Schedulers.io());

        Flowable<String> showForeground = runBackground.observeOn(Schedulers.single());

        showForeground.subscribe(System.out::println, Throwable::printStackTrace);
        
        //Thread.sleep(2000);
    }
}
