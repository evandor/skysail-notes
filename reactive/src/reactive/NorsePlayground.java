package reactive;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import reactive.NorseObservable.Event;

public class NorsePlayground {

    public static void main(String[] args) throws InterruptedException {
        Observable<Event> obs = NorseObservable.create().share();


        Disposable disposable = obs.subscribe(e -> System.out.println(e.city));
        Observable<String> cityStream = obs.map(e -> e.city);

        Disposable disposable1 = obs.subscribe();
        Disposable disposable2 = cityStream.subscribe();


        //cityStream.subscribe(e -> System.out.println(e));

        TimeUnit.SECONDS.sleep(10);
        disposable1.dispose();
        disposable2.dispose();
    }
}
