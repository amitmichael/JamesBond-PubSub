package bgu.spl.mics;

import java.util.List;

public class AgentsAvailableEvent implements Event<String> {
    private List<String> serials;
    private int timeout;

    public AgentsAvailableEvent(List<String> serials , int timeout)
    {
        this.timeout = timeout;
        this.serials=serials;
    }

    public int getTimeout(){
        return timeout;
    }
    public List<String> getserials() {
        return serials;
    }

}
