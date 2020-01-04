package dev.voleum.ordermolder.helpers;

import io.reactivex.Single;

public class Exchanger {

    public static Single<String> exchange() {
        return Single.create(subscriber -> {
            ConnectionHelper connection = new ConnectionHelper();
            subscriber.onSuccess(connection.exchange());
        });
    }
}
