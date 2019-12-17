package bgu.spl.mics;

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
