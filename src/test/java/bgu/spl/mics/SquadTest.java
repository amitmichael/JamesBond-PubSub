package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Squad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

public class SquadTest {

    Squad sq;
    Agent ag1;
    Agent ag2;

    @BeforeEach
    public void setUp() {
        sq = Squad.getInstance();
        ag1 = new Agent("amit", "007");
        ag2 =  new Agent("Shachaf", "006");
        Agent[] agarray = {ag1, ag2};
        sq.load(agarray);    }

    @Test
    public void getInstancetest(){
        assertNotEquals(null,sq);
    }

    @Test
    public void getAgenttest() throws InterruptedException {
        List<String> listserial = new LinkedList<String>();
        listserial.add(ag1.getSerialNumber());
        listserial.add(ag2.getSerialNumber());
        assertTrue(sq.getAgents(listserial));
    }

    @Test
    public void getAgentnamestest() {
        List<String> listserial = new LinkedList<String>();
        listserial.add(ag1.getSerialNumber());
        listserial.add(ag2.getSerialNumber());
        List<String> listnames = new LinkedList<String>();
        listnames = sq.getAgentsNames(listserial);
        assertTrue(listnames.contains("amit"));
        assertFalse(listnames.contains("Stam"));

    }

    @Test
    public void releaseAgentstest() throws  InterruptedException {
        List<String> listserial = new LinkedList<String>();
        listserial.add(ag1.getSerialNumber());
        sq.sendAgents(listserial, 100);
        sq.releaseAgents(listserial);
        assertTrue(sq.getAgents(listserial)); // agent was released
    }

}
