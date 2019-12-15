package bgu.spl.mics;


public class TickBroadcast implements Broadcast {
    private long time;

    public TickBroadcast(long time){
        super();
        this.time=time;
    }

    public long getTime(){
        return time;
    }
}
