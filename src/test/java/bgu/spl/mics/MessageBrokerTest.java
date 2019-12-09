package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.subscribers.M;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

public class MessageBrokerTest {
    MessageBroker MessageBroker;

    @BeforeEach
    public void setUp(){
        MessageBroker = MessageBrokerImpl.getInstance();
    }

    @Test
    public void subscribeEventtest(){
        Subscriber sb = new M() ;
        //Event MissionRecievedEvent = new MissionReceivedEvent;
        //MessageBroker.subscribeEvent(,sb);
    }
}
