package bgu.spl.mics;

import bgu.spl.mics.application.subscribers.M;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    public void subscribeEventtest() {
        Subscriber sb = new M();
        MissionReceivedEvent event = new MissionReceivedEvent("moo");
        broker.subscribeEvent(event.getClass(), sb);
    }

    public <T> void completeTest() {
        Future<T> result=new Future<>();
     //   MessageBroker.complete(MissionRecievedEvent,result);
       // assertEquals();
    }
}
