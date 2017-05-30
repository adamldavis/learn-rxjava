import io.reactivex.*;

import learn_rxjava.*;

public class HelloWorld {
    public static void main(String[] args) {
        try {
            Flows.runComputation(); // asynch
            
            Flowable.just("Hello world").subscribe(System.out::println);
        
            Flowable.just(Flows.doSquares()).subscribe(System.out::println);
            
            Flowable.just(Flows.doParallelSquares()).subscribe(System.out::println);
    
            Thread.sleep(1000); // allows computation to finish
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

