package bgu.spl.mics;

import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/**
 * The Subscriber is an abstract class that any subscriber in the system
 * must extend. The abstract Subscriber class is responsible to get and
 * manipulate the singleton {@link MessageBroker} instance.
 * <p>
 * Derived classes of Subscriber should never directly touch the MessageBroker.
 * the derived class also supplies a {@link Callback} that should be called when
 * a message of the subscribed type was taken from the Subscriber
 * message-queue (see {@link MessageBroker#register(Subscriber)}
 * method). The abstract Subscriber stores this callback together with the
 * type of the message is related to.
 * 
 * Only private fields and methods may be added to this class.
 * <p>
 */
public abstract class Subscriber extends RunnableSubPub {
    private boolean terminated = false;
    private LogManager logM = LogManager.getInstance();
    private HashMap<Class,Callback> callbackmap;

    /**
     * @param name the Subscriber name (used mainly for debugging purposes -
     *             does not have to be unique)
     */
    public Subscriber(String name) {
        super(name);
        callbackmap = new HashMap<Class, Callback>();
    }

    /**
     * Subscribes to events of type {@code type} with the callback
     * {@code callback}. This means two things:
     * 1. Subscribe to events in the singleton MessageBroker using the supplied
     * {@code type}
     * 2. Store the {@code callback} so that when events of type {@code type}
     * are received it will be called.
     * <p>
     * For a received message {@code m} of type {@code type = m.getClass()}
     * calling the callback {@code callback} means running the method
     * {@link Callback#call(java.lang.Object)} by calling
     * {@code callback.call(m)}.
     * <p>
     * @param <E>      The type of event to subscribe to.
     * @param <T>      The type of result expected for the subscribed event.
     * @param type     The {@link Class} representing the type of event to
     *                 subscribe to.
     * @param callback The callback that should be called when messages of type
     *                 {@code type} are taken from this Subscriber message
     *                 queue.
     */
    protected synchronized final <T, E extends Event<T>> void subscribeEvent(Class<E> type, Callback<E> callback) {
        MessageBrokerImpl.getInstance().subscribeEvent(type,this);
        callbackmap.put(type,callback);
    }

    /**
     * Subscribes to broadcast message of type {@code type} with the callback
     * {@code callback}. This means two things:
     * 1. Subscribe to broadcast messages in the singleton MessageBroker using the
     * supplied {@code type}
     * 2. Store the {@code callback} so that when broadcast messages of type
     * {@code type} received it will be called.
     * <p>
     * For a received message {@code m} of type {@code type = m.getClass()}
     * calling the callback {@code callback} means running the method
     * {@link Callback#call(java.lang.Object)} by calling
     * {@code callback.call(m)}.
     * <p>
     * @param <B>      The type of broadcast message to subscribe to
     * @param type     The {@link Class} representing the type of broadcast
     *                 message to subscribe to.
     * @param callback The callback that should be called when messages of type
     *                 {@code type} are taken from this Subscriber message
     *                 queue.
     */
    protected final <B extends Broadcast> void subscribeBroadcast(Class<B> type, Callback<B> callback) {
        MessageBrokerImpl.getInstance().subscribeBroadcast(type,this);
        callbackmap.put(type,callback);
    }

    /**
     * Completes the received request {@code e} with the result {@code result}
     * using the MessageBroker.
     * <p>
     * @param <T>    The type of the expected result of the processed event
     *               {@code e}.
     * @param e      The event to complete.
     * @param result The result to resolve the relevant Future object.
     *               {@code e}.
     */
    protected final <T> void complete(Event<T> e, T result) {
        MessageBrokerImpl.getInstance().complete(e,result);
    }

    /**
     * Signals the event loop that it must terminate after handling the current
     * message.
     */
    protected synchronized final void terminate() {
        this.terminated = true;
        MessageBrokerImpl.getInstance().unregister(this);
    }

    /**
     * The entry point of the Subscriber. TODO: you must complete this code
     * otherwise you will end up in an infinite loop.
     */
    @Override
    public final void run() {

            initialize();


        while (!terminated) {
            Message msg = null;

            try {
                msg = MessageBrokerImpl.getInstance().awaitMessage(this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (msg!=null)
                logM.log.info("msg " + msg.getClass() + " received to subscriber " + this.getName());

            if (msg!=null && callbackmap.containsKey(msg.getClass())) {
                logM.log.info("callback was found, calling call");
                try {
                    callbackmap.get(msg.getClass()).call(msg);//dont forget msg contains reference to future
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else {
                if(msg !=null)
                    logM.log.warning("Callback not found");
                else
                    logM.log.warning("msg is null - terminating");
            }
        }
    }

}
