package bgu.spl.mics;

import java.util.List;

public class AgentsAvailableEvent implements Event<String> {
    private String msg;
    private List<String> serials;

    public AgentsAvailableEvent(String msg,List<String> serials)
    {
        this.msg = msg;
        this.serials=serials;
    }


    public List<String> getserials() {
        return serials;
    }

}
