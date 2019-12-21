package bgu.spl.mics.events;


import bgu.spl.mics.Broadcast;

public class TickBroadcast implements Broadcast {
    private int time;

    public TickBroadcast(int time){
        super();
        this.time=time;
    }

    public int getTime(){
        return time;
    }

    public void setTime(int t) {this.time = t;}


}
