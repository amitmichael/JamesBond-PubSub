package bgu.spl.mics;


public class MissionReceivedEvent implements Event<String> {
    private String msg;

    public MissionReceivedEvent(String msg)
    {
        this.msg = msg;
    }

}
