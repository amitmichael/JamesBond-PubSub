package bgu.spl.mics.events;

import bgu.spl.mics.Event;

import java.util.List;

public class ExecuteMission implements Event<String> {
    private List<String> serials;
    private int duration;

    public ExecuteMission(List<String> serials, int duration)
    {
        this.serials=serials;
        this.duration = duration;
    }

    public int getDuration(){
        return this.duration;
    }
    public List<String> getserials() {
        return serials;
    }

}
