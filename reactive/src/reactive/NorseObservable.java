package reactive;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class NorseObservable {

    private static final String NORSE_WS = "ws://map.norsecorp.com/socketcluster/";

    public static Observable<Event> create() {
        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine engine = sem.getEngineByName("javascript");
        Observable<Event> obs = Observable.create(emitter -> {

            Function<String, Map<String, Map<String, List<Map<String, Object>>>>> parser = (String json) -> {
                String script = "Java.asJSONCompatible(" + json + ")";
                try {
                    return (Map<String, Map<String, List<Map<String, Object>>>>) engine.eval(script);
                } catch (ScriptException ex) {
                }
                return Collections.EMPTY_MAP; // parse problem
            };

            WebSocketClient socket = connect(
                    (String message) -> {
                        final Map<String, Map<String, List<Map<String, Object>>>> map = parser.apply(message);
                        if (!map.containsKey("event")) {
                            // System.out.println("Not an event message");
                            return;
                        }
                        if (!"#publish".equals(map.get("event"))) {
                            // System.out.println("Not a publish, event is " +
                            // map.get("event"));
                            return;
                        }
                        if (!map.containsKey("data")) {
                            return;
                        }
                        List<Map<String, Object>> data = map.get("data").get("data");
                        if (data == null) {
                            return;
                        }
                        final Map<String, Object> val = data.get(0);
                        emitter.onNext(Event.from(val));
                    },
                    close -> emitter.onComplete(), // System.out.println(String.format("close,
                                                   // status %s, reason %s,
                                                   // remote %b",
                                                   // close.getCode(),
                                                   // close.getReason(),
                                                   // close.isRemote())),
                    error -> emitter.onError(error),
                    handshake -> System.out.println(String.format("Handshake status: %s, message: %s",
                            handshake.getHttpStatus(), handshake.getHttpStatusMessage())));

            // disposable
            emitter.setDisposable(new Disposable() {
                private volatile boolean disposed;

                @Override
                public void dispose() {
                    socket.close();
                    this.disposed = true;
                }

                @Override
                public boolean isDisposed() {
                    return disposed;
                }
            });

        });
        return obs;
    }

    public static WebSocketClient connect(Consumer<String> messageHandler, Consumer<CloseMessage> closeHandler,
            Consumer<Exception> errorHandler, Consumer<ServerHandshake> handshakeHandler) {
        try {
            WebSocketClient socket = createSocket(Optional.of(messageHandler), Optional.of(closeHandler),
                    Optional.of(errorHandler), Optional.of(handshakeHandler));
            socket.connectBlocking();
            return socket;
        } catch (InterruptedException | MalformedURLException | URISyntaxException ex) {
            errorHandler.accept(ex);
        }
        return null;
    }

    public static WebSocketClient createSocket(Optional<Consumer<String>> messageHandler,
            Optional<Consumer<CloseMessage>> closeHandler, Optional<Consumer<Exception>> errorHandler,
            Optional<Consumer<ServerHandshake>> handshakeHandler) throws MalformedURLException, URISyntaxException {
        URI url = new URI(NORSE_WS);

        return new WebSocketClient(url, new Draft_10()) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {

                // initial handshake
                try {
                    this.send("{\"event\":\"#handshake\",\"data\":{\"authToken\":null},\"cid\":1}");
                    Thread.sleep(100L);
                    this.send("{\"event\":\"#subscribe\",\"data\":{\"channel\":\"global\"},\"cid\":2}");

                    handshakeHandler.ifPresent(h -> h.accept(handshakedata));
                } catch (InterruptedException ex) {
                }
            }

            @Override
            public void onMessage(String message) {
                // keep alive handshake
                if (message.equals("#1")) {
                    this.send("#2");
                    return;
                }
                messageHandler.ifPresent(c -> c.accept(message));
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                closeHandler.ifPresent(c -> c.accept(new CloseMessage(code, reason, remote)));
            }

            @Override
            public void onError(Exception ex) {
                errorHandler.ifPresent(e -> e.accept(ex));
            }
        };
    }

    public static class Event {
        public String id;
        public Number latitude;
        public Number longitude;
        public String countryCode;
        public String country;
        public String city;

        public Number latitude2;
        public Number longitude2;
        public String countryCode2;
        public String country2;
        public String city2;

        public String md5;
        public String dport;
        public String svc;
        public String type;
        public String org;

        public static Event from(Map<String, Object> val) {
            Event event = new Event();
            event.countryCode = String.valueOf(val.get("countrycode"));
            event.countryCode2 = String.valueOf(val.get("countrycode2"));
            event.city = String.valueOf(val.get("city"));
            event.city2 = String.valueOf(val.get("city2"));
            event.latitude = (Number) val.get("latitude");
            event.latitude2 = (Number) val.get("latitude2");
            event.longitude = (Number) val.get("longitude");
            event.longitude2 = (Number) val.get("longitude2");
            return event;
        }
    }

    public static class CloseMessage {
        private final int code;
        private final String reason;
        private final boolean remote;

        public CloseMessage(int code, String reason, boolean remote) {
            this.code = code;
            this.reason = reason;
            this.remote = remote;
        }

        public int getCode() {
            return code;
        }

        public String getReason() {
            return reason;
        }

        public boolean isRemote() {
            return remote;
        }
    }

}
