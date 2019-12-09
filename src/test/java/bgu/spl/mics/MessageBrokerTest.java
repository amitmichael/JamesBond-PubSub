package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.subscribers.M;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MessageBrokerTest {
    MessageBroker MessageBroker;
    Event<?> MissionRecievedEvent;

    @BeforeEach
    public void setUp() {
        MessageBroker = MessageBrokerImpl.getInstance();
    }

    @Test
    public void getInstancetest() {
        assertNotEquals(null, MessageBroker);
    }


    @Test
    public void subscribeEventtest() {
        Subscriber sb = new M();
        MissionRecievedEvent = new MissionReceivedEvent<String>();
     //   MessageBroker.subscribeEvent(MissionRecievedEvent.getClass(), sb);
    }

    public <T> void completeTest() {
        Future result=new Future();
     //   MessageBroker.complete(MissionRecievedEvent,result);
       // assertEquals();
    }
}
