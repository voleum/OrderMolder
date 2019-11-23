package dev.voleum.ordermolder.helpers;

import io.reactivex.Completable;

public class Exchanger {

    // TODO: flowable? (connected, received, sent)
    public static Completable exchange() {
        return Completable.create(subscriber -> {
            ConnectionHelper connection = new ConnectionHelper();
            connection.exchange();
            subscriber.onComplete();
        });
    }
}
