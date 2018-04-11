package learn_rxjava;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.processors.AsyncProcessor;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.processors.UnicastProcessor;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Processor;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.*;
import java.util.*;

public class Flows {

    public static List doSquares() {
        List squares = new ArrayList();
        Flowable.range(1, 64)
            .observeOn(Schedulers.computation())
            .map(v -> v * v)
            .doOnError(ex -> ex.printStackTrace())
            .doOnComplete(() -> System.out.println("Completed doSquares"))
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
            .doOnError(ex -> ex.printStackTrace())
            .doOnComplete(() -> System.out.println("Completed doParallelSquares"))
            .blockingSubscribe(squares::add);
            
        return squares;
    }
            
    public static void runComputation() throws Exception {
        StringBuffer sb = new StringBuffer();
        Flowable<String> source = Flowable.fromCallable(() -> {
            Thread.sleep(1000); //  imitate expensive computation
            return "Done";
        });
        source.doOnComplete(() -> System.out.println("Completed runComputation"));

        Flowable<String> runBackground = source.subscribeOn(Schedulers.io());

        Flowable<String> showForeground = runBackground.observeOn(Schedulers.single());

        showForeground.subscribe(System.out::println, Throwable::printStackTrace);

        //Thread.sleep(2000);
    }

    public static void writeFile(File file) {
        try (PrintWriter pw = new PrintWriter(file)) {
            Flowable.range(1, 100)
                .observeOn(Schedulers.io())
                .blockingSubscribe(pw::println);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void readFile(File file) {
        try (final BufferedReader br = new BufferedReader(new FileReader(file))) {

            Flowable<String> flowable = Flowable.fromPublisher(new FilePublisher(br));

            flowable.observeOn(Schedulers.io())
                    .blockingSubscribe(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readFileWithProcessor(File file) {
        try (final BufferedReader br = new BufferedReader(new FileReader(file))) {

            UnicastProcessor processor = UnicastProcessor.create();
            Flowable.fromPublisher(new FilePublisher(br))
                    .subscribeOn(Schedulers.io())
                    .subscribe(processor);
            processor.observeOn(Schedulers.single())
                    .blockingSubscribe(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class FilePublisher implements Publisher<String> {
        BufferedReader reader;
        public FilePublisher(BufferedReader reader) { this.reader = reader; }
        @Override
        public void subscribe(Subscriber<? super String> subscriber) {
            subscriber.onSubscribe(new FilePublisherSubscription(this, subscriber));
        }
        public String readLine() throws IOException {
            return reader.readLine();
        }
    }

    static class FilePublisherSubscription implements Subscription {
        FilePublisher publisher;
        Subscriber<? super String> subscriber;
        public FilePublisherSubscription( FilePublisher pub, Subscriber<? super String> sub) {
            this.publisher = pub;
            this.subscriber = sub;
        }
        @Override
        public void request(long n) {
            try {
                String line;
                for (int i = 0; i < n && publisher != null && (line = publisher.readLine()) != null; i++) {
                    if (subscriber != null) subscriber.onNext(line);
                }
            } catch (IOException ex) {
                subscriber.onError(ex);
            }
            subscriber.onComplete();
        }
        @Override
        public void cancel() {
            publisher = null;
        }
    }
}
