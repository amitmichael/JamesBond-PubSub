package bgu.spl.mics;


public class MissionReceivedEvent implements Event<String> {
    private String msg;
    private Future fut;

    public MissionReceivedEvent(String msg)
    {
        this.msg = msg;
    }

    public void setFuture(Future t){
        this.fut = t;
    }
}
