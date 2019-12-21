package bgu.spl.mics.events;

import bgu.spl.mics.Event;

import java.util.List;

public class AgentsAvailableEvent implements Event<String> {
    private List<String> serials;

    public AgentsAvailableEvent(List<String> serials)
    {
        this.serials=serials;
    }


    public List<String> getserials() {
        return serials;
    }

}
