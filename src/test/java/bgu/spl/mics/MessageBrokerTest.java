package bgu.spl.mics;

import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.events.GadgetAvailableEvent;
import bgu.spl.mics.events.TickBroadcast;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Queue;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

public class MessageBrokerTest {
   MessageBroker broker;

    @BeforeEach
    public void setUp() {
        broker = MessageBrokerImpl.getInstance();
    }

    @Test
    public void getInstancetest() {
        assertNotEquals(null, broker);
    }

    @Test
    public void subscribeEventtest()   {
        Subscriber sb = new M("m",1);
        GadgetAvailableEvent event = new GadgetAvailableEvent("moo");
        broker.subscribeEvent(event.getClass(), sb);
    }



    @Test
    public void registertest() throws InterruptedException {
        Subscriber sb = new M("M",1);
        broker.register(sb);
        broker.subscribeBroadcast(TickBroadcast.class,sb);
        broker.sendBroadcast(new TickBroadcast(1));
        Message  ms =  broker.awaitMessage(sb);
        assertTrue(ms!=null);
    }

    @Test
    public void unregistertest() throws InterruptedException {
        Subscriber sb = new M("M",1);
        broker.subscribeBroadcast(TickBroadcast.class,sb);
        broker.unregister(sb);
        broker.subscribeBroadcast(TickBroadcast.class,sb);
        broker.sendBroadcast(new TickBroadcast(1));
        Message  ms1 =  broker.awaitMessage(sb);
        assertTrue(ms1==null);
    }

    @Test
    public void awaitMessagetest() throws InterruptedException {
        Subscriber sb = new M("M",1);
        broker.register(sb);
        broker.subscribeBroadcast(TickBroadcast.class,sb);
        broker.sendBroadcast(new TickBroadcast(1));
        sleep(30);
        Message  ms =  broker.awaitMessage(sb);
        assertTrue(ms!=null);
    }


    public void completeTest() {
        GadgetAvailableEvent gd = new GadgetAvailableEvent("test");
        Future fut = broker.sendEvent(gd);
        assertTrue(!fut.isDone()); //not resolved
        broker.complete(gd,"blabla");
        assertTrue(fut.isDone()); //resolved
    }


}
