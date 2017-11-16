import io.reactivex.*;

import learn_rxjava.*;

import java.io.File;

public class HelloWorld {
    public static void main(String[] args) {
        try {
            Flows.runComputation(); // asynch
            
            Flowable.just("Hello world").subscribe(System.out::println);
        
            Flowable.just(Flows.doSquares()).subscribe(System.out::println);
            
            Flowable.just(Flows.doParallelSquares()).subscribe(System.out::println);
    
            File f = new File("test");
            Flows.writeFile(f);
            Thread.sleep(1000); // allows computation to finish

            Flows.readFile(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

