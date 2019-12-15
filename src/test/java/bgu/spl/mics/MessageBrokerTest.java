package bgu.spl.mics;

import bgu.spl.mics.application.subscribers.M;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

public class MessageBrokerTest {
    MessageBroker broker;
/*
    @BeforeEach
    public void setUp() {
        broker = MessageBrokerImpl.getInstance();
    }

    @Test
    public void getInstancetest() {
        assertNotEquals(null, broker);
    }


    @Test
    public void subscribeEventtest() throws InterruptedException {
        Subscriber sb = new M();
        MissionReceivedEvent event = new MissionReceivedEvent("moo");
        broker.subscribeEvent(event.getClass(), sb);
        List<Subscriber> ls = broker.getMissionEventSubscribers(); //method that return the sub that subscribed to MissionEvent
        assertTrue(ls.contains(sb));
    }

    @Test
    public void subscribeBroadcasttest() throws InterruptedException {
        Subscriber sb = new M();
        TickBroadcast tick = new TickBroadcast();
        broker.subscribeBroadcast(tick.getClass(),sb);
        List<Subscriber> ls = broker.getBroadcastSubscribers(); //method that return the sub that subscribed to broadcast
        assertTrue(ls.contains(sb));
    }

    @Test
    public void sendBroadcasttest() {
        Broadcast tick = new TickBroadcast();
        Subscriber sb = new M();
        broker.subscribeBroadcast(TickBroadcast.class,sb);
        broker.sendBroadcast(tick);
        Queue<Event> qu = broker.getSubQueue(sb); //method that return the queue of the specific subscriber
        assertTrue(qu.peek().equals(tick));
    }

    @Test
    public void sendEventtest() {
        Subscriber sb = new M();
        broker.subscribeEvent(MissionReceivedEvent.class,sb);
        MissionReceivedEvent mre=  new MissionReceivedEvent("mission msg");
        Future future = broker.sendEvent(mre);
        Queue<Event> qu = broker.getSubQueue(sb);//method that return the queue of the specific subscriber
        assertTrue(qu.peek().equals(mre));
    }

    @Test
    public void registertest() {
        Subscriber sb = new M();
        broker.register(sb);
        Queue<Event> qu = broker.getSubQueue(sb); //method that return the queue of the specific subscriber
        assertTrue(qu!=null);
    }

    @Test
    public void unregistertest() {
        Subscriber sb = new M();
        broker.unregister(sb);
        Queue<Event> qu = broker.getSubQueue(sb); //method that return the queue of the specific subscriber
        assertTrue(qu==null);
    }

    @Test
    public void awaitMessagetest() throws InterruptedException {
        Subscriber sb = new M();
        broker.subscribeBroadcast(TickBroadcast.class,sb);
        broker.sendBroadcast(new TickBroadcast());
        Message  ms =  broker.awaitMessage(sb);
        assertTrue(ms!=null);
    }


    public void completeTest() {
        MissionReceivedEvent ms = new MissionReceivedEvent("test");
        Future future = new Future();
        broker.addEventAndFuturePair(ms,future); // add the event his future to the data structure
        assertTrue(!future.isDone()); //not resolved
        broker.complete(ms,"blabla");
        assertTrue(future.isDone()); //resolved
    }
    */

}
