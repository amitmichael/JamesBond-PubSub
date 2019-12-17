package bgu.spl.mics;


public class TickBroadcast implements Broadcast {
    private int time;

    public TickBroadcast(int time){
        super();
        this.time=time;
    }

    public int getTime(){
        return time;
    }


}
