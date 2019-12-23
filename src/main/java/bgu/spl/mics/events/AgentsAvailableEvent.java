package bgu.spl.mics.events;

import bgu.spl.mics.Event;

import java.util.List;

public class AgentsAvailableEvent implements Event<String> {
    private List<String> serials;
    private int durationinMillis;

    public AgentsAvailableEvent(List<String> serials,int durationinMillis)
    {
        this.serials=serials;
        this.durationinMillis = durationinMillis;
    }

    public int getDurationinMillis() {
        return durationinMillis;
    }

    public List<String> getserials() {
        return serials;
    }


}
