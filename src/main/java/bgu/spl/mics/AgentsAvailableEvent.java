package bgu.spl.mics;

import java.util.List;

public class AgentsAvailableEvent implements Event<String> {
    private String msg;
    private Future fut;
    private List<String> serials;

    public AgentsAvailableEvent(String msg,List<String> serials)
    {
        this.msg = msg;
        this.serials=serials;
    }

    public Future getFut(){
        return fut;
    }
    public List<String> getserials(){
        return serials;
      
    @Override
    public void setFuture(Future fut) {

    }
}
