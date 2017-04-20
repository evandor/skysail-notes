package reactive;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

public class Test {

    public static void main(String[] args) throws InterruptedException {
        new Test().run();
    }

    public void run() throws InterruptedException {
        // Iterator<String> iterator = Arrays.asList("hi","there").iterator();
//        Observable<String> observable = Observable.fromArray(new String[] { "Alice", "bob" });
//        observable.subscribe(
//                number -> System.out.println(number),
//                error -> System.out.println("error"),
//                () -> System.out.println("completed"));

        Observable<Long> observable2 = Observable.interval(1,  TimeUnit.SECONDS);

        observable2.subscribe(
                number -> System.out.println(number),
                error -> System.out.println("error"),
                () -> System.out.println("completed"));
        TimeUnit.SECONDS.sleep(10);
       // Disposable

    }

}
